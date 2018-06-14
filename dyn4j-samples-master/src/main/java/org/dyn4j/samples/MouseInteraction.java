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

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.samples.framework.SimulationBody;
import org.dyn4j.samples.framework.SimulationFrame;

import java.util.concurrent.ThreadLocalRandom;

public class MouseInteraction extends SimulationFrame {

	//private static final long serialVersionUID = -1366264828445805140L;
	static int MIN_BALLS_TO_CREATE = 1;
	static int MAX_BALLS_TO_CREATE = 3;
	static double MAX_BOUNDARY_X = 27;
	static double MIN_BOUNDARY_X = -27;
	static double BOUNDARY_Y = 75;
	static int TURN = 1;
	static boolean WAIT = false;
	static double[] Yebenen = {4,0,-4,-8};
	static double[] Xebenen = {5,0,-5};

	ArrayList<SimulationBody> targetSack= new ArrayList<>();

	//Point um Mauspos. zu speichern
	private Point point;
	//Vektor fuer
	private Vector2 shootingVector;
	//Liste aller erstellten Baelle
	static ArrayList<SimulationBody> ballSack = new ArrayList<>();

	//Statics für die Gamelogik
	//private static long SLEEPTIMER = 5; //Zeit bis man eine neue Salve abfeuern kann
	private static double TIME_BETWEEN_BALLS = 0.1; //Zeit zwischen den Schuessen einer Salve
    private static double TIMERCOUNTER_BETWEEN_BALLS; //Zaehlt die vergangenen Sekunden - wird zurückgedsetzt

    private static int MAXBALLS = 5; //Anzahl an Schuessen pro Salve
    private static double TIMER; //Zaehlt die Vergangenen Sekunden
	private static Point POINTSHOOTER = new Point(250,40); //Punkt an dem Schuesse abgefeuert werden



	private final class CustomMouseAdapter extends MouseAdapter {
		@Override

		public void mousePressed(MouseEvent e) {
            //Maus Klick Position speichern

            if (targetSack.size()>0)
            {
                liftBalls();
            }
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
            createBalls();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			//point = null;
		}
	}

	/**
	 * Default constructor.
	 */
	public MouseInteraction() {
		super("Mouse Interaction", 32.0);

		MouseAdapter ml = new CustomMouseAdapter();
		this.canvas.addMouseMotionListener(ml);
		this.canvas.addMouseWheelListener(ml);
		this.canvas.addMouseListener(ml);
	}

	protected void initializeWorld() {
		//Wände erstellen und Positionieren
		SimulationBody leftWall = new SimulationBody();
		SimulationBody rightWall = new SimulationBody();
		SimulationBody ceiling = new SimulationBody();

		leftWall.addFixture(Geometry.createRectangle(11, 100));
		leftWall.setColor(Color.GRAY);
		leftWall.translate(-13.5,0);

		rightWall.addFixture(Geometry.createRectangle(11, 100));
		rightWall.setColor(Color.GRAY);
		rightWall.translate(13.5,0);

		ceiling.addFixture(Geometry.createRectangle(50, 10));
		ceiling.setColor(Color.GRAY);
		ceiling.translate(0,17);

	    leftWall.setMass(MassType.INFINITE);
		rightWall.setMass(MassType.INFINITE);
		ceiling.setMass(MassType.INFINITE);

	    this.world.addBody(leftWall);
		this.world.addBody(rightWall);
		this.world.addBody(ceiling);
	}

	@Override
	protected void update(Graphics2D g, double elapsedTime) {
		TIMERCOUNTER_BETWEEN_BALLS += elapsedTime;
		TIMER += elapsedTime;

		//Nur schiessen falls Salve noch nicht beendet wurde
		if (TIMERCOUNTER_BETWEEN_BALLS > TIME_BETWEEN_BALLS && this.ballSack.size() < MAXBALLS){
			//System.out.println(TIMERCOUNTER_BETWEEN_BALLS);
			TIMERCOUNTER_BETWEEN_BALLS = 0;
			//Wurde geklickt und gibt es einen Vektor
			if (this.point != null && this.shootingVector != null) {
				//Umrechnung der Dimensionen
				double x =  (this.POINTSHOOTER.getX() - this.canvas.getWidth() / 2.0) / this.scale;
				double y = -(this.POINTSHOOTER.getY() - this.canvas.getHeight() / 2.0) / this.scale;

				// Neuen Schuss erstellen
				SimulationBody ball = new SimulationBody();
				BodyFixture fixture = new BodyFixture(Geometry.createCircle(0.3));

				fixture.setDensity(200);
				fixture.setRestitution(0.9);
				ball.addFixture(fixture);

				ball.translate(x, y);
				ball.setLinearVelocity(shootingVector);
				ball.setMass(MassType.NORMAL);
                //Schuss der Welt hinzufuegen
				this.world.addBody(ball);
				//Arraylist ballSack befuellen
				ballSack.add(ball);
			}
		}
		super.update(g, elapsedTime);
	}


	private void createBall(double xKoord, double yKoord)
	{
		SimulationBody no = new SimulationBody();
		BodyFixture fixture = new BodyFixture(Geometry.createCircle(1));
		no.addFixture(fixture);
		fixture.setRestitution(1.5);
		no.translate(xKoord,yKoord);
		no.setMass(MassType.INFINITE);
		this.world.addBody(no);
		targetSack.add(no);
	}

	//private SimulationBody ballSack[] = new SimulationBody[20];

	//Zu zerstörende Baelle generieren
	private void createBalls(){
		//Zufallsanzahl an Baellen
		int randomNoBalls = ThreadLocalRandom.current().nextInt(MIN_BALLS_TO_CREATE, MAX_BALLS_TO_CREATE );
		for(int i = 0; i < randomNoBalls + 1; i++){
			createBall(Xebenen[i],Yebenen[3]); //-4|-8
			//System.out.println(targetSack.get(i).getTransform().getTranslationX()+" "+targetSack.get(i).getTransform().getTranslationY());
		}
	}

	public void liftBalls()
	{

		for(int i = 0; i < targetSack.size(); i++){
			if (targetSack.get(i).getTransform().getTranslationY() >= 3)
			{
				System.out.println("Verloren!!!");
				//TODO: Game Exit
			}else
			{
				//double tempX = targetSack.get(i).getTransform().getTranslationX();
				//double tempY = targetSack.get(i).getTransform().getTranslationY();
				//tempX += 4;
				//System.out.println("Targetsack: "+tempX + " " +tempY);

				targetSack.get(i).translate(0,4);
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
