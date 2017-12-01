/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.network;

import com.ulb.cryptography.cryptocurrency.Account;
import com.ulb.cryptography.cryptocurrency.Block;
import com.ulb.cryptography.cryptocurrency.MasterNode;
import com.ulb.cryptography.cryptocurrency.Transaction;
import com.ulb.cryptography.cryptocurrency.Wallet;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author masterulb
 */
public class MasterNodeServer {

    private static final Logger LOGGER
            = Logger.getLogger(MasterNode.class.getName());
    private static boolean closed = false;
    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;
    private static final int MAX_CLIENTS = 10;
    private static final MasterClientThread[] CLIENT_THREADS
            = new MasterClientThread[MAX_CLIENTS];
    static MasterNode MASTER_NODE;
    private static final int DEFAULT_PORT_NUMBER = 3333;
    static Wallet WALLET;

    public static void main(String args[]) {

        MASTER_NODE = new MasterNode();
        WALLET = new Wallet();

        try {
            // initialize blockchain (below)
            Account a = new Account("masternode");
            MASTER_NODE.setAccount(a);
            Transaction t
                    = new Transaction(
                            10000000.00f,
                            0f,
                            10000000.00f,
                            MASTER_NODE.getAccount().getStrAddress(),
                            MASTER_NODE.getAccount().getStrAddress(),
                            new Date()
                    );
            Block b = new Block();
            b.createFirstBlock();
            b.getListOfTransactions().add(t);
            MASTER_NODE.addBlockToChain(b);
            int accountId = MASTER_NODE.getAccount().getAcountID();
            LOGGER.log(Level.INFO, "Masternode account id: {0}", accountId);
            LOGGER.log(Level.INFO, "address: {0}", a.getStrAddress());
            WALLET.getAccounts().add(a);
            // initialize blockcahin (above)

        } catch (GeneralSecurityException | IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        //
        int portNumber = DEFAULT_PORT_NUMBER;

        if (args.length < 1) {
            LOGGER.log(Level.INFO, "Now using port number={0}", portNumber);
        } else {
            portNumber = Integer.parseInt(args[0]);
        }

        /*
         * Open a server socket on the portNumber (default 2222).
         * Note that we can not choose a port less than 1023 if we are not
         * privileged users (root).
         */
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IO exception creating the socket", e);
        }

        /*
         * Create a client socket for each connection and pass it to a new client
         * thread.
         */
        while (true) {
            try {
                clientSocket = serverSocket.accept();

                int i = 0;
                for (i = 0; i < MAX_CLIENTS; i++) {
                    if (CLIENT_THREADS[i] == null) {
                        (CLIENT_THREADS[i] = new MasterClientThread(clientSocket, CLIENT_THREADS)).start();
                        break;
                    }
                }
                if (i == MAX_CLIENTS) {
                    //PrintStream os = new PrintStream(clientSocket.getOutputStream());
                    //os.println("Server too busy. Try later.");
                    //os.close();
                    clientSocket.close();
                }

            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Io exception ", e);
            }
        }
    }
}
