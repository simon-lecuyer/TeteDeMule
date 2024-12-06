import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Diary extends Remote {

    public void createFile(String fileName, String username) throws RemoteException;

    public void updateFile(String fileName, String[] usernames) throws RemoteException;

    public String[] getFileUsers(String fileName) throws RemoteException;

    public void deleteFileUsers(String fileName, String[] usernames) throws RemoteException;
}
