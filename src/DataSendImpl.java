

public class DataSendImpl implements DataSend {
    private FileUser file;
    private int sizeSlot;
    private int slot;
    private int offset;
    private String targetUser;
    
    /** Construct the Data to parse between the Downloader and Daemon
     * 
     * @param file
     * @param sizeSlot
     * @param slot
     * @param offset
     * @param targetUser
     */
    public DataSendImpl(FileUser file, int sizeSlot, int slot, int offset, String targetUser) {
        this.file = file;
        this.sizeSlot = sizeSlot;
        this.slot = slot;
        this.offset = offset;
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
    public int getOffset() {
        return this.offset;
    }
}
