package org.dyn4j.bouncingballs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScoreDialog extends JDialog{
    int place;
    int lvlCnt;
    JFrame mainMenu;

    public ScoreDialog(Window window, JFrame mainMenu, int place, int lvlCnt) {
        super(window);
        this.place = place;
        this.lvlCnt = lvlCnt;
        this.mainMenu = mainMenu;

        this.setUndecorated(true);
        ImageIcon gameOver = new ImageIcon(getClass().getResource("resources/gameOver.png"));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
        JLabel losing = new JLabel(gameOver);
        JLabel namePrompt = new JLabel("Bitte geben Sie Ihren Namen ein");
        JTextField nameInput = new JTextField();
        JButton okButton = new JButton("OK");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScoreEntry entry = new ScoreEntry(place, nameInput.getText(), lvlCnt);
                entry.save();
                window.setVisible(false);
                mainMenu.setVisible(true);
            }
        });

        panel.add(losing);
        panel.setBackground(Color.BLACK);
        panel.add(namePrompt);
        panel.add(nameInput);
        panel.add(okButton);

        this.getContentPane().add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public ScoreDialog(Window window, JFrame mainMenu) {
        super(window);

        this.mainMenu = mainMenu;

        this.setUndecorated(true);
        ImageIcon gameOver = new ImageIcon(getClass().getResource("resources/gameOver.png"));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));

        JLabel losing = new JLabel(gameOver);
        JButton backButton = new JButton("Zurück zum Hauptmenü");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.setVisible(false);
                mainMenu.setVisible(true);
            }
        });

        panel.add(losing);
        panel.setBackground(Color.BLACK);
        panel.add(backButton);

        this.getContentPane().add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
