import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class DaemonImpl extends UnicastRemoteObject implements Daemon {

    // A film is stored : name, path
    HashMap<String, String> filmRegistry;

    public DaemonImpl() throws RemoteException {
        filmRegistry = new HashMap<String, String>();
    }

    @Override
    public byte[] upload(String fileName, int fragmentBegin, int fragmentSize) throws RemoteException {

        

        return null;
    }

    // public static void main(String[] args) {
    //     try {
    //         Daemon d = new DaemonImpl();
    //         Naming.rebind("//"+ InetAddress.getLocalHost().getHostName()+"/test",  d);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }
}
