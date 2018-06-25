package org.dyn4j.samples;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.samples.framework.SimulationBody;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class ShotBallBody extends SimulationBody {

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
        g.setColor(Color.WHITE);
        g.fill(c);
        // Kreis rendern
        g.draw(c);
    }
}
