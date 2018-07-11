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
    private String[][] data =  {{"Platz","Name","Level","Score"},
                                {"1","Peter","123","432"},
                                {"2","Paul","122","332"},
                                {"3","Sven","121","223"},
                                {"4","karl","121","223"},
                                {"5","sad","121","223"},
                                {"6","sqerfd","121","223"},
                                {"7","dddd","121","223"},
                                {"8","wqer","121","223"},
                                {"9","qewrh","121","223"},
                                {"10","Kathi","112","112"}};

    public HighscoreFrame(JFrame parentFrame){
        highscoreFrame = new JFrame("Highscore");
        highscoreFrame.setSize(500, 700);
        highscoreFrame.setResizable(false);
        highscoreFrame.setLocationRelativeTo(null);
        highscoreFrame.setUndecorated(true);
        this.parentFrame = parentFrame;

        menuIcon = new ImageIcon(".\\dyn4j-samples-master\\src\\main\\java\\org\\dyn4j\\bouncingballs\\resources\\menu klein.png");
        highScoreIcon = new ImageIcon(".\\dyn4j-samples-master\\src\\main\\java\\org\\dyn4j\\bouncingballs\\resources\\highscoreklein.png");

        /*
        try
        {
            ArrayList<ScoreEntry> scoreEntry = ScoreEntry.getLeaderboard();
            for (int i=0;i<scoreEntry.size();i++)
            {
                //data[i][0] = scoreEntry.get(i).getPlace()+" "+i;
                data[i][0] = " "+i+1;
                data[i][1] = " Peter"+i;
                data[i][2] = " 12"+i;
            }

        }catch (Exception e)
        {
            //noch keine Highscores hinterlegt

        }
*/
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
