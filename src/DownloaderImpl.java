<<<<<<< HEAD
=======
import java.io.File;
>>>>>>> old-state
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.util.ArrayList;


public class DownloaderImpl implements Downloader {
    private String queryingUser;
    private Diary diary;

<<<<<<< HEAD
    private HashMap<String, Daemon> daemon;
    private Diary diary;
    private String clientQuerying;

    public DownloaderImpl(String diaryHost, String clientQuerying) {
        try {
            this.clientQuerying = clientQuerying;
            daemon = new HashMap<>();
            diary = (Diary) Naming.lookup("//" + diaryHost + "/diary");
            System.out.println("Diary found");
=======
    public DownloaderImpl(String queryingUser, String diaryHost) {
        try {
            this.queryingUser = queryingUser;
            diary = (Diary) Naming.lookup(diaryHost);
            System.out.println("Diary found : downloader");
            download("projet.pdf");
>>>>>>> old-state
        } catch (Exception e) {
            System.out.println("Cannot connect to diary");
            e.printStackTrace();
        }
    }

    @Override
    public void download(String fileName) {
        try {
<<<<<<< HEAD
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
=======
            ArrayList<String> usersList= diary.getFileUsers(fileName);
            int threadsNumber = usersList.size(); 
            Thread th[] = new DownloaderSlave[threadsNumber];
>>>>>>> old-state

            FileUser newFile = new FileUserImpl(fileName, diary.getFileSize(fileName));
            for (int i = 0; i < threadsNumber; i++) {
                System.out.println("Thread : "+i);
                th[i] = new DownloaderSlave(queryingUser, newFile, usersList.get(i), i, threadsNumber);
                th[i].start();
            }

            for (Thread th1 : th) {
                th1.join();
            }

            System.out.println("Going to sleep...");
            Thread.sleep(4000);
            System.out.println("Wake up !");

            // Recomposed file
            FileOutputStream outputFile = new FileOutputStream("../Download/" + fileName);
            for (int i = 0; i < threadsNumber; i++) {
                System.out.println("i = "+i);
                String slotI = "{"+i+"}";
                //Recomposed slot {i} file
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
                File fileInputIdel = new File("../Download/" + slotI + fileName );
                if (fileInputIdel.delete()) {
                    System.out.println("File : " + slotI + fileName +" deleted");
                }
            }
            outputFile.close();
            System.out.println("End recomposed file");

        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
}
