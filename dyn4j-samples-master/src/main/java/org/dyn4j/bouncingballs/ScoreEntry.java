package org.dyn4j.bouncingballs;

import java.io.*;
import java.util.ArrayList;

public class ScoreEntry implements Serializable {
    private int place;
    private String name;
    private int level;

    public ScoreEntry(int place, String name, int level) {
        this.place = place;
        this.name = name;
        this.level = level;
    }
    public int getPlace() {
        return place;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public void save() {
        ArrayList<ScoreEntry> entries = new ArrayList<>();

        try {
            entries = getLeaderboard();
            this.place = checkPlace(this.level);
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

    public static int checkPlace(int level) {
        ArrayList<ScoreEntry> places;

        try {
            places = getLeaderboard();
        } catch (IOException ex) {
            return 1;
        }

        int i = 1;
        for (ScoreEntry entry : places) {
            int score = entry.getLevel();

            if (score < level) {
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
