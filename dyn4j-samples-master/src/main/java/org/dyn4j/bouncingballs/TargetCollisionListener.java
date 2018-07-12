package org.dyn4j.bouncingballs;

import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionAdapter;
import org.dyn4j.dynamics.World;

import java.util.ArrayList;

public class TargetCollisionListener extends CollisionAdapter {
    private TargetBody target;
    private BoosterBody booster;
    private BouncingBalls bB;
    private World world;
    private int hitCnt;
    private ArrayList<TargetBody> targetSack;

    TargetCollisionListener(TargetBody target, World world, int hits, BouncingBalls bB) {
        this.target = target;
        this.world = world;
        this.hitCnt = hits;
        this.bB = bB;
        //this.targetSack = targetSack;
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
            if (!bB.getMuteMode()) {
                SoundManager sm = new SoundManager();
                sm.play(Sound.BOOSTER);
            }

            if (!booster.getActive())
            {
                booster.setActive(true);
                bB.activateBooster(booster.getType());
                bB.removeTarget(booster);
            }
        }

        if((body1 == target && body2 instanceof ShotBallBody) || ( body1 instanceof ShotBallBody && body2 == target)) {
            AnimationThread aT = new AnimationThread();
            if ((this.hitCnt <= 1)||(target.getHitNumber()<=1))
            {
                bB.removeTarget(target);
                bB.setCurrScore(bB.getCurrScore()+1);
            }else
            {
                target.setHitNumber(target.getHitNumber()-1);
                if (!bB.getMuteMode()) {
                    SoundManager sm = new SoundManager();
                    sm.play(Sound.TREFFER);
                }
                target.setColor(BouncingBalls.getSemiRandomColor(target.getHitNumber()));
                aT.run(target,false);
            }
        }
        return true;
    }
}
