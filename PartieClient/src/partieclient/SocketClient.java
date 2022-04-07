package partieclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private String pseudo;
    
    public SocketClient(Socket socket, String pseudo) {
        try {
            this.socket = socket;
            this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            this.pseudo = pseudo;
            
            this.envoyerPseudo();
            this.recevoirMessage();
            this.envoyerMessage();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void envoyerPseudo() {
        try {
            bw.write(this.pseudo);
            bw.newLine();
            bw.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void envoyerMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Scanner clavier = new Scanner(System.in);
                        String msg = clavier.nextLine();
                        bw.write("[" + pseudo + "] : " + msg);
                        bw.newLine();
                        bw.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
        
    }
    
    private void recevoirMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        String msg = br.readLine();
                        System.out.println(msg);
                    } catch (IOException ex) {
                        System.out.println("Le serveur n'est plus en ligne...");
                        System.out.println("Bye");
                        fermerConnexion();
                        System.exit(0);
                    } 
                }
            }
        }).start();
    }
    
    private void fermerConnexion() {
        try {
            socket.close();
            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
