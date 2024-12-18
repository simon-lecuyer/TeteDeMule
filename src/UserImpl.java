import java.rmi.Naming;

public class UserImpl implements User {
    String username;

    public UserImpl(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
    }

    
}
