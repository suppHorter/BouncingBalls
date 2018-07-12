package org.dyn4j.bouncingballs;

import java.awt.event.ActionEvent;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.event.ActionListener;

public class StartBallsFrame implements ActionListener{

    private JFrame startBalls,helpFrame;
    private JPanel panel,helpPanel;
    private JLabel label,helpLbl;

    private int[] labellist;
    private int labelPosistion;

    private Icon title1,title2,title3,title4;
    private Icon button1,button2,button3,button4,muteTrue,muteFalse,helpImg;
    private JButton start,challenge,highscore,quit,quit_Help,btnMuteFalse,btnMuteTrue,btnHelp;
    private HighscoreFrame highscoreFrame;
    private boolean muteMode;

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
        helpFrame = new JFrame("Help");

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
        muteTrue = new ImageIcon(getClass().getResource("resources/MuteFalse.png"));
        muteFalse = new ImageIcon(getClass().getResource("resources/MuteTrue.png"));
        helpImg = new ImageIcon(getClass().getResource("resources/help.png"));

        helpLbl = new JLabel(helpImg);
        //Erzeugen der Buttons in Form der ImageIcons
        start = new JButton(button1);
        challenge = new JButton(button2);
        highscore = new JButton(button3);
        quit = new JButton(button4);
        quit_Help = new JButton(button4);
        btnMuteTrue = new JButton(muteFalse);
        btnMuteFalse = new JButton(muteTrue);
        btnHelp = new JButton("HELP");

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
                BouncingBalls bB = new BouncingBalls(startBalls,muteMode);
                bB.setVisible(true);

            }
        });

        //Alles für den Mute Button
        btnMuteTrue.setBounds(0, 600, 158, 80);
        btnMuteTrue.setBorder(new BasicBorders.ButtonBorder(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK));
        btnMuteTrue.setBackground(Color.black);
        btnMuteTrue.setForeground(Color.white);
        btnMuteTrue.setContentAreaFilled(false);
        btnMuteTrue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                muteMode = false;
                btnMuteTrue.setVisible(false);
                btnMuteFalse.setVisible(true);
            }
        });

        //Alles für den Mute Button
        btnMuteFalse.setBounds(0, 600, 158, 80);
        btnMuteFalse.setBorder(new BasicBorders.ButtonBorder(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK));
        btnMuteFalse.setBackground(Color.black);
        btnMuteFalse.setForeground(Color.white);
        btnMuteFalse.setContentAreaFilled(false);
        btnMuteFalse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                muteMode = true;
                btnMuteTrue.setVisible(true);
                btnMuteFalse.setVisible(false);
            }
        });

        //Alles für den Help Button
        btnHelp.setBounds(350, 600, 100, 50);
        btnHelp.setBorder(new BasicBorders.ButtonBorder(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK));
        btnHelp.setBackground(Color.black);
        btnHelp.setForeground(Color.white);
        btnHelp.setContentAreaFilled(false);
        btnHelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startBalls.setVisible(false);
                helpFrame.setVisible(true);
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
                BouncingBallsChallenge bBC = new BouncingBallsChallenge(startBalls,muteMode);
                bBC.setVisible(true);
            }
        });

        //Alles für Highscore Button
        highscore.setBounds(111,450,300,50);
        highscore.setBackground(Color.BLACK);
        highscore.setContentAreaFilled(false);
        highscore.setBorder(new BasicBorders.ButtonBorder(Color.black, Color.black,Color.black, Color.black));
        highscore.setForeground(Color.WHITE);
        highscore.addActionListener(e -> {
            startBalls.setVisible(false);
            highscoreFrame = new HighscoreFrame(startBalls);
        });

        //Alles für den QUIT Button
        quit.setBounds(193, 550, 122, 50);
        quit.setBackground(Color.BLACK);
        quit.setContentAreaFilled(false);
        quit.setBorder(new BasicBorders.ButtonBorder(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK));
        quit.setForeground(Color.white);
        quit.addActionListener(new ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent e) {

               System.exit(0);
           }
        });

        //Alles für den QUIT Button
        quit_Help.setBounds(193, 550, 122, 50);
        quit_Help.setBackground(Color.BLACK);
        quit_Help.setContentAreaFilled(false);
        quit_Help.setBorder(new BasicBorders.ButtonBorder(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK));
        quit_Help.setForeground(Color.white);
        quit_Help.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                startBalls.setVisible(true);
                helpFrame.setVisible(false);
            }
        });

        //Erzeugen des JPanels und hinzufügen der Buttons
        helpPanel= new JPanel();
        helpPanel.setLayout(null);
        helpPanel.setBackground(Color.black);
        helpLbl.setSize(500,700);
        helpPanel.add(helpLbl);
        helpPanel.add(quit_Help);

        helpFrame.setSize(500, 700);
        helpFrame.setResizable(false);
        helpFrame.setLocationRelativeTo(null);
        helpFrame.setUndecorated(true);
        helpFrame.add(helpPanel);
        helpFrame.setVisible(false);

        //Erzeugen des JPanels und hinzufügen der Buttons
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.black);
        panel.add(label);
        panel.add(start);
        panel.add(quit);
        panel.add(challenge);
        panel.add(highscore);
        panel.add(btnHelp);
        panel.add(btnMuteFalse);
        panel.add(btnMuteTrue);

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
    public void actionPerformed(ActionEvent e) {}
}



