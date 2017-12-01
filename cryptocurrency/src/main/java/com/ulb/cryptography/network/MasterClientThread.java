/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.network;

import com.ulb.cryptography.cryptocurrency.Block;
import com.ulb.cryptography.cryptocurrency.Blockchain;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author masterulb
 */
public class MasterClientThread extends Thread {

    private static final Logger LOGGER = Logger.getLogger(MasterClientThread.class.getName());
    private Socket clientSocket = null;
    private final MasterClientThread[] threads;
    private final int maxClientsCount;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;

    public MasterClientThread(Socket clientSocket, MasterClientThread[] threads) {
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
                LOGGER.log(Level.INFO, "New client starts a connection");

                //send the las blockchain
                LOGGER.log(
                        Level.INFO,
                        "Sending the current blockchain to the new client"
                );
                oos.writeObject(
                        new Message(MasterNodeServer.MASTER_NODE.getBlockChain())
                );

                oos.reset();// the most important line in the project

                while (true) {
                    Message messageFromClient = (Message) ois.readObject();
                    Object objectInMessage = messageFromClient.getObject();

                    if (Block.class.isInstance(objectInMessage)) {
                        LOGGER.log(
                                Level.INFO,
                                "Receiving a new mined Block from some relay"
                        );
                        // Verify if the block is valid (Below)
                        Block block = (Block) objectInMessage;
                        MasterNodeServer.MASTER_NODE.addBlockToChain(block);
                        LOGGER.log(
                                Level.INFO,
                                "New Block added to the blockchain,"
                                + " blocks in the blockchain: {0}",
                                MasterNodeServer.MASTER_NODE
                                        .getBlockChain()
                                        .getListOfBlocks()
                                        .size()
                        );
                        // Verify if the block is valid (Above)
                        for (MasterClientThread thread : threads) {
                            if (thread != null) {
                                LOGGER.log(
                                        Level.INFO,
                                        "Sending the updated blockchain to the relays"
                                );
                                thread.oos.writeObject(
                                        new Message(
                                                MasterNodeServer.MASTER_NODE
                                                        .getBlockChain()
                                        )
                                );
                                thread.oos.reset();// the most important line in the project
                            }
                        }
                        /*LOGGER.log(
                                Level.INFO,
                                "Sending the updated blockchain to the relays"
                        );
                        oos.writeObject(
                                new Message(
                                        MasterNodeServer.MASTER_NODE
                                                .getBlockChain()
                                )
                        );
                        oos.reset();*/
                    }
                    // If we want to send the new blockchain only to the relay
                    // who has sent the block, we have to use the code above.
                }
            } catch (ClassNotFoundException ex) {
                LOGGER.log(Level.SEVERE, "Class not found Exception", ex);
            } catch (EOFException ex) {
                LOGGER.log(Level.INFO, "Client finishes connection", ex);
            }
            ois.close();
            oos.close();
            clientSocket.close();

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "IO Exception", ex);
        }
    }
}
