
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

public class TeteDeMule {
    public static boolean daemonRunning = true;

    /** Add all files to the diary from the directory pathname
     * 
     * @param pathname the path to directory
     * @param diary the diary to add the files
     * @param user the name of the user
     */
    static void AddFiles (String pathname, Diary diary, String user) {

        try {
            File dir = new File(pathname);
            File[] files = dir.listFiles();
            for (File file : files) {
                FileUser newFile = new FileUserImpl(file.getName(), (int)file.length());
                diary.addFileUser(newFile, user);
            }
        } catch (RemoteException e) {
            System.out.println("Impossible d'ajouter les fichiers");
        }
    }

    public static void main(String[] args) throws Exception {
        try {

            System.out.println(" _____    _            _       __  __      _     \n" + //
                           "|_   _|__| |_ ___   __| |___  |  \\/  |_  _| |___ \n" + //
                           "  | |/ -_)  _/ -_) / _` / -_) | |\\/| | || | / -_)\n" + //
                           "  |_|\\___|\\__\\___| \\__,_\\___| |_|  |_|\\_,_|_\\___|");

            System.out.println("\n Bienvenue dans Tête de Mule !\n");
        
            Scanner sc = new Scanner(System.in);

            //% the name of the user, working only on N7, getHostAddress to get the IP
            String userId = InetAddress.getLocalHost().getHostAddress();


            System.out.println("Entrez le nom du diary :");
            String diaryName = sc.nextLine();

            Diary diary;

            diary = (Diary)Naming.lookup("//" + diaryName+":4000/diary");

          

            System.out.println("Entrez le port du serveur :");

            String serverPort = sc.nextLine();
            String user = userId + ":" + serverPort;

            ServerSocket ss;

            int port = Integer.parseInt(serverPort);
            ss = new ServerSocket(port);
            System.out.println("Server Socket créé\n");
        

            //% Check if the directory of available files exists 
            if (Files.notExists(Paths.get("../Available"))) {
                Files.createDirectory(Paths.get("../Available"));
            }
            AddFiles("../Available", diary, user);


            //% The main thread for the client to download, see all files, ...
            Thread runDownloader = new Thread(() -> {
                System.out.println("Downloader Thread running");

                //! Changed the constructor with diary to do not lookup twice in the Registry
                new DownloaderImpl(user, diary);
            });

            //% When the user disconnect, to leave the diary and stop de Daemon 
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        ss.close();
                        daemonRunning = false;
                        diary.userLeaves(user);
                    } catch (IOException e) {
                        throw new RuntimeException("Stop Daemon error : " + e.getMessage());
                    }
                }
            });

            runDownloader.start();

            //% Start the Daemon to upload fragment when solliciting
            while (daemonRunning) {
                new DaemonImpl(ss.accept()).start();
            }

        } catch (RemoteException e) {
            System.out.println("Diary non trouvé");
        } catch (NumberFormatException e) {
            System.out.println("Le port n'est pas un entier");
        } catch (IOException e) {
            System.out.println("Server port erreur");
        }
        catch (Exception e) {
            e.printStackTrace();
        }        
    }
}