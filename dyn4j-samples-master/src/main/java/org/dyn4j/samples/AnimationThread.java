package org.dyn4j.samples;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

class AnimationThread extends Thread {
    private MouseInteraction bB;

    AnimationThread() {
        super();
    }
    public void run(TargetBody target,boolean dead){
        bB = target.getBouncingBallContr();
        if (target.getTimer()<=1)
        {
            target.setTimer(2000);
        }
        bB.getsHitAni(target,true);

        if (dead)
        {
            bB.destroyedAni(target);
        }
    }
}