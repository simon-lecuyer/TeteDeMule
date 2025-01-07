import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static java.lang.Math.floor;

public class DownloaderSlave extends Thread {
    private String queryingUser; // Who is asking to download
    private String targetUser; // Who respond to the download
    private FileUser file;
    private int slot; // Number of this slot
    private int totalSlots; // Total slots

    public DownloaderSlave(String queryingUser, FileUser file, String targetUser, int slot, int totalSlots) {
        this.queryingUser = queryingUser;
        this.file = file;
        this.targetUser = targetUser;
        this.slot = slot;
        this.totalSlots = totalSlots;
    }

    @Override
    public void run() {
        String[] userInfo = targetUser.split(":");
        String userName = userInfo[0];
        int userPort = Integer.parseInt(userInfo[1]);
        try {
            Socket daemonSocket = new Socket(userName, userPort);

            // I/O stream
            InputStream daemonIn = daemonSocket.getInputStream();
            ObjectOutputStream dameonOut = new ObjectOutputStream(daemonSocket.getOutputStream());

            // The data to send to the deamon
            int sizeSlot = (int) floor((double) file.getFileSize()/(double) totalSlots);
            int offset = slot*sizeSlot;
            DataSend ds = new DataSendImpl(file, sizeSlot, slot, offset, queryingUser);

            dameonOut.writeObject(ds);

            // Write file slot {i}
            String slotI = "{"+ slot +"}";
            String fileNameI = slotI + file.getFileName();
            FileOutputStream outputFileI = new FileOutputStream("../Download/" + fileNameI);
            
            int byteRead = 0;
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            while (byteRead != -1) {
                byteRead = daemonIn.read(buffer, 0, 1024);
                if (byteRead != -1) {
                    outputFileI.write(buffer, 0, byteRead);
                }
            }

            // Close I/O
            daemonSocket.close();
            outputFileI.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
