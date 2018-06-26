/*
 * Copyright (c) 2010-2016 William Bittle  http://www.dyn4j.org/
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted 
 * provided that the following conditions are met:
 * 
 *   * Redistributions of source code must retain the above copyright notice, this list of conditions 
 *     and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice, this list of conditions 
 *     and the following disclaimer in the documentation and/or other materials provided with the 
 *     distribution.
 *   * Neither the name of dyn4j nor the names of its contributors may be used to endorse or 
 *     promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR 
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT 
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.dyn4j.samples;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.List;
import java.util.*;

import com.sun.xml.internal.ws.util.Pool;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.*;
import org.dyn4j.samples.framework.SimulationBody;
import org.dyn4j.samples.framework.SimulationFrame;

import java.util.concurrent.ThreadLocalRandom;

public class MouseInteraction extends SimulationFrame {
    //private static final long serialVersionUID = -1366264828445805140L;
    static int ballsInGame = 0; //Anzahl an Schuessen die momentan im Spiel vorhanden sind (maximal MAXBALLS)
    static int ballsCreated = 0; //Anzahl aller Schuesse die jemals erstellt wurden
    static boolean canShoot = true; //Man darf nur schiessen wenn die ballsInGame leer geworden sind
    static int rowsOfTargetsCreated;
	static int TURN = 1;
	static boolean WAIT = false;
	static double[] Yebenen = {4,0,-4,-8};
	static double[] Xebenen = {-5, 0, 5};

	static ArrayList<SimulationBody> targetSack= new ArrayList<>();

	//Point um Mauspos. zu speichern
	private Point point;
	//Vektor fuer
	private Vector2 shootingVector;
	//Liste aller erstellten Baelle
	private List<Body> ballList;
	//Boundary am unteren Ende
	private Body lowerBounds;

	//Statics für die Gamelogik
	//private static long SLEEPTIMER = 5; //Zeit bis man eine neue Salve abfeuern kann
	private static double TIME_BETWEEN_BALLS = 0.5; //Zeit zwischen den Schuessen einer Salve
    private static double TIMERCOUNTER_BETWEEN_BALLS; //Zaehlt die vergangenen Sekunden - wird zurückgedsetzt
    private static int MIN_BALLS_TO_CREATE = 1;
    private static int MAX_BALLS_TO_CREATE = 3;
    private static int MAXBALLS = 1; //Anzahl an Schuessen pro Salve
    private static double TIMER; //Zaehlt die Vergangenen Sekunden
	private static Point POINTSHOOTER = new Point(250,40); //Punkt an dem Schuesse abgefeuert werden



	private final class CustomMouseAdapter extends MouseAdapter {
		@Override

		public void mousePressed(MouseEvent e) {
		    //Maus Klick Position speichern
            if (canShoot) {
                point = new Point(e.getX(), e.getY());
                //Neuen Vektor für die Schuesse erstellen
                shootingVector = new Vector2();
                double dx = 0.1 * (e.getX() - POINTSHOOTER.getX());
                double dy = -0.1 * (e.getY() - POINTSHOOTER.getY());
                System.out.print(dx);
                System.out.print(" x ");
                System.out.print(dy);
                System.out.println("");
                shootingVector.set(dx, dy);
            }
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			//point = null;
		}
	}

	public MouseInteraction() {
		super("Mouse Interaction", 32.0);
		MouseAdapter ml = new CustomMouseAdapter();
		this.canvas.addMouseMotionListener(ml);
		this.canvas.addMouseWheelListener(ml);
		this.canvas.addMouseListener(ml);
	}

	protected void initializeWorld() {
	    //Gravitation der Welt anpassen
        Vector2 gravityVector = new Vector2();
        gravityVector.set(0,-20);
        this.world.setGravity(gravityVector);

		//Wände erstellen und Positionieren
		SimulationBody leftWall = new SimulationBody();
		SimulationBody rightWall = new SimulationBody();
		SimulationBody ceiling = new SimulationBody();
		lowerBounds = new SimulationBody();

		leftWall.addFixture(Geometry.createRectangle(10, 100));
		leftWall.setColor(Color.GRAY);
		leftWall.translate(-13.9,0);

		rightWall.addFixture(Geometry.createRectangle(10, 100));
		rightWall.setColor(Color.GRAY);
		rightWall.translate(13.9,0);

		ceiling.addFixture(Geometry.createRectangle(50, 10));
		ceiling.setColor(Color.GRAY);
		ceiling.translate(0,17);

		lowerBounds.addFixture(Geometry.createRectangle(50, 10));
		lowerBounds.translate(0, -40);

	    leftWall.setMass(MassType.INFINITE);
		rightWall.setMass(MassType.INFINITE);
		ceiling.setMass(MassType.INFINITE);
		lowerBounds.setMass(MassType.INFINITE);

	    this.world.addBody(leftWall);
		this.world.addBody(rightWall);
		this.world.addBody(ceiling);
		this.world.addBody(lowerBounds);
		this.world.addListener(new BoundaryCollisionListener(lowerBounds, world));

		//Ersten Targets erstellen
        createTargets();
	}

	@Override
	protected void update(Graphics2D g, double elapsedTime) {
		TIMERCOUNTER_BETWEEN_BALLS += elapsedTime;
		//Allgemeine vergangene Zeit (noch nicht verwendet)
		TIMER += elapsedTime;
		if(TURN > 1 && rowsOfTargetsCreated < TURN){
                if (targetSack.size() > 0) {
                    liftBalls();
                }
                createTargets();
        }
		//Nur schiessen falls Salve noch nicht beendet wurde
		if (TIMERCOUNTER_BETWEEN_BALLS > TIME_BETWEEN_BALLS
                && ballsCreated < (MAXBALLS * TURN)
                && canShoot){
			//System.out.println(TIMERCOUNTER_BETWEEN_BALLS);
			TIMERCOUNTER_BETWEEN_BALLS = 0;
			//Wurde geklickt und gibt es einen Vektor
			if (this.point != null && this.shootingVector != null) {
				//Umrechnung der Dimensionen
				double x =  (this.POINTSHOOTER.getX() - this.canvas.getWidth() / 2.0) / this.scale;
				double y = -(this.POINTSHOOTER.getY() - this.canvas.getHeight() / 2.0) / this.scale;

				// Neuen Schuss erstellen
				ShotBallBody ball = new ShotBallBody();
				BodyFixture fixture = new BodyFixture(Geometry.createCircle(0.3));

				fixture.setDensity(200);
				fixture.setRestitution(0.6);
				ball.addFixture(fixture);
				ball.translate(x, y);
				ball.setLinearVelocity(shootingVector);
				ball.setMass(MassType.NORMAL);
                //Schuss der Welt hinzufuegen
				this.world.addBody(ball);
				ballsInGame += 1;
				ballsCreated += 1;
				if (ballsCreated == (MAXBALLS * TURN)){
				    //Kein Schiessen mehr moeglich nachdem alle Schuesse einer Salve abgefeuert wurden
				    canShoot = false;
				    //Mausposition nullen
				    point = null;
                }
			}
		}
		super.update(g, elapsedTime);
	}


	private void createTargetBall(double xKoord, double yKoord)
	{
		TargetBody target = new TargetBody();
        BodyFixture fixture = new BodyFixture(Geometry.createCircle(1));
        target.hitNumber =  ThreadLocalRandom.current().nextInt(1,  5);
        target.addFixture(fixture);
		fixture.setRestitution(0.2);
        target.translate(xKoord,yKoord);
		target.setMass(MassType.INFINITE);
		this.world.addBody(target);
        this.world.addListener(new TargetCollisionListener(target, world, target.hitNumber));
		targetSack.add(target);
	}

	//private SimulationBody ballSack[] = new SimulationBody[20];

	//Zu zerstörende Baelle generieren
    public void createTargets(){
        rowsOfTargetsCreated += 1;
		//Zufallsanzahl an Baellen
        int randomPosBallsArr[] = new int[3];
		int randomNoBalls = ThreadLocalRandom.current().nextInt(MIN_BALLS_TO_CREATE, MAX_BALLS_TO_CREATE );
		for(int i = 0; i < randomNoBalls + 1; i++){
            ///createTargetBall(Xebenen[0],Yebenen[3]); //-4|-8
            createTargetBall(Xebenen[i],Yebenen[3]); //-4|-8
        }
	}

	private boolean searchInArray(int arr[],int no)
    {
        for (int i=0;i<arr.length;i++)
        {
            if (arr[i]==no)
            {
                return false;
            }
        }
        return true;
    }

	public void liftBalls() {

        for (int i = 0; i < targetSack.size(); i++) {
            if (targetSack.get(i).getTransform().getTranslationY() >= 3) {
                System.out.println("Verloren!!!");
                targetSack.get(i).removeAllFixtures();
                //TODO: Game Exit
            } else {
                //double tempX = targetSack.get(i).getTransform().getTranslationX();
                //double tempY = targetSack.get(i).getTransform().getTranslationY();
                //tempX += 4;
                //System.out.println("Targetsack: "+tempX + " " +tempY);

                targetSack.get(i).translate(0, 4);
                //System.out.println(targetSack.get(i).getTransform().getTranslationX()+" "+targetSack.get(i).getTransform().getTranslationY());
                //For schleife
                //Y Koordinaten eine ebene hoch
            }
        }
    }




	public static void main(String[] args) {
		MouseInteraction simulation = new MouseInteraction();
		simulation.run();
	}

}
