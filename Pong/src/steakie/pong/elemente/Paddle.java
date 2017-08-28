package steakie.pong.elemente;

public class Paddle {

    public int xPos, yPos;
    public final int width = 10;
    public final int height = 50;
    public final int color;
    public int center;

    public int[] pixels = new int[width * height];

    public Paddle(int x, int y, int color) {
        this.xPos = x;
        this.yPos = y;
        this.color = color;
    }

    public void updateCenter() {
        center = yPos + height / 2;
    }

}
