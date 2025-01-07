import java.util.ArrayList;

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

    @Override
    public FileUser getFileFromName(ArrayList<FileUser> filesList, String name) {
        for (FileUser file : filesList) {
            if (file.getFileName().equals(name)) {
                return file;
            }
        }
        //! Normally should not happen
        return null;
    }

}
