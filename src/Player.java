import java.awt.*;

public abstract class Player {
    public int[][] map;
    public Point currentPosition;

    protected Player() {
    }

    protected Player(int[][] map) {
        this.map = map;
        this.currentPosition = new Point();
        this.currentPosition.setLocation(0, 0);
    }

    void setCurrentPosition(Point currentPosition) {
        this.currentPosition.setLocation(currentPosition);
    }

    abstract Point nextPosition(Point lastPosition);
}
