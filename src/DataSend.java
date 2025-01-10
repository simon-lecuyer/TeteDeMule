<<<<<<< HEAD
public class DataSend {
    private String client;
    private String fileName;
    private long size;
    private long downloadByteBegin;


    public DataSend(String client, String fileName, long size, long downloadByteBegin) {
        this.client = client;
        this.fileName = fileName;
        this.size = size;
        this.downloadByteBegin = downloadByteBegin;
    }

    public String getClient() {
        return client;
    }

    public String getFileName() {
        return fileName;
    }

    public long getSize() {
        return size;
    }

    public long getDownloadByteBegin() {
        return downloadByteBegin;
    }
=======
import java.io.Serializable;

public interface DataSend extends Serializable{
    public FileUser getFile();

    public int getSizeSlot();

    public int getSlot();

    public String getTargetUser(); 

    public int getOffset();
>>>>>>> old-state
}
