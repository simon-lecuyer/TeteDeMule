

public class DataSendImpl implements DataSend {
    private FileUser file;
    private int sizeSlot;
    private int slot;
    private String targetUser;
    
    public DataSendImpl(FileUser file, int sizeSlot, int slot, String targetUser) {
        this.file = file;
        this.sizeSlot = sizeSlot;
        this.slot = slot;
        this.targetUser = targetUser;
    }

    @Override
    public FileUser getFile() {
        return file;
    }

    @Override
    public int getSizeSlot() {
        return sizeSlot;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public String getTargetUser() {
        return targetUser;
    }

    @Override
    public long getOffset() {
        return (long) sizeSlot*slot;
    }
}
