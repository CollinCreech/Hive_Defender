package game;

/** This class is a subclass of the superclass Enemy. A red wasp is the common enemy in the game and the weakest enemy. */
public class RedWasp extends Enemy {
    // Instance variable
    private int dph; //damage per hit.

    /** Constructor for a red wasp. The red wasp is instantiated with 5 damage per hit and 30 health. It is worth 100 points. */
    public RedWasp() {
        super();
        imageFilename = "redwasp.png";
        dph = 5;
        health = 30;
        points = 100;
    }

    /** Getter function for the dph (damage per hit). */
    public int getDph() {
        return dph;
    }
}
