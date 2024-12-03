import java.io.*;
import java.net.Socket;
import java.util.Random;

public class Slave extends Thread {
    static String hosts[] = { "localhost", "localhost" };
    static int ports[] = { 8081, 8082 };
    static int nbHosts = 2;
    static Random rand = new Random();
    Socket socketClient;

    public Slave(Socket s) {
        this.socketClient = s;
    }

    public void run() {
        try {
            int target = rand.nextInt(nbHosts);
            int portTarget = ports[target];
            String hostTarget = hosts[target];

            System.out.println("Balancing to socket : " + portTarget);

            // Les stream I/O du client vers le LB

            InputStreamReader clientIn;
            clientIn = new InputStreamReader(socketClient.getInputStream());
            PrintStream clientOut;
            clientOut = new PrintStream(socketClient.getOutputStream());
            String rq = new LineNumberReader(clientIn).readLine();
            System.out.println();
            

            // Créer la connection entre LB/server

            Socket web = new Socket(hostTarget, portTarget);
            System.out.println(web);
            // Les stream I/O du server

            InputStreamReader webOut;
            webOut = new InputStreamReader(web.getInputStream());
            DataOutputStream webIn;
            webIn = new DataOutputStream(web.getOutputStream());
            

            // Lire la requête du client et l'envoyer au server

            webIn.write(rq.getBytes());
            webIn.writeBytes("\n");

            // Renvoyer la réponse du server au client

            System.out.println("Lecture server");
            char[] buffer = new char[1024];
            var byteRead = webOut.read(buffer, 0, 1024);
            System.out.println("Lu : " + byteRead);

            // Envoie au client
            clientOut.println(buffer);
            clientOut.flush();


            // Fermer les sockets et les stream

            clientIn.close();
            clientOut.close();
            webIn.close();
            webOut.close();
            socketClient.close();
            web.close();
            System.out.println("Tout est fermé ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
