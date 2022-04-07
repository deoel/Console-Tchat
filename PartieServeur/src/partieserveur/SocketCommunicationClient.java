package partieserveur;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketCommunicationClient implements Runnable {
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private String pseudoClient;
    
    public SocketCommunicationClient(Socket socket) {
        try {
            this.socket = socket;
            this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            
            this.initPseudoClient();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void initPseudoClient() {
        try {
            String pseudo = br.readLine();
            this.pseudoClient = pseudo;
            envoyerATous("[" + pseudo + "] a rejoint le tchat");
        } catch (IOException ex) {
            fermerConnexion();
            System.out.println("Un utilisateur vient de se deconnecter avant de se présenter");
        }
    }
    
    private void envoyerMessage(String msg){
        try {
            bw.write(msg);
            bw.newLine();
            bw.flush();
        } catch (IOException ex) {
            fermerConnexion();
        }
    }
    
    private void recevoirMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        String msg = br.readLine();
                        envoyerATous(msg);
                    } catch (IOException ex) {
                        envoyerATous("[" + pseudoClient + "] a quitté le tchat");
                        fermerConnexion();
                        break;
                    } 
                }
            }
        }).start();
    }
    
    private void envoyerATous(String msg){
        for (SocketCommunicationClient sc : Serveur.listeSocketClient) {
            if(!sc.socket.isClosed()) {
                if(!sc.pseudoClient.equals(this.pseudoClient)) {
                    sc.envoyerMessage(msg);
                }
            }
        }
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

    @Override
    public void run() {
        this.recevoirMessage();
    }
}
