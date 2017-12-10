/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.network;

import com.ulb.cryptography.cryptocurrency.Account;
import com.ulb.cryptography.cryptocurrency.Blockchain;
import com.ulb.cryptography.cryptocurrency.Transaction;
import com.ulb.cryptography.cryptocurrency.Wallet;
import java.io.IOException;
import java.io.ObjectInputStream;
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

    private static final Logger LOGGER = Logger.getLogger(WalletClient.class.getName());
    private static Socket[] clientSockets = new Socket[2];
    private static ObjectOutputStream[] oosarray = new ObjectOutputStream[2];
    private static ObjectInputStream[] oisarray = new ObjectInputStream[2];
    private static boolean closed = false;
    static Wallet WALLET;
    private static final int[] DEFAULT_RELAY_PORT_NUMBER = {2223, 2222};
    private static final String DEFAULT_HOST = "localhost";

    public static void main(String[] args) throws GeneralSecurityException {

        WALLET = new Wallet();

        int portNumber[] = DEFAULT_RELAY_PORT_NUMBER;
        String host = DEFAULT_HOST;

        if (args.length < 2) {
            LOGGER.log(
                    Level.INFO,
                    "Now using host={0}, portNumber={1}",
                    new Object[]{host, portNumber}
            );
        } else {
            host = args[0];
            portNumber[0] = Integer.parseInt(args[1]);
            portNumber[1] = Integer.parseInt(args[2]);
            LOGGER.log(
                    Level.INFO,
                    "Now using host={0}, portNumber={1}",
                    new Object[]{host, portNumber}
            );
        }

        /*
         * Open a socket on a given host and port. Open input and output streams.
         */
        try {
            for (int i = 0; i < portNumber.length; i++) {
                System.out.println(portNumber[i]);
                clientSockets[i] = new Socket(host, portNumber[i]);
                oosarray[i] = new ObjectOutputStream(clientSockets[i].getOutputStream());
                oisarray[i] = new ObjectInputStream(clientSockets[i].getInputStream());
            }

        } catch (UnknownHostException e) {

            LOGGER.log(Level.SEVERE, "Don't know about host {0}", host);

        } catch (IOException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Couldn''t get I/O for the connection to the host {0}",
                    host
            );
        }

        /*
         * If everything has been initialized then we want to write some data to the
         * socket we have opened a connection to on the port portNumber.
         */
        if (clientSockets[0] != null && oosarray[0] != null && oisarray[0] != null) {
            try {

                /* Create a thread to read from the server. */
                new Thread(new WalletClient()).start();
                while (!closed) {

                    Message messageFromClient = (Message) oisarray[0].readObject();
                    Object objectInMessage = messageFromClient.getObject();
                    LOGGER.log(Level.INFO, "Im here waiting messages from the relay");
                    if (Blockchain.class.isInstance(objectInMessage)) {
                        System.out.println("Getting the new blockchain");
                        Blockchain newBlockchain = (Blockchain) objectInMessage;
                        WALLET.setBlockchain(newBlockchain);
                        LOGGER.log(
                                Level.INFO,
                                "Blocks in the new blockchain {0}",
                                newBlockchain.getListOfBlocks().size()
                        );
                    }

                    for (ObjectOutputStream oos : oosarray) {
                        oos.writeObject(new Message(new StringBuilder("hi everyone")));
                    }

                    WalletClient.walletMenu();

                }
                /*
                 * Close the output stream, close the input stream, close the socket.
                 */
                oosarray[0].close();
                oisarray[0].close();
                clientSockets[0].close();
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            } catch (ClassNotFoundException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
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
                        String idAccount = WALLET.createAccount(password);
                        System.out.println("your idAccount is: " + idAccount);
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

                        Account activeAccount = WALLET.login(entradaTeclado, entradaTeclado2);

                        if (activeAccount != null) {
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

                                        String addressToSend = entradaTeclado3;
                                        String addressFromSend = activeAccount.getStrAddress();
                                        Float total
                                                = WALLET.getBlockchain()
                                                        .getLastTransactionBySenderAddress(
                                                                addressFromSend
                                                        );
                                        Float toSend = Float.parseFloat(entradaTeclado4);
                                        // creating the transaction
                                        Transaction t = new Transaction(
                                                total,
                                                toSend,
                                                total - toSend,
                                                addressFromSend,
                                                addressToSend,
                                                new Date()
                                        );
                                        t.setTranType("normal");
                                        // sign the transaction before send to the relay
                                        t.signTransaction(activeAccount.getPrivateKey());

                                        System.out.println(t.getFltInputSenderAmount()); // 10000000
                                        System.out.println(t.getFltOutputReceiverAmount());//100
                                        System.out.println(t.getFltOutputSenderAmount());//1000000-100

                                        for (ObjectOutputStream oos : oosarray) {
                                            oos.writeObject(new Message(t));// send the transaction to the relay server
                                        }

                                        System.out.println("Successful transaction");
                                        break;
                                    case 2:
                                        System.out.println("Your balance is:");
                                        break;
                                    case 0:
                                        account = false;
                                        break;
                                    default:
                                        System.out.println("Choose a valid option");
                                        break;
                                }
                            }
                            men = 1;
                        } else {
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
                LOGGER.log(Level.SEVERE, "Uoop! Error!", e);
            } catch (GeneralSecurityException | IOException ex) {
                LOGGER.log(Level.SEVERE, "Ge", ex);
            }
        }
        closed = true;
    }

}
