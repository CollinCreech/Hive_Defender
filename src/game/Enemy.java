package game;

/** This superclass represents the "bad" objects of the game. The objects of this class and subclasses are the objects the player
 * wants to eliminate. This class inherits the class GameObject.
 */
public class Enemy extends GameObject {
    protected int health; // The hitpoints for an enemy.
    protected Location prevLocation; // The last location to reverse movement if it reaches player.
    protected int dph; // The damage per hit an enemy does.
    protected int points; // The number of points an enemy is worth.

    /** Constructs a new enemy. There can be multiple of the per canvas.. */
    public Enemy() {
        super();
        setLocation();
        health = 100;
        setHeight(50);
        setWidth(50);
        prevLocation = new Location(0,0);
        dph = 0;
        points = 0;
    }

    /** Getter function for the dph (damage per hit). */
    public int getDph() {
        return dph;
    }

    /** Getter function for the amount of points an enemy is worth. */
    public int getPoints() {
        return points;
    }

    /** Pushes back any enemy that reaches a player. Uses previous location to push the enemy back significantly. */
    public void pushBackEnemy(Location currLocation, Location prevLocation) {
        int xDiff = prevLocation.getX() - currLocation.getX();
        int yDiff = prevLocation.getY() - currLocation.getY();
        currLocation.setX(currLocation.getX() + (xDiff * 10));
        currLocation.setY(currLocation.getY() + (yDiff * 10));
    }

    /** Setter function for the previous location of an enemy. */
    public void setPrevLocation(Location newLocation) {
        prevLocation = newLocation;
    }

    /** Getter function for the previous location of an enemy. */
    public Location getPrevLocation() {
        return prevLocation;
    }

    /** Getter function for the health of an enemy. */
    public int getHealth() {
        return health;
    }

    /** Setter function for the health of an enemy. */
    public void setHealth(int healthLost) {
        health = health - healthLost;
    }

    /** Setter function for the location of an enemy. */
    public void setLocation() {
        int randomNumY = (int)(Math.random() * 651 + 50); // random Y coordinate that allows full image to appear on screen
        location.setX(1400);
        location.setY(randomNumY);
    }

    /** Moves the enemy towards the bee. Looks at the enemy's location compared to the player's and then determines which
     * direction it needs to move in. Takes the bee in as a parameter.
     */
    public void moveTowardsPlayer(Bee bee) {
        if (location.getX() > bee.getLocation().getX()) {
            moveLeft();
        }
        else {
            moveRight();
        }
        if (location.getY() > bee.getLocation().getY()) {
            moveUp();
        }
        else {
            moveDown();
        }
    }

    /** This function moves the enemy image right by 10 pixels. */
    public void moveRight() {
        setLocationX(getLocation().getX()+10);
    }

    /** This function moves the enemy image left by 10 pixels. */
    public void moveLeft() {
        setLocationX(getLocation().getX()-10);
    }

    /** This function moves the enemy image up by 10 pixels. */
    public void moveUp() {
        setLocationY(getLocation().getY()-10);
    }

    /** This function moves the enemy image down by 10 pixels. */
    public void moveDown() {
        setLocationY(getLocation().getY()+10);
    }

    /** The toString function for an enemy. Mainly used to determine the location of an enemy during testing. */
    public String toString() {
        return "This enemy is at " + location;
    }
}
