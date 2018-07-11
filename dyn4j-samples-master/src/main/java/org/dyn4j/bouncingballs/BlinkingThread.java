package org.dyn4j.bouncingballs;

public class BlinkingThread extends Thread {

    private StartBallsFrame sf;

    public BlinkingThread(StartBallsFrame tempSf){
        super();
        this.sf = tempSf;
    }

    public void run(){

        while(true) {
            try {
                // StartBallsFrame.switchPositions();
                sf.blinking();
                sleep(200);
            }
            catch(InterruptedException e) {
            }

        }

    }

}
