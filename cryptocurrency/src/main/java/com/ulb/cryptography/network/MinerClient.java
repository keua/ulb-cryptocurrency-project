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
import java.io.IOException;
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
    private static Socket[] clientSocket = null;
    private static ObjectOutputStream[] oosarray = null;
    private static ObjectInputStream[] oisarray = null;
    private static final int[] DEFAULT_RELAY_PORT_NUMBER = {2222, 2223};
    private static boolean closed = false;
    static Miner MINER;
    private static Socket MINER_SOCKET;
    private static ObjectOutputStream MINER_OOS;
    private static ObjectInputStream MINER_OIS;
    static Account activeAccount;

    public static void main(String[] args) throws GeneralSecurityException, NoSuchAlgorithmException, IOException, ClassNotFoundException {

        MINER = new Miner();
        activeAccount = MINER.getWallet().getAccounts().get(0);
        clientSocket = new Socket[2];
        oosarray = new ObjectOutputStream[2];
        oisarray = new ObjectInputStream[2];
        LOGGER.log(
                Level.INFO, "my address is: {0}, my id is: {1}",
                new Object[]{activeAccount.getStrAddress(), activeAccount.getAcountID()}
        );
        int[] portNumber = DEFAULT_RELAY_PORT_NUMBER;
        String host = "localhost";

        if (args.length < 2) {
            LOGGER.log(
                    Level.SEVERE,
                    "Now using host={0}, portNumber={1}",
                    new Object[]{host, portNumber}
            );
        } else {
            host = args[0];
            portNumber[0] = Integer.parseInt(args[1]);
            portNumber[1] = Integer.parseInt(args[2]);
        }

        /*
         * Open a socket on a given host and port. Open input and output streams.
         */
        try {

            for (int i = 0; i < portNumber.length; i++) {
                clientSocket[i] = new Socket(host, portNumber[i]);
                oosarray[i] = new ObjectOutputStream(clientSocket[i].getOutputStream());
                oisarray[i] = new ObjectInputStream(clientSocket[i].getInputStream());
            }
            MINER_SOCKET = clientSocket[0];
            MINER_OOS = oosarray[0];
            MINER_OIS = oisarray[0];

        } catch (UnknownHostException e) {

            LOGGER.log(Level.SEVERE, "Don't know about host {0}", host);

        } catch (IOException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Couldn''t get I/O for the connection to the host {0}",
                    host
            );
        }

        /*
         * If everything has been initialized then we want to write some data to the
         * socket we have opened a connectioMINER_SOCKETt poMINER_OOS        */
        if (MINER_SOCKET != null && MINER_OOS != null && MINER_OIS != null) {
            try {
                /* Create a thread to read from the server. */
                new Thread(new MinerClient()).start();
                while (!closed) {//!closed

                    receiveAnswer();

                    requestTransactions();

                }
                /*
                 * Close the output stream, close the input streamMINER_OOS socket.
                 */
                MINER_OOS.close();
                MINER_OIS.close();
                MINER_SOCKET.close();
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
                    + "2.- Login \n"
                    + "0.- Exit"
            );
            select = Integer.parseInt(scanner.nextLine());
            Scanner entradaEscaner = new Scanner(System.in);
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
                    MINER_OOS.writeObject(new Message(rft));

                    messageFromClient = (Message) MINER_OIS.readObject();
                    objectInMessage = messageFromClient.getObject();

                    // Process the transaction list (mining stuff here)
                    LOGGER.log(Level.INFO, "Processing the transactions");
                    LinkedList<Transaction> transactions
                            = (LinkedList<Transaction>) objectInMessage;
                    //if we are mining the first block in the blockchain we dont need to do the validations
                    Block minedBlock;
                    if (transactions.isEmpty() && MINER.getBlockchain().getListOfBlocks().isEmpty()) {
                        String minerAddress = activeAccount.getStrAddress();
                        Transaction minerTransaction
                                = new Transaction(
                                        0f,
                                        0f,
                                        1.5f,
                                        minerAddress,
                                        minerAddress,
                                        new Date()
                                );
                        minerTransaction.setTranType("reward");
                        minerTransaction.signTransaction(activeAccount.getPrivateKey());
                        transactions.add(minerTransaction);
                        Block block = MINER.CreateNewBlock(transactions);
                        minedBlock = MINER.ProveOfWork(block, 4);
                        LOGGER.log(Level.INFO, "Done Mining!");
                    } else {
                        // validate the receiver address
                        LinkedList<Transaction> cleanTransactions = MINER.validateReceiverAddress(transactions);
                        // validate the signature
                        cleanTransactions = MINER.validateSignature(cleanTransactions);
                        // validate the balace
                        cleanTransactions = MINER.validateAmount(cleanTransactions);
                        // adding miner transactions
                        String minerAddress = activeAccount.getStrAddress();
                        Float total
                                = MINER.getLastTransactionBySenderAddress(minerAddress);
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
                        minedBlock = MINER.ProveOfWork(block, 4);
                        LOGGER.log(Level.INFO, "Done Mining!");
                    }

                    // Process the transaction list (mining stuff here)
                    LOGGER.log(Level.INFO, "Sending mined block to the relay");
                    MINER_OOS.writeObject(new Message(minedBlock, MINER.getNotValidTransactions()));
                    receiveAnswer();
                    break;
                case 2:
                    System.out.println("Login\n");

                    System.out.println("User:");
                    String entradaTeclado = "2";
                    entradaTeclado = entradaEscaner.nextLine();

                    System.out.println("password:");
                    String entradaTeclado2 = "";
                    entradaTeclado2 = entradaEscaner.nextLine();

                    Account activeAccount2 = MINER.getWallet().login(entradaTeclado, entradaTeclado2);

                    if (activeAccount2 != null) {
                        Boolean account = true;
                        while (account) {
                            System.out.println(
                                    "Options:\n"
                                    + "1.- Transfer \n"
                                    + "2.- Blance \n"
                                    + "0.- Logout"
                            );

                            int select2 = Integer.parseInt(scanner.nextLine());

                            switch (select2) {
                                case 1:
                                    System.out.println("Addressee to transfer");
                                    String entradaTeclado3 = "";
                                    entradaTeclado3 = entradaEscaner.nextLine();
                                    System.out.println("Quantity");
                                    String entradaTeclado4 = "";
                                    entradaTeclado4 = entradaEscaner.nextLine();

                                    String addressToSend = entradaTeclado3;
                                    String addressFromSend = activeAccount2.getStrAddress();
                                    Float totalsend
                                            = MINER.getBlockchain()
                                                    .getLastTransactionBySenderAddress(
                                                            addressFromSend
                                                    );
                                    Float toSend = Float.parseFloat(entradaTeclado4);
                                    // creating the transaction
                                    Transaction t = new Transaction(
                                            totalsend,
                                            toSend,
                                            totalsend - toSend,
                                            addressFromSend,
                                            addressToSend,
                                            new Date()
                                    );
                                    t.setTranType("normal");
                                    // sign the transaction before send to the relay
                                    t.signTransaction(activeAccount2.getPrivateKey());

                                    System.out.println(t.getFltInputSenderAmount()); // 10000000
                                    System.out.println(t.getFltOutputReceiverAmount());//100
                                    System.out.println(t.getFltOutputSenderAmount());//1000000-100

                                    for (ObjectOutputStream oos : oosarray) {
                                        oos.writeObject(new Message(t));// send the transaction to the relay server
                                    }

                                    receiveAnswer();

                                    System.out.println("Successful transaction");
                                    break;
                                case 2:
                                    System.out.println("Your balance is:");
                                    break;
                                case 0:
                                    account = false;
                                    break;
                                default:
                                    System.out.println("Choose a valid option");
                                    break;
                            }
                        }
                        men = 1;
                    } else {
                        System.out.println("Invalid account");
                        men = 1;
                    }
                    break;
                case 0:
                    for (ObjectOutputStream oos : oosarray) {

                        oos.close();
                    }
                    for (ObjectInputStream ois : oisarray) {

                        ois.close();
                    }
                    for (Socket socket : clientSocket) {
                        socket.close();
                    }
                    men = 0;
                    break;
                default:
                    System.out.println("Option not recognized");
                    break;

            }
        }
    }

    private static void receiveAnswer() {
        try {
            // reading the relay answer
            Message messageFromClient = (Message) MINER_OIS.readObject();
            Object objectInMessage = messageFromClient.getObject();
            if (Blockchain.class.isInstance(objectInMessage)) {
                LOGGER.log(Level.INFO, "Getting the new blockchain");
                Blockchain newBlockchain = (Blockchain) objectInMessage;
                MINER.setBlockchain(newBlockchain);
                LOGGER.log(
                        Level.INFO,
                        "Blocks in the new blockchain {0}",
                        MINER.getBlockchain().getListOfBlocks().size()
                );
            }
        } catch (IOException | ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
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
