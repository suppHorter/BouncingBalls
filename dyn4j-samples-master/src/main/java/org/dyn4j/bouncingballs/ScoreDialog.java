package org.dyn4j.bouncingballs;

import javax.swing.*;
import java.awt.*;

public class ScoreDialog extends JDialog{

    public ScoreDialog(Window window) {
        super(window);
        this.setUndecorated(true);
        ImageIcon gameOver = new ImageIcon(".\\dyn4j-samples-master\\src\\main\\java\\org\\dyn4j\\bouncingballs\\resources\\gameOver.png");

        JPanel panel = new JPanel();
        JLabel losing = new JLabel(gameOver);


        panel.add(losing);
        panel.setBackground(Color.BLACK);
        this.getContentPane().add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
