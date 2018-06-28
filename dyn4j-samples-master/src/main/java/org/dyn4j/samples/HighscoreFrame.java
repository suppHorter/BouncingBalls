package org.dyn4j.samples;
import java.awt.event.ActionEvent;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.Timer;
import java.awt.event.ActionListener;

import javax.swing.*;

public class HighscoreFrame {

    public JPanel panel;
    public JFrame highscoreFrame;
    public JList scoreList;

    public static void main(String[] args){
       HighscoreFrame hf = new HighscoreFrame();

    }

    public HighscoreFrame(){

        panel = new JPanel();
        scoreList = new JList();
        highscoreFrame = new JFrame("Highscore");

        panel.add(scoreList);
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.black);


        highscoreFrame = new JFrame();

        highscoreFrame.add(panel);

        highscoreFrame.setSize(500, 700);
        highscoreFrame.setResizable(false);
        highscoreFrame.setLocationRelativeTo(null);
        highscoreFrame.setUndecorated(true);
        highscoreFrame.setVisible(true);






    }


}
