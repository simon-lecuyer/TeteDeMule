import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class DiaryImpl extends UnicastRemoteObject implements Diary {

    HashMap<String, ArrayList<String>> diary;

    public DiaryImpl() throws RemoteException {
        diary = new HashMap<String, ArrayList<String>>();
    }

    @Override

    public void addFileUsers(String fileName, String[] usernames) throws RemoteException {
        ArrayList<String> currentUsers = diary.get(fileName);
        if (currentUsers == null) {
            ArrayList<String> newUsers = new ArrayList<String>();
            for (String user : usernames) {
                newUsers.add(user);
            }
            diary.put(fileName, newUsers);
        } else {
            for (String user : usernames) {
                currentUsers.add(user);
            }
            diary.put(fileName, currentUsers);
        }
    }

    @Override
    public void deleteFileUsers(String fileName, String[] usernames) throws RemoteException {
        ArrayList<String> delUsers = new ArrayList<>();
        for (String user : usernames) {
            delUsers.add(user);
        }

        diary.get(fileName).removeAll(delUsers);

    }

    @Override
    public String[] getFileUsers(String fileName) throws RemoteException {

        return diary.get(fileName).toArray(new String[0]);
    }

    public String getFileUsersToString(String fileName) {
        String[] usernamesArray = diary.get(fileName).toArray(new String[0]);

        String usersString = "";

        for (String user : usernamesArray) {
            usersString = user + " ; " + usersString;
        }

        return usersString;
    }

}
