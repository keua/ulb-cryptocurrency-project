
package com.ulb.cryptography.network;
import java.io.Console;
import java.util.Scanner;
/**
 *
 * @author Oscarand
 */
public class ClaseMenu {
 static Scanner scanner = new Scanner(System.in);
static int select = -1; //opción 
    
public ClaseMenu (){
}


public void setMenu(){


			try{
				System.out.println("Options:\n1.- " +
						"\n2.- Create new addresses\n" +
						"3.-  Full copy of teh blockchain\n" +
					"4.-  Tranfer\n" +	
                                        "0.- Exit");
				
				select = Integer.parseInt(scanner.nextLine()); 
	
				
				switch(select){
				case 1: 
					
					break;
				case 2: 
					
                                        break;
				case 3: 
						
					break;
				case 4: 
						System.out.println ("Cantidad");
					break;
				
                                case 0: 
					System.out.println("bye!");
					break;
				default:
					System.out.println("Número no reconocido");break;
				}
				
				System.out.println("\n"); 
				
			}catch(Exception e){
				System.out.println("Uoop! Error!");
			}
		}





  

}


