package unsw.loopmania;

public class MathHelper {
    static public boolean inRadius(int x1, int y1, int x2, int y2, int radius) {
        return (Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2) < Math.pow(radius, 2));
    }

    static public boolean inPosition(int x1, int y1, int x2, int y2) {
        return ((x1 == x2) && (y1 == y2));
    }
}
