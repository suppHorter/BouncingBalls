package org.dyn4j.samples;

import java.io.Serializable;

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
}
