public class DiaryImplTest {
    public static void main(String[] args) throws Exception {
        System.out.println("Test des commandes de Diary");

        DiaryImpl diary = new DiaryImpl();

        String[] names = { "Guillaume", "Simon" };
        String[] names2 = { "Louis", "Timothée", "Nolann" };

        diary.addFileUsers("film1", names);
        diary.addFileUsers("film2", names2);

        System.out.println("Ajout succes !");

        System.out.println("Affichage");
        System.out.println(diary.getFileUsersToString("film1"));
        System.out.println(diary.getFileUsersToString("film2"));

        System.out.println("Delete !");

        diary.deleteFileUsers("film1", new String[] { "Guillaume" });
        diary.deleteFileUsers("film2", new String[] { "Louis", "Nolann" });

        System.out.println(diary.getFileUsersToString("film1"));
        System.out.println(diary.getFileUsersToString("film2"));

        System.out.println("Delete complete");

    }
}
