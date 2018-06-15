package org.dyn4j.samples;

import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionAdapter;
import org.dyn4j.dynamics.World;
import org.dyn4j.samples.framework.SimulationBody;

public class BoundaryCollisionListener extends CollisionAdapter {
    private Body ball;
    private Body boundary;
    private World world;

    BoundaryCollisionListener(Body ball, Body boundary, World world) {
        this.ball = ball;
        this.boundary = boundary;
        this.world = world;
    }


    @Override
    public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Penetration penetration) {
        if((body1 == ball && body2 == boundary) || (body2 == ball && body1 == boundary)) {
            world.removeBody(ball);
            MouseInteraction.ballsInGame -= 1;
            if (MouseInteraction.ballsInGame == 0){
                MouseInteraction.canShoot = true;
            }
            return false;
        }
        return true;
    }
}
