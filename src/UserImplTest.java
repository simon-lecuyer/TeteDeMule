public class UserImplTest {
    private UserImpl user;

    public void setUp() {
        user = new UserImpl("testUser");
    }

    public void testGetUsername() {
        if (!"testUser".equals(user.getUsername())) {
            System.out.println("testGetUsername failed");
        } else {
            System.out.println("testGetUsername passed");
        }
    }

    public static void main(String[] args) {
        UserImplTest test = new UserImplTest();
        test.setUp();
        test.testGetUsername();
        test.setUp();
        System.exit(0);
    }
}
