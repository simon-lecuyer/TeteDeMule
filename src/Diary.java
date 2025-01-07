import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Diary extends Remote {

    public void addFileUser(FileUser file, String user) throws RemoteException;

    public void deleteFileUser(FileUser file, String user) throws RemoteException;

    public ArrayList<String> getFileUsers(String fileName) throws RemoteException;

    public ArrayList<String> getAllFiles() throws RemoteException;

    public void userLeaves(String user) throws RemoteException;

    public int getFileSize(String fileName);
}
