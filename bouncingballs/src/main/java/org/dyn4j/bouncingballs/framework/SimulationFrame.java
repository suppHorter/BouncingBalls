package org.dyn4j.bouncingballs.framework;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.dyn4j.dynamics.World;

public abstract class SimulationFrame extends JFrame {
	public static final double NANO_TO_BASE = 1.0e9;

	protected final Canvas canvas;
	
	protected final World world;
	
	protected final double scale;
	
	private boolean stopped;
	
	private boolean paused;
	
	private long last;
	
	public SimulationFrame(String name, double scale) {
		super(name);

		this.setUndecorated(true);
		this.scale = scale;
		this.world = new World();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
                //Stop the Simulation before exiting
				stop();
				super.windowClosing(e);
			}
		});
		
		Dimension size = new Dimension(500, 700);
		
		this.canvas = new Canvas();
		this.canvas.setPreferredSize(size);
		this.canvas.setMinimumSize(size);
		this.canvas.setMaximumSize(size);
		
		this.add(this.canvas);
		
		this.setResizable(false);
		
		this.pack();
		this.setLocationRelativeTo(null);
		
		this.initializeWorld();
	}
	
	/**
	 * Creates game objects and adds them to the world.
	 */
	protected abstract void initializeWorld();
	
	/**
	 * Start active rendering the simulation.
	 * <p>
	 * This should be called after the JFrame has been shown.
	 */
	private void start() {
		this.last = System.nanoTime();
		this.canvas.setIgnoreRepaint(true);
		this.canvas.createBufferStrategy(2);

		Thread thread = new Thread() {
			public void run() {
				while (!isStopped()) {
					gameLoop();
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {}
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
	}
	
	private void gameLoop() {
		Graphics2D g = (Graphics2D)this.canvas.getBufferStrategy().getDrawGraphics();
		
		this.transform(g);
		
		this.clear(g);
		
        long time = System.nanoTime();

        long diff = time - this.last;
        this.last = time;
    	double elapsedTime = (double)diff / NANO_TO_BASE;
		
		this.render(g, elapsedTime);
        
		if (!paused) {
	        this.update(g, elapsedTime);
		}
		
		g.dispose();
		
		BufferStrategy strategy = this.canvas.getBufferStrategy();
		if (!strategy.contentsLost()) {
			strategy.show();
		}
		
        Toolkit.getDefaultToolkit().sync();
	}

	protected void transform(Graphics2D g) {
		final int w = this.canvas.getWidth();
		final int h = this.canvas.getHeight();
		
		// before we render everything im going to flip the y axis and move the
		// origin to the center (instead of it being in the top left corner)
		AffineTransform yFlip = AffineTransform.getScaleInstance(1, -1);
		AffineTransform move = AffineTransform.getTranslateInstance(w / 2, -h / 2);
		g.transform(yFlip);
		g.transform(move);
	}
	
	protected void clear(Graphics2D g) {
		final int w = this.canvas.getWidth();
		final int h = this.canvas.getHeight();
		
		g.setColor(Color.BLACK);
		g.fillRect(-w / 2, -h / 2, w, h);
	}
	
	protected void render(Graphics2D g, double elapsedTime) {
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		for (int i = 0; i < this.world.getBodyCount(); i++) {
			SimulationBody body = (SimulationBody) this.world.getBody(i);
			this.render(g, elapsedTime, body);
		}
	}

	protected void render(Graphics2D g, double elapsedTime, SimulationBody body) {
		body.render(g, this.scale);
	}

	protected void update(Graphics2D g, double elapsedTime) {
        this.world.update(elapsedTime);
	}
	
	public synchronized void stop() {
		this.stopped = true;
	}
	
	public boolean isStopped() {
		return this.stopped;
	}
	
	public synchronized void pause() {
		this.paused = true;
	}
	
	public synchronized void resume() {
		this.paused = false;
	}
	
	public boolean isPaused() {
		return this.paused;
	}
	
	public void run() {
		// set the look and feel to the system look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		// show it
		this.setVisible(true);
		
		// start it
		this.start();
	}
}
