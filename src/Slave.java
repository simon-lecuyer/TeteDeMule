import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;
import static java.lang.Math.floor;

public class Slave extends Thread {
    private String clientToQuery;
    private String fileName;
    private String clientQuerying;
    private long fileSize;
    private int totalDownload;
    private int nbDownload;

    public Slave(String clientToQuery, String fileName, String clientQuerying, long fileSize, int total, int nb) {
        this.clientToQuery = clientToQuery;
        this.fileName = fileName;
        this.clientQuerying = clientQuerying;
        this.fileSize = fileSize;
        this.totalDownload = total;
        this.nbDownload = nb;
    }
    
    public void run() {
        try {
            String[] diaryInfo = clientToQuery.split(":");
            String diaryName = diaryInfo[0];
            int diaryPort = Integer.parseInt(diaryInfo[1]);

            System.out.println("Balancing to socket : " + diaryPort);

            Socket ds = new Socket(diaryName, diaryPort);

            // Les stream I/O 

            InputStream in;
            in = ds.getInputStream();

            ObjectOutputStream out;
            out = new ObjectOutputStream(ds.getOutputStream());
            
            long downloadPointBegin =  (long) ((nbDownload-1) * floor((double) fileSize / (double)nbDownload));
            long sizeToDownload = (long) floor((double) fileSize / (double)nbDownload);
            
            DataSend dataSend = new DataSend(clientQuerying, fileName, sizeToDownload, downloadPointBegin);

            // Data à envoyer au Daemon
            out.writeObject(dataSend);

            // Renvoyer la réponse du server au client
            String fileNameOutput = "{" + nbDownload + "}" + fileName  ;
            FileOutputStream outputfile = new FileOutputStream("Output/"+ fileNameOutput);
            
            byte[] buffer = new byte[1024];

            int byteRead = 0;
            int size = 0;
            while (byteRead != -1) {
                byteRead = in.read(buffer, 0, 1024);
                if (byteRead != -1){
                    size += byteRead;
                    outputfile.write(buffer, 0, byteRead);
                }
            }

            // Fermer les sockets et les stream
            ds.close();
            outputfile.close();

            System.out.println("Connection closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
