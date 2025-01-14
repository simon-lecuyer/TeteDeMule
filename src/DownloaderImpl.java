import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;



public class DownloaderImpl implements Downloader {
    private String queryingUser;
    private Diary diary;

    /** Create the downloader for the client
     * 
     * @param queryingUser the user querying a download
     * @param diaryHost the name of the diary
     */
    public DownloaderImpl(String queryingUser, Diary diaryHost) {
        try {
            this.queryingUser = queryingUser;
            this.diary = diaryHost;
            displayDownload();
        } catch (Exception e) {
            System.out.println("Cannot connect to diary");
            e.printStackTrace();
        }
    }

    
    /** Main method to download the file
     * 
     * @param fileName name of the file to download
     */
    @Override
    public void download(String fileName) {
        try {
            long timeStart = System.currentTimeMillis();
            //% Get the users registered to a file
            ArrayList<String> usersList= diary.getFileUsers(fileName);
            int threadsNumber = usersList.size(); 
            Thread th[] = new DownloaderSlave[threadsNumber];

            //% Create download directory if it does not exist
            File downloadDir = new File("../Download");
            if (!downloadDir.exists()) {
                if (downloadDir.mkdirs()) {
                    System.out.println("Download directory created : " + downloadDir.getName());
                } else {
                    System.out.println("Failed to create download directory.");
                }
            }

            System.out.println("\n");

            //% Create a FileUser to ask the users to send with thread
            FileUser newFile = new FileUserImpl(fileName, diary.getFileSize(fileName));
            for (int i = 0; i < threadsNumber; i++) {
                th[i] = new DownloaderSlave(queryingUser, newFile, usersList.get(i), i, threadsNumber);
                th[i].start();
            }

            //% Wait all the thread to finish
            for (Thread th1 : th) {
                th1.join();
            }
            long timeAllFragments = System.currentTimeMillis();
    
            //% Recompose the file with all the fragments
            FileOutputStream outputFile = new FileOutputStream("../Download/" + fileName);
            for (int i = 0; i < threadsNumber; i++) {
                //% Recompose the fragment {i} file in the outputFile
                String slotI = "{"+i+"}";
                FileInputStream fileInputI = new FileInputStream("../Download/" + slotI + fileName );
                long slotSize = Files.size(Paths.get("../Download/" + slotI + fileName ));

                long byteRead = 0;
                int cursor;
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                while(byteRead < slotSize) {
                    cursor = fileInputI.read(buffer, 0, bufferSize);

                    byteRead += cursor;
                    outputFile.write(buffer, 0, cursor);
                }
                
                fileInputI.close();
                
                //% Delete the partial file {i}
                File fileInputIdel = new File("../Download/" + slotI + fileName );
                if (fileInputIdel.delete()) {
                    System.out.println("Partial file : " + slotI + fileName +" deleted");
                }
            }
            outputFile.close();
            long timeEnd = System.currentTimeMillis();
            
            System.out.println("End recomposed  : " + fileName + "\n");
            System.out.println("Time needed to download: " + ( timeEnd - timeStart) + "ms\n");
            System.out.println("Time to get all partial files : " + (timeEnd - timeAllFragments) + "ms\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }


    /** To list the files available from the diary with its users
     * 
     * @param diary the diary
     */
    private void ListFiles (Diary diary) {
        try {
            for (String file : diary.getAllFiles()) {
                System.out.println("- " + file + " : " + diary.getFileSize(file) + " bytes" +(diary.getFileUsers(file).size() > 0 ? " - " + diary.getFileUsers(file).size() + " users" : ""));
                System.out.println(diary.getFileUsers(file) + "\n");
            }
            System.out.println("\n");
        } catch (Exception e) {
            System.out.println("Impossible de lister les fichiers");
        }

    }

    /** Add all files to the diary from the directory pathname
     * 
     * @param pathname the path to directory
     * @param diary the diary to add the files
     * @param user the name of the user
     */
    private void AddFiles (String pathname, Diary diary, String user) {

        try {
            File dir = new File(pathname);
            File[] files = dir.listFiles();
            if (files.length > 0) {
                for (File file : files) {
                    //% Move the file to the folder ../Available to be upload with daemon later
                    file.renameTo(new File("../Available/"+file.getName()));
                    FileUser newFile = new FileUserImpl(file.getName(), (int)file.length());
                    diary.addFileUser(newFile, user);
                    System.out.println("Fichier ajouté : " + file.getName() + "\n");
                }
            } else {
                System.out.println("Pas de fichier à ajouter pour le dossier : " + pathname);
            }
            
        } catch (Exception e) {
            System.out.println("Impossible d'ajouter les fichiers");
        }
    }

    private void DeleteFile(String fileName, Diary diary, String user) {
        try {
            ArrayList<String> allFiles = diary.getAllFiles();
            if (allFiles.contains(fileName)) {
                if (diary.getFileUsers(fileName).contains(user)) {
                    FileUser file = new FileUserImpl(fileName, diary.getFileSize(fileName));
                    diary.deleteFileUser(file, user);
                    System.out.println("Fichier supprimé : " + fileName);
                } else {
                    System.out.println("Vous n'avez pas ce fichier à disposition d'envoi");
                }
            } else {
                System.out.println("Le fichier : " + fileName + "n'est pas dans la liste des fichiers disponibles à télécharger, donc supprimer");
            }
        } catch (RemoteException e) {
            System.out.println("Erreur de suppression de fichier");
        } 
    }


    public void displayDownload() {

        System.out.println("Bienvenue dans Tête de Mule, " + queryingUser + " !\n");
        
        Scanner sc = new Scanner(System.in);
        try {
            while (TeteDeMule.daemonRunning) {
                System.out.println("=========================================");
                System.out.println("Voici vos options :");
                System.out.println("1. Voir les fichiers disponibles");
                System.out.println("2. Télécharger un fichier");
                System.out.println("3. Ajouter un dossier à la liste des fichiers disponibles");
                System.out.println("4. Supprimer un fichier disponible au téléchargement");
                System.out.println("5. Quitter");
                System.out.println("=========================================");
                System.out.println("Entrez le numéro de l'option choisie :");

                String option = sc.nextLine();

                System.out.println("=========================================\n");

                switch (option) {
                    case "1":
                        ListFiles(diary);
                        break;
                    case "2":
                        {
                            System.out.println("Entrez le nom du fichier à télécharger :");
                            String fileName = sc.nextLine();
                            ArrayList<String> allFiles = diary.getAllFiles();
                            if (allFiles != null){
                                if (allFiles.contains(fileName)){
                                    download(fileName);
                                } else{
                                    System.out.println("Erreur, le Diary ne contient pas le fichier : " + fileName + "\n");
                                }
                            } else{
                                System.out.println("Aucun fichier dans le Diary, impossible de télécharger\n");
                            }       break;
                        }
                    case "3":
                        System.out.println("Entrez le chemin du dossier à ajouter :");
                        String pathname = sc.nextLine();
                        System.out.println("\n");
                        AddFiles(pathname, diary, queryingUser);
                        break;
                    case "4":
                        {
                            System.out.println("Entrez le nom du fichier à supprimer :");
                            String fileName = sc.nextLine();
                            DeleteFile(fileName, diary, queryingUser);
                            break;
                        }
                    case "5":
                        TeteDeMule.daemonRunning = false;
                        
                        System.exit(0);
                    default:
                        System.out.println("Option invalide");
                        break;
                }
            } 
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        sc.close();
    }
}
