package org.dyn4j.samples;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.samples.framework.SimulationBody;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Timer;

public final class TargetBody extends SimulationBody {
    /**
     * Body für die Targets
     */
    private int hitNumber;
    private static MouseInteraction bB;
    private double currRadius, posX,posY;
    public boolean isTarget = false;
    private boolean growed = false;
    private int timerForAni = 0;

    public double getPosY(){return this.posY;}
    public void setPosY(double tPosY){ this.posY = tPosY;}
    public double getPosX(){return this.posX;}
    public void setPosX(double tPosX){ this.posX = tPosX;}
    public int getHitNumber() {return this.hitNumber;}
    public void setHitNumber(int hitNo){this.hitNumber = hitNo;}
    public double getCurrRadius(){return this.currRadius;}
    public void setCurrRadius(double rad){this.currRadius = rad;}
    public void setBouncingBallContr(MouseInteraction tBB){this.bB = tBB;}
    public static MouseInteraction getBouncingBallContr() {return bB;}
    public void setGrowed(boolean stat){this.growed = stat;}
    public boolean getGrowed(){return this.growed;}
    public void setTimer(int timer){this.timerForAni = timer;}
    public int getTimer(){return this.timerForAni;}

    public TargetBody()
    {
        this.growed = false;
    }

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
            int offsetX = String.valueOf(hitNumber).length() * 7;
            int offsetY = 7;
            g.drawString(String.valueOf(hitNumber), (int)center.x-offsetX, (int)center.y+offsetY);
    }
}