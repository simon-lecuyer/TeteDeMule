import java.net.ServerSocket;


public class DownloaderImpl implements Downloader {
public static void main(String[] args) {
    try {
        ServerSocket ss = new ServerSocket(8080);
        while (true) {
            Thread t = new Slave(ss.accept());
            t.start();
            
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
