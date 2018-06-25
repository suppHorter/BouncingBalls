package org.dyn4j.samples;

import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionAdapter;
import org.dyn4j.dynamics.World;
import org.dyn4j.samples.framework.SimulationBody;

public class BoundaryCollisionListener extends CollisionAdapter {
    private Body boundary;
    private World world;

    BoundaryCollisionListener(Body boundary, World world) {
        this.boundary = boundary;
        this.world = world;
    }


    @Override
    public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Penetration penetration) {
        if ((body1 instanceof ShotBallBody && body2 == boundary) || (body2 instanceof ShotBallBody && body1 == boundary)) {
            if (body1 instanceof ShotBallBody) {
                world.removeBody(body1);
            } else {
                world.removeBody(body2);
            }
            MouseInteraction.ballsInGame -= 1;
            if (MouseInteraction.ballsInGame == 0) {
                MouseInteraction.canShoot = true;
                MouseInteraction.TURN += 1;
            }
            return false;
        }
        return true;
    }
}
