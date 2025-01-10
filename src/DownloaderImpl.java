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

    /** Create the downloader for the client
     * 
     * @param queryingUser the user querying a download
     * @param diaryHost the name of the diary
     */
    public DownloaderImpl(String queryingUser, String diaryHost) {
        try {
            this.queryingUser = queryingUser;
            diary = (Diary) Naming.lookup(diaryHost);
            System.out.println("Diary found : Downloader");
            download("projet.pdf");
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
            System.out.println("End recomposed  : " + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
}
