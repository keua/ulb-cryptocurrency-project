/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.network;

import com.ulb.cryptography.cryptocurrency.Block;
import com.ulb.cryptography.cryptocurrency.Blockchain;
import com.ulb.cryptography.cryptocurrency.Transaction;
import java.io.EOFException;
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
public class MasterClientThread extends Thread {

    private static final Logger LOGGER = Logger.getLogger(MasterClientThread.class.getName());
    private Socket clientSocket = null;
    private final MasterClientThread[] threads;
    private final int maxClientsCount;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    //private PrintStream os = null;

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
                //os = new PrintStream(clientSocket.getOutputStream());
                LOGGER.log(Level.INFO, "Client starts connection");

                //send the las blockchain
                oos.writeObject(
                        new Message(MasterNodeServer.MASTER_NODE.getBlockChain())
                );

                int i = 0;
                while (true) {
                    //String line = ois.readLine();
                    //if (line.startsWith("/quit")) {
                    //    break;
                    //}
                    System.out.println("Here im receving the data");
                    Message messageFromClient = (Message) ois.readObject();
                    Object objectInMessage = messageFromClient.getObject();

                    if (Block.class.isInstance(objectInMessage)) {
                        Block block = (Block) objectInMessage;
                        MasterNodeServer.MASTER_NODE.addBlockToChain(block);
                        System.out.println(
                                "block added to the blockchain, "
                                + "blocks in the blockchain: "
                                + MasterNodeServer.MASTER_NODE.getBlockChain().getListOfBlocks().size()
                        );

                        oos.writeObject(
                                new Message(
                                        MasterNodeServer.MASTER_NODE.getBlockChain()
                                )
                        );
                        oos.reset();
                    }

                    if (Blockchain.class.isInstance(objectInMessage)) {

                    }

                    /*for (MasterClientThread thread : threads) {
                        if (thread != null) {
                            System.out.println(
                                    "sending new blockchain with, "
                                    + MasterNodeServer.MASTER_NODE
                                            .getBlockChain()
                                            .getListOfBlocks().size()
                                    + " blocks");
                            thread.oos.writeObject(MasterNodeServer.MASTER_NODE.getBlockChain());
                        }
                    }*/
                    //oos.writeObject(o);
                    i++;
                }
            } catch (ClassNotFoundException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            } catch (EOFException ex) {
                LOGGER.log(Level.INFO, "Client finishes connection");
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
