
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.Naming;
import java.util.Scanner;

public class TeteDeMule {
    private static boolean daemonRunning = true;

    static void ListFiles (Diary diary) {
        try {
            for (String file : diary.getAllFiles()) {
                System.out.println("- " + file + " : " + diary.getFileSize(file) + " bytes" +(diary.getFileUsers(file).size() > 0 ? " - " + diary.getFileUsers(file).size() + " users" : ""));
                System.out.println(diary.getFileUsers(file));
            }
            System.out.println("\n");
        } catch (Exception e) {
            System.out.println("Impossible de lister les fichiers");
        }

    }

    static void AddFiles (String pathname, Diary diary, String user, String diaryName) {

        try {
            File dir = new File(pathname);
            File[] files = dir.listFiles();
            for (File file : files) {
                FileUser newFile = new FileUserImpl(file.getName(), (int)file.length());
                diary.addFileUser(newFile, user);
            }
        } catch (Exception e) {
            System.out.println("Impossible d'ajouter les fichiers");
        }
    }

    static void DownloadFile (String fileName, Diary diary, String user, ServerSocket ss, String diaryName) {

        Thread downloaderThread = new Thread(() -> {
            System.out.println("Downloader Thread running.");
            new DownloaderImpl(user, diaryName+":4000/diary",fileName);
        });


        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    ss.close();
                    daemonRunning = false;
                    diary.userLeaves(user);
                } catch (IOException e) {
                    throw new RuntimeException("Erreur stop daemon");
                }
            }
        });

        downloaderThread.start();

        while (daemonRunning) {
            try{ new DaemonImpl(ss.accept()).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } 

    public static void main(String[] args) throws Exception {
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
       
        Diary diary = null;
        try {
            diary = (Diary)Naming.lookup(diaryName+":4000/diary");
            System.out.println("Carnet de notes trouvé");
        } catch (Exception e) {
            System.out.println("Impossible de trouver le diary");
            System.out.println("Voulez-vous réessayer ? (O/N)");
            String response = sc.nextLine();
            if (response.equals("N")){
                System.exit(0);
            }
            else{
                System.out.println("Entrez le nom du diary :");
                diaryName = sc.nextLine();
            }
        }

        final String diaryNameFinal = diaryName;
        final Diary diaryFinal = diary;

        System.out.println("Entrez le port du serveur :");

        String serverPort = sc.nextLine();
        String user = userId + ":" + serverPort;

        ServerSocket ss = null;

        try {
            int port = Integer.parseInt(serverPort);
            ss = new ServerSocket(port);
            System.out.println("Port valide");
        } catch (Exception e) {
            System.out.println("Port invalide");
            System.out.println("Voulez-vous réessayer ? (O/N)");
            String response = sc.nextLine();
            if (response.equals("N")){
                System.exit(0);
            }
            else{
                System.out.println("Entrez le port du serveur :");
                serverPort = sc.nextLine();
            }
        }

        final ServerSocket ssFinal = ss;


        boolean TeteDeMuleRunning = true;

        while (TeteDeMuleRunning) {
            System.out.println("Voici vos options :");
            System.out.println("1. Voir les fichiers disponibles");
            System.out.println("2. Télécharger un fichier");
            System.out.println("3. Ajouter un dossier à la liste des fichiers disponibles");
            System.out.println("4. Quitter");

            String option = sc.nextLine();
            if (option.equals("1")) {
                ListFiles(diaryFinal);
            } else if (option.equals("2")) {
                System.out.println("Entrez le nom du fichier à télécharger :");
                String fileName = sc.nextLine();
                DownloadFile(fileName, diaryFinal, user, ssFinal, diaryNameFinal);
            } else if (option.equals("3")) {
                System.out.println("Entrez le chemin du dossier à ajouter :");
                String pathname = sc.nextLine();
                AddFiles(pathname, diaryFinal, user, diaryNameFinal);
            } else if (option.equals("4")) {
                TeteDeMuleRunning = false;
                try {
                    ssFinal.close();
                    daemonRunning = false;
                    diaryFinal.userLeaves(user);
                    TeteDeMuleRunning = false;
                    sc.close();
                } catch (IOException e) {
                    throw new RuntimeException("Erreur stop daemon");
                }
            } else {
                System.out.println("Option invalide");
        }
    }
    System.exit(0);
    }
}