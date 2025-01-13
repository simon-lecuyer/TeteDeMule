

public class DataSendImpl implements DataSend {
    private FileUser file; // the file to send
    private int sizeSlot; // size  of the fragment
    private int slot; // number of the fragment
    private int offset; // offset of the file
    private String targetUser; // the user to ask the fragment
    
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
