package org.dyn4j.bouncingballs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScoreDialog extends JDialog{

    private int place;
    private int lvlCnt;

    public ScoreDialog(Window window, int lvlCnt, int place) {
        super(window);
        this.setUndecorated(true);
        this.lvlCnt = lvlCnt;
        this.place = place;

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
        JLabel textInputLabel = new JLabel();
        textInputLabel.setText("Bitte geben Sie Ihren Namen ein!");
        JTextField nameInput = new JTextField();
        JButton okButton = new JButton("OK");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScoreEntry scoreEntry = new ScoreEntry(place, nameInput.getText() , lvlCnt);
                scoreEntry.save();

                window.setVisible(false);
            }
        });
        panel.add(textInputLabel);
        panel.add(nameInput);
        panel.add(okButton);

        this.getContentPane().add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
