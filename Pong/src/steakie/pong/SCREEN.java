package steakie.pong;

import steakie.pong.elemente.BALL;
import steakie.pong.elemente.Paddle;

public class SCREEN {

	public int width, height;
	public int[] pixels;
	public Keyboard keys;
	public int mode = 2;

	public SCREEN(int width, int height, Keyboard keys) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		this.keys = keys;
	}

	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}

	public void renderMiddle() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < 2; x++) {
				pixels[(x + width / 2 - 1) + y * width] = 0x00ffff;
			}
		}
	}

	public void renderPaddle(Paddle p) {
		for (int y = 0; y < p.height; y++) {
			for (int x = 0; x < p.width; x++) {
				pixels[(x + p.xPos) + (y + p.yPos) * width] = p.color;
			}
		}
	}

	public void renderBall(BALL b) {
		for (int y = 0; y < BALL.size; y++) {
			for (int x = 0; x < BALL.size; x++) {
				pixels[(x + (int) b.xPos) + (y + (int) b.yPos) * width] = 0xF09B24;
			}
		}
	}

	public int getBallSize() {
		return BALL.size;
	}

	public void schlag(BALL ball, Paddle paddle1, Paddle paddle2) {
		double top = ball.yPos;
		double bot = ball.yPos + BALL.size;

		if ((ball.angle < -1 * Math.PI / 2 % Math.PI) || (ball.angle > Math.PI / 2 % Math.PI)) {

			if (ball.xPos <= paddle1.xPos + paddle1.width && ball.xPos > paddle1.xPos + paddle1.width - ball.speed) {
				if (bot >= paddle1.yPos && top <= paddle1.yPos + paddle1.height) {

					getSchlagwinkelPaddleAddition(paddle1, ball);
					ball.getDir();
					ball.speedUp(0.2);
				}
			}
		} else {

			if (ball.xPos + BALL.size >= paddle2.xPos && ball.xPos + BALL.size < paddle2.xPos + ball.speed) {
				if (bot >= paddle2.yPos && top <= paddle2.yPos + paddle2.height) {

					getSchlagwinkelPaddleAddition2(paddle2, ball);
					ball.getDir();
					ball.speedUp(0.2);
				}
			}
		}
	}

	public void getSchlagwinkelPaddle(Paddle p, BALL ball) {
		p.updateCenter();
		ball.updateCenter();

		if (ball.center < p.center) {
			int abstandVonMitte = p.center - (int) ball.center;
			double newAngle = (ball.MAXangle / (p.height / 2)) * abstandVonMitte * (-1); // Formel
			ball.setAngle(newAngle);
		}
		if (ball.center >= p.center) {
			int abstandVonMitte = (int) ball.center - p.center;
			double newAngle = (ball.MAXangle / (p.height / 2)) * abstandVonMitte;
			ball.setAngle(newAngle);
		}

	}

	public void getSchlagwinkelPaddle2(Paddle p, BALL ball) {
		p.updateCenter();
		ball.updateCenter();

		if (ball.center < p.center) {
			int abstandVonMitte = p.center - (int) ball.center;
			double newAngle = Math.PI - (ball.MAXangle / 25) * abstandVonMitte * (-1);
			ball.setAngle(newAngle);
		}

		if (ball.center >= p.center) {
			int abstandVonMitte = (int) ball.center - p.center;
			double newAngle = Math.PI - (ball.MAXangle / 25) * abstandVonMitte;
			ball.setAngle(newAngle);
		}

	}

	public void getSchlagwinkelPaddleAddition(Paddle p, BALL ball) {
		p.updateCenter();
		ball.updateCenter();

		if (ball.center < p.center) {
			int abstandVonMitte = p.center - (int) ball.center;
			double newAngle = (ball.MAXangle / 25) * abstandVonMitte * (-1); // Formel
			if (ball.angle > 0) {
				
				newAngle = (Math.PI - ball.angle) + newAngle;
				ball.setAngle(newAngle);
			} else {
				
				newAngle = (Math.PI + ball.angle) * -1 + newAngle;
				
				if (newAngle < -1.2) {

					newAngle = -1.2;
				}
				
				ball.setAngle(newAngle);
			}
		}

		if (ball.center >= p.center) {
			int abstandVonMitte = (int) ball.center - p.center;
			double newAngle = (ball.MAXangle / 25) * abstandVonMitte;
			if (ball.angle > 0) {
				
				newAngle = (Math.PI - ball.angle) + newAngle;
				
				if (newAngle > 1.2) {

					newAngle = 1.2;
				}
				
				ball.setAngle(newAngle);
			} else {
				ball.setAngle((Math.PI + ball.angle) * -1 + newAngle);
			}
		}

	}

	public void getSchlagwinkelPaddleAddition2(Paddle p, BALL ball) {
		p.updateCenter();
		ball.updateCenter();

		if (ball.center < p.center) {
			int abstandVonMitte = p.center - (int) ball.center;
			double newAngle = (ball.MAXangle / 25) * abstandVonMitte;
			if (ball.angle > 0) {

				System.out.println((Math.PI - ball.angle) + " + " + newAngle);

				newAngle = (Math.PI - ball.angle) + newAngle;

				ball.setAngle(newAngle);
			} else {

				System.out.println((Math.PI - ball.angle) + -1 + " + " + newAngle);

				newAngle = (Math.PI + ball.angle) * -1 + newAngle;

				if (newAngle > -2) {

					newAngle = -2;
				}
				ball.setAngle(newAngle);
			}
		}

		if (ball.center >= p.center) {
			int abstandVonMitte = (int) ball.center - p.center;
			double newAngle = (ball.MAXangle / 25) * abstandVonMitte;
			if (ball.angle > 0) {

				System.out.println((Math.PI - ball.angle) + " + " + -newAngle);

				newAngle = (Math.PI - ball.angle) - newAngle;
				if (newAngle < 2) {

					newAngle = 2;
				}

				ball.setAngle(newAngle);
			} else {

				System.out.println((Math.PI - ball.angle) * -1 + " + " + newAngle * -1);

				newAngle = (Math.PI + ball.angle) * -1 + newAngle * -1;
				ball.setAngle(newAngle);
			}
		}

	}

	public void updateBall(BALL ball) {
		if (keys.enter && !ball.playing) {
			ball.calcAngle();
			ball.getDir();
			ball.playing = true;
		}
		ball.update();
	}

}
