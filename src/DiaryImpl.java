import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class DiaryImpl extends UnicastRemoteObject implements Diary {

    HashMap<String, ArrayList<User>> diary;

    public DiaryImpl() throws RemoteException {
        diary = new HashMap<String, ArrayList<User>>();
    }

    @Override

    public void addFileUsers(String fileName, ArrayList<User> users) throws RemoteException {
        ArrayList<User> currentUsers = diary.get(fileName);
        if (currentUsers == null) {
            ArrayList<User> newUsers = new ArrayList<User>();
            for (User user : users) {
                newUsers.add(user);
            }
            diary.put(fileName, newUsers);
        } else {
            for (User user : users) {
                currentUsers.add(user);
            }
            diary.put(fileName, currentUsers);
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
            DiaryImpl diary = new DiaryImpl();
            Naming.rebind("//"+InetAddress.getLocalHost().getHostName()+"/daemon", diary);
            System.out.println("Diary started");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
