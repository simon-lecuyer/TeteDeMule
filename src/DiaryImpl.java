import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class DiaryImpl extends UnicastRemoteObject implements Diary {

    HashMap<String, ArrayList<String>> diary;
    ArrayList<String> connectedUsers;

    public DiaryImpl() throws RemoteException {
        diary = new HashMap<String, ArrayList<String>>();
        connectedUsers = new ArrayList<String>();
    }

    @Override
    public void addFileUser(String fileName, String user) throws RemoteException {
        if (!connectedUsers.contains(user)) {
            connectedUsers.add(user);
            System.out.println("New user : " + user);
        }
        if (!diary.containsKey(fileName)) {
            ArrayList<String> newUser = new ArrayList<>();
            newUser.add(user);
            diary.put(fileName, newUser);
        }
    }

    @Override
    public void deleteFileUsers(String fileName, ArrayList<User> users) throws RemoteException {
        ArrayList<User> delUsers = new ArrayList<>();
        for (User user : users) {
            delUsers.add(user);
        }

        diary.get(fileName).removeAll(delUsers);

    }

    @Override
    public ArrayList<User> getFileUsers(String fileName) throws RemoteException {

        return diary.get(fileName);
    }

    public String getFileUsersToString(String fileName) {
        String[] usernamesArray = diary.get(fileName).toArray(new String[0]);

        String usersString = "";

        for (String user : usernamesArray) {
            usersString = user + " ; " + usersString;
        }

        return usersString;
    }

    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                System.out.println("Need the diary address"); 
            } else {
                LocateRegistry.createRegistry(4000);
                Naming.bind(args[0] + ":4000/diary", new DiaryImpl());
                System.out.println("Diary on : " + args[0] + ":4000/diary");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
