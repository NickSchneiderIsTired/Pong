package steakie.pong;

import steakie.pong.elemente.BALL;
import steakie.pong.elemente.Paddle;

public class SCREEN {

	private final int width, height;
	public int[] pixels;
	private Keyboard keys;

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

	public void shoot(BALL ball, Paddle paddle1, Paddle paddle2) {
		double top = ball.yPos;
		double bot = ball.yPos + BALL.size;

		if ((ball.angle < -1 * Math.PI / 2 % Math.PI) || (ball.angle > Math.PI / 2 % Math.PI)) {

			if (ball.xPos <= paddle1.xPos + paddle1.width && ball.xPos > paddle1.xPos + paddle1.width - ball.speed) {
				if (bot >= paddle1.yPos && top <= paddle1.yPos + paddle1.height) {

					getShotAnglePaddleAddition(paddle1, ball);
					ball.getDir();
					ball.speedUp(0.2);
				}
			}
		} else {

			if (ball.xPos + BALL.size >= paddle2.xPos && ball.xPos + BALL.size < paddle2.xPos + ball.speed) {
				if (bot >= paddle2.yPos && top <= paddle2.yPos + paddle2.height) {

					getShotAnglePaddleAddition2(paddle2, ball);
					ball.getDir();
					ball.speedUp(0.2);
				}
			}
		}
	}


	private void getShotAnglePaddleAddition(Paddle p, BALL ball) {
		p.updateCenter();
		ball.updateCenter();

		if (ball.center < p.center) {
			int distanceFromMiddle = p.center - ball.center;
			double newAngle = (ball.MAX_ANGLE / 25) * distanceFromMiddle * (-1); // Formel
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
			int distanceFromMiddle = ball.center - p.center;
			double newAngle = (ball.MAX_ANGLE / 25) * distanceFromMiddle;
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

	private void getShotAnglePaddleAddition2(Paddle p, BALL ball) {
		p.updateCenter();
		ball.updateCenter();

		if (ball.center < p.center) {
			int distanceFromMiddle = p.center - ball.center;
			double newAngle = (ball.MAX_ANGLE / 25) * distanceFromMiddle;
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
			int distanceFromMiddle = ball.center - p.center;
			double newAngle = (ball.MAX_ANGLE / 25) * distanceFromMiddle;
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
