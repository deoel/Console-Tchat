package partieclient;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class PartieClient {

    public static void main(String[] args){
        try {
            Socket s = new Socket("localhost", 1234);
            Scanner clavier = new Scanner(System.in);
            System.out.print("Saisir le pseudo : ");
            String pseudo = clavier.nextLine();
            SocketClient sc = new SocketClient(s, pseudo);
        } catch (IOException ex) {
            System.out.println("Le serveur n'est pas connecté ou l'adresse n'est pas valide!!!");
        }
    }
    
}
