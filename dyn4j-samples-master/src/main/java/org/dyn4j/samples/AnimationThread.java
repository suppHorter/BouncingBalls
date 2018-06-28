package org.dyn4j.samples;

//Setzt Timer und erstellt Animation für den Treffer Feedback  von Targets

class AnimationThread extends Thread {
    private BouncingBalls bB;

    AnimationThread() {
        super();
    }
    public void run(TargetBody target,boolean dead){
        bB = target.getBouncingBallContr();
        if (target.getTimer()<=1)
        {
            target.setTimer(5);
        }
        bB.getsHitAni(target,true);
    }
}