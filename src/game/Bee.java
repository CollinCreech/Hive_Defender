package game;

import java.awt.Color;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Objects;

/** This class represents the "good" object of the game. The player will control this object. This class inherits the class GameObject.*/
public class Bee extends GameObject {
    // Instance variables
    private ArrayList<Bullet> bullets; // Arraylist for all active bullets in game.
    private int ammo; // Keeps track of bullets left in clip.
    private boolean reloadedStatus; // If true, print "RELOAD".
    private int reloadCounter; // The reload counter to prevent firing during reloading.
    private int health; // The health of the main bee.
    private int topBoundary; // The boundary the bee cannot go by.

    /** Constructs a new bee. There should only be one of these per canvas. */
    public Bee() {
        setImageFilename("pistolBee.png");
        height = 100;
        width = 100;
        bullets = new ArrayList<>();
        ammo = 30;
        reloadedStatus = true;
        reloadCounter = 0;
        health = 100;
        topBoundary = 0;
    }

    /** Getter function for health. */
    public int getHealth() { return health;}

    /** Setter function for health. */
    public void setHealth(int dmg) {
        health = health - dmg;
    }

    /** This function sets the health of the bee to max health (100 health). */
    public void heal() {
        health = 100;
    }

    /** Getter function for ammo. */
    public int getAmmo() {
        return ammo;
    }

    /** Setter function for the ammo. */
    public void setAmmo(int newAmmo) {
        ammo = newAmmo;
    }

    /** Getter function for the reload counter. */
    public int getReloadCounter() {
        return reloadCounter;
    }

    /** Setter function for the reload counter. */
    public void setReloadCounter(int newReloadCounter) {
        reloadCounter = newReloadCounter;
    }

    /** Getter function for the reloaded status. */
    public boolean getReloadedStatus() {
        return reloadedStatus;
    }

    /** Setter function for the reloaded status. */
    public void setReloadedStatus(boolean value) {
        reloadedStatus = value;
    }

    /** Fire function for the bee. If there is ammo and gun is not being reloaded, the bee can shoot a bullet. The bullet
     * spawns on top of the bee and is added to the bullets array list.
     */
    public void fire() {
        if (ammo > 0 && reloadCounter == 0) {
            Bullet z = new Bullet();
            z.setLocationX(this.getCenterX());
            z.setLocationY(this.getCenterY());
            bullets.add(z);
            ammo--;
        }
        else {
            reloadedStatus = false;
        }
    }

    /** Fire function but implementing mouse. Same as fire function but sets the bullet's x and y velocities to values depending
     * on where the mouse is when the player clicks.
     */
    public void fireWithMouse(double mouseX, double mouseY) {
        if (ammo > 0 && reloadCounter == 0) {
            Bullet z = new Bullet();
            double angle = Math.atan2(mouseX - getCenterX(), mouseY - getCenterY());
            z.setLocationX(this.getCenterX());
            z.setLocationY(this.getCenterY());
            z.setYVelocity(Math.cos(angle) * z.getBaseVelocity()); // Geometry to get correct y velocity.
            z.setXVelocity(Math.sin(angle) * z.getBaseVelocity()); // Geometry to get correct x velocity.
            bullets.add(z);
            ammo--;
        }
        else {
            reloadedStatus = false;
        }
    }

    /** Getter function for the bullets array list. */
    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    /** Sets the reload counter for the bee to 40 ticks which the player must wait before being able to shoot again. */
    public void reload() { // function, just sets timer to 40 ticks in order for runGame to call reduceReloadCounter ~ 2 seconds
        reloadCounter = 40;
    }

    /** Draws the bee on the canvas along with its bullets. */
    public void draw(SimpleCanvas canvas) {
        canvas.drawImage(getLeftX(), getTopY(), imageFilename, width, height);
        for (int w = 0; w < bullets.size(); w++) {
            Bullet currentBullet = bullets.get(w);
            currentBullet.draw(canvas);
        }
    }

    /** This function moves the bee image to the right by 20 pixels if it is at least 20 pixels away from the right border of the canvas. */
    public void moveRight() {
        if (this.getRightX() <= 1260)
            setLocationX(getLocation().getX() + 20);
    }

    /** This function moves the bee image to the left by 20 pixels if it is at least 20 pixels away from the left border of the canvas. */
    public void moveLeft() {
        if (this.getLeftX() >= 20)
            setLocationX(getLocation().getX() - 20);
    }

    /** This function moves the bee image up by 20 pixels if it is at least 20 pixels away from the border of the HUH. */
    public void moveUp() {
        if (this.getTopY() >= topBoundary + 20) {
            setLocationY(getLocation().getY() - 20);
        }
    }

    /** This function moves the bee image down by 20 pixels if it is at least 20 pixels away from the bottom border of the canvas. */
    public void moveDown() {
        if (this.getBottomY() <= 700) {
            setLocationY(getLocation().getY() + 20);
        }
    }

    /** This function sets the new top boundary of the canvas incorporating the HUD as part of the canvas. */
    public void setTopBoundary(int boundary) {
        topBoundary = boundary;
    }
}
