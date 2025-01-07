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
            diary = (Diary) Naming.lookup(diaryHost + "/diary");
            System.out.println("Diary found : downloader");
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
                
                th[i] = new DownloaderSlave(newFile, usersList.get(i), i, threadsNumber);
                th[i].start();
            }

            for (int i = 0; i < th.length; i++) {
                th[i].join();
            }

            // Recomposed file
            FileOutputStream outputFile = new FileOutputStream("../Download/" + fileName);
            for (int i = 1; i <= threadsNumber; i++) {
                String slotI = "{"+i+"}";
                //Recomposed slot {i} file
                FileInputStream fileInputI= new FileInputStream("../Download/" + slotI + fileName );
                long slotSize = Files.size(Paths.get("../Download/" + slotI + fileName ));

                long byteRead = 0;
                int cursor;
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                while(byteRead < slotSize) {
                    cursor = fileInputI.read(buffer, 0, 512);

                    byteRead += cursor;
                    outputFile.write(buffer, 0, cursor);
                }
                
                fileInputI.close();
            }
            outputFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }

    

}
