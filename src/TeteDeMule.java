
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.Naming;

public class TeteDeMule {
    private static boolean daemonRunning = true;

    public static void main(String[] args) throws Exception {
        try {
            String userId = args[0];
            String diaryName = args[1];
            Diary diary = (Diary)Naming.lookup(diaryName+":4000/diary");
            System.out.println("Diary found : " + diaryName +":4000/diary");

            int serverPort = Integer.parseInt(args[2]);
            String user = userId + ":" + serverPort;
            
            ServerSocket ss = new ServerSocket(serverPort);

            //% Add the user and its files available
            File dir = new File("../Available");
            File[] files = dir.listFiles();
            for (File file : files) {
                FileUser newFile = new FileUserImpl(file.getName(), (int)file.length());
                diary.addFileUser(newFile, user);
            }
            
            //% Print all the files available to download
            for (String file : diary.getAllFiles()) {
                System.out.println("- " + file);
                System.out.println(diary.getFileUsers(file));
            }
            System.out.println("\n");

            //% The main thread for the client to download, see all files, ...
            Thread runDownloader = new Thread(() -> {
                System.out.println("Downloader Thread running");
                new DownloaderImpl(user, diaryName+":4000/diary");
            });

            //% When the user disconnect, to leave the diary
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        ss.close();
                        daemonRunning = false;
                        diary.userLeaves(user);
                    } catch (IOException e) {
                        throw new RuntimeException("Stop Daemon error : " + e.getMessage());
                    }
                }
            });

            runDownloader.start();

            while (daemonRunning) {
                new DaemonImpl(ss.accept()).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}