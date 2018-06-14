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
	static int MAX_BALLS_TO_CREATE = 4;
	static double MAX_BOUNDARY_X = 27;
	static double MIN_BOUNDARY_X = -27;
	static double BOUNDARY_Y = 75;
	static int TURN = 1;
	static boolean WAIT = false;

	//Point um Mauspos. zu speichern
	private Point point;
	//Vektor fuer
	private Vector2 shootingVector;
	//Liste aller erstellten Baelle
	private List<Body> ballList;

	//Statics für die Gamelogik
	private static long SLEEPTIME = 150; //Zeit in ms zwischen Schuessen
	private static double ACTIONTIMER = 5.0; //Zeit bis neue Baelle auftauchen

	private static double TIMERCOUNTER;
	private static Point POINTSHOOTER = new Point(300,80);



	private final class CustomMouseAdapter extends MouseAdapter {
		@Override

		public void mousePressed(MouseEvent e) {
		    if (!WAIT) {
                WAIT = true;
                for (int i = 0; i < TURN; i++) {
                    point = new Point(e.getX(), e.getY());
                    //Maus Klick Position speichern
                    //Neuen Vektor für die Schuesse erstellen
                    shootingVector = new Vector2();
                    double dx = 0.1 * (e.getX() - POINTSHOOTER.getX());
                    double dy = -0.1 * (e.getY() - POINTSHOOTER.getY());
                    System.out.print(dx);
                    System.out.print(" x ");
                    System.out.print(dy);
                    System.out.println("");
                    shootingVector.set(dx, dy);
                    try {
                        Thread.sleep(SLEEPTIME);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                if(TURN < 5) {TURN += 1;}
                WAIT = false;
            }
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			point = null;
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
	
	/**
	 * Creates game objects and adds them to the world.
	 */
		protected void initializeWorld() {

		//Wände erstellen und Positionieren
		SimulationBody leftWall = new SimulationBody();
		SimulationBody rightWall = new SimulationBody();
		SimulationBody ceiling = new SimulationBody();


		leftWall.addFixture(Geometry.createRectangle(10, 100));
		leftWall.setColor(Color.GRAY);
		leftWall.translate(-13.5,0);

		rightWall.addFixture(Geometry.createRectangle(10, 100));
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
		
		// see if the user clicked
		if (this.point != null && this.shootingVector != null) {
			// convert from screen space to world space coordinates
			double x =  (this.POINTSHOOTER.getX() - this.canvas.getWidth() / 2.0) / this.scale;
			double y = -(this.POINTSHOOTER.getY() - this.canvas.getHeight() / 2.0) / this.scale;
			
			// Neuen Ball erstellen und
			SimulationBody no = new SimulationBody();
			BodyFixture fixture = new BodyFixture(Geometry.createCircle(0.3));

			fixture.setDensity(200);
			fixture.setRestitution(0.9);
			no.addFixture(fixture);

			no.translate(x, y);
			no.setLinearVelocity(shootingVector);
			no.setMass(MassType.NORMAL);
			this.world.addBody(no);

			
			// clear the point
			this.point = null;
		}

		TIMERCOUNTER += elapsedTime;
		if (TIMERCOUNTER > ACTIONTIMER){
			//System.out.println(TIMERCOUNTER);
			TIMERCOUNTER = 0;

		}

		super.update(g, elapsedTime);
	}

	//Zu zerstörende Baelle generieren
	private void CreateBalls(){
		//Zufallsanzahl an Baellen
		int randomNoBalls = ThreadLocalRandom.current().nextInt(MIN_BALLS_TO_CREATE, MAX_BALLS_TO_CREATE + 1);
		for(int i = 0; i < randomNoBalls + 1; i++){
			SimulationBody no = new SimulationBody();
			BodyFixture fixture = new BodyFixture(Geometry.createCircle(0.3));
			no.addFixture(fixture);
			//no.translate();
			no.setMass(MassType.INFINITE);
		}
	}

	/**
	 * Entry point for the example application.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		MouseInteraction simulation = new MouseInteraction();
		simulation.run();
	}

}
