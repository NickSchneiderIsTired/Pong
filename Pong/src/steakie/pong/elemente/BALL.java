package steakie.pong.elemente;

import java.util.Random;
import steakie.pong.MAIN;

public class BALL {

    public double xPos, yPos;
    private final int startXPos = 316;
    private final int startYPos = 180;
    public static int size = 10;
    public int center;
    public double angle;
    public final double MAX_ANGLE = 0.5;
    private final Random random = new Random();
    private final Score score;
    public boolean playing = false;
    private double nx, ny;
    private double DistanceX, DistanceY;
    public double speed = 1.6;
    private int a = 1;
    public int time = 0;
    private int edgeDistance = 2;

    public BALL(Score s) {
        score = s;
    }

    public void update() {

        if (playing) {
            a++;
            getBoarder();
            move();

        }

        if (a % 100 == 0) {
            time++;

        }

        if (xPos <= 1) {
            score.R++;
            reset();
        }

        if (xPos >= MAIN.width - size - 1) {
            score.L++;
            reset();
        }

    }

    public void updateCenter() {
        center = (int) yPos + (size / 2);
    }
    
    public void setAngle(double angle) {
    	System.out.println(angle);
        this.angle = angle;
    }

    private void reset() {
        xPos = startXPos;
        yPos = startYPos;
        playing = false;
        speed = 1.6;
        size = 10;
        a = 1;
        time = 0;
        edgeDistance = 2;
    }

    public void speedUp(double speed) {
        this.speed += speed;
    }

    public void calcAngle() {

        int point = random.nextInt(360);
        
        int sideChange = random.nextInt(2);
        if (sideChange == 1) {
            DistanceX = MAIN.width - (startXPos + size / 2);
        } else {
            DistanceX = 0 - (startXPos + size / 2);
        }

        DistanceY = point - (startYPos + size / 2);

        angle = Math.atan2(DistanceY, DistanceX);

    }

    public void getDir() {
        nx = Math.cos(angle);
        ny = Math.sin(angle);
    }

    private void getBoarder() {

        if (speed > 3.6) {
            edgeDistance = 3;
        }

        if (speed > 5.5) {
            edgeDistance = 4;
        }

        if (yPos > edgeDistance && yPos < (MAIN.height - size - edgeDistance)) {

        } else {
            getAngleOfReflection();
        }

    }

    private void getAngleOfReflection() {
        angle *= -1;
        getDir();
    }

    private void move() {

        xPos += speed * nx;
        yPos += speed * ny;

    }

}
