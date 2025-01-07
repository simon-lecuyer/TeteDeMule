import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;


public class DownloaderImpl implements Downloader {

    private HashMap<String, Daemon> daemon;
    private Diary diary;
    private String clientQuerying;

    public DownloaderImpl(String diaryHost, String clientQuerying) {
        try {
            this.clientQuerying = clientQuerying;
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
            Slave[] th = new Slave[users.size()];

            for (int i = 1; i <= users.size(); i++) {
                th[i-1] = new Slave(host, fileName, clientQuerying, 10000, users.size(), i);
                th[i-1].start();
            }

            for (int i = 0; i < users.size(); i++) {
                try {
                    th[i].join();
                } catch (InterruptedException e) {
                    throw new RuntimeException("Thread : " + i + " download error");
                }
            }

            if (users.size() != 1) {
            // Fichier final
            FileOutputStream fichierFinal = new FileOutputStream("Output/" + fileName);
            for (int i = 1; i <= users.size(); i++) {
                // Récupération du fragment i
                FileInputStream fileInputStream = new FileInputStream("Output/" + fileName + "(" + i + ")");
                long size = Files.size(Paths.get("Output/" + fileName + "(" + i + ")"));

                long sizeRead = 0;
                int currentRead;
                // On vérifie que la taille du fichier n'est pas trop grosse
                if (size > Integer.MAX_VALUE*0.001) {
                    byte[] buffer = new byte[(int)(Integer.MAX_VALUE*0.0001)];
                    /* Envoyer le fichier */
                    while (sizeRead < size) {
                        currentRead = fileInputStream.read(buffer, 0, (int)(Integer.MAX_VALUE*0.0001));
                        sizeRead += currentRead;
                        // Écriture du fragment i dans le fichier final
                        fichierFinal.write(buffer, 0, currentRead);
                    }
                } else {
                    int smallerSize = (int) ((size > 2000) ? size / 10 : size);
                    byte[] buffer = new byte[smallerSize];
                    while (sizeRead < size) {
                        currentRead = fileInputStream.read(buffer, 0, smallerSize);
                        sizeRead += currentRead;
                        // Écriture du fragment i dans le fichier final
                        fichierFinal.write(buffer, 0, currentRead);
                    }
                }
                fileInputStream.close();
            }
            fichierFinal.close();
        }

        } catch (RemoteException e) {
            System.out.println("No users available !");
        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    // Regarder TD1 pour avoir le téléchargement de fragments sur plusieurs machines

    public static void main(String[] args) {
        try {
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}
