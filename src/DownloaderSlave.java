import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import static java.lang.Math.floor;
import java.net.Socket;
import java.util.zip.InflaterInputStream;

public class DownloaderSlave extends Thread {
    private String queryingUser; // Who is asking to download
    private String targetUser; // Who respond to the download
    private FileUser file;
    private int slot; // Number of this slot
    private int totalSlots; // Total slots

    /** The downloader of the partial file using thread
     * 
     * @param queryingUser user requesting the download
     * @param file FileUser to dowload the slot
     * @param targetUser user to download from
     * @param slot fragment size
     * @param totalSlots all the fragments needed
     */
    public DownloaderSlave(String queryingUser, FileUser file, String targetUser, int slot, int totalSlots) {
        this.queryingUser = queryingUser;
        this.file = file;
        this.targetUser = targetUser;
        this.slot = slot;
        this.totalSlots = totalSlots;
        System.out.println("DownloaderSlave: targetUser -> " + targetUser);
    }

    /** The method to run when calling  th[i].start() in DownloaderImpl
     * 
     */
    @Override
    public void run() {
        String[] userInfo = targetUser.split(":");
        String userName = userInfo[0];
        int userPort = Integer.parseInt(userInfo[1]);
        try {
            Socket daemonSocket = new Socket(userName, userPort);

            //% I/O stream
            InputStream daemonInC = daemonSocket.getInputStream();
            InflaterInputStream daemonIn = new InflaterInputStream(daemonInC);

            ObjectOutputStream dameonOut = new ObjectOutputStream(daemonSocket.getOutputStream());

            //% The data to send to the deamon
            int sizeSlot = (int) floor((double) file.getFileSize()/(double) totalSlots);
            System.out.println("File size : " + sizeSlot);
            int offset = slot*sizeSlot;
            System.out.println("offset : " + offset);
            DataSend ds = new DataSendImpl(file, sizeSlot, slot, offset, queryingUser);

            System.out.println("DataSend to Daemon\n");
            
            dameonOut.writeObject(ds);
            
            //% Write in the file {slot}fileName
            String slotI = "{"+ slot +"}";
            String fileNameI = slotI + file.getFileName();
            System.out.println("Partial file created : " + fileNameI);
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

            //% Close I/O
            daemonSocket.close();
            outputFileI.close();
            System.out.println("DownloaderSlave" + slot +"closed !\n");

        } catch (IOException e) {
            System.out.println("IOException occured in downloaderSlave: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
