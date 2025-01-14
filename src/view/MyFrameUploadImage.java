package view;

import javax.swing.*;
import java.awt.*;

public class MyFrameUploadImage extends JFrame {
    private final String title = "Football - Hors Jeu";

    public MyFrameUploadImage() {
        setTitle(title);
        setLayout(new BorderLayout());
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MyButtonUploadImage uploadButton = new MyButtonUploadImage(this);
        add(uploadButton, BorderLayout.CENTER);

        setVisible(true);
    }
}
