import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Diary extends Remote {

    public void addFileUser(String fileName, String user) throws RemoteException;

    public void deleteFileUser(String fileName, String users) throws RemoteException;

    public ArrayList<User> getFileUsers(String fileName) throws RemoteException;
}
