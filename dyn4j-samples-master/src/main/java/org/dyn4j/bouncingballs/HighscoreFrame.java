package org.dyn4j.bouncingballs;

import java.awt.event.ActionEvent;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HighscoreFrame {

    private JFrame parentFrame,highscoreFrame;
    private JLabel highScorePic;
    private ImageIcon ic;
    private JButton menu;
    private Icon menuIcon,highScoreIcon;
    private JPanel panel;
    //public JList scoreList;
    private JTable highScoreTable;

    private String[] columnNames = {"Platz",
            "Name",
            "Level",
            "Score"};
    private String[][] data = new String[10][4];

    public HighscoreFrame(JFrame parentFrame){
        highscoreFrame = new JFrame("Highscore");
        highscoreFrame.setSize(500, 700);
        highscoreFrame.setResizable(false);
        highscoreFrame.setLocationRelativeTo(null);
        highscoreFrame.setUndecorated(true);
        this.parentFrame = parentFrame;

        menuIcon = new ImageIcon(getClass().getResource("resources/menu klein.png"));
        highScoreIcon = new ImageIcon(getClass().getResource("resources/highscoreklein.png"));

        try
        {
            ArrayList<ScoreEntry> scoreEntry = ScoreEntry.getLeaderboard();
            data[0][0] = columnNames[0];
            data[0][1] = columnNames[1];
            data[0][2] = columnNames[2];
            data[0][3] = columnNames[3];
            for (int i=1;i<=scoreEntry.size();i++)
            {
                //data[i][0] = scoreEntry.get(i).getPlace()+" "+i;
                data[i][0] = Integer.toString(scoreEntry.get(i-1).getPlace());
                data[i][1] = scoreEntry.get(i-1).getName();
                data[i][2] = Integer.toString(scoreEntry.get(i-1).getLevel());
                data[i][3] = Integer.toString(scoreEntry.get(i-1).getScore());
            }

        }catch (Exception e)
        {
            System.out.print(e);

        }
        menu = new JButton(menuIcon);
        menu.setBounds(10,10,100,50);
        menu.setBorder(new BasicBorders.ButtonBorder(Color.BLACK,Color.BLACK,Color.BLACK,Color.BLACK));
        menu.setContentAreaFilled(false);
        menu.setForeground(Color.WHITE);
        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highscoreFrame.setVisible(false);
                parentFrame.setVisible(true);

            }
        });

        //Highscore Logo erstellen
        highScorePic = new JLabel(highScoreIcon);
        highScorePic.setBounds(100,50,300,50);

        //Höhe der Zellen anpassen
        highScoreTable = new JTable(data, columnNames);
        for (int i=0;i<11;i++)
        {
            highScoreTable.setRowHeight(i, 30);
        }
        //HighscoreTable erstellen
        highScoreTable.setLayout(null);
        highScoreTable.setBounds(10,150,480,330);
        highScoreTable.setBackground(Color.BLACK);
        highScoreTable.setForeground(Color.WHITE);

        //Für Alignment=Center:
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.CENTER);
        highScoreTable.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);
        highScoreTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        highScoreTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        highScoreTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

        //Gelber Rand:
        Border b;
        b = BorderFactory.createCompoundBorder();
        b = BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(2,2,2,2,Color.YELLOW));
        highScoreTable.setBorder(b);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.BLACK);
        panel.setBounds(100,200,100,100);
        panel.add(menu);
        panel.add(highScorePic);

        panel.add(highScoreTable);

        //panel.add(highScoreTable);

        highscoreFrame.add(panel);
        highscoreFrame.setVisible(true);
    }
}
