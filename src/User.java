import java.io.*;

public interface User extends Serializable {
    public String getUsername();
    public Daemon getDaemon();
} 