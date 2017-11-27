/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.network;

import com.ulb.cryptography.cryptocurrency.Block;
import com.ulb.cryptography.cryptocurrency.Miner;
import com.ulb.cryptography.cryptocurrency.RelayNode;
import com.ulb.cryptography.cryptocurrency.Transaction;
import com.ulb.cryptography.cryptocurrency.Wallet;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author masterulb
 */
public class MinerClient implements Runnable {

    private static Socket clientSocket = null;
    // The output stream
    private static ObjectOutputStream os = null;
    // The input stream
    //private static DataInputStream is = null;
    private static BufferedReader inputLine = null;
    private static boolean closed = false;
    public static Miner MINER;
    private static ObjectInputStream ois = null;

    public static void main(String[] args) throws GeneralSecurityException, NoSuchAlgorithmException, IOException, ClassNotFoundException {

        MINER = new Miner();

        // The default port.
        int portNumber = 2222;
        // The default host.
        String host = "localhost";

        if (args.length < 2) {
            System.out.println(
                    "Now using host=" + host + ", portNumber=" + portNumber
            );
        } else {
            host = args[0];
            portNumber = Integer.valueOf(args[1]).intValue();
        }

        /*
         * Open a socket on a given host and port. Open input and output streams.
         */
        try {

            clientSocket = new Socket(host, portNumber);
            inputLine = new BufferedReader(new InputStreamReader(System.in));
            os = new ObjectOutputStream(clientSocket.getOutputStream());
            //is = new DataInputStream(clientSocket.getInputStream());
            ois = new ObjectInputStream(clientSocket.getInputStream());

        } catch (UnknownHostException e) {

            System.err.println("Don't know about host " + host);

        } catch (IOException e) {
            System.err.println(
                    "Couldn't get I/O for the connection to the host "
                    + host
            );
        }

        /*
         * If everything has been initialized then we want to write some data to the
         * socket we have opened a connection to on the port portNumber.
         */
        if (clientSocket != null && os != null && ois != null) {
            try {

                /* Create a thread to read from the server. */
                new Thread(new MinerClient()).start();
                while (!closed) {
                    System.out.println("Here we have to send what ever we want");
                    if ("1".equals(inputLine.readLine())) {
                        RequestForTransactions rft = new RequestForTransactions();
                        rft.setAddress(MINER.getWallet().getAccounts().get(0).getStrAddress());
                        os.writeObject(rft);
                        Object o = ois.readObject();
                        LinkedList<Transaction> rn = (LinkedList<Transaction>)o;
                        System.out.println(rn.get(0).getIntAmount());
                        //if (LinkedList.class.isInstance(o)) {
                            //LinkedList<Transaction> transactions = (LinkedList<Transaction>) o;
                            //System.out.println(transactions.get(0).getIntAmount());
                        //}
                        Block b = new Block(rn, "hash", "nonce");
                        os.writeObject(b);
                    }
                }
                /*
                 * Close the output stream, close the input stream, close the socket.
                 */
                os.close();
                ois.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }

    }

    /**
     * Create a thread to read from the server. (non-Javadoc).
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        /*
         * Keep on reading from the socket till we receive "Bye" from the
         * server. Once we received that then we want to break.
         */
        /*String responseLine;
        try {
            while ((responseLine = is.readLine()) != null) {
                System.out.println(responseLine);
                if (responseLine.contains("*** Bye")) {
                    break;
                }
            }
            closed = true;
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }*/
    }
}
