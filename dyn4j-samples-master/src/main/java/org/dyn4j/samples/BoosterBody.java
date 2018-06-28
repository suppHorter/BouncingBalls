package org.dyn4j.samples;

import netscape.security.Target;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.samples.framework.SimulationBody;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Timer;

public class BoosterBody extends TargetBody{

    private int type;
    private Timer aliveTimer;
    private boolean active;
    private static BouncingBalls bB;
    public int getType(){return this.type;}
    public void setType(int type){this.type = type;}

    public BoosterBody()
    {
        this.active = false;
    }

    public void setActive(boolean active) {this.active = active;}
    public boolean getActive() {return this.active;}

    @Override
    protected void renderFixture(Graphics2D g, double scale, BodyFixture fixture, Color color) {
        // Cirlce fixture holen
        Convex circle = fixture.getShape();
        double radius = circle.getRadius();
        Vector2 center = circle.getCenter();

        double radius2 = 2.0 * radius;
        Ellipse2D.Double c = new Ellipse2D.Double(
                (center.x - radius) * scale,
                (center.y - radius) * scale,
                radius2 * scale,
                radius2 * scale);
        g.setColor(color);
        // Kreis rendern
        g.draw(c);
        // Zahl im Target anzeigen
        g.scale(1, -1);
        g.setFont(new Font("Default", Font.PLAIN, 20));
        //Offset für Beschriftung anhand der hitNumber erstellen
        //Schriftbreite = 7 also bei zweistelligen zahlen 2 * 7
        int offsetX = String.valueOf("Booster").length() * 7;
        int offsetY = 7;
        g.drawString(String.valueOf("Booster"), (int)center.x-offsetX, (int)center.y+offsetY);
    }
}
