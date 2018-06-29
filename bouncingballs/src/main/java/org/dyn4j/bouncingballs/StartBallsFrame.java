package org.dyn4j.bouncingballs;

import java.awt.event.ActionEvent;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.Timer;
import java.awt.event.ActionListener;



public class StartBallsFrame implements ActionListener{

    public JLabel label1 = new JLabel();
    public JPanel panel;
    public Timer timer1;
    public int[] labellist;
    public Icon title1;
    public Icon title2;
    public Icon title3;
    public Icon title4;
    public int timerDelay = 1000;
    public int seconds = 0;
    public static JFrame startBalls;
    private static BlinkingThread bt;

    //  Main-Methode zum Erzeugen des Startbildschirms und Starten des für das Blinken verantworlichen Threads
    public static void main(String[] args) {
        StartBallsFrame bf = new StartBallsFrame();
        bt = new BlinkingThread(bf);
        bt.run();
    }
    // Konstruktor, hier werden alles Felder,Buttons,Fenster erzeugt.
    public StartBallsFrame() {

        //Erzeugen des Frames
        startBalls = new JFrame("Bouncing Balls");

        //Erzeugen der ImageIcons für den blinkenden Titel
        title1 = new ImageIcon(".\\dyn4j-bouncingballs-master\\src\\main\\java\\org\\dyn4j\\bouncingballs\\resources\\bb1klein.png");
        title2 = new ImageIcon(".\\dyn4j-bouncingballs-master\\src\\main\\java\\org\\dyn4j\\bouncingballs\\resources\\bb2klein.png");
        title3 = new ImageIcon(".\\dyn4j-bouncingballs-master\\src\\main\\java\\org\\dyn4j\\bouncingballs\\resources\\bb3klein.png");
        title4 = new ImageIcon(".\\dyn4j-bouncingballs-master\\src\\main\\java\\org\\dyn4j\\bouncingballs\\resources\\bb4klein.png");

        // Array für wechselndes Titelbild
        labellist = new int[]{1, 2, 3, 4};

        //Erzeugen der ImageIcons als Button
        Icon button1 = new ImageIcon(".\\dyn4j-bouncingballs-master\\src\\main\\java\\org\\dyn4j\\bouncingballs\\resources\\startklein.png");
        Icon button2 = new ImageIcon(".\\dyn4j-bouncingballs-master\\src\\main\\java\\org\\dyn4j\\bouncingballs\\resources\\challengeklein.png");
        Icon button3 = new ImageIcon(".\\dyn4j-bouncingballs-master\\src\\main\\java\\org\\dyn4j\\bouncingballs\\resources\\quitklein.png");
        Icon button4 = new ImageIcon(".\\dyn4j-bouncingballs-master\\src\\main\\java\\org\\dyn4j\\bouncingballs\\resources\\highscoreklein.png");

        //Erzeugen der Buttons in Form der ImageIcons
        JButton start = new JButton(button1);
        JButton challenge = new JButton(button2);
        JButton quit = new JButton(button3);
        JButton highscore = new JButton(button4);

        //Erzeugen des JPanels und hinzufügen der Buttons
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.black);
        panel.add(start);
        panel.add(quit);
        panel.add(challenge);
        panel.add(highscore);

        //Alles für den START Button
        start.setBounds(175, 250, 158, 50);
        start.setBorder(new BasicBorders.ButtonBorder(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK));
        start.setBackground(Color.black);
        start.setForeground(Color.white);
        start.setContentAreaFilled(false);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startBalls.setVisible(false);
                BouncingBalls.main(new String[1]);
            }
        });

        //Alles für den CHALLENGE Button
        challenge.setBounds(111, 350, 286, 50);
        challenge.setBackground(Color.black);
        challenge.setContentAreaFilled(false);
        challenge.setBorder(new BasicBorders.ButtonBorder(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK));
        challenge.setForeground(Color.white);

        //Alles für Highscore Button
        highscore.setBounds(111,450,300,50);
        highscore.setBackground(Color.black);
        highscore.setContentAreaFilled(false);
        highscore.setBorder(new BasicBorders.ButtonBorder(Color.black, Color.black,Color.black, Color.black));
        highscore.setForeground(Color.WHITE);
        highscore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                startBalls.setVisible(false);
                HighscoreFrame.main(new String[1]);

            }
        });


        //Alles für den QUIT Button
        quit.setBounds(193, 550, 122, 50);
        quit.setBackground(Color.black);
        quit.setContentAreaFilled(false);
        quit.setBorder(new BasicBorders.ButtonBorder(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK));
        quit.setForeground(Color.white);
        quit.addActionListener(new ActionListener() {
                                   public void actionPerformed(java.awt.event.ActionEvent e) {
                                           System.exit(0);
                                           }
                                    }
        );
        //Dimension size = new Dimension(500,700);
        //Einstellen des Fensters und Sichtbar machen sowie aufgebautes Panel hinzufügen
        startBalls.setSize(500, 700);
        startBalls.setResizable(false);
        startBalls.setLocationRelativeTo(null);
        startBalls.setUndecorated(true);

        startBalls.add(panel);
        startBalls.setVisible(true);

    }
    //Funktion die im Thread aufgerufen wird um die verschiedenen LAbels einzublenden
    public void blinking() {

        switchPositions();
        panel.remove(label1);
        int label = labellist[0];

        switch (label) {

            case 1:
                label1 = new JLabel(title1);
                label1.setVisible(true);

                break;
            case 2:
                label1 = new JLabel(title2);
                label1.setVisible(true);

                break;
            case 3:
                label1 = new JLabel(title3);
                label1.setVisible(true);

                break;
            case 4:
                label1 = new JLabel(title4);
                label1.setVisible(true);

                break;
        }

        label1.setLayout(null);
        label1.setBounds(8, 50, 479, 150);
        panel.add(label1);
        startBalls.repaint();
    }

    //Methode um die ArrayPositionen zu switchen und somit die bilder im wechsel durch die BlinkingFunktion aufrufen
    public void switchPositions() {

        int temp = labellist[3];

         for (int i = 3; i > 0; i--) {
            labellist[i] = labellist[i - 1];

         }

        labellist[0] = temp;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {

    }





}



