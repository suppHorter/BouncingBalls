package org.dyn4j.samples;

import java.io.*;
import java.util.ArrayList;

public class Score {
    private int place;
    private int level;
    private String name;

    public Score(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public void save() {
        ArrayList<ScoreEntry> entries = new ArrayList<>();

        try {
            entries = getLeaderboard();
            this.place = this.checkPlace();
        } catch (IOException ex) {
            this.place = 1;
        }

        try {
            ObjectOutputStream outputWriter = new ObjectOutputStream(new FileOutputStream(new File("./highscore")));

            entries.add(this.place - 1, new ScoreEntry(place, this.name, this.level));
            if (entries.size() > 10) {
                entries.subList(10, entries.size()).clear();
            }

            for (ScoreEntry entry : entries) {
                outputWriter.writeObject(entry);
            }

            outputWriter.close();

        } catch (IOException ex) {
            System.out.println("File cannot be created!");
        }
    }

    public int checkPlace() {
        ArrayList<ScoreEntry> places;

        try {
            places = getLeaderboard();
        } catch (IOException ex) {
            return 1;
        }

        int i = 1;
        for (ScoreEntry entry : places) {
            int score = entry.getLevel();

            if (score < this.level) {
                return i;
            }
            i++;
        }
        return i;
    }

    public static ArrayList<ScoreEntry> getLeaderboard() throws IOException {
        FileInputStream fileReader = new FileInputStream(new File("./highscore"));
        ObjectInputStream objectInputStream = new ObjectInputStream(fileReader);

        ArrayList<ScoreEntry> entries = new ArrayList<>();
        ScoreEntry entry;

        while (true) {
            try {
                entry = (ScoreEntry) objectInputStream.readObject();
                entries.add(entry);

            } catch (EOFException ex) {
                objectInputStream.close();
                return entries;
            } catch (ClassNotFoundException ex) {
                System.out.println("Fehler beim Lesen der Objekte.");
                objectInputStream.close();
                return entries;
            }
        }
    }
}
