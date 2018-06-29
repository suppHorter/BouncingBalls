package org.dyn4j.bouncingballs;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.bouncingballs.framework.SimulationBody;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

//Klasse um die Kanone anzuzeigen -> einbindung von Bild in die SimulationBody Klasse

final class CannonBody extends SimulationBody {

    public BufferedImage image = getImageSuppressExceptions("/org/dyn4j/bouncingballs/resources/canon.png");;

    @Override
    protected void renderFixture(Graphics2D g, double scale, BodyFixture fixture, Color color) {
        Convex convex = fixture.getShape();
        org.dyn4j.geometry.Rectangle r = (Rectangle) convex;
        Vector2 c = r.getCenter();
        double w = r.getWidth();
        double h = r.getHeight();
        g.drawImage(image,
                (int) Math.ceil((c.x - w / 2.0) * scale),
                (int) Math.ceil((c.y - h / 2.0) * scale)-5,
                (int) Math.ceil(w * scale),
                (int) Math.ceil(h * scale),
                null);
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

