package com.ulb.cryptography.test;

import java.util.Scanner;

/**
 *
 * @author Oscarand
 */
public class ClaseMenu {

    static Scanner scanner = new Scanner(System.in);
    static int select = -1; //opción 
    static int men = 1;

    public ClaseMenu() {
    }

    public void setMenu() {
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
                        String entradaTeclado5 = "";
                        entradaTeclado5 = entradaEscaner.nextLine();
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
            }
        }
    }

}
