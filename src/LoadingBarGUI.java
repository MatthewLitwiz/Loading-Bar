import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

public class LoadingBarGUI extends JFrame{

    // folder path that we are going to delete
    public static final String FOLDER_PATH = "files";

    // constructor
    public LoadingBarGUI() {
        // set the title of the GUI
        super("Delete Files");

        // set the size of the GUI
        setSize(563, 392);

        // set the close operation to exit on close to end the process after closing the GUI
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // set the location of the GUI to be in the center
        setLocationRelativeTo(null);

        addGuiComponents();
    }

    private void addGuiComponents() {
        // create the delete button
        JButton deleteButton = new JButton("Delete Files");

        // remove the focus from the button
        deleteButton.setFocusable(false);

        // change the font of the button
        deleteButton.setFont(new Font("Dialog", Font.BOLD, 48));

        // perform action when button is clicked
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get the files in the folder
                File folder = new File(FOLDER_PATH);
                File[] files = folder.listFiles();

                // check if the folder is empty
                if (files.length == 0){
                    showResultDialog("No Files to Delete");
                    return;
                }

                // delete the files
                deleteFiles(files);
            }
        });

        // add to the GUI
        add(deleteButton, BorderLayout.CENTER);
    }

    private void showResultDialog(String message){
        JDialog resultDialog = new JDialog(this, "Result", true); // association with  our gui / the message that appears / modal true that blocks input until it is disposed
        resultDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // close the dialog when the user clicks the close button
        resultDialog.setSize(300, 150);
        resultDialog.setLocationRelativeTo(this);

        // message label
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Dialog", Font.BOLD, 26));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultDialog.add(messageLabel, BorderLayout.CENTER);

        // confirm button
        JButton confirmButton = new JButton("Confirm");
        confirmButton.setFocusable(false);
        confirmButton.setFont(new Font("Dialog", Font.BOLD, 18));
        confirmButton.addActionListener(e -> resultDialog.dispose());

        resultDialog.add(confirmButton, BorderLayout.SOUTH);
        resultDialog.setVisible(true);
    }

    private void deleteFiles(File[] files){
        // create a dialog where the progress bar will be stored
        JDialog loadingDialog = new JDialog(this, "Deleting Files", true);
        loadingDialog.setSize(300, 100);
        loadingDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        loadingDialog.setLocationRelativeTo(this);

        // progress bar
        JProgressBar progressBar = new JProgressBar();
        progressBar.setFont(new Font("Dialog", Font.BOLD, 18));

        // change the color of the progress bar to green
        progressBar.setForeground(Color.decode("#2c8558"));

        // init the progress var value to 0% 
        progressBar.setValue(0);

        // delete the files thread
        Thread deleteFilesThread = new Thread(new Runnable(){
            @Override
            public void run(){
                // calculate the num of file in the 'files' directory
                int totalFiles = files.length;

                // keep count of the num of files that are deleted
                int filesDeleted = 0;

                // keep track of the progress
                int progress;

                for (File file : files){
                    // delete file
                    if (file.delete()){
                        filesDeleted++;

                        // calculate the progress
                        progress = (int) ((((double) filesDeleted / totalFiles) * 100));

                        try {
                            Thread.sleep(60);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // update progress bar
                        progressBar.setValue(progress);
                    }
                }

                // dispose the loading dialog when the progress is complete
                if(loadingDialog.isVisible()){
                    loadingDialog.dispose();
                }

                // show result dialog
                showResultDialog("Files Deleted: " + filesDeleted + " / " + totalFiles);

            }
        });

        // start the delete files thread
        deleteFilesThread.start();

        // add the percentage symbol
        progressBar.setStringPainted(true);

        loadingDialog.add(progressBar, BorderLayout.CENTER);
        loadingDialog.setVisible(true);

    }

}