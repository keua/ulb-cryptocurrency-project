/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.network;

import com.ulb.cryptography.cryptocurrency.Block;
import com.ulb.cryptography.cryptocurrency.Transaction;
import java.io.DataInputStream;
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
/*
 * The chat client thread. This client thread opens the input and the output
 * streams for a particular client, ask the client's name, informs all the
 * clients connected to the server about the fact that a new client has joined
 * the chat room, and as long as it receive data, echos that data back to all
 * other clients. When a client leaves the chat room this thread informs also
 * all the clients about that and terminates.
 */
public class RelayClientWalletThread extends Thread {

    private ObjectInputStream is = null;
    //private PrintStream os = null;
    private Socket clientSocket = null;
    private final RelayClientWalletThread[] threads;
    private int maxClientsCount;
    private ObjectOutputStream oos = null;

    public RelayClientWalletThread(Socket clientSocket, RelayClientWalletThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        this.maxClientsCount = threads.length;
    }

    @Override
    public void run() {
        int maxClientsCount = this.maxClientsCount;
        RelayClientWalletThread[] threads = this.threads;

        try {

            is = new ObjectInputStream(clientSocket.getInputStream());
            //os = new PrintStream(clientSocket.getOutputStream());
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            System.out.println("New client is connected in this server");
            //os.println("The client is connected to the server");
            /*for (int i = 0; i < maxClientsCount; i++) {

                if (threads[i] != null && threads[i] != this) {
                    threads[i].os.println("*** new client is connected ");
                }
            }*/
            while (true) {
                //String line = is.readLine();
                /*if (line.startsWith("/quit")) {
                    break;
                }*/
                System.out.println("here is the problem");
                Object o = is.readObject();
                if (Transaction.class.isInstance(o)) {

                    Transaction t = (Transaction) o;
                    RelayServer.RELAY_NODE.getTransactionList().add(t);
                    System.out.println("transaction signed: " + Arrays.toString(t.getTransactionSigned()));

                } else if (RequestForTransactions.class.isInstance(o)) {
                    RequestForTransactions rft = (RequestForTransactions) o;
                    System.out.println(rft.getAddress());
                    oos.writeObject(RelayServer.RELAY_NODE.getTransactionList());
                } else if (Block.class.isInstance(o)) {
                    threads[0].oos.writeObject(o);// the master node must be the first connected client to the relay node
                }
                    //oos.writeObject(o);
                }


                /*for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null) {
                        threads[i].os.println("Client " + i + " is sending something " + line);
                    }
                }*/
            } catch (IOException ex) {
            Logger.getLogger(RelayClientWalletThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RelayClientWalletThread.class.getName()).log(Level.SEVERE, null, ex);
        }

            //os.println("*** Bye  ***");
            /*
             * Close the output stream, close the input stream, close the socket.
             */
 /*is.close();
            os.close();
            clientSocket.close();*/
        //} catch (IOException e) {
            /*try {
                is.close();
                os.close();
                clientSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(RelayClientWalletThread.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        //} catch (ClassNotFoundException ex) {
            //Logger.getLogger(RelayClientWalletThread.class.getName()).log(Level.SEVERE, null, ex);
        //}
    }

}
