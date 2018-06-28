package org.dyn4j.samples;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.samples.framework.SimulationBody;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MenuButtonBox extends SimulationBody {
    /**
     * Body für die Targets
     */

    public BufferedImage image = getImageSuppressExceptions("/org/dyn4j/samples/resources/menu klein.png");

    @Override
    protected void renderFixture(Graphics2D g, double scale, BodyFixture fixture, Color color) {
        // Cirlce fixture holen

        double radius2 = 2.0 * 2;
        Rectangle2D.Double c = new Rectangle2D.Double(
                (1 - 2) * scale,
                (1 - 2) * scale,
                2 * scale,
                2 * scale);
        g.setColor(color);
        g.draw(c);
        g.scale(1, -1);
        g.drawImage(image, 0, 0, 50, 25, null);


    }
    private static final BufferedImage getImageSuppressExceptions (String pathOnClasspath){
        try {
            return ImageIO.read(RenderingImagesOnBodies.class.getResource(pathOnClasspath));
        } catch (IOException e) {
            return null;
        }

    }


}
