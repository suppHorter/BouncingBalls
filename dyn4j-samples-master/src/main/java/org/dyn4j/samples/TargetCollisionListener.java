package org.dyn4j.samples;

import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionAdapter;
import org.dyn4j.dynamics.World;

import java.awt.*;
import java.lang.annotation.Target;

public class TargetCollisionListener extends CollisionAdapter {
    private TargetBody target;
    private BoosterBody booster;
    private BouncingBalls bB;
    private World world;
    private int hitCnt;

    TargetCollisionListener(TargetBody target, World world, int hits) {
        this.target = target;
        this.world = world;
        this.hitCnt = hits;
    }
    TargetCollisionListener(BoosterBody booster, World world, BouncingBalls bB) {
        this.booster = booster;
        this.world = world;
        this.bB = bB;
    }

    @Override
    public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Penetration penetration) {

        if((body1 == booster && body2 instanceof ShotBallBody) || ( body1 instanceof ShotBallBody) && (body2  == booster))
        {
            if (!booster.getActive())
            {
                booster.setActive(true);
                bB.activateBooster(booster.getType());
                world.removeBody(booster);
            }
        }

        if((body1 == target && body2 instanceof ShotBallBody) || ( body1 instanceof ShotBallBody && body2 == target)) {
            AnimationThread aT = new AnimationThread();
            if ((this.hitCnt <= 1)||(target.getHitNumber()<=1))
            {
                world.removeBody(target);
            }else
            {
                target.setHitNumber(target.getHitNumber()-1);
                target.setColor(BouncingBalls.getSemiRandomColor(target.getHitNumber()));
                aT.run(target,false);
            }
        }
        return true;
    }
}
