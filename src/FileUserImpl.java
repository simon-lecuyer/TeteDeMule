public class FileUserImpl implements FileUser{
    private String name;
    private int size;

    public FileUserImpl(String name, int size) {
        super();
        this.name = name;
        this.size = size;
    }
    
    @Override
    public int getFileSize() {
        return this.size;
    }

    @Override
    public String getFileName() {
        return this.name;
    }
}
