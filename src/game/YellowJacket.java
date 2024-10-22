package game;

/** This class is a subclass of the superclass Enemy. A yellow jacket is the rare enemy in the game and the strongest enemy. */
public class YellowJacket extends Enemy {
    //Instance variable
    private int dph; //damage per hit

    /** Constructor for a yellow jacket. The yellow jacket is instantiated with 10 damage per hit and 100 health. It is worth 300 points. */
    public YellowJacket() {
        super();
        imageFilename = "yellowjacket.png";
        setHeight(70);
        setWidth(70);
        dph = 10;
        health = 100;
        points = 300;
    }

    /** Getter function for the dph (damage per hit). */
    public int getDph() {
        return dph;
    }
}
