import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;


public class DownloaderImpl implements Downloader {

    HashMap<String, Daemon> daemon;
    Diary diary;

    public DownloaderImpl(String diaryHost) {
        try {
            daemon = new HashMap<>();
            diary = (Diary) Naming.lookup("//" + diaryHost + "/diary");
        } catch (Exception e) {
            System.out.println("Cannot connect to diary");
            e.printStackTrace();
        }
    }

    @Override
    public void download(String host, String fileName, int fragmentSize) {
        try {
            ArrayList<User> users = diary.getFileUsers(fileName);
            for (User user : users) {
                Daemon d = (Daemon) Naming.lookup("//" + user.getUsername() + "/daemon");
                daemon.put(user.getUsername(), d);
            }
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
