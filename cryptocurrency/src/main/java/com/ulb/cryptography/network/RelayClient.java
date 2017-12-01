/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.network;

import com.ulb.cryptography.cryptocurrency.Blockchain;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author masterulb
 */
public class RelayClient extends Thread {

    private static final Logger LOGGER = Logger.getLogger(RelayClient.class.getName());
    private Socket masterClientSocket = null;
    public ObjectOutputStream moos = null;
    public ObjectInputStream mois = null;

    public RelayClient(String host, int masterPort) {
        /*
         * Open a socket on a given host and port. Open input and output streams.
         */
        try {

            masterClientSocket = new Socket(host, masterPort);
            moos = new ObjectOutputStream(masterClientSocket.getOutputStream());
            mois = new ObjectInputStream(masterClientSocket.getInputStream());

        } catch (UnknownHostException e) {

            LOGGER.log(Level.SEVERE, "Don''t know about host {0}", host);

        } catch (IOException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Couldn''t get I/O for the connection to the host {0}",
                    host
            );
        }

    }

    private void close() {
        try {
            /*
             * Close the output stream, close the input stream, close the socket.
             */
            moos.close();
            mois.close();
            masterClientSocket.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "IO exception closing streams", ex);
        }

    }

    @Override
    public void run() {
        while (!masterClientSocket.isClosed()) {
            try {
                Message messageFromClient = (Message) mois.readObject();
                Object objectInMessage = messageFromClient.getObject();
                if (Blockchain.class.isInstance(objectInMessage)) {
                    LOGGER.log(
                            Level.INFO,
                            "Getting the new blockchain from master node"
                    );
                    Blockchain newBlockchain = (Blockchain) objectInMessage;
                    RelayServer.RELAY_NODE.setBlockChain(newBlockchain);
                    LOGGER.log(
                            Level.INFO,
                            "Blocks in the new blockchain {0}",
                            newBlockchain.getListOfBlocks().size()
                    );
                    moos.reset();
                }
            } catch (IOException | ClassNotFoundException ex) {
                LOGGER.log(Level.SEVERE, " IO or Class exception", ex);
            }
        }
    }
}
