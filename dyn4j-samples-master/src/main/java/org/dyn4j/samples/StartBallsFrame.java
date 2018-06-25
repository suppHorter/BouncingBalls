package org.dyn4j.samples;
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
    public Timer bt;
    public int timerDelay = 1000;
    public int seconds = 0;
    public JFrame startBalls;


    public static void main(String[] args) {
        StartBallsFrame bf = new StartBallsFrame();
        BlinkingThread bt = new BlinkingThread(bf);
        bt.run();
    }

    public StartBallsFrame() {

        startBalls = new JFrame("Start them balls");

        title1 = new ImageIcon("C:\\Users\\RT\\Desktop\\Software Projekt\\bb1klein.png");
        title2 = new ImageIcon("C:\\Users\\RT\\Desktop\\Software Projekt\\bb2klein.png");
        title3 = new ImageIcon("C:\\Users\\RT\\Desktop\\Software Projekt\\bb3klein.png");
        title4 = new ImageIcon("C:\\Users\\RT\\Desktop\\Software Projekt\\bb4klein.png");

        labellist = new int[]{1, 2, 3, 4};

     //   ActionListener al = new ActionListener()
     //   {
     //     public void actionPerformed(ActionEvent ev)
     //     {
     //         seconds++;
     //         if (seconds == 2)
     //         {
     //             blinking();
     //         }
     //     }
     //   };

     //   bt = new Timer(timerDelay,al);

        //Icons als Button

        Icon button1 = new ImageIcon("C:\\Users\\RT\\Desktop\\Software Projekt\\startklein.png");
        Icon button2 = new ImageIcon("C:\\Users\\RT\\Desktop\\Software Projekt\\challengeklein.png");
        Icon button3 = new ImageIcon("C:\\Users\\RT\\Desktop\\Software Projekt\\quitklein.png");
        JButton start = new JButton(button1);
        JButton challenge = new JButton(button2);
        JButton quit = new JButton(button3);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.black);
        panel.add(start);
        panel.add(quit);
        panel.add(challenge);

        start.setBounds(171, 300, 158, 50);
        start.setBorder(new BasicBorders.ButtonBorder(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK));
        start.setBackground(Color.black);
        start.setForeground(Color.white);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MouseInteraction.main(new String[1]);
            }
        });

        challenge.setBounds(107, 400, 286, 50);
        challenge.setBackground(Color.black);
        challenge.setBorder(new BasicBorders.ButtonBorder(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK));
        challenge.setForeground(Color.white);

        quit.setBounds(189, 500, 122, 50);
        quit.setBackground(Color.black);
        quit.setBorder(new BasicBorders.ButtonBorder(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK));
        quit.setForeground(Color.white);
        quit.addActionListener(new ActionListener() {
                                   public void actionPerformed(java.awt.event.ActionEvent e) {
                                           System.exit(0);
                                           }
                                    }
        );


        startBalls.setSize(500, 700);
        startBalls.add(panel);
        startBalls.setVisible(true);

    }

    public void blinking() {

        switchPositions();
        panel.remove(label1);
        int label = labellist[0];

        switch (label) {

            case 1:
                label1 = new JLabel(title1);
                label1.setVisible(true);
                System.out.println("1");
                break;
            case 2:
                label1 = new JLabel(title2);
                label1.setVisible(true);
                System.out.println("2");
                break;
            case 3:
                label1 = new JLabel(title3);
                label1.setVisible(true);
                System.out.println("3");
                break;
            case 4:
                label1 = new JLabel(title4);
                label1.setVisible(true);
                System.out.println("4");
                break;
        }

        label1.setLayout(null);
        label1.setBounds(4, 50, 479, 150);
        panel.add(label1);
        startBalls.repaint();
    }

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



