package org.dyn4j.bouncingballs;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.bouncingballs.framework.SimulationBody;

import java.awt.*;

public final class LvlBoxBody extends SimulationBody {
    /**
     * Body für die Targets
     */
    public int lvlNumber;
    public boolean isTarget = false;

    @Override
    protected void renderFixture(Graphics2D g, double scale, BodyFixture fixture, Color color) {
        // Cirlce fixture holen

        g.setColor(color);
        g.scale(1, -1);
        g.setFont(new Font("Default", Font.PLAIN, 20));
        if (this.lvlNumber<10)
        {
            g.drawString(String.valueOf(this.lvlNumber), (int)radius-6, (int)radius+5);
        }
        else
        {
            g.drawString(String.valueOf(this.lvlNumber), (int)radius-9, (int)radius+5);
        }
    }

    public void incLvl()
    {
        this.lvlNumber++;
    }
}