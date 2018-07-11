package org.dyn4j.bouncingballs;

import javax.swing.*;



public class BouncingBallsChallenge extends BouncingBalls {

    private static LiftThread lt;


    public BouncingBallsChallenge(JFrame parentFrame) {
        super(parentFrame);
        activateBooster(3);
        //lt = new LiftThread(this);
        //lt.run();
    }
}

