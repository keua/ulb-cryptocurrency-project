/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.network;

import com.ulb.cryptography.cryptocurrency.Account;
import com.ulb.cryptography.cryptocurrency.Block;
import com.ulb.cryptography.cryptocurrency.Blockchain;
import com.ulb.cryptography.cryptocurrency.Miner;
import com.ulb.cryptography.cryptocurrency.Transaction;
import static com.ulb.cryptography.network.WalletClient.WALLET;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author masterulb
 */
public class MinerClient implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(MinerClient.class.getName());
    private static Socket clientSocket = null;
    private static ObjectOutputStream oos = null;
    private static BufferedReader inputLine = null;
    private static boolean closed = false;
    static Miner MINER;
    private static ObjectInputStream ois = null;
    static Account activeAccount;

    public static void main(String[] args) throws GeneralSecurityException, NoSuchAlgorithmException, IOException, ClassNotFoundException {

        MINER = new Miner();
        activeAccount = MINER.getWallet().getAccounts().get(0);
        int portNumber = 2222;
        String host = "localhost";

        if (args.length < 2) {
            LOGGER.log(
                    Level.SEVERE,
                    "Now using host={0}, portNumber={1}",
                    new Object[]{host, portNumber}
            );
        } else {
            host = args[0];
            portNumber = Integer.parseInt(args[1]);
        }

        /*
         * Open a socket on a given host and port. Open input and output streams.
         */
        try {

            clientSocket = new Socket(host, portNumber);
            inputLine = new BufferedReader(new InputStreamReader(System.in));
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ois = new ObjectInputStream(clientSocket.getInputStream());

        } catch (UnknownHostException e) {

            LOGGER.log(Level.SEVERE, "Don''t know about host {0}", host);

        } catch (IOException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Couldn''t get I/O for the connection to the host {0}",
                    host
            );
        }

        /*
         * If everything has been initialized then we want to write some data to the
         * socket we have opened a connection to on the port portNumber.
         */
        if (clientSocket != null && oos != null && ois != null) {
            try {
                /* Create a thread to read from the server. */
                new Thread(new MinerClient()).start();
                while (!closed) {//!closed

                    Message messageFromClient = (Message) ois.readObject();
                    Object objectInMessage = messageFromClient.getObject();

                    if (Blockchain.class.isInstance(objectInMessage)) {
                        LOGGER.log(Level.INFO, "Getting the new blockchain");
                        Blockchain newBlockchain = (Blockchain) objectInMessage;
                        MINER.setBlockchain(newBlockchain);
                        LOGGER.log(
                                Level.INFO,
                                "Blocks in the new blockchain {0}",
                                newBlockchain.getListOfBlocks().size()
                        );
                    }

                    requestTransactions();

                }
                /*
                 * Close the output stream, close the input stream, close the socket.
                 */
                oos.close();
                ois.close();
                clientSocket.close();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "IOException:  {0}", e);
            }
        }

    }

    private static void requestTransactions() throws IOException, GeneralSecurityException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int select = -1; //opción 
        int men = 1;
        Message messageFromClient;
        Object objectInMessage;
        while (men == 1) {
            System.out.println(
                    "Options:\n"
                    + "1.- Request transactions \n"
                    + "0.- Exit"
            );
            select = Integer.parseInt(scanner.nextLine());
            switch (select) {
                case 1:
                    LOGGER.log(Level.INFO, "Requesting transactions");

                    RequestForTransactions rft = new RequestForTransactions();
                    rft.setAddress(
                            MINER
                                    .getWallet()
                                    .getAccounts()
                                    .get(0)
                                    .getStrAddress()
                    );
                    oos.writeObject(new Message(rft));

                    messageFromClient = (Message) ois.readObject();
                    objectInMessage = messageFromClient.getObject();

                    // Process the transaction list (mining stuff here)
                    LOGGER.log(Level.INFO, "Processing the transactions");
                    LinkedList<Transaction> transactions
                            = (LinkedList<Transaction>) objectInMessage;
                    //Block b = new Block(transactions);
                    // validate the receiver address
                    LinkedList<Transaction> cleanTransactions = MINER.validateReceiverAddress(transactions);
                    // validate the signature
                    cleanTransactions = MINER.validateSignature(cleanTransactions);
                    // validate the balace
                    cleanTransactions = MINER.validateAmount(cleanTransactions);
                    // adding miner transactions
                    String minerAddress = activeAccount.getStrAddress();
                    Float total
                            = MINER.getBlockchain()
                                    .getLastTransactionBySenderAddress(minerAddress);
                    Transaction minerTransaction
                            = new Transaction(
                                    total,
                                    0f,
                                    cleanTransactions.size() * 1.5f,
                                    minerAddress,
                                    minerAddress,
                                    new Date()
                            );
                    minerTransaction.setTranType("reward");
                    minerTransaction.signTransaction(activeAccount.getPrivateKey());
                    cleanTransactions.add(minerTransaction);
                    // create the new block
                    Block block = MINER.CreateNewBlock(cleanTransactions);
                    // Here I'm only mining this for testing
                    //we must be mining the new created block
                    block.calcBlockHash();
                    Block minedBlock = MINER.ProveOfWork(block, 4);
                    LOGGER.log(Level.INFO, "Done Mining!");
                    // Process the transaction list (mining stuff here)

                    LOGGER.log(Level.INFO, "Sending mined block to the relay");
                    oos.writeObject(new Message(minedBlock, MINER.getNotValidTransactions()));
                    break;
                case 0:
                    oos.close();
                    ois.close();
                    clientSocket.close();
                    men = 0;
                    break;
                default:
                    System.out.println("Option not recognized");
                    break;

            }
            // reading the relay answer
            messageFromClient = (Message) ois.readObject();
            objectInMessage = messageFromClient.getObject();
            if (Blockchain.class.isInstance(objectInMessage)) {
                LOGGER.log(Level.INFO, "Getting the new blockchain");
                Blockchain newBlockchain = (Blockchain) objectInMessage;
                MINER.setBlockchain(newBlockchain);
                LOGGER.log(
                        Level.INFO,
                        "Blocks in the new blockchain {0}",
                        newBlockchain.getListOfBlocks().size()
                );
            }
        }
    }

    /**
     * Create a thread to read from the server. (non-Javadoc).
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {

    }
}
