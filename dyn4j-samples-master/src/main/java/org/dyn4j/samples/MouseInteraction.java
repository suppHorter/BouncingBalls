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

import java.awt.geom.Line2D;
import java.util.List;
import java.util.*;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.*;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.samples.framework.SimulationBody;
import org.dyn4j.samples.framework.SimulationFrame;

import java.util.concurrent.ThreadLocalRandom;

public class MouseInteraction extends SimulationFrame {
    //private static final long serialVersionUID = -1366264828445805140L;
    static int ballsInGame = 0; //Anzahl an Schuessen die momentan im Spiel vorhanden sind (maximal MAXBALLS)
    static int ballsCreated = 0; //Anzahl aller Schuesse die jemals erstellt wurden
    static boolean canShoot = true; //Man darf nur schiessen wenn die ballsInGame leer geworden sind
    static int rowsOfTargetsCreated;
	static int turn = 1;
	static boolean WAIT = false;
	static double[] Yebenen = {4,0,-4,-8};
	static double[] Xebenen = {-5, 0, 5};
	LvlBoxBody lvlBox;

	static ArrayList<SimulationBody> targetSack= new ArrayList<>();

	//Point um Mauspos. zu speichern
	private Point point;
	//Vektor fuer
	private Vector2 shootingVector;
	//Liste aller erstellten Baelle
	private List<Body> ballList;
	//Boundary am unteren Ende
	private Body lowerBounds;
	//Points für Mausposition
    private Point movedPoint;
    private Line2D mouseLine;

    private static double timer; //Zaehlt die Vergangenen Sekunden

	//Constants für die Gamelogik
	//private static long SLEEPTIMER = 5; //Zeit bis man eine neue Salve abfeuern kann
	private static double TIME_BETWEEN_BALLS = 0.5; //Zeit zwischen den Schuessen einer Salve
    private static double TIMERCOUNTER_BETWEEN_BALLS; //Zaehlt die vergangenen Sekunden - wird zurückgedsetzt
    private static int MIN_BALLS_TO_CREATE = 1;
    private static int MAX_BALLS_TO_CREATE = 3;
    private static int MIN_HIT_NUMBER = 5; //Minimalanzahl Treffer benötigt für zerstörung von targets
	private static int MAX_HIT_NUMBER = 20;//Maximalanzahl Treffer benötigt für zerstörung von targets
    private static int MAXBALLS = 5; //Anzahl an Schuessen pro Salve
	private static Point POINTSHOOTER = new Point(250,40); //Punkt an dem Schuesse abgefeuert werden
	private static int lvlCnt;



	private final class CustomMouseAdapter extends MouseAdapter {

        @Override
        public void mouseMoved(MouseEvent e) {
            movedPoint = canvas.getMousePosition();

        }

		@Override
		public void mousePressed(MouseEvent e) {
		    //Maus Klick Position speichern
            if (canShoot) {
				lvlCnt++;
                point = new Point(e.getX(), e.getY());
                //Neuen Vektor für die Schuesse erstellen
                shootingVector = new Vector2();
                double dx = 0.1 * (e.getX() - POINTSHOOTER.getX());
                double dy = -0.1 * (e.getY() - POINTSHOOTER.getY());
                shootingVector.set(dx, dy);
            }
		}

		@Override
		public void mouseReleased(MouseEvent e) {
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
		lvlCnt = 0;
		lvlBox = new LvlBoxBody();
		createLvlLbl();
		//Gravitation der Welt anpassen
        Vector2 gravityVector = new Vector2();
        gravityVector.set(0,-20);
        this.world.setGravity(gravityVector);

		//Wände erstellen und Positionieren
		SimulationBody leftWall = new SimulationBody();
		SimulationBody rightWall = new SimulationBody();
		SimulationBody ceiling = new SimulationBody();
		lowerBounds = new SimulationBody();

		leftWall.addFixture(Geometry.createRectangle(12, 100));
		leftWall.setColor(Color.GRAY);
		leftWall.translate(-13.9,0);

		rightWall.addFixture(Geometry.createRectangle(12, 100));
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
        //Umrechnung der Dimensionen Schusspunkt
        Vector2 shootToVector = this.toWorldCoordinates(POINTSHOOTER);

        //Schusslinie anzeigen
        if (movedPoint != null){
            Vector2 aimLine = this.toWorldCoordinates(movedPoint);
            g.setColor(Color.WHITE);
            g.draw(new Line2D.Double(shootToVector.x * scale, shootToVector.y * scale, aimLine.x * scale, aimLine.y * scale));
        }

		TIMERCOUNTER_BETWEEN_BALLS += elapsedTime;
		//Allgemeine vergangene Zeit (noch nicht verwendet)
		timer += elapsedTime;

		if(turn > 1 && rowsOfTargetsCreated < turn){
                if (targetSack.size() > 0) {
                    liftBalls();
					updateLvlLbl();
                }
                createTargets();
        }
		//Nur schiessen falls Salve noch nicht beendet wurde
		if (TIMERCOUNTER_BETWEEN_BALLS > TIME_BETWEEN_BALLS
                && ballsCreated < (MAXBALLS * turn)
                && canShoot){
			//System.out.println(TIMERCOUNTER_BETWEEN_BALLS);
			TIMERCOUNTER_BETWEEN_BALLS = 0;
			//Wurde geklickt und gibt es einen Vektor
			if (this.point != null && this.shootingVector != null) {
				// Neuen Schuss erstellen
				ShotBallBody ball = new ShotBallBody();
				BodyFixture fixture = new BodyFixture(Geometry.createCircle(0.6));

				fixture.setDensity(200);
				fixture.setRestitution(0.6);
				ball.addFixture(fixture);
				ball.translate(shootToVector);
				ball.setLinearVelocity(shootingVector);
				ball.setMass(MassType.NORMAL);
                //Schuss der Welt hinzufuegen
				this.world.addBody(ball);
				ballsInGame += 1;
				ballsCreated += 1;
				if (ballsCreated == (MAXBALLS * turn)){
				    //Kein Schiessen mehr moeglich nachdem alle Schuesse einer Salve abgefeuert wurden
				    canShoot = false;
				    //Mausposition nullen
				    point = null;
                }
			}
		}
		super.update(g, elapsedTime);
	}

	private void createLvlLbl()
	{
		BodyFixture fixture = new BodyFixture(Geometry.createRectangle(2,2));
		lvlBox.addFixture(fixture);
		lvlBox.lvlNumber = lvlCnt;
		fixture.setRestitution(0);
		lvlBox.setColor(Color.WHITE);
		lvlBox.translate(7,10);
		lvlBox.setMass(MassType.INFINITE);
		this.world.addBody(lvlBox);

		//g.setFont(new Font("Default", Font.PLAIN, 20));
		//g.drawString(String.valueOf(hitNumber), (int)radius-12, (int)radius+5);
	}

	public void updateLvlLbl()
	{
		lvlBox.lvlNumber = lvlCnt;
	}

	private void createTargetBall(double xKoord, double yKoord)
	{
		TargetBody target = new TargetBody();
		double rad;
		rad =  Math.random()+1;
        BodyFixture fixture = new BodyFixture(Geometry.createCircle(rad));
        target.hitNumber =  ThreadLocalRandom.current().nextInt(10,  20);
        target.setColor(getSemiRandomColor(target.hitNumber));
        target.addFixture(fixture);
		fixture.setRestitution(1);
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
				//System.out.println("Verloren!!!");
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
    public static final Color getSemiRandomColor(int i) {
        //Farbe anhand der restlichen schritte wählen
        int diff = MAX_HIT_NUMBER - MIN_HIT_NUMBER;
        int step = 	Math.round(diff/6);
        if(i < MIN_HIT_NUMBER) {
            return new Color(0,255,0);
        }
        if (i < MIN_HIT_NUMBER + step) {
            return new Color(81,124,14);
        }
        if (i < MIN_HIT_NUMBER + step * 2) {
            return new Color(166,215,31);
        }
        if (i < MIN_HIT_NUMBER + step * 3) {
            return new Color(255,255,0);
        }
        if (i < MIN_HIT_NUMBER + step * 4) {
            return new Color(255,160,62);
        }
        if (i < MIN_HIT_NUMBER + step * 5) {
            return new Color(255,0,0);
        }
        if (i <= MAX_HIT_NUMBER) {
            return new Color(138,0,0);
        }
        return Color.WHITE;
    }

    private Vector2 toWorldCoordinates(Point point) {
	    //Umrechnen von Punktkoordinaten zu Koordinaten des Spielfelds
        double x =  (point.getX() - this.canvas.getWidth() / 2.0) / this.scale;
        double y = -(point.getY() - this.canvas.getHeight() / 2.0) / this.scale;
        return new Vector2(x, y);
    }


	public static void main(String[] args) {
		MouseInteraction simulation = new MouseInteraction();
		simulation.run();
	}

}
