package view;

import java.io.File;

import javax.swing.*;

public class MyWindow extends JFrame {


    public MyWindow(){
        //new MyFrameUploadImage();
        new MyFrameSeeImage(new File("C:/Users/Pyvas/OneDrive/Documents/C-ITU-Files-L3/S5/02-Code/08-JSwing-Football/assets/football.png"));
    }

}
