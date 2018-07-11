package org.dyn4j.bouncingballs;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.bouncingballs.framework.SimulationBody;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MenuButtonBox extends SimulationBody {
    /**
     * Body für die Targets
     */

    public BufferedImage image = getImageSuppressExceptions("/org/dyn4j/bouncingballs/resources/menu klein.png");

    @Override
    protected void renderFixture(Graphics2D g, double scale, BodyFixture fixture, Color color) {
        // Menüfeld einfügen
        g.scale(1, -1);
        g.drawImage(image, 0, 0, 73, 20, null);
    }

    private static final BufferedImage getImageSuppressExceptions (String pathOnClasspath){
        try {
            return ImageIO.read(RenderingImagesOnBodies.class.getResource(pathOnClasspath));
        } catch (IOException e) {
            return null;
        }

    }


}
