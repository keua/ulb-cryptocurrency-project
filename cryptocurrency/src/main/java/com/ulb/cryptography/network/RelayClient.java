/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.network;

import com.ulb.cryptography.cryptocurrency.Blockchain;
import com.ulb.cryptography.cryptocurrency.RelayNode;
import com.ulb.cryptography.cryptocurrency.Transaction;
import static com.ulb.cryptography.network.RelayServer.masterConn;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author masterulb
 */
public class RelayClient extends Thread {

    // to be a master client
    private Socket masterClientSocket = null;
    public ObjectOutputStream moos = null;
    public ObjectInputStream mois = null;
    //private static DataInputStream mis = null;

    public RelayClient(String host, int masterPort) {
        /*
         * Open a socket on a given host and port. Open input and output streams.
         */
        try {

            masterClientSocket = new Socket(host, masterPort);
            moos = new ObjectOutputStream(masterClientSocket.getOutputStream());
            mois = new ObjectInputStream(masterClientSocket.getInputStream());//here
            //mis = new DataInputStream(masterClientSocket.getInputStream());

        } catch (UnknownHostException e) {

            System.err.println("Don't know about host " + host);

        } catch (IOException e) {
            System.err.println(
                    "Couldn't get I/O for the connection to the host "
                    + host
            );
        }

    }

    public void close() {
        try {
            /*
            * Close the output stream, close the input stream, close the socket.
             */
            moos.close();
            //mis.close();
            mois.close();
            masterClientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(RelayClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        int i = 0;
        while (!masterClientSocket.isClosed()) {
            try {
                System.out.println("time, " + i);
                Message messageFromClient = (Message) mois.readObject();
                Object objectInMessage = messageFromClient.getObject();
                if (Blockchain.class.isInstance(objectInMessage)) {
                    System.out.println("getting the new blockchain");
                    Blockchain newBlockchain = (Blockchain) objectInMessage;
                    RelayServer.RELAY_NODE.setBlockChain(newBlockchain);
                    System.out.println(
                            "blocs in the mew blockchain "
                            + newBlockchain.getListOfBlocks().size()
                    );
                    moos.reset();
                } else if (StringBuilder.class.isInstance(objectInMessage)) {
                    StringBuilder s = (StringBuilder) objectInMessage;
                    System.out.println(s);
                } else if (LinkedList.class.isInstance(objectInMessage)) {
                    LinkedList<Transaction> nt = (LinkedList<Transaction>) objectInMessage;
                    System.out.println("list size " + nt.size());
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(RelayServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            i++;
        }
    }

}
