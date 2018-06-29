package org.dyn4j.bouncingballs;
import java.awt.event.ActionEvent;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.event.ActionListener;

public class HighscoreFrame {

    public JPanel panel;
    public JFrame highscoreFrame;
    public JList scoreList;
    public JButton menu;
    public Icon menuIcon;

    public static void main(String[] args){
       HighscoreFrame hf = new HighscoreFrame();

    }

    public HighscoreFrame(){

        scoreList = new JList();
        scoreList.setLayout(null);

        highscoreFrame = new JFrame("Highscore");
        highscoreFrame.setSize(500, 700);
        highscoreFrame.setResizable(false);
        highscoreFrame.setLocationRelativeTo(null);
        highscoreFrame.setUndecorated(true);

        menuIcon = new ImageIcon(".\\dyn4j-bouncingballs-master\\src\\main\\java\\org\\dyn4j\\bouncingballs\\resources\\menu klein.png");

        menu = new JButton(menuIcon);
        menu.setBounds(10,10,100,50);
        menu.setBorder(new BasicBorders.ButtonBorder(Color.BLACK,Color.BLACK,Color.BLACK,Color.BLACK));
        menu.setContentAreaFilled(false);
        menu.setForeground(Color.WHITE);
        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highscoreFrame.setVisible(false);
                StartBallsFrame.startBalls.setVisible(true);


            }
        });


        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.black);
        //panel.add(scoreList);
        panel.add(menu);

        highscoreFrame.add(panel);
        highscoreFrame.setVisible(true);






    }


}
