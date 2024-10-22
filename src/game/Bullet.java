package game;

import java.awt.Color;

/** This class represents a bullet object in the game. */
public class Bullet extends GameObject {
    // Instance variables
    private double baseVelocity; // The base speed for the bullet.
    private double xVelocity; // How fast the bullet moves in the x direction.
    private double yVelocity; // How fast the bullet moves in the y direction.

    /** Constructs a new instance of the Bullet class. */
    public Bullet() {
        setImageFilename("bullet.png");
        setBorderColor(Color.BLACK);
        height = 50;
        width = 50;
        baseVelocity = 20.0;
        yVelocity = 0.0;
        xVelocity = 20.0;
    }

    /** Draws the bullet instance onto the given canvas parameter. */
    public void draw(SimpleCanvas canvas) {
        canvas.drawImage(getLeftX(), getTopY(), imageFilename, width, height);
    }

    /** Getter function for the base velocity. */
    public double getBaseVelocity() {
        return baseVelocity;
    }

    /** Setter function for the x velocity. */
    public void setXVelocity(double newXVelocity) {
        xVelocity = newXVelocity;
    }

    /** Getter function for the x velocity. */
    public double getXVelocity() {
        return xVelocity;
    }

    /** Setter function for the y velocity. */
    public void setYVelocity(double newYVelocity) {
        yVelocity = newYVelocity;
    }

    /** Getter function for the x velocity. */
    public double getYVelocity() {
        return yVelocity;
    }
}