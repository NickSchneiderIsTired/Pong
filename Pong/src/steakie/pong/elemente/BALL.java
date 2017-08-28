package steakie.pong.elemente;

import java.util.Random;
import steakie.pong.MAIN;

public class BALL {

    public double xPos, yPos;
    public int startxPos = 316;
    public int startyPos = 180;
    public static int size = 10;
    public int center;
    public double angle;
    public double MAXangle = 0.5;
    private Random random = new Random();
    public Spielstand spielstand;
    public boolean playing = false;
    public double nx, ny;
    public double DistanceX, DistanceY;
    public double speed = 1.6;
    public int a = 1;
    public int time = 0;
    public int wandAbstand = 2;

    public BALL(double x, double y, int angle, Spielstand s) {
        xPos = x;
        yPos = y;
        this.angle = angle;
        spielstand = s;
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
            spielstand.R++;
            reset();
        }

        if (xPos >= MAIN.width - size - 1) {
            spielstand.L++;
            reset();
        }

    }

    public void updateCenter() {
        center = (int) yPos + (size / 2);
    }

    public void specialMove() {
        if (size == 10) {
            size = 30;
            return;
        }
        if (size == 30) {
            size = 10;
        }
    }
    
    

    public void setAngle(double angle) {
    	System.out.println(angle);
        this.angle = angle;
    }

    public void reset() {
        xPos = startxPos;
        yPos = startyPos;
        playing = false;
        speed = 1.6;
        size = 10;
        a = 1;
        time = 0;
        wandAbstand = 2;
    }

    public void speedUp(double speed) {
        this.speed += speed;
    }

    public void calcAngle() {

        int Punkt = random.nextInt(360);
        
        int seitenWechsel = random.nextInt(2);
        if (seitenWechsel == 1) {
            DistanceX = MAIN.width - (startxPos + size / 2);
        } else {
            DistanceX = 0 - (startxPos + size / 2);
        }

        DistanceY = Punkt - (startyPos + size / 2);

        angle = Math.atan2(DistanceY, DistanceX);

    }

    public void getDir() {
        nx = Math.cos(angle);
        ny = Math.sin(angle);
    }

    public void getBoarder() {

        if (speed > 3.6) {
            wandAbstand = 3;
        }

        if (speed > 5.5) {
            wandAbstand = 4;
        }

        if (yPos > wandAbstand && yPos < (MAIN.height - size - wandAbstand)) {
            return;
        } else {
            getAusfallswinkel();
        }

    }

    public void getAusfallswinkel() {
        angle *= -1;
        getDir();
    }

    public void move() {

        xPos += speed * nx;
        yPos += speed * ny;

    }

}
