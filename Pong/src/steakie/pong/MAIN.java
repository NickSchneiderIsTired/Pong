package steakie.pong;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.*;

import steakie.pong.elemente.BALL;
import steakie.pong.elemente.Paddle;
import steakie.pong.elemente.Score;

public class MAIN extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final int width = 640;
	public static final int height = 360;
	private static final int scale = 2;

	private Thread thread;
	private Graphics g;
	private JFrame frame;
	private SCREEN screen;
	private static Score score;
	private Font font;

	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	private boolean running = false;
	private Keyboard keys;

	private Paddle paddle1;
	private Paddle paddle2;

	private BALL ball;

	public MAIN() {
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);
		frame = this;
		setUndecorated(true);
		keys = new Keyboard();
		font = new Font("Arial", Font.BOLD, 70);
		score = new Score();
		screen = new SCREEN(width, height, keys);
		paddle1 = new Paddle(18, 155, 0x00ff00);
		paddle2 = new Paddle(612, 155, 0xff0000);
		ball = new BALL(score);

		addKeyListener(keys);
	}

	private synchronized void start() {
		running = true;
		thread = new Thread(this, "MAIN");
		thread.start();
	}

	private synchronized void stop() {
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

	private void render() {
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

		// Score
		g.setColor(new Color(0xD5F02B));
		g.setFont(font);
		g.drawString(score.L + " : " + score.R, 570, 60);
		g.setColor(new Color(0x117AF2));
		g.drawString("" + ball.time, getWidth() - 85, 70);

		//Render Ball
		g.setColor(Color.ORANGE);
		g.fillOval((int) ball.xPos * scale, (int) ball.yPos * scale, BALL.size * scale, BALL.size * scale);

		g.dispose();
		bs.show();

	}

	private double paddleMoveSpeed = 3;

	private void update() {
		keys.update();

		if (paddle1.yPos < height - paddle1.height - 2 && keys.down1) {
			paddle1.yPos += paddleMoveSpeed;
		}

		if (keys.up1 && paddle1.yPos > 2) {
			paddle1.yPos -= paddleMoveSpeed;
		}

		if (paddle2.yPos < height - paddle2.height - 2 && keys.down2) {
			paddle2.yPos += paddleMoveSpeed;
		}

		if (keys.up2 && paddle2.yPos > 2) {
			paddle2.yPos -= paddleMoveSpeed;
		}

		screen.updateBall(ball);

		screen.shoot(ball, paddle1, paddle2);

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
		game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		game.setLocationRelativeTo(null);

		game.start();

	}

}
