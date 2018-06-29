package org.dyn4j.samples;

import javax.swing.*;
import java.awt.*;

public class ScoreDialog extends JDialog{

    public ScoreDialog(Window window) {
        super(window);
        this.setUndecorated(true);

        JPanel panel = new JPanel();
        JLabel gratulations = new JLabel();
        gratulations.setText("Herzlichen Glückwunsch");

        panel.add(gratulations);

        this.getContentPane().add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
