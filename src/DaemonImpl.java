import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.net.UnknownHostException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class DaemonImpl extends UnicastRemoteObject implements Daemon {

    // A file is stored : name, path
    HashMap<String, String> fileRegistry;
    

    public DaemonImpl() throws RemoteException {
        fileRegistry = new HashMap<String, String>();
        
    }

    @Override
    public byte[] upload(String fileName, int fragmentBegin, int fragmentSize) throws RemoteException {
        String filepath = fileRegistry.get(fileName);
        if (filepath == null) {
            return null;
        }
        try {
            RandomAccessFile file = new RandomAccessFile(filepath, "r");
            byte[] fragment = new byte[fragmentSize];
            file.seek(fragmentBegin);
            file.read(fragment, 0, fragmentSize);
            file.close();
            return fragment;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void notifyDiaryIn(Diary d) throws RemoteException, UnknownHostException { 
        String[] username = {InetAddress.getLocalHost().getHostName()};
        for (String fileName : fileRegistry.keySet()) {
            d.addFileUsers(fileName, username );
        }
    }
    @Override
    public void notifyDiaryOut(Diary d) throws RemoteException, UnknownHostException {
        String[] username ={InetAddress.getLocalHost().getHostName()};
        for (String fileName : fileRegistry.keySet()) {
            d.deleteFileUsers(fileName, username );
        }
    }   
}
