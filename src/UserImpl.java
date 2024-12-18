public class UserImpl implements User {
    String username;
    Daemon daemon;

    public UserImpl(String username, Daemon daemon) {
        this.username = username;
        this.daemon = daemon;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Daemon getDaemon() {
        return daemon;
    }
}
