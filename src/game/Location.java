package game;

/** A Location represents an (x, y) location. */
public class Location {
    // Instance variables
    private int x, y;

    /** Constructor for a location. */
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** Getter function for the location's x value. */
    public int getX() {
        return x;
    }

    /** Setter function for the location's x value. */
    public void setX(int x) {
        this.x = x;
    }

    /** Getter function for the location's y value. */
    public int getY() {
        return y;
    }

    /** Setter function for the location's y value. */
    public void setY(int y) {
        this.y = y;
    }

    /** A toString function for a Location. This function returns a string for the location when called. */
    public String toString() {
        return "This location is (" + getX() + "," + getY() + ")";
    }
}
