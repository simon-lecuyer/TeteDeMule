import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;


public class DownloaderImpl implements Downloader {

    HashMap<String, Daemon> daemon;
    Diary diary;

    public DownloaderImpl(String diaryHost) {
        try {
            daemon = new HashMap<>();
            diary = (Diary) Naming.lookup("//" + diaryHost + "/diary");
            System.out.println("Diary found");
        } catch (Exception e) {
            System.out.println("Cannot connect to diary");
            e.printStackTrace();
        }
    }

    @Override
    public void download(String host, String fileName) {
        try {
            ArrayList<User> users = diary.getFileUsers(fileName);
            for (User user : users) {
                Daemon d = (Daemon) Naming.lookup("//" + user.getUsername() + "/daemon");
                daemon.put(user.getUsername(), d);
            }
            

        } catch (RemoteException e) {
            System.out.println("No users available !");
        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(8080);
            while (true) {
                Thread t = new Slave(ss.accept());
                t.start();
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // public void run() {
    //     try {
    //         int target = rand.nextInt(nbHosts);
    //         int portTarget = ports[target];
    //         String hostTarget = hosts[target];

    //         System.out.println("Balancing to socket : " + portTarget);

    //         // Les stream I/O du client vers le LB

    //         InputStreamReader clientIn;
    //         clientIn = new InputStreamReader(socketClient.getInputStream());
    //         PrintStream clientOut;
    //         clientOut = new PrintStream(socketClient.getOutputStream());
    //         String rq = new LineNumberReader(clientIn).readLine();
    //         System.out.println();
            

    //         // Créer la connection entre LB/server

    //         Socket web = new Socket(hostTarget, portTarget);
    //         System.out.println(web);
    //         // Les stream I/O du server

    //         InputStreamReader webOut;
    //         webOut = new InputStreamReader(web.getInputStream());
    //         DataOutputStream webIn;
    //         webIn = new DataOutputStream(web.getOutputStream());
            

    //         // Lire la requête du client et l'envoyer au server

    //         webIn.write(rq.getBytes());
    //         webIn.writeBytes("\n");

    //         // Renvoyer la réponse du server au client

    //         System.out.println("Lecture server");
    //         char[] buffer = new char[1024];
    //         var byteRead = webOut.read(buffer, 0, 1024);
    //         System.out.println("Lu : " + byteRead);

    //         // Envoie au client
    //         clientOut.println(buffer);
    //         clientOut.flush();


    //         // Fermer les sockets et les stream

    //         clientIn.close();
    //         clientOut.close();
    //         webIn.close();
    //         webOut.close();
    //         socketClient.close();
    //         web.close();
    //         System.out.println("Tout est fermé ");
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }
}
