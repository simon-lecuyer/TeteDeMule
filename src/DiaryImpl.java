import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;
import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class DiaryImpl extends UnicastRemoteObject implements Diary {

    HashMap<String, ArrayList<String>> diary;
    ArrayList<String> connectedUsers;
    ArrayList<FileUser> fileInfo;

    public DiaryImpl() throws RemoteException {
        diary = new HashMap<String, ArrayList<String>>();
        connectedUsers = new ArrayList<String>();
        fileInfo = new ArrayList<FileUser>();
    }

    @Override
    public void addFileUser(FileUser file, String user) throws RemoteException {
        if (!connectedUsers.contains(user)) {
            connectedUsers.add(user);
            System.out.println("New user : " + user);
        }
        if (!diary.containsKey(file.getFileName())) {
            ArrayList<String> newUser = new ArrayList<>();
            newUser.add(user);
            fileInfo.add(file);
            diary.put(file.getFileName(), newUser);
        } 
        else {
            ArrayList<String> users = diary.get(file.getFileName());
            if (!users.contains(user)) {
                users.add(user);
            }
            diary.put(file.getFileName(), users);
        }
    }

    @Override
    public void deleteFileUser(FileUser file, String user)  throws RemoteException {
        ArrayList<String> remainingUsers = diary.get(file.getFileName());
        remainingUsers.remove(user);
        if (remainingUsers.isEmpty()) {
            diary.remove(file.getFileName());
            fileInfo.remove(file);
        } else {
            diary.put(file.getFileName(), remainingUsers);
        }
    }

    @Override
    public ArrayList<String> getFileUsers(String fileName) throws RemoteException {

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

    @Override
    public ArrayList<String> getAllFiles() throws RemoteException {
        ArrayList<String> allFiles = new ArrayList<>();
        for (String file : diary.keySet()) {
            allFiles.add(file);
        }
        return allFiles;
    }

    public void userLeaves(String user) throws RemoteException {
        connectedUsers.remove(user);
        System.out.println("User  : " + user + " has left");

        ArrayList<String> toRemove = new ArrayList<>();
        for (String filename : diary.keySet()) {
            ArrayList<String> users = diary.get(filename);
            users.remove(user);
            if (users.isEmpty()) {
                toRemove.add(filename);
            } else {
                diary.put(filename, users);
            }
        }
        for (String filename : toRemove) {
            diary.remove(filename);
            FileUser dummy = new FileUserImpl("Dummy", 0);
            fileInfo.remove(dummy.getFileFromName(fileInfo, filename));
        }
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
