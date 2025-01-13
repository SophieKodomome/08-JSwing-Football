package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Fenetre extends JFrame {
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final String title = "Football - Hors Jeu";
    private final JButton jButtonTitle = new JButton("Ajouter une image de jeu");

    public Fenetre(){
        setTitle(title);
        setLayout(new BorderLayout());
        setSize(screenSize);
        add(jButtonTitle,BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
