import java.io.Serializable;

public interface DataSend extends Serializable{
    public FileUser getFile();

    public int getSizeSlot();

    public int getSlot();

    public String getTargetUser(); 

    public int getOffset();
}
