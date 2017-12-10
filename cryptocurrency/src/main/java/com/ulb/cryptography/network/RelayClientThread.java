/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.network;

import com.ulb.cryptography.cryptocurrency.Block;
import com.ulb.cryptography.cryptocurrency.Transaction;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
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
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;

    public RelayClientThread(Socket clientSocket, RelayClientThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        this.maxClientsCount = threads.length;
    }

    @Override
    public void run() {
        try {

            try {

                ois = new ObjectInputStream(clientSocket.getInputStream());
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
                System.out.println("New client is connected in this server");

                oos.writeObject(
                        new Message(RelayServer.RELAY_NODE.getBlockChain())
                );

                oos.reset();

                while (true) {

                    System.out.println("Im listening for wallets and miners");
                    Message messageFromClient = (Message) ois.readObject();
                    Object objectInMessage = messageFromClient.getObject();

                    if (Transaction.class.isInstance(objectInMessage)) { // from wallet

                        Transaction t = (Transaction) objectInMessage;
                        RelayServer.RELAY_NODE.getTransactionList().add(t);
                        LOGGER.log(
                                Level.INFO, "New transaction added to the list"
                        );

                    } else if (RequestForTransactions.class.isInstance(objectInMessage)) { // from miner

                        LOGGER.log(
                                Level.INFO,
                                "New request for transactions has arrived"
                        );
                        RequestForTransactions rft
                                = (RequestForTransactions) objectInMessage;

                        oos.writeObject(
                                new Message(
                                        (LinkedList<Transaction>) RelayServer.RELAY_NODE
                                                .getTransactionList()
                                                .clone()
                                )
                        );

                        LOGGER.log(
                                Level.INFO,
                                "The List has been sent to the miner"
                        );

                        // clear the transaction list
                        //RelayServer.RELAY_NODE.getTransactionList().clear();
                        LOGGER.log(
                                Level.INFO,
                                "The list has been cleared"
                        );

                    } else if (Block.class.isInstance(objectInMessage)) {

                        LOGGER.log(Level.INFO, "A mined Block has been received");

                        Block block = (Block) objectInMessage;
                        RelayServer.RELAY_NODE.setMinedBlock(block);
                        RelayServer.masterConn.moos
                                .writeObject(
                                        new Message(
                                                RelayServer.RELAY_NODE.getMinedBlock(), messageFromClient.getObject2()
                                        )
                                );
                        LOGGER.log(
                                Level.INFO,
                                "The mined Block has been sent to the master node"
                        );

                    } else if (StringBuilder.class.isInstance(objectInMessage)) {
                        StringBuilder st = (StringBuilder) objectInMessage;
                        System.out.println(st);
                    }
                }
            } catch (ClassNotFoundException ex) {
                LOGGER.log(Level.SEVERE, "Class Exception", ex);
            }

            ois.close();
            oos.close();
            clientSocket.close();

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "IO exception, run thread method", ex);
        }
    }

}
