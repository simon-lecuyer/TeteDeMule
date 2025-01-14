import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class DiaryImpl extends UnicastRemoteObject implements Diary {

    HashMap<String, ArrayList<String>> diary; // The diary stores <FileName, [clients]>
    ArrayList<String> connectedUsers; // To store the connected users to the diary
    ArrayList<FileUser> fileInfo; // Store information of files in the diary

    public DiaryImpl() throws RemoteException {
        this.diary = new HashMap<>();
        connectedUsers = new ArrayList<>();
        fileInfo = new ArrayList<>();
    }

    
    /** Add the file and its user associated
     * 
     * @param file the FileUser to add in the diary
     * @param user the user with the file available
     */
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

    /** Delete the user from the list associated with file. Remove the file from diary if nobody left
     * 
     * @param file the file associated
     * @param user user to delete
     */
    @Override
    public void deleteFileUser(FileUser file, String user)  throws RemoteException {
        System.out.println("Suppression de : " + file.getFileName());
        ArrayList<String> remainingUsers = diary.get(file.getFileName());
        remainingUsers.remove(user);
        if (remainingUsers.size() == 0) {
            diary.remove(file.getFileName());
            fileInfo.remove(file);
        } else {
            diary.put(file.getFileName(), remainingUsers);
        }
        System.out.println(getAllFiles());
    }

    /** Get all the users who get fileName available
     * 
     * @param fileName the file to retrieve the users
     */
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

    /** Get all files name from diary
     * 
     */
    @Override
    public ArrayList<String> getAllFiles() throws RemoteException {
        ArrayList<String> allFiles = new ArrayList<>();
        for (String file : diary.keySet()) {
            allFiles.add(file);
        }
        return allFiles;
    }

    /** delete user from diary
     * 
     * @param user user leaving the diary
     */
    @Override
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

    /** Get the byte size of fileName
    * @param fileName the file to get size
    */
    @Override
    public int getFileSize(String fileName) throws RemoteException {
        FileUser dummy = new FileUserImpl("Dummy", 0);
        return dummy.getFileFromName(fileInfo, fileName).getFileSize();
    }

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("Bienvenue dans l'annuaire de Tête de Mule");
        System.out.println("=========================================");
        System.out.println("Entrez votre adresse pour démarrer l'annuaire de Tête de Mule :");
        
        Scanner sc = new Scanner(System.in);
        String address = sc.nextLine();
        Boolean diaryNotInitialized = true;

        while (diaryNotInitialized) {
            try {
                
                System.out.println("Initialisation du diary de Tête de Mule...");
                LocateRegistry.createRegistry(4000);
                Naming.rebind("//"+address + ":4000/diary", new DiaryImpl());
                System.out.println("Annuaire de Tête de Mule sur : //" + address + ":4000/diary");
                diaryNotInitialized = false;
            } catch (RemoteException e) {
                System.out.println("Impossible de créer l'annuaire de Tête de Mule : //" + address + ":4000/diary");
                System.out.println("Voulez-vous réessayer ? (O/N)");
                String response = sc.nextLine();
                if (response.equals("N")){
                    System.exit(0);
                }
                else{
                    
                    System.out.println("Entrez votre adresse pour démarrer l'annuaire de Tête de Mule :");
                    address = sc.nextLine();
                }
            } 
            catch (MalformedURLException e) {
                System.out.println("Ceci n'est pas une syntaxe valide d'adresse : //" + address + ":4000/diary");
                System.out.println("Voulez-vous réessayer ? (O/N)");
                String response = sc.nextLine();
                if (response.equals("N")){
                    System.exit(0);
                }
                else{
                    System.out.println("Entrez votre adresse pour démarrer l' annuaire de Tête de Mule :");
                    address = sc.nextLine();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        sc.close();

    }

}