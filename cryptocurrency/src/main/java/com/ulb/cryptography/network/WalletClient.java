/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.network;

import com.ulb.cryptography.cryptocurrency.Transaction;
import com.ulb.cryptography.cryptocurrency.Wallet;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author masterulb
 */
public class WalletClient implements Runnable {

    private static Socket clientSocket = null;
    private static ObjectOutputStream oos = null;
    private static DataInputStream is = null;
    private static boolean closed = false;
    static Wallet WALLET;
    private static final int DEFAULT_PORT_NUMBER = 2222;
    private static final String DEFAULT_HOST = "localhost";

    public static void main(String[] args) throws GeneralSecurityException {

        WALLET = new Wallet();

        int portNumber = DEFAULT_PORT_NUMBER;
        String host = DEFAULT_HOST;

        if (args.length < 2) {
            System.out.println(
                    "Now using host=" + host + ", portNumber=" + portNumber
            );
        } else {
            host = args[0];
            portNumber = Integer.parseInt(args[1]);
        }

        /*
         * Open a socket on a given host and port. Open input and output streams.
         */
        try {

            clientSocket = new Socket(host, portNumber);
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            is = new DataInputStream(clientSocket.getInputStream());

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
        if (clientSocket != null && oos != null && is != null) {
            try {

                /* Create a thread to read from the server. */
                new Thread(new WalletClient()).start();
                while (!closed) {
                    System.out.println("Here we have to send what ever we want");

                    WalletClient.walletMenu();

                }
                /*
                 * Close the output stream, close the input stream, close the socket.
                 */
                oos.close();
                is.close();
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
        String responseLine;
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
        }
    }

    private static void walletMenu() {

        Scanner scanner = new Scanner(System.in);
        int select = -1; //opción 
        int men = 1;

        while (men == 1) {

            try {
                System.out.println(
                        "Options:\n"
                        + "1.- Create new addresses \n"
                        + "2.- Login \n"
                        + "3.- Full copy of teh blockchain \n"
                        + "0.- Exit"
                );

                select = Integer.parseInt(scanner.nextLine());
                Scanner entradaEscaner = new Scanner(System.in);

                switch (select) {
                    case 1:
                        System.out.println("password:");
                        String password = "";
                        password = entradaEscaner.nextLine();
                        String address = WALLET.createAccount(password);
                        System.out.println("your address is: " + address);
                        // this we will see later but it was working.
                        Transaction t = new Transaction(5000, "AddSender", "AddReceiver", new Date());
                        t.signTransaction(WALLET.getAccounts().get(0).getPrivateKey());
                        oos.writeObject(new Message(t));
                        men = 1;
                        break;
                    case 2:

                        System.out.println("Login\n");

                        System.out.println("User:");
                        String entradaTeclado = "2";
                        entradaTeclado = entradaEscaner.nextLine();

                        System.out.println("password:");
                        String entradaTeclado2 = "";
                        entradaTeclado2 = entradaEscaner.nextLine();
                        System.out.println(entradaTeclado2);
                        System.out.println(entradaTeclado);

                        if ("2".equals(entradaTeclado) && "2".equals(entradaTeclado2)) {
                            Boolean account = true;
                            while (account) {
                                System.out.println(
                                        "Options:\n"
                                        + "1.- Transfer \n"
                                        + "2.- Blance \n"
                                        + "0.- Logout"
                                );

                                int select2 = Integer.parseInt(scanner.nextLine());

                                switch (select2) {
                                    case 1:
                                        System.out.println("Addressee to transfer");
                                        String entradaTeclado3 = "";
                                        entradaTeclado3 = entradaEscaner.nextLine();
                                        System.out.println("Quantity");
                                        String entradaTeclado4 = "";
                                        entradaTeclado4 = entradaEscaner.nextLine();
                                        System.out.println("Successful transaction");
                                        break;
                                    case 2:
                                        System.out.println("Your balance is:");
                                        break;
                                    case 0:
                                        account = false;
                                        break;
                                }
                            }
                            men = 1;
                        } else if (!"2".equals(entradaTeclado2) || !"2".equals(entradaTeclado2)) {
                            System.out.println("Invalid account");
                            men = 1;
                        }
                        break;
                    case 3:
                        men = 1;
                        break;

                    case 0:
                        System.out.println("bye!");
                        men = 0;
                        break;
                    default:
                        System.out.println("Número no reconocido");
                        break;
                }

                System.out.println("\n");

            } catch (NumberFormatException e) {
                System.out.println("Uoop! Error!");
            } catch (GeneralSecurityException | IOException ex) {
                Logger.getLogger(WalletClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        closed = true;
    }

}
