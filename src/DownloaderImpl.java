import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.util.ArrayList;


public class DownloaderImpl implements Downloader {
    private String queryingUser;
    private Diary diary;

    public DownloaderImpl(String queryingUser, String diaryHost) {
        try {
            this.queryingUser = queryingUser;
            diary = (Diary) Naming.lookup(diaryHost);
            System.out.println("Diary found : downloader");
            download("projet.pdf");
        } catch (Exception e) {
            System.out.println("Cannot connect to diary");
            e.printStackTrace();
        }
    }

    @Override
    public void download(String fileName) {
        try {
            ArrayList<String> usersList= diary.getFileUsers(fileName);
            int threadsNumber = usersList.size(); 
            Thread th[] = new DownloaderSlave[threadsNumber];

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
