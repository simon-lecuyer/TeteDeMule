import java.io.Serializable;
import java.util.ArrayList;

public interface FileUser extends Serializable{
    public String getFileName();

    public int getFileSize();

    public FileUser getFileFromName(ArrayList<FileUser> filesList, String name);
}
