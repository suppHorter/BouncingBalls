package org.dyn4j.samples;

import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionAdapter;
import org.dyn4j.dynamics.World;
import org.dyn4j.samples.framework.SimulationBody;

public class TargetCollisionListener extends CollisionAdapter {
    private TargetBody target;
    private World world;
    private int hitCnt;

    TargetCollisionListener(TargetBody target, World world, int hits) {
        this.target = target;
        this.world = world;
        this.hitCnt = hits;
    }

    @Override
    public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2) {
        if((body1 == target || body2 == target)) {
            //world.removeBody(target);
            if (this.hitCnt <= 0)
            {
                target.removeAllFixtures();
                return false;
            }
            this.hitCnt--;
            target.hitNumber--;
            System.out.println(this.hitCnt);
        }
        return true;
    }
}
