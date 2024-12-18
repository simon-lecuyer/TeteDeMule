import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class DaemonImplTest {

    private Path createTempTestFile() throws Exception {
        Path tempFile = Files.createTempFile("testFile", ".txt");
        Files.write(tempFile, "This is a test file content".getBytes(), StandardOpenOption.WRITE);
        return tempFile;
    }

    public void testUpload() {
        try {
            DaemonImpl daemon = new DaemonImpl();
            Path tempFile = createTempTestFile();
            daemon.fileRegistry.put("testFile", tempFile.toString());

            byte[] fragment = daemon.upload("testFile", 0, 10);
            if (fragment != null && fragment.length == 10) {
                System.out.println("testUpload réussi");
            } else {
                System.out.println("testUpload échoué");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("testUpload échoué");
        }
    }

    public void testNotifyDiaryIn() {
        try {
            DaemonImpl daemon = new DaemonImpl();
            DiaryImpl diary = new DiaryImpl();
            Path tempFile = createTempTestFile();
            daemon.fileRegistry.put("testFile", tempFile.toString());
            daemon.notifyDiaryIn(diary);

            if (diary.getFileUsers("testFile") != null) {
                System.out.println("testNotifyDiaryIn réussi");
            } else {
                System.out.println("testNotifyDiaryIn échoué");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("testNotifyDiaryIn échoué");
        }
    }

    public void testNotifyDiaryOut() {
        try {
            DaemonImpl daemon = new DaemonImpl();
            DiaryImpl diary = new DiaryImpl();
            Path tempFile = createTempTestFile();
            daemon.fileRegistry.put("testFile", tempFile.toString());
            System.out.println(diary.getFileUsers("testFile"));
            daemon.notifyDiaryIn(diary);
            System.out.println(diary.getFileUsers("testFile"));
            daemon.notifyDiaryOut(diary);
            System.out.println(diary.getFileUsers("testFile"));
            
            if (diary.getFileUsers("testFile") == null) {
                System.out.println("testNotifyDiaryOut réussi");
            } else {
                System.out.println("testNotifyDiaryOut échoué");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("testNotifyDiaryOut échoué");
        }
    }

    public static void main(String[] args) {
        DaemonImplTest test = new DaemonImplTest();
        test.testUpload();
        test.testNotifyDiaryIn();
        test.testNotifyDiaryOut();

        System.exit(0);
    }
}