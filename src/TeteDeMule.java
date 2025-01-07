import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.Naming;

public class TeteDeMule {
    private static boolean daemonRunning = false;

    public static void main(String[] args) throws Exception {
        try {
            String diaryUrl = args[0];
            Diary diary = (Diary)Naming.lookup(diaryUrl+":4000/diary");
            System.out.println("diary found");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        
    }
}