/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.network;

import com.ulb.cryptography.cryptocurrency.Block;
import com.ulb.cryptography.cryptocurrency.Blockchain;
import com.ulb.cryptography.cryptocurrency.Transaction;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author masterulb
 */
public class RelayClientThread extends Thread {

    private static final Logger LOGGER = Logger.getLogger(RelayClientThread.class.getName());
    private Socket clientSocket = null;
    private final RelayClientThread[] threads;
    private final int maxClientsCount;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    private PrintStream os = null;

    public RelayClientThread(Socket clientSocket, RelayClientThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        this.maxClientsCount = threads.length;
    }

    @Override
    public void run() {
        try {
            int maxClientsCount = this.maxClientsCount;
            RelayClientThread[] threads = this.threads;

            try {

                ois = new ObjectInputStream(clientSocket.getInputStream());
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
                System.out.println("New client is connected in this server");

                oos.writeObject(
                        new Message(RelayServer.RELAY_NODE.getBlockChain())
                );

                while (true) {
                    //String line = ois.readLine();
                    //if (line.startsWith("/quit")) {
                    //    break;
                    //}
                    System.out.println("here im receiving the data");
                    Message messageFromClient = (Message) ois.readObject();
                    Object objectInMessage = messageFromClient.getObject();

                    if (Transaction.class.isInstance(objectInMessage)) { // from wallet

                        Transaction t = (Transaction) objectInMessage;
                        RelayServer.RELAY_NODE.getTransactionList().add(t);
                        System.out.println("transaction signed: " + Arrays.toString(t.getTransactionSigned()));

                    } else if (RequestForTransactions.class.isInstance(objectInMessage)) { // from miner

                        RequestForTransactions rft = (RequestForTransactions) objectInMessage;
                        System.out.println(rft.getAddress());

                        oos.writeObject(
                                new Message(
                                        RelayServer.RELAY_NODE.getTransactionList()
                                )
                        );

                    } else if (Block.class.isInstance(objectInMessage)) {

                        System.out.println("sending block to the master");
                        Block block = (Block) objectInMessage;
                        RelayServer.RELAY_NODE.setMinedBlock(block);
                        RelayServer.masterConn.moos
                                .writeObject(
                                        new Message(
                                                RelayServer.RELAY_NODE.getMinedBlock()
                                        )
                                );
                        System.out.println("the block has been sent");

                        oos.writeObject(
                                new Message(RelayServer.RELAY_NODE.getBlockChain())
                        );

                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
            /*
            * Close the output stream, close the input stream, close the socket.
             */
            //os.println("*** Bye  ***");
            ois.close();
            oos.close();
            clientSocket.close();

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

}
