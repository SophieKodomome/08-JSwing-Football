package view;

import javax.swing.*;

import main.Side;
import util.ShapeDetector;

import java.awt.*;
import java.io.File;

public class MyFrameResult extends JFrame{
    private final String title = "Football - Hors Jeu";
    private MyPanelPicture panelPicture;

    public MyFrameResult(){}

    public MyFrameResult(File file,Side side){
        setTitle(title);
        
        setLayout(new BorderLayout());
        setSize(500, 1000);

        ShapeDetector shapeDetector = new ShapeDetector(file,side);

        setMyPanelPicture(shapeDetector.getResultFile());
        add(getMyPanelPicture(), BorderLayout.CENTER);

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