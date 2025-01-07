import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

public class DaemonImpl extends Thread implements Daemon {
    private Socket targetUserSocket;

    
    public DaemonImpl(Socket targetUserSocket) throws RemoteException {
        this.targetUserSocket = targetUserSocket;
    };

    @Override
    public void run() {
        try {
            // I/O between client with the file and the querying user
            ObjectInputStream userIn = new ObjectInputStream(targetUserSocket.getInputStream());
            OutputStream userOut = targetUserSocket.getOutputStream();

            // The data to send to the querying user
            DataSend ds = (DataSendImpl)userIn.readObject();

            FileInputStream fileInput = new FileInputStream("Available/"+ds.getFile().getFileName());
            
            // Read file from the offset
            fileInput.skip(ds.getOffset());

            long byteRead = 0;
            int cursor;
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            while(byteRead < ds.getSizeSlot()) {
                //* is byteRead + bufferSize bigger than the slot size allocated to be transfered
                if (byteRead+bufferSize > ds.getSizeSlot()) {
                    cursor = (int) (ds.getSizeSlot() - byteRead);
                } else {
                    //* is slot size allocated bigger than byteRead+bufferSize i.e. is better to fullfill a slot of bufferSize or ds.getSizeSlot() - byteRead
                    cursor = bufferSize;
                }
                byteRead += cursor;
                userOut.write(buffer, 0, cursor);
            }
            System.out.println("Bytes read: " + byteRead);

            // Close the I/O
            fileInput.close();
            userOut.close();

        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
}
