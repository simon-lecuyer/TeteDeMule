import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
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
            System.out.println("New file : " + fileName);
            ArrayList<User> newUsers = new ArrayList<User>();
            for (User user : users) {
                newUsers.add(user);
            }
            diary.put(fileName, newUsers);
        } else {
            System.out.println("New user for : " + fileName);
            for (User user : users) {
                if (!currentUsers.contains(user)){
                currentUsers.add(user);
                }
            }
            diary.put(fileName, currentUsers);
        }
    }

    @Override
    public void deleteFileUsers(String fileName, ArrayList<User> users) throws RemoteException {
        ArrayList<User> delUsers = new ArrayList<>();
        for (User user : users) {
            System.out.println("User : " + user + " has left");
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
            LocateRegistry.createRegistry(2500);
            Naming.bind("//"+InetAddress.getLocalHost().getHostName()+":2500/daemon", diary);
            System.out.println("Diary started");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
