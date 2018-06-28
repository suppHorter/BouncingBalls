package org.dyn4j.samples;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.samples.framework.SimulationBody;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ShotBallBody extends SimulationBody {

    public BufferedImage image = getImageSuppressExceptions("/org/dyn4j/samples/resources/shot.png");

    @Override
    protected void renderFixture(Graphics2D g, double scale, BodyFixture fixture, Color color) {
        // Cirlce fixture holen
        Convex circle = fixture.getShape();
        double radius = circle.getRadius();
        Vector2 center = circle.getCenter();
        //Bild auf Circle abbilden
        int x = (int)Math.ceil((center.x - radius) * scale);
        int y = (int)Math.ceil((center.y - radius) * scale);
        int w = (int)Math.ceil(radius * 2 * scale);
        //Rendern
        g.drawImage(image, x, y, w, w, null);

        /*
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
        */
    }

    //Hilfsfunktion um Bilder einzubinden
    private static final BufferedImage getImageSuppressExceptions (String pathOnClasspath){
        try {
            return ImageIO.read(RenderingImagesOnBodies.class.getResource(pathOnClasspath));
        } catch (IOException e) {
            return null;
        }

    }

}
