package view;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.*;


public class MyFrameSeeImage extends JFrame {
    private final String title = "Football - Hors Jeu";
    private MyPanelPicture panelPicture;

    public MyFrameSeeImage() {
    }

    public MyFrameSeeImage(File file) {
        setTitle(title);

        setLayout(new BorderLayout());
        setSize(500, 1000);

        MyPanelChosingSides panelChosingSides = new MyPanelChosingSides(this,file); // Add this line
        //ShapeDetector shapeDetector = new ShapeDetector(f);
        setMyPanelPicture(file);

        
        //MyButtonUploadImage uploadButton = new MyButtonUploadImage(this);

        add(getMyPanelPicture(), BorderLayout.CENTER);
        //add(uploadButton, BorderLayout.SOUTH);
        add(panelChosingSides, BorderLayout.NORTH); // Add the panelChosingSides to the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void setMyPanelPicture(File file) {
        panelPicture = new MyPanelPicture(file);
    }

    public MyPanelPicture getMyPanelPicture() {
        return panelPicture;
    }
}
