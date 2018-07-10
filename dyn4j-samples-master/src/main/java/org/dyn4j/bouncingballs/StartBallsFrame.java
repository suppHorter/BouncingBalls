package org.dyn4j.bouncingballs;

import java.awt.event.ActionEvent;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.event.ActionListener;

public class StartBallsFrame implements ActionListener{

    public JFrame startBalls;
    public JPanel panel;
    public JLabel label;

    public int[] labellist;
    public int labelPosistion;

    public Icon title1;
    public Icon title2;
    public Icon title3;
    public Icon title4;
    public Icon button1;
    public Icon button2;
    public Icon button3;
    public Icon button4;
    public JButton start;
    public JButton challenge;
    public JButton highscore;
    public JButton quit;

    protected static BlinkingThread bt;

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
        title1 = new ImageIcon(getClass().getResource("resources/bb1klein.png"));
        title2 = new ImageIcon(getClass().getResource("resources/bb2klein.png"));
        title3 = new ImageIcon(getClass().getResource("resources/bb3klein.png"));
        title4 = new ImageIcon(getClass().getResource("resources/bb4klein.png"));

        // Array für wechselndes Titelbild
        labellist = new int[]{1, 2, 3, 4};
        label = new JLabel(title1);

        //Erzeugen der ImageIcons als Button
        button1 = new ImageIcon(getClass().getResource("resources/startklein.png"));
        button2 = new ImageIcon(getClass().getResource("resources/challengeklein.png"));
        button3 = new ImageIcon(getClass().getResource("resources/highscoreklein.png"));
        button4 = new ImageIcon(getClass().getResource("resources/quitklein.png"));

        //Erzeugen der Buttons in Form der ImageIcons
        start = new JButton(button1);
        challenge = new JButton(button2);
        highscore = new JButton(button3);
        quit = new JButton(button4);

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
                BouncingBalls bB = new BouncingBalls(startBalls);
                bB.setVisible(true);

            }
        });

        //Alles für den CHALLENGE Button
        challenge.setBounds(111, 350, 286, 50);
        challenge.setBackground(Color.black);
        challenge.setContentAreaFilled(false);
        challenge.setBorder(new BasicBorders.ButtonBorder(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK));
        challenge.setForeground(Color.white);
        challenge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startBalls.setVisible(false);
                BouncingBallsChallenge bBC = new BouncingBallsChallenge(startBalls);
                bBC.setVisible(true);


            }
        });

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
                HighscoreFrame highscoreFrame = new HighscoreFrame(startBalls);

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
        });

        //Erzeugen des JPanels und hinzufügen der Buttons
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.black);
        panel.add(label);
        panel.add(start);
        panel.add(quit);
        panel.add(challenge);
        panel.add(highscore);

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
        labelPosistion = labellist[0];
        panel.remove(label);

        switch (labelPosistion) {

            case 1:
                label = new JLabel(title1);
                label.setVisible(true);

                break;
            case 2:
                label = new JLabel(title2);
                label.setVisible(true);

                break;
            case 3:
                label = new JLabel(title3);
                label.setVisible(true);

                break;
            case 4:
                label = new JLabel(title4);
                label.setVisible(true);

                break;
        }

        label.setLayout(null);
        label.setBounds(8, 50, 479, 150);
        panel.add(label);
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
    public void actionPerformed(ActionEvent e) {

    }
}



