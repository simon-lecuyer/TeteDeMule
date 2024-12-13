import java.rmi.Remote;
import java.rmi.RemoteException;
import java.net.UnknownHostException;

public interface Daemon extends Remote{
    public byte[] upload(String fileName, int fragmentBegin, int fragmentSize) throws RemoteException;

    public void notifyDiaryIn(Diary d) throws RemoteException, UnknownHostException;

    public void notifyDiaryOut(Diary d) throws RemoteException, UnknownHostException;
}

