import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TeteDeMule {
    private static boolean daemonRunning = false;

    public static void main(String[] args) throws Exception {
    //     JFrame frame = new JFrame("TeteDeMule");
    //     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //     frame.setSize(400, 300);

    //     JPanel panel = new JPanel();
    //     panel.setLayout(new GridLayout(5, 1));

    //     JButton startDaemonButton = new JButton("Start Daemon");
    //     JButton stopDaemonButton = new JButton("Stop Daemon");
    //     JButton checkDiaryButton = new JButton("Check Public Diary");
    //     JButton addFileButton = new JButton("Add Local File to Diary");
    //     JButton downloadFileButton = new JButton("Download File from Diary");

    //     startDaemonButton.addActionListener(new ActionListener() {
    //         public void actionPerformed(ActionEvent e) {
    //             if (!daemonRunning) {
    //                 daemonRunning = true;
    //                 JOptionPane.showMessageDialog(frame, "Daemon started.");
    //             } else {
    //                 JOptionPane.showMessageDialog(frame, "Daemon is already running.");
    //             }
    //         }
    //     });

    //     stopDaemonButton.addActionListener(new ActionListener() {
    //         public void actionPerformed(ActionEvent e) {
    //             if (daemonRunning) {
    //                 daemonRunning = false;
    //                 // Code to stop the daemon
    //                 JOptionPane.showMessageDialog(frame, "Daemon stopped.");
    //             } else {
    //                 JOptionPane.showMessageDialog(frame, "Daemon is not running.");
    //             }
    //         }
    //     });

    //     checkDiaryButton.addActionListener(new ActionListener() {
    //         public void actionPerformed(ActionEvent e) {
    //             // Code to check the public diary
    //             JOptionPane.showMessageDialog(frame, "Checking Public Diary...");
    //             // Implement the logic to check the public diary
    //             // For example, read from a file or database
    //             try {
    //                 String diaryContent = new String(Files.readAllBytes(Paths.get("public_diary.txt")));
    //                 JOptionPane.showMessageDialog(frame, diaryContent);
    //             } catch (IOException ex) {
    //                 JOptionPane.showMessageDialog(frame, "Error reading diary: " + ex.getMessage());
    //             }
    //         }
    //     });

    //     addFileButton.addActionListener(new ActionListener() {
    //         public void actionPerformed(ActionEvent e) {
    //             JFileChooser fileChooser = new JFileChooser();
    //             int returnValue = fileChooser.showOpenDialog(null);
    //             if (returnValue == JFileChooser.APPROVE_OPTION) {
    //                 File selectedFile = fileChooser.getSelectedFile();
    //                 // Code to add the selected file to the public diary
    //                 JOptionPane.showMessageDialog(frame, "Adding " + selectedFile.getName() + " to Diary...");
    //                 // Implement the logic to add the file to the diary
    //                 try {
    //                     Files.copy(selectedFile.toPath(), Paths.get("public_diary/" + selectedFile.getName()));
    //                 } catch (IOException ex) {
    //                     JOptionPane.showMessageDialog(frame, "Error adding file to diary: " + ex.getMessage());
    //                 }
    //             }
    //         }
    //     });

    //     downloadFileButton.addActionListener(new ActionListener() {
    //         public void actionPerformed(ActionEvent e) {
    //             // Code to download a file from the public diary
    //             String fileName = JOptionPane.showInputDialog(frame, "Enter the name of the file to download:");
    //             if (fileName != null && !fileName.isEmpty()) {
    //                 JOptionPane.showMessageDialog(frame, "Downloading " + fileName + " from Diary...");
    //                 // Implement the logic to download the file from the diary
    //                 try {
    //                     Files.copy(Paths.get("public_diary/" + fileName), Paths.get("downloads/" + fileName));
    //                 } catch (IOException ex) {
    //                     JOptionPane.showMessageDialog(frame, "Error downloading file: " + ex.getMessage());
    //                 }
    //             }
    //         }
    //     });

    //     panel.add(startDaemonButton);
    //     panel.add(stopDaemonButton);
    //     panel.add(checkDiaryButton);
    //     panel.add(addFileButton);
    //     panel.add(downloadFileButton);

    //     frame.getContentPane().add(panel);
    //     frame.setVisible(true);
    

        
    }
}