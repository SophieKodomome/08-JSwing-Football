package view;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class MyButtonUploadImage extends JButton {
    public MyButtonUploadImage(JFrame parentFrame) {
        super("Ajouter une image");

        // Add action listener for file upload
        this.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose an Image");

            // Restrict file types to images
            FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
                    "Image Files (jpg, png, gif, bmp)", "jpg", "png", "gif", "bmp");
            fileChooser.setFileFilter(imageFilter);

            // Show file chooser dialog
            int result = fileChooser.showOpenDialog(parentFrame);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                new MyFrameSeeImage(selectedFile); // Open new frame to see the image
                parentFrame.setVisible(false); // Hide the parent frame
            } else if (result == JFileChooser.CANCEL_OPTION) {
                JOptionPane.showMessageDialog(parentFrame,
                        "File selection canceled.",
                        "No File Selected",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}
