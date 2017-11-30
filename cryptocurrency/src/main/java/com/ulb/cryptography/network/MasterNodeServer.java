/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.network;

import com.ulb.cryptography.cryptocurrency.MasterNode;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author masterulb
 */
public class MasterNodeServer {

    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;
    private static final int MAX_CLIENTS = 10;
    private static final MasterClientThread[] CLIENT_THREADS = new MasterClientThread[MAX_CLIENTS];
    static  MasterNode MASTER_NODE;
    private static final int DEFAULT_PORT_NUMBER = 3333;

    public static void main(String args[]) {

        MASTER_NODE = new MasterNode();

        int portNumber = DEFAULT_PORT_NUMBER;

        if (args.length < 1) {
            System.out.println("Now using port number=" + portNumber);
        } else {
            portNumber = Integer.parseInt(args[0]);
        }

        /*
         * Open a server socket on the portNumber (default 2222). Note that we can
         * not choose a port less than 1023 if we are not privileged users (root).
         */
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println(e);
        }

        // here create the client part
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
                System.out.println(e);
            }
        }
    }
}
