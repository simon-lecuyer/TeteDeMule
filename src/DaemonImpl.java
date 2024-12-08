import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class DaemonImpl extends UnicastRemoteObject implements Daemon {

    // A film is store : name, path
    HashMap<String, String> filmRegistry;

    public DaemonImpl() throws RemoteException {
        filmRegistry = new HashMap<String, String>();
    }

    @Override
    public byte[] upload(String fileName, int fragmentBegin, int fragmentSize) throws RemoteException {

        return null;
    }
}
