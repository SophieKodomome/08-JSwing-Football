package view;

import java.awt.*;
import java.io.File;

import javax.swing.*;

import main.Side;

public class MyPanelChosingSides extends JPanel {
    private Side side;

    public MyPanelChosingSides(JFrame parentFrame, File file) {
        this.setLayout(new FlowLayout()); // Now this will not throw a NullPointerException

        JButton sidesUpButton = new JButton("Put Red Team Up");
        JButton sidesDownButton = new JButton("Put Red Team Down");

        this.add(sidesUpButton);
        this.add(sidesDownButton);

        sidesDownButton.addActionListener(e -> {
            System.out.println("Selected Down");

            side = new Side();
            side.setColor("Red");
            side.setPosition("Down");

            new MyFrameResult(file, side);
            parentFrame.setVisible(false);
        });

        sidesUpButton.addActionListener(e -> {
            System.out.println("Selected up");

            side = new Side();
            side.setColor("Red");
            side.setPosition("Up");
            
            new MyFrameResult(file, side);
            parentFrame.setVisible(false);
        });
    }
}
