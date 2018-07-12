
package org.dyn4j.bouncingballs;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.*;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.*;
import org.dyn4j.bouncingballs.framework.SimulationBody;
import org.dyn4j.bouncingballs.framework.SimulationFrame;

import javax.swing.*;
import java.util.Timer;
import java.util.concurrent.ThreadLocalRandom;

public class BouncingBalls extends SimulationFrame {
    public JFrame parentFrame;

    private int eg;
    //Durchmesser der Schüsse
    static double bulletRadius = 0.6;
    //Anzahl an Schuessen die momentan im Spiel vorhanden sind (maximal MAXBALLS)
    static int ballsInGame = 0;
    //Anzahl aller Schuesse die erstellt wurden. Wird nach einer Salve zurückgesetzt
    static int ballsCreated = 0;
    //Automatik oder Einzelschuss
    static boolean shootStyle;
    //Man darf nur schiessen wenn die ballsInGame leer geworden sind
    static boolean canShoot = true;
    //Anzahl der erstellten Target Reihen
    static int rowsOfTargetsCreated;
    //Runden zählen
    public static int turn = 1;

    public static int getLvlCnt() {
        return lvlCnt;
    }

    private static int lvlCnt;
	//Koordinaten für die Target Reihen
	static double[] Yebenen = {4,0,-4,-8};
	static double[] Xebenen = {-5, 0, 5};
	//Anzeigeboxen
	private LvlBoxBody lvlBox;
    private LvlBoxBody highScoreBox;
    private LvlBoxBody currScoreBox;
    private LvlBoxBody currShotsBox;
    //Liste von Targets für diverse Zwecke
    private SimulationBody boosterTramp;
    //Menubutton
    private MenuButtonBox menuBox;
    // Zweidimensionales array 3 spalten 4 zeilen
	static ArrayList<TargetBody> targetSack= new ArrayList<>();
	//Point um Mauspos. zu speichern
	private Point point;
    private Point point_menu;
	//Vektor fuer Schüsse

	private Vector2 shootingVector;
    private Vector2 rapidShootingVector;
	//Boundary am unteren Ende
	private Body lowerBounds;
	//Body fuer Kanone
	private CannonBody cannon;
	//Points für Mausposition
    private Point movedPoint;
    //Zaehlt die Vergangenen Sekunden
    private static double timer;
    //Timer für Trampolin Booster
    private static double trampBoosterTimer;
    //Zaehlt die vergangenen Sekunden - wird zurückgedsetzt
    private static double timercounter_between_balls;
    private static int MIN_HIT_NUMBER_START = 5; //Minimalanzahl Treffer benötigt für zerstörung von targets - Constant
    private static int MAX_HIT_NUMBER_START = 10; //Maximalanzahl Treffer benötigt für zerstörung von targets - Constant
    private static int min_hit_number = MIN_HIT_NUMBER_START; //Minimalanzahl Treffer wird erhöht wärend Laufzeit
    private static int max_hit_number = MAX_HIT_NUMBER_START; //Maximalanzahl Treffer wird erhöht wärend Laufzeit
    private static boolean trampRotateRight = true;
    private static double trampRotationStep = 0.01;
    //Constants für die Gamelogik
    private static int BOOSTER_CHANCE = 40; //Prozentuale Chance auf Booster in einer Reihe
    private static double TIME_BETWEEN_BALLS = 0.3; //Zeit zwischen den Schuessen einer Salve
    private static double MAX_BALL_RADIUS = 0.8; //Maximaler Radius der Schüsse
    private static int MIN_BALLS_TO_CREATE = 1;
    private static int MAX_BALLS_TO_CREATE = 3;

    private boolean challengeMode = false;
    private boolean muteMode = false;

    public boolean getMuteMode(){return this.muteMode;}
    public void setMuteMode(boolean muteMode){this.muteMode = muteMode;}

    public int getMaxBalls() {return maxBalls;}

    private static int maxBalls = 4; //Anzahl an Schuessen pro Salve
	private static Point POINTSHOOTER = new Point(250,40); //Punkt an dem Schuesse abgefeuert werden
    private static boolean allowedBoosters = true;

	//Booster Aktiv:
    private boolean trampActive = false;
    private boolean bombActive = false;

    private int highScore,currScore;
    private Timer timerChallenge;

    public void setCurrScore(int score){this.currScore = score;}

    public int getCurrScore(){return this.currScore;}
    public void setMode(boolean mode){this.challengeMode = mode;}

	private final class CustomMouseAdapter extends MouseAdapter {
        private BouncingBalls bouncingBalls;

        public CustomMouseAdapter(BouncingBalls bouncingBalls) {
            this.bouncingBalls = bouncingBalls;
        }
        @Override
        public void mouseMoved(MouseEvent e) {
            movedPoint = canvas.getMousePosition();
            Point rapidPoint = new Point(canvas.getMousePosition());
            //Neuen Vektor für die Schuesse erstellen
            //Faktor 0,15 da sonst Schüsse zu stark
            rapidShootingVector = new Vector2();
            double dx =  0.15 * (rapidPoint.getX() - POINTSHOOTER.getX());
            double dy = -0.15 * (rapidPoint.getY() - POINTSHOOTER.getY());
            rapidShootingVector.set(dx, dy);
        }
		@Override
		public void mousePressed(MouseEvent e) {
		    //Maus Klick Position speichern
            point_menu = new Point(canvas.getMousePosition());
            //Menü angeklickt
            if ((point_menu.getX()>23 && point_menu.getX()<96)&&(point_menu.getY()>22 && point_menu.getY()<45))
            {
                this.bouncingBalls.stop();
                this.bouncingBalls.setVisible(false);
                this.bouncingBalls.parentFrame.setVisible(true);
                setStandard();

            }else if (canShoot && ballsCreated == 0) {
                point = new Point(canvas.getMousePosition());
				lvlCnt++;
				//Benötigte Schuesse für Ziele in abhängigkeit des Levels hochsetzen
                //TODO
                //BALANCE
                min_hit_number = Math.round((1+lvlCnt/10)*MIN_HIT_NUMBER_START);
                max_hit_number = Math.round((1+lvlCnt/10)*MAX_HIT_NUMBER_START);
                //Neuen Vektor für die Schuesse erstellen
                //Faktor 0,15 da sonst Schüsse zu stark
                shootingVector = new Vector2();
                double dx =  0.15 * (point.getX() - POINTSHOOTER.getX());
                double dy = -0.15 * (point.getY() - POINTSHOOTER.getY());
                shootingVector.set(dx, dy);
            }
		}
		@Override
		public void mouseReleased(MouseEvent e) {
            //Überschrieben mit leer da sonst ungewünschtes verhalten
		}
	}

	public BouncingBalls(JFrame parentFrame, boolean muteMode) {
		super("Bouncing Balls", 32.0);
		this.parentFrame = parentFrame;
		MouseAdapter ml = new CustomMouseAdapter(this);
		this.canvas.addMouseMotionListener(ml);
		this.canvas.addMouseWheelListener(ml);
		this.canvas.addMouseListener(ml);
		this.muteMode = muteMode;
		this.run();
		/*
		if (challengeMode)
        {

            timerChallenge = new Timer(1000, new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    // 1 Sekunde abziehen
                    timerChallenge.counterValue--;

                    // Zahl in Label darstellen
                    Countdown.label.setText(String.valueOf(counterValue));

                    // Falls Zähler = 0, Countdown abgelaufen!
                    if(Countdown.counterValue == 0){
                        System.out.println("Counterdown ausgelaufen!");

                        // Timer stoppen
                        Countdown.timer.stop();
                    }
                }
            });
        }*/

    }

	protected void initializeWorld() {
        min_hit_number = MIN_HIT_NUMBER_START;
        max_hit_number = MAX_HIT_NUMBER_START;
		lvlCnt = 1;
        shootStyle = false;
        trampBoosterTimer = 0;
        trampActive = false;
		lvlBox = new LvlBoxBody();
		highScoreBox = new LvlBoxBody();
		//currScoreBox = new LvlBoxBody();
        currShotsBox = new LvlBoxBody();
		createLvlLbl();
        createHighScore();
        //createCurrScore();
        createCurrShotsBox();
        menuBox = new MenuButtonBox();
		//Gravitation der Welt anpassen
        Vector2 gravityVector = new Vector2();
        gravityVector.set(0,-30);
        this.world.setGravity(gravityVector);

		//Wände erstellen und Positionieren
		SimulationBody leftWall = new SimulationBody();
		SimulationBody rightWall = new SimulationBody();
		SimulationBody ceiling = new SimulationBody();
        boosterTramp = new SimulationBody();
		lowerBounds = new SimulationBody();

		BodyFixture wallFixture = new BodyFixture(Geometry.createRectangle(12, 100));
		wallFixture.setRestitution(0.5);

		leftWall.addFixture(wallFixture);
		leftWall.setColor(Color.GRAY);
		leftWall.translate(-13.9,0);
        leftWall.setMass(MassType.INFINITE);

		rightWall.addFixture(wallFixture);
		rightWall.setColor(Color.GRAY);
		rightWall.translate(13.9,0);
        rightWall.setMass(MassType.INFINITE);


        ceiling.addFixture(Geometry.createRectangle(50, 10));
		ceiling.setColor(Color.GRAY);
		ceiling.translate(0,17);
        ceiling.setMass(MassType.INFINITE);


        BodyFixture trampFix = new BodyFixture(Geometry.createRectangle(50, 0.5));
		trampFix.setRestitution(80);
        boosterTramp.addFixture(trampFix);
        boosterTramp.getTransform().setRotation(0.1);
        boosterTramp.setColor(Color.GREEN);
        boosterTramp.translate(0,-10);
        boosterTramp.setMass(MassType.INFINITE);

		lowerBounds.addFixture(Geometry.createRectangle(50, 10));
		lowerBounds.translate(0, -40);
		lowerBounds.setMass(MassType.INFINITE);

	    this.world.addBody(leftWall);
		this.world.addBody(rightWall);
		this.world.addBody(ceiling);
		this.world.addBody(lowerBounds);
		this.world.addListener(new BoundaryCollisionListener(lowerBounds, world));

        //Kanone erstellen
        BodyFixture fixture = new BodyFixture(Geometry.createRectangle(6, 03));

        //Kollision deaktivieren
        fixture.setSensor(true);

        cannon = new CannonBody();
        cannon.addFixture(fixture);
        cannon.setMass(MassType.INFINITE);
        cannon.translate(0.0, 0.0);
        cannon.translate(0,9.5);
        this.world.addBody(cannon);



        createMenuButton();
		//Ersten Targets erstellen
        createTargets();
	}

	public void createMenuButton(){
        BodyFixture fixture = new BodyFixture(Geometry.createRectangle(6, 03));
        fixture.setSensor(true);
        menuBox.addFixture(fixture);
        menuBox.setMass(MassType.INFINITE);
        menuBox.translate(-7.05, 10.2);

        this.world.addBody(menuBox);

    }

	public void activateBooster(int type)
    {
        switch (type)
        {
            case 0: //Trampolin
                if (!trampActive)
                {
                    this.world.addBody(boosterTramp);
                    trampBoosterTimer = 0;
                    trampActive = true;
                }
                break;
            case 1: //Bombe
                for (int i=0;i<targetSack.size();i++)
                {
                    targetSack.get(i).setHitNumber(targetSack.get(i).getHitNumber()-(5*(1+lvlCnt/10)));
                    if ((targetSack.get(i).getHitNumber()<=0) &&(!(targetSack.get(i) instanceof BoosterBody)))
                    {
                        this.removeTarget(targetSack.get(i));
                    }
                }
                break;
            case 2: //großere Schüsse
                if (bulletRadius<MAX_BALL_RADIUS)
                {
                    bulletRadius+=0.02;
                }
                break;
            case 3: //Rapid Fire
                if (!shootStyle)
                {
                    maxBalls += 20;
                    TIME_BETWEEN_BALLS = 0.1;
                    shootStyle = true;
                }
                break;
            case 4: // Zusatzschuss
                maxBalls++;
                break;
        }

    }


    public void deActivateBooster(int type)
    {
        //Hier alle Booster löschen (nach Zugende)this.world.removeBody(boosterTramp);
        switch (type)
        {
            case 0:
                this.world.removeBody(boosterTramp);
                trampActive = false;
                trampBoosterTimer = 1000;
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                shootStyle = false;
                maxBalls -= 20;
                TIME_BETWEEN_BALLS = 0.3;
                ballsCreated = 0;
                break;
            case 4:
                break;
        }
    }

	@Override
	protected void update(Graphics2D g, double elapsedTime) {

        //Umrechnung der Dimensionen Schusspunkt
        Vector2 shootToVector = this.toWorldCoordinates(POINTSHOOTER);

        //Schusslinie anzeigen u. drehen / Kanone drehen
        if (movedPoint != null){
            Vector2 aimLine = this.toWorldCoordinates(movedPoint);
            g.setColor(Color.LIGHT_GRAY);
            g.draw(new Line2D.Double(shootToVector.x * scale, shootToVector.y * scale, aimLine.x * scale, aimLine.y * scale));
            //Rotation Kanone
			Vector2 xAxis = new Vector2(1.0, 0.0);
			double angle = xAxis.getAngleBetween(shootToVector.to(aimLine));
            cannon.getTransform().setRotation(angle);
        }
        //Zeit zwischen den Schüssen einer Salve messen
		timercounter_between_balls += elapsedTime;
		//Allgemeine vergangene Zeit (noch nicht verwendet)
		timer += elapsedTime;
		
		//Wiggle that Tramp biatch
        if (boosterTramp != null){
            double radTramp = boosterTramp.getTransform().getRotation();
            if(radTramp >= 0.2){
                trampRotateRight = false;
            }
            if (radTramp <= -0.2){
                trampRotateRight = true;
            }
            if (trampRotateRight){
                radTramp = radTramp + trampRotationStep;
            }else{
                radTramp = radTramp - trampRotationStep;
            }

            boosterTramp.getTransform().setRotation(radTramp);
        }


        trampBoosterTimer += elapsedTime;
        //Zeit für Trampolintimer
        if (trampBoosterTimer>=10)
        {
            deActivateBooster(0);
        }
        highScoreBox.lvlNumber = currScore;

		//targets erstellen falls momentane Runde abgeschlossen wurde
		if((turn > 1) && (rowsOfTargetsCreated < turn)){
                if (targetSack.size() > 0) {
                    liftBalls();
                    lvlBox.lvlNumber = lvlCnt;
                }
                if(!this.isStopped()) {
                    createTargets();
                }
        }
        if (!this.isStopped()) {
            if (targetSack.isEmpty())
            {
                //System.out.print("Clear");
                deActivateBooster(0);
                //currScoreBox.lvlNumber = lvlCnt;
                lvlBox.lvlNumber = lvlCnt;
            }

            //Animation für Targetfeedback anhand des Timers beenden
            if (targetSack.size()>0)
            {
                for (int i=0;i<targetSack.size();i++)
                {
                    if (targetSack.get(i).getTimer() > 0)
                    {
                        targetSack.get(i).setTimer(targetSack.get(i).getTimer()-1);
                    }else
                    {
                        if (targetSack.get(i).getGrowed())
                        {
                            TargetBody tb = targetSack.get(i);
                            tb.setGrowed(false);
                            getsHitAni(tb, false);
                        }
                    }
                }
            }
            //Nur schießen falls Salve noch nicht beendet wurde
            if (timercounter_between_balls > TIME_BETWEEN_BALLS
                    //&& ballsCreated < (maxBalls * turn)
                    && canShoot){
                timercounter_between_balls = 0;

                //Wurde geklickt und gibt es einen Vektor
                if (this.point != null && this.shootingVector != null && this.rapidShootingVector != null) {
                    //Neuen Schuss erstellen
                    ShotBallBody ball = new ShotBallBody();
                    BodyFixture fixture = new BodyFixture(Geometry.createCircle(bulletRadius));
                    fixture.setDensity(200);
                    fixture.setRestitution(0.6);
                    ball.addFixture(fixture);
                    ball.translate(shootToVector);
                    ball.translate(0,-0.2);
                    if (!shootStyle)
                    {
                        ball.setLinearVelocity(shootingVector);
                    }
                    else
                    {
                        ball.setLinearVelocity(rapidShootingVector);
                    }

                    ball.setMass(MassType.NORMAL);
                    //Schuss der Welt hinzufuegen
                    this.world.addBody(ball);
                    if (!muteMode)
                    {
                        SoundManager sm = new SoundManager();
                        sm.play(Sound.SCHUSS);
                    }
                    ballsInGame += 1;
                    ballsCreated += 1;
                    currShotsBox.lvlNumber = maxBalls - ballsCreated;
                    if ((ballsCreated >= maxBalls)&&shootStyle==true)
                    {
                        rapidShootingVector = null;
                        canShoot = false;
                        point = null;
                        deActivateBooster(3);
                    }
                    else if ((ballsCreated >= maxBalls) && !shootStyle){
                        //Kein Schiessen mehr moeglich nachdem alle Schuesse einer Salve abgefeuert wurden
                        canShoot = false;
                        ballsCreated = 0;
                        //Mausposition nullen
                        point = null;
                    }

                }
            }
            super.update(g, elapsedTime);
        }
    }

    private void createCurrShotsBox()
    {
        BodyFixture fixture = new BodyFixture(Geometry.createRectangle(2,2));
        fixture.setSensor(true);
        currShotsBox.addFixture(fixture);
        currShotsBox.lvlNumber = currScore;
        fixture.setRestitution(0);
        currShotsBox.setColor(Color.GREEN);
        currShotsBox.translate(2,8.5);
        currShotsBox.setMass(MassType.INFINITE);
        this.world.addBody(currShotsBox);
    }

	private void createLvlLbl()
	{
	    //Levelzähler anzeigen
		BodyFixture fixture = new BodyFixture(Geometry.createRectangle(0.5,0.5));
		fixture.setSensor(true);
		lvlBox.addFixture(fixture);
		lvlBox.lvlNumber = lvlCnt;
		fixture.setRestitution(0);
		lvlBox.setColor(Color.LIGHT_GRAY);
		lvlBox.translate(7,10);
		lvlBox.setMass(MassType.INFINITE);
		this.world.addBody(lvlBox);
	}

    private void createCurrScore()
    {
        BodyFixture fixture = new BodyFixture(Geometry.createRectangle(2,2));
        fixture.setSensor(true);
        currScoreBox.addFixture(fixture);
        currScoreBox.lvlNumber = currScore;
        fixture.setRestitution(0);
        currScoreBox.setColor(Color.WHITE);
        currScoreBox.translate(7,9);
        currScoreBox.setMass(MassType.INFINITE);
        this.world.addBody(currScoreBox);
    }

    private void createHighScore()
    {
        BodyFixture fixture = new BodyFixture(Geometry.createRectangle(2,2));
        fixture.setSensor(true);
        highScoreBox.addFixture(fixture);
        highScoreBox.lvlNumber = highScore;
        fixture.setRestitution(0);
        highScoreBox.setColor(Color.WHITE);
        highScoreBox.translate(7,9);
        highScoreBox.setMass(MassType.INFINITE);
        this.world.addBody(highScoreBox);

        //g.setFont(new Font("Default", Font.PLAIN, 20));
        //g.drawString(String.valueOf(hitNumber), (int)radius-12, (int)radius+5);
    }

    public void removeTarget(TargetBody target)
    {
        world.removeBody(target);
        targetSack.remove(target);
        target.removeAllFixtures();
    }

    //Animation durchführen oder beenden
    public void getsHitAni(TargetBody target, boolean grow) {
        double rad;
        rad = target.getCurrRadius();
        if (grow){
            BodyFixture fixture = new BodyFixture(Geometry.createCircle(rad - 0.2));
            target.removeAllFixtures();
            target.addFixture(fixture);
            target.setGrowed(true);
        }else{
            BodyFixture fixture = new BodyFixture(Geometry.createCircle(rad + 0.2));
            target.removeAllFixtures();
            target.addFixture(fixture);
        }
	}

    public void destroyedAni(TargetBody target)
    {
        removeTarget(target);
    }

    protected void createBooster(int type, double xKoord, double yKoord)
    {
        BoosterBody booster = new BoosterBody();
        BodyFixture fixture = new BodyFixture(Geometry.createCircle(0.7));
        booster.setPosX(xKoord);
        booster.setPosY(yKoord);
        booster.addFixture(fixture);
        booster.setType(type);
        booster.setColor(booster.getColor());
        fixture.setRestitution(0.6);
        booster.translate(xKoord, yKoord);
        booster.setMass(MassType.INFINITE);
        this.world.addBody(booster);
        this.world.addListener(new TargetCollisionListener(booster, world, this));
        targetSack.add(booster);
    }

	private void createTargetBall(double xKoord, double yKoord)
    {
		TargetBody target = new TargetBody();
        target.setBouncingBallContr(this);
		double rad;
		int hitNo;
		rad =  Math.random()+0.75;
        BodyFixture fixture = new BodyFixture(Geometry.createCircle(rad));
        hitNo = ThreadLocalRandom.current().nextInt(min_hit_number,max_hit_number);
        target.setHitNumber(hitNo);
        target.setColor(getSemiRandomColor(target.getHitNumber()));
        target.setPosX(xKoord);
        target.setPosY(yKoord);

        target.setCurrRadius(rad);
        target.addFixture(fixture);
		fixture.setRestitution(10);
        target.translate(xKoord,yKoord);
		target.setMass(MassType.INFINITE);
		this.world.addBody(target);
        this.world.addListener(new TargetCollisionListener(target, world, target.getHitNumber(), this));
		targetSack.add(target);

	}

	//Zu zerstörende Baelle generieren
    public void createTargets() {
        currShotsBox.lvlNumber = maxBalls;
        rowsOfTargetsCreated += 1;
        //Zufallsanzahl an Baellen
        int randomNoBalls = ThreadLocalRandom.current().nextInt(MIN_BALLS_TO_CREATE, MAX_BALLS_TO_CREATE);
        int boosterPosib;
        int boosterTypePosib;
        for (int i = 0; i < randomNoBalls + 1; i++) {
            ///createTargetBall(Xebenen[0],Yebenen[3]); //-4|-8
            boosterPosib = ThreadLocalRandom.current().nextInt(0, 100);
            boosterTypePosib = ThreadLocalRandom.current().nextInt(0, 5);

            if ((boosterPosib > 0) && (boosterPosib < BOOSTER_CHANCE) && allowedBoosters) {
                //Prüfung ob schon groß genug
                while ((bulletRadius >= MAX_BALL_RADIUS) && (boosterTypePosib == 2)) {
                    //Wenn ja dann solange random bis keine 2 mehr
                    boosterTypePosib = ThreadLocalRandom.current().nextInt(0, 4);
                }
                if ((!challengeMode)&&(turn>1)) {
                    createBooster(boosterTypePosib, Xebenen[i], Yebenen[3]);
                }
                allowedBoosters = false;
            } else {
                createTargetBall(Xebenen[i], Yebenen[3]); //-4|-8
            }

        }
        allowedBoosters = true;
	}

    //Targets eine Reihe nach oben verschieben
    public void liftBalls() {
        for (int i = 0; i < targetSack.size(); i++) {
            if (targetSack.get(i).getTransform().getTranslationY() >= 3) {
                targetSack.get(i).removeAllFixtures();
                this.endGame();
            } else {
                targetSack.get(i).translate(0, 4);
                targetSack.get(i).setPosX(targetSack.get(i).getPosX());
                targetSack.get(i).setPosY(targetSack.get(i).getPosY()+4);
            }
        }
    }

    //Farbe anhand der restlichen HitNumbers für die Targets wählen
    public static final Color getSemiRandomColor(int i) {
        int diff = max_hit_number - min_hit_number;
        int step = 	Math.round(diff/5);
        if(i < min_hit_number) {
            return new Color(0,255,0);
        }
        /*if (i < min_hit_number + step) {
            return new Color(120,220,30);
        }*/
        if (i < min_hit_number + step * 1) {
            return new Color(170,220,30);
        }
        if (i < min_hit_number + step * 2) {
            return new Color(255,255,0);
        }
        if (i < min_hit_number + step * 3) {
            return new Color(255,160,62);
        }
        if (i < min_hit_number + step * 4) {
            return new Color(255,100,30);
        }
        if (i <= max_hit_number) {
            return new Color(255,0,0);
        }
        return Color.WHITE;
    }

    private Vector2 toWorldCoordinates(Point point) {
	    //Umrechnen von Punktkoordinaten zu Koordinaten des Spielfelds
        double x =  (point.getX() - this.canvas.getWidth() / 2.0) / this.scale;
        double y = -(point.getY() - this.canvas.getHeight() / 2.0) / this.scale;
        return new Vector2(x, y);
    }

    public void endGame() {
	    this.stop();
	    int place = ScoreEntry.checkPlace(currScore);
        if (place <= 10) {
            //Neuer Leaderboard Eintrag
            setStandard();
            ScoreDialog scoreDialog = new ScoreDialog(this, parentFrame, place, lvlCnt,currScore);
        }
        else {
            setStandard();
            ScoreDialog scoreDialog = new ScoreDialog(this, parentFrame);
            //Kein neuer Leaderboardeintrag
        }
    }
    public void setStandard(){
        maxBalls = 4;
        shootStyle = false;
        ballsCreated = 0;
        canShoot = true;
        targetSack.clear();
        turn = 1;
        rowsOfTargetsCreated = 0;
        ballsInGame = 0;
        bulletRadius = 0.6;
	}
}
