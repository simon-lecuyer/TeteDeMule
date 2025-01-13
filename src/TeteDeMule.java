
import java.io.File;
import java.io.IOException;
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
            // String userId = args[0];
            // String diaryName = args[1];
            // Diary diary = (Diary)Naming.lookup(diaryName+":4000/diary");
            // System.out.println("Diary found : " + diaryName +":4000/diary");

            // int serverPort = Integer.parseInt(args[2]);
            // String user = userId + ":" + serverPort;
            
            // ServerSocket ss = new ServerSocket(serverPort);


            // //% Add the user and its files available
            // File dir = new File("../Available");
            // File[] files = dir.listFiles();
            // for (File file : files) {
            //     FileUser newFile = new FileUserImpl(file.getName(), (int)file.length());
            //     diary.addFileUser(newFile, user);
            // }
            
            // //% Print all the files available to download
            // for (String file : diary.getAllFiles()) {
            //     System.out.println("- " + file);
            //     System.out.println(diary.getFileUsers(file));
            // }
            // System.out.println("\n");

            

            System.out.println(" _____    _            _       __  __      _     \n" + //
                           "|_   _|__| |_ ___   __| |___  |  \\/  |_  _| |___ \n" + //
                           "  | |/ -_)  _/ -_) / _` / -_) | |\\/| | || | / -_)\n" + //
                           "  |_|\\___|\\__\\___| \\__,_\\___| |_|  |_|\\_,_|_\\___|");

            System.out.println("\n Bienvenue dans Tête de Mule !\n");
        
            Scanner sc = new Scanner(System.in);

            System.out.println("Entrez  votre ID :");
            String userId = sc.nextLine();


            System.out.println("Entrez le nom du diary :");
            String diaryName = sc.nextLine();

            Diary diary;

            diary = (Diary)Naming.lookup("//" + diaryName+":4000/diary");


            if (Files.notExists(Paths.get("../Available"))) {
                Files.createDirectory(Paths.get("../Available"));
            }

          
            System.out.println("Entrez le port du serveur :");

            String serverPort = sc.nextLine();
            String user = userId + ":" + serverPort;

            ServerSocket ss;

            int port = Integer.parseInt(serverPort);
            ss = new ServerSocket(port);
            System.out.println("Server Socket créé\n");
        


            AddFiles("../Available", diary, user);


            //% The main thread for the client to download, see all files, ...
            Thread runDownloader = new Thread(() -> {
                System.out.println("Downloader Thread running");
                new DownloaderImpl(user, diary);
            });

            //% When the user disconnect, to leave the diary
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