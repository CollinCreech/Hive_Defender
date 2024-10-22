package game;

/** Test class made just for simple little function checks.*/
public class Tests {
    public static void main(String[] args) {
        //testRandomUpgrades();
        //testPlacingUpgrades();
        //testSpawningEnemies();
    }

    /** Tests creation of random upgrade location. */
    public static void testRandomUpgrades() {
        Upgrade upgrade = new Upgrade();
        System.out.println(upgrade.getLocation());
    }

    /** Tests placing the upgrades on a canvas. */
    public static void testPlacingUpgrades() {
        Upgrade upgrade = new Upgrade();
        SimpleCanvas canvas = new SimpleCanvas(1280, 720);
        upgrade.draw(canvas);
        canvas.update();
        canvas.show();
    }

    /** Tests the spawning of enemies on a canvas. */
    public static void testSpawningEnemies() {
        YellowJacket yellowJacket = new YellowJacket();
        SimpleCanvas canvas = new SimpleCanvas(1280, 720);
        yellowJacket.draw(canvas);
        canvas.update();
        canvas.show();
    }
}
