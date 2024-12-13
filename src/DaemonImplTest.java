import java.nio.file.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.util.Arrays;

public class DaemonImplTest {

    private DaemonImpl daemon;
    private DiaryImpl diary;
    private Path testFolder;

    public void setUp() throws RemoteException, IOException {
        daemon = new DaemonImpl();
        diary = new DiaryImpl();
        testFolder = Files.createTempDirectory("testFolder");
    }

    public void testUpload() throws RemoteException, IOException {
        String fileName = "testFile1.txt";
        Path filePath = Files.createFile(testFolder.resolve(fileName));
        Files.write(filePath, "0123456789".getBytes());

        daemon.fileRegistry.put(fileName, filePath.toString());

        byte[] fragment = daemon.upload(fileName, 0, 10);
        if (fragment == null || fragment.length != 10) {
            System.out.println("testUpload failed");
        } else {
            System.out.println("testUpload passed");
        }
    }

    public void testUploadFileNotFound() throws RemoteException {
        byte[] fragment = daemon.upload("nonExistentFile.txt", 0, 10);
        if (fragment != null) {
            System.out.println("testUploadFileNotFound failed");
        } else {
            System.out.println("testUploadFileNotFound passed");
        }
    }

    public void testNotifyDiaryIn() throws RemoteException, UnknownHostException, IOException {
        String fileName = "testFile2.txt";
        Path filePath = Files.createFile(testFolder.resolve(fileName));
        Files.write(filePath, "0123456789".getBytes());

        daemon.fileRegistry.put(fileName, filePath.toString());

        daemon.notifyDiaryIn(diary);
        if (!Arrays.asList(diary.getFileUsers(fileName)).contains(InetAddress.getLocalHost().getHostName())) {
            System.out.println("testNotifyDiaryIn failed");
        } else {
            System.out.println("testNotifyDiaryIn passed");
        }
    }

    public void testNotifyDiaryOut() throws RemoteException, UnknownHostException, IOException {
        String fileName = "testFile3.txt";
        Path filePath = Files.createFile(testFolder.resolve(fileName));
        Files.write(filePath, "0123456789".getBytes());

        daemon.fileRegistry.put(fileName, filePath.toString());

        daemon.notifyDiaryIn(diary);
        daemon.notifyDiaryOut(diary);
        if (Arrays.asList(diary.getFileUsers(fileName)).contains(InetAddress.getLocalHost().getHostName())) {
            System.out.println("testNotifyDiaryOut failed");
        } else {
            System.out.println("testNotifyDiaryOut passed");
        }
    }

    public static void main(String[] args) throws Exception {
        DaemonImplTest test = new DaemonImplTest();
        test.setUp();
        test.testUpload();
        test.testUploadFileNotFound();
        test.testNotifyDiaryIn();
        test.testNotifyDiaryOut();
        System.exit(0);
    }
}
