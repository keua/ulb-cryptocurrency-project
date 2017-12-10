/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.network;

import com.ulb.cryptography.cryptocurrency.RelayNode;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author masterulb
 */
public class RelayServer {

    private static final Logger LOGGER = Logger.getLogger(RelayServer.class.getName());

    // to be a relay server
    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;
    private static final int MAX_CLIENTS = 10;
    static final RelayClientThread[] CLIENT_THREADS = new RelayClientThread[MAX_CLIENTS];
    static RelayNode RELAY_NODE;
    private static final int DEFAULT_PORT_NUMBER = 2222;
    // to be a master client
    private static final int DEFAULT_MASTER_PORT_NUMBER = 3333;
    private static final String DEFAULT_HOST = "localhost";
    static int masterPort;
    static String masterHost;
    static RelayClient masterConn = null;
    // to be a Relay Client
    static int relayServerPort;

    public static void main(String args[]) throws ClassNotFoundException {

        RELAY_NODE = new RelayNode();

        int portNumber = DEFAULT_PORT_NUMBER;
        RelayServer.masterPort = DEFAULT_MASTER_PORT_NUMBER;
        RelayServer.masterHost = DEFAULT_HOST;

        if (args.length < 2) {
            LOGGER.log(Level.INFO, "Now using port number={0}", portNumber);
        } else {
            portNumber = Integer.parseInt(args[0]);
            RelayServer.masterPort = Integer.parseInt(args[1]);
            RelayServer.masterHost = args[2];
            relayServerPort = Integer.parseInt(args[3]);
            LOGGER.log(Level.INFO, "Now using port number={0}", portNumber);
        }

        /*
         * Open a server socket on the portNumber (default 2222). Note that we can
         * not choose a port less than 1023 if we are not privileged users (root).
         */
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IO exception creating the server socket", e);
        }

        // here create the client part
        LOGGER.log(Level.INFO, "Starting connection with the Master node");
        masterConn = new RelayClient(masterHost, masterPort);
        masterConn.start();
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
                        (CLIENT_THREADS[i] = new RelayClientThread(clientSocket, CLIENT_THREADS)).start();
                        break;
                    }
                }
                if (i == MAX_CLIENTS) {
                    PrintStream os = new PrintStream(clientSocket.getOutputStream());
                    os.println("Server too busy. Try later.");
                    os.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "IO exception", e);
            }

        }
    }
}
