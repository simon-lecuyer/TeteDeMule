import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Diary extends Remote {

    public void addFileUsers(String fileName, String[] usernames) throws RemoteException;

    public void deleteFileUsers(String fileName, String[] usernames) throws RemoteException;

    public String[] getFileUsers(String fileName) throws RemoteException;
}
