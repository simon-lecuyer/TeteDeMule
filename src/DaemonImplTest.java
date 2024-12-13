import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.nio.file.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.net.UnknownHostException;
import java.net.InetAddress;

public class DaemonImplTest {

    private DaemonImpl daemon;
    private DiaryImpl diary;
    private Path testFolder;

    @Before
    public void setUp() throws RemoteException, IOException {
        daemon = new DaemonImpl();
        diary = new DiaryImpl();
        testFolder = Files.createTempDirectory("testFolder");
    }

    @Test
    public void testUpload() throws RemoteException, IOException {
        String fileName = "testFile.txt";
        Path filePath = Files.createFile(testFolder.resolve(fileName));
        Files.write(filePath, "0123456789".getBytes());

        daemon.fileRegistry.put(fileName, filePath.toString());

        byte[] fragment = daemon.upload(fileName, 0, 10);
        assertNotNull(fragment);
        assertEquals(10, fragment.length);
    }

    @Test
    public void testUploadFileNotFound() throws RemoteException {
        byte[] fragment = daemon.upload("nonExistentFile.txt", 0, 10);
        assertNull(fragment);
    }

    @Test
    public void testNotifyDiaryIn() throws RemoteException, UnknownHostException, IOException {
        String fileName = "testFile.txt";
        Path filePath = Files.createFile(testFolder.resolve(fileName));
        Files.write(filePath, "0123456789".getBytes());

        daemon.fileRegistry.put(fileName, filePath.toString());

        daemon.notifyDiaryIn(diary);
        assertTrue(diary.getFileUsers(fileName).contains(InetAddress.getLocalHost().getHostName()));
    }

    @Test
    public void testNotifyDiaryOut() throws RemoteException, UnknownHostException, IOException {
        String fileName = "testFile.txt";
        Path filePath = Files.createFile(testFolder.resolve(fileName));
        Files.write(filePath, "0123456789".getBytes());

        daemon.fileRegistry.put(fileName, filePath.toString());

        daemon.notifyDiaryIn(diary);
        daemon.notifyDiaryOut(diary);
        assertFalse(diary.getFileUsers(fileName).contains(InetAddress.getLocalHost().getHostName()));
    }
}
