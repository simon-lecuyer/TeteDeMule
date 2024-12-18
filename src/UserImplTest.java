import java.rmi.RemoteException;

public class UserImplTest {
    private UserImpl user;
    private Daemon daemon;

    public void setUp() {
        try {
            daemon = new DaemonImpl();
        } catch (RemoteException e) {
            e.printStackTrace();
        } 
        user = new UserImpl("testUser", daemon);
    }

    public void testGetUsername() {
        if (!"testUser".equals(user.getUsername())) {
            System.out.println("testGetUsername failed");
        } else {
            System.out.println("testGetUsername passed");
        }
    }

    public void testGetDaemon() {
        if (!daemon.equals(user.getDaemon())) {
            System.out.println("testGetDaemon failed");
        } else {
            System.out.println("testGetDaemon passed");
        }
    }

    public static void main(String[] args) {
        UserImplTest test = new UserImplTest();
        test.setUp();
        test.testGetUsername();
        test.setUp();
        test.testGetDaemon();
    }
}
