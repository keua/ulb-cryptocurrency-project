/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.network;

import java.io.Console;
import java.util.Scanner;
public class Login {

   
    public static void main(String[] args)  {
    
        

        System.out.println ("Login");
        System.out.println ("User:");
        String entradaTeclado = "";
        Scanner entradaEscaner = new Scanner (System.in); 
        entradaTeclado = entradaEscaner.nextLine (); 
        System.out.println ("Entrada recibida por teclado es: \"" + entradaTeclado +"\"");
        
        System.out.println ("password:");
        String entradaTeclado2 = "";
        
        Scanner entradaEscaner2 = new Scanner (System.in); 
        
       
        entradaTeclado2 = entradaEscaner.nextLine ();
        System.out.println ("Entrada recibida por teclado es: \"" + entradaTeclado2 +"\"");

  }
}
