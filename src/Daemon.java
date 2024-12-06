import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Daemon extends Remote{
    public byte[] upload(String fileName, int fragmentBegin, int fragmentSize) throws RemoteException;
}
