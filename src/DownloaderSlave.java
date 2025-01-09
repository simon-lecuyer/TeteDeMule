import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import static java.lang.Math.floor;
import java.net.Socket;

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
        System.out.println("DownloaderSlave: targetUser -> " + targetUser);
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
            System.out.println("File size : " + sizeSlot);
            int offset = slot*sizeSlot;
            System.out.println("offset : " + offset);
            DataSend ds = new DataSendImpl(file, sizeSlot, slot, offset, queryingUser);

            System.out.println("DataSend to Daemon\n");
            
            dameonOut.writeObject(ds);
            // Write file slot {i}
            String slotI = "{"+ slot +"}";
            String fileNameI = slotI + file.getFileName();
            System.out.println(fileNameI);
            FileOutputStream outputFileI = new FileOutputStream("../Download/" + fileNameI);
            
            int byteRead = 0;
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            while (byteRead != -1) {
                byteRead = daemonIn.read(buffer, offset, 1024);
                if (byteRead != -1) {
                    outputFileI.write(buffer, 0, byteRead);
                }
            }

            // Close I/O
            daemonSocket.close();
            outputFileI.close();
            System.out.println("DownloaderSlave closed !");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
