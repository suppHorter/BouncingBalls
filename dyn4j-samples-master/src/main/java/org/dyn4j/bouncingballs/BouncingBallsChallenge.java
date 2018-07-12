package org.dyn4j.bouncingballs;

import javax.swing.*;



public class BouncingBallsChallenge extends BouncingBalls {

    private static LiftThread lt;

    public BouncingBallsChallenge(JFrame parentFrame, boolean muteMode) {
        super(parentFrame,muteMode);
        this.setMode(true);
    }
}

