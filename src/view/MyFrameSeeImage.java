package view;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.*;

public class MyFrameSeeImage extends JFrame {
    // private final Dimension screenSize =
    // Toolkit.getDefaultToolkit().getScreenSize();
    private final String title = "Football - Hors Jeu";
    private MyPanelPicture panelPicture;

    public MyFrameSeeImage() {
    }

    public MyFrameSeeImage(File f) {
        setTitle(title);

        setLayout(new BorderLayout());
        setSize(500, 1000);
        setMyPanelPicture(f);

        
        MyButtonUploadImage uploadButton = new MyButtonUploadImage(this);

        add(getMyPanelPicture(),BorderLayout.CENTER);
        add(uploadButton,BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void setMyPanelPicture(File f) {
        panelPicture = new MyPanelPicture(f);
    }

    public MyPanelPicture getMyPanelPicture() {
        return panelPicture;
    }

}
