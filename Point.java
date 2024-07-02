import java.io.Serializable;

/**
 * Represents a Point with x and y as coordinates
 * to make storing data for memorygame easier
 * 
 * @Author Adarsh Vipat
 * @Version 1.0
 */
public class Point implements Serializable {
    private int x;
    private int y;

    /**
     * A point with coordinates x and y
     * 
     * @param x x
     * @param y y
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets X-coordinate
     * 
     * @return X
     */
    public int getX() {
        return x;
    }

    /**
     * Sets x-coordinate
     * 
     * @param x x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets y-coordinate
     * 
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * Sets y-coordinate
     * 
     * @param y y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Checks if a point is equal to another by comparing x and y coordinates.
     * 
     * @param obj an object
     * @return if it is equal
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Point point = (Point) obj;
        return x == point.x && y == point.y;
    }

    /**
     * Returns a representation of the object in a string
     * 
     * @return string in form of x, y
     */
    public String toString() {
        return x + ", " + y;
    }
}