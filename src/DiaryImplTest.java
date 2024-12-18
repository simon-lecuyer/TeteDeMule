import java.util.ArrayList;

public class DiaryImplTest {
    public static void main(String[] args) throws Exception {
        System.out.println("Test des commandes de Diary");

        DiaryImpl diary = new DiaryImpl();

        String computerName1 = "Ordinateur1";
        String computerName2 = "Ordinateur2";

        ArrayList<User> users1 = new ArrayList<>();

        users1.add(new UserImpl(computerName1));

        ArrayList<User> users2 = new ArrayList<>();

        users2.add(new UserImpl(computerName2));

        diary.addFileUsers("film1", users1);
        diary.addFileUsers("film2", users2);

        System.out.println("Ajout succès !");

        System.out.println("Affichage");
        System.out.println(diary.getFileUsersToString("film1"));
        System.out.println(diary.getFileUsersToString("film2"));

        System.out.println("Suppression !");

        ArrayList<User> delUsers1 = new ArrayList<>();
        delUsers1.add(new UserImpl(computerName1));

        ArrayList<User> delUsers2 = new ArrayList<>();
        delUsers2.add(new UserImpl(computerName2));

        diary.deleteFileUsers("film1", delUsers1);
        diary.deleteFileUsers("film2", delUsers2);

        System.out.println(diary.getFileUsersToString("film1"));
        System.out.println(diary.getFileUsersToString("film2"));

        System.out.println("Suppression complète");

        // Tests supplémentaires
        System.out.println("Tests supplémentaires");

        ArrayList<User> users3 = new ArrayList<>();
        users3.add(new UserImpl(computerName1));
        users3.add(new UserImpl(computerName2));

        diary.addFileUsers("film3", users3);

        System.out.println("Ajout succès !");
        System.out.println(diary.getFileUsersToString("film3"));

        ArrayList<User> delUsers3 = new ArrayList<>();
        delUsers3.add(new UserImpl(computerName1));

        diary.deleteFileUsers("film3", delUsers3);

        System.out.println(diary.getFileUsersToString("film3"));

        System.out.println("Tests supplémentaires complets");
    }
}
