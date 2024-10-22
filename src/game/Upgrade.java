package game;

import java.lang.Math;

/** This class is a subclass of the superclass GameObject. An Upgrade is a gameobject that the Bee class can "pick up"
 * and gain some type of benefit (either health or killing all the enemies).
 */
class Upgrade extends GameObject {

    /** Constructor for an Upgrade. Will instantiate an upgrade with a random location in the game. */
    public Upgrade() {
        super();
        setHeight(70);
        setWidth(70);
        randomLocationGenerator();
        imageFilename = "crate.png";
    }

    /** Sets the random location to place the upgrade on the screen. */
    public void randomLocationGenerator() {
        int randomNumX = (int)(Math.random() * 1001 + 50); // random X coordinate that allows full image to appear on screen
        int randomNumY = (int)(Math.random() * 401 + 200); // random Y coordinate that allows full image to appear on screen
        location.setX(randomNumX);
        location.setY(randomNumY);
    }
}
