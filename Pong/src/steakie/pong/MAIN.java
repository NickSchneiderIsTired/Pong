package steakie.pong;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import steakie.pong.elemente.BALL;
import steakie.pong.elemente.Paddle;
import steakie.pong.elemente.Spielstand;

public class MAIN extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;

	public static int width = 640;
	public static int height = 360;
	public static int scale = 2;

	private Thread thread;
	private Graphics g;
	private JFrame frame;
	private SCREEN screen;
	private static Spielstand spielstand;

	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	private boolean running = false;
	private Keyboard keys;

	public Paddle paddle1;
	public Paddle paddle2;

	public BALL ball;

	public MAIN() {

		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);
		frame = this;
		setUndecorated(true);
		keys = new Keyboard();
		spielstand = new Spielstand(0, 0);
		screen = new SCREEN(width, height, keys);
		paddle1 = new Paddle(18, 155, 0x00ff00);
		paddle2 = new Paddle(612, 155, 0xff0000);
		ball = new BALL(315, 180, 1, spielstand);

		addKeyListener(keys);
	}

	private synchronized void start() {
		running = true;
		thread = new Thread(this, "MAIN");
		thread.start();
	}

	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long last = System.nanoTime();
		double ns = 1000000000.0 / 100.0;
		double delta = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - last) / ns;
			last = now;
			while (delta >= 1) {
				update();
				delta--;
			}
			render();
		}
		stop();
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}

		screen.clear();
		screen.renderMiddle();
		screen.renderPaddle(paddle1);
		screen.renderPaddle(paddle2);

		g = bs.getDrawGraphics();

		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

		// Spielstand
		g.setColor(new Color(0xD5F02B));
		Font font = new Font("Arial", Font.BOLD, 70);
		g.setFont(font);
		g.drawString(spielstand.L + " : " + spielstand.R, 570, 60);
		g.setColor(new Color(0x117AF2));
		g.drawString("" + ball.time, getWidth() - 85, 70);

		g.setColor(Color.ORANGE);
		g.fillOval((int) ball.xPos * scale, (int) ball.yPos * scale, BALL.size * scale, BALL.size * scale);

		g.dispose();
		bs.show();

	}

	public static Spielstand gibSpielstand() {
		return spielstand;
	}

	double movespeed = 3;

	public void update() {
		keys.update();

		if (paddle1.yPos < height - paddle1.height - 2 && keys.down1) {
			paddle1.yPos += movespeed;
		}

		if (keys.up1 && paddle1.yPos > 2) {
			paddle1.yPos -= movespeed;
		}

		if (paddle2.yPos < height - paddle2.height - 2 && keys.down2) {
			paddle2.yPos += movespeed;
		}

		if (keys.up2 && paddle2.yPos > 2) {
			paddle2.yPos -= movespeed;
		}

		// System.out.println(ball.angle);

		screen.updateBall(ball);

		screen.schlag(ball, paddle1, paddle2);

		if (keys.esc) {
			System.exit(0);
		}

	}

	public static void main(String[] args) {
		MAIN game = new MAIN();
		game.setResizable(false);
		game.setVisible(true);

		game.setTitle("Pong");
		game.pack();
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setLocationRelativeTo(null);

		game.start();

	}

}
