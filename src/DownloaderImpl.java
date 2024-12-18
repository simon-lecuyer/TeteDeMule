import java.net.ServerSocket;
import java.rmi.Naming;
import java.rmi.RemoteException;


public class DownloaderImpl implements Downloader {

    Daemon[] daemon;
    Diary diary;

    public DownloaderImpl(String diaryHost) {
        try {
            diary = (Diary) Naming.lookup("//" + diaryHost + "/diary");
        } catch (Exception e) {
            System.out.println("Cannot connect to diary");
            e.printStackTrace();
        }
    }

    @Override
    public void download(String host, String fileName, int fragmentSize) {
        try {
            String[] users = diary.getFileUsers(fileName);
        } catch (RemoteException e) {
            System.out.println("No users available !");
        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }



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
