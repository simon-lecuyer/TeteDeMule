import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Diary extends Remote {

    public void addFileUsers(String fileName, ArrayList<User> users) throws RemoteException;

    public void deleteFileUsers(String fileName, ArrayList<User> users) throws RemoteException;

    public ArrayList<User> getFileUsers(String fileName) throws RemoteException;
}
