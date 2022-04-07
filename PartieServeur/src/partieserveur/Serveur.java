package partieserveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Serveur {
    private ServerSocket ss;
    public static ArrayList<SocketCommunicationClient> listeSocketClient;
    
    public Serveur(int port) {
        try {
            this.ss = new ServerSocket(port);
            Serveur.listeSocketClient = new ArrayList<>();
        } catch (IOException ex) {
            System.out.println("Un autre serveur s'exécute sur la même adresse et le même port!!!");
            System.exit(0);
        }
    }
    
    public void demarrer() {
        System.out.println("Le serveur vient de démarrer...");
        while(true) {
            try {
                Socket nouvelleConnexion = this.ss.accept();
                SocketCommunicationClient scc = new SocketCommunicationClient(nouvelleConnexion);
                Serveur.listeSocketClient.add(scc);
                new Thread(scc).start();
                System.out.println("Une nouvelle personne s'est connectée");
            } catch (IOException ex) {
                System.out.println("Une tentative de connexion a échoué!!!");
            }
        }
    }
}
