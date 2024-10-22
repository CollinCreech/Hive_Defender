package game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.String;
import java.awt.Color;
import java.util.ArrayList;

/** This class represents a generic game on a canvas.  */
public class Game {
    // Instance variables
    private SimpleCanvas canvas; // Represents the canvas for which the game is played on.
    private boolean isVisible; // Represents if the canvas is visible.
    private Bee mainBee; // The main "good" object in the game. This object is what the player controls.
    private int gameCounter; // The counter for game, used to count how many ticks have commenced.
    private ArrayList<Enemy> enemies; // The current enemies spawned in.
    private int points; // The points collected by the main player.
    private int heightOfHUD; // The height of the HUD.
    private ArrayList<Upgrade> upgrades; // All the upgrades on the map.

    /** Construct a new instance of the game with a given width and height. */
    public Game(int width, int height)
    {
        isVisible = false;
        canvas = new SimpleCanvas(width, height, "My Game");
        mainBee = new Bee();
        enemies = new ArrayList<>();
        heightOfHUD = 0;
        upgrades = new ArrayList<>();
    }

    /** Draw the state of the game on the canvas. */
    public void draw()
    {
        canvas.clear(); // always clear first.
        canvas.drawImage(0,0, "beehive.jpg",canvas.getWidth(), canvas.getHeight()); // Draws background for game.
        drawUpgrades(); // Draw all upgrades on canvas.
        mainBee.draw(canvas); // always update where main "good" object is.
        drawEnemies(); // Draw all enemies on canvas.
        constructHUD(); // Creates and updates the HUD.
        // Show the window if needs be.
        if (!isVisible) {
            canvas.show();
            isVisible = true;
        }
        else {
            canvas.update();
        }
    }

    /** Returns true if game is over either by player dying or player winning.  */
    private boolean isGameOver(boolean death) {
        if (death) {
            return true;
        }
        gameCounter++;
        if (points >= 4000) {
            return true;
        }
        return false;
    }

    /** Start the game running. */
    public void runGame() {
        createStartingScreen(); // Creates the starting screen for the game.
        canvas.waitForClick(); // Wait for click to begin game.

        // Set main bee's starting location.
        mainBee.setLocationX(canvas.getWidth() / 2);
        mainBee.setLocationY(canvas.getHeight() / 2);

        // Loop while the game is not over:
        while (!isGameOver(false)) {
            moveBullets(); // moves bullets horizontally right until it hits wall.
            moveEnemies(); // moves enemies towards player.
            checkForCollisions(); // checks player interaction with any other objects.
            removeCommonEnemies(); // deletes enemies when their health is 0 or less.
            generateUpgrades(); // Randomly generates upgrades.
            if (mainBee.getReloadCounter() > 0) { // starts timer for reload.
                reduceReloadCounter(); // the timer reduction method for the reload.
            }
            if (!mainBee.getReloadedStatus()) { // determines if game needs to tell player to reload with message.
                drawReloadStatusMessage();
            }
            generateCommonEnemies(); //Randomly generate enemies.
            draw();

            // handle keyboard
            handleKeyboard();

            // handle mouse
            handleMouse();

            canvas.update();
            // Checks if main bee's health is 0 or less. Ends game if bee is dead.
            if (checkForDeathOfBee()) {
                isGameOver(true);
                runDeathScreen();
            }
            canvas.pause(50);
        }
        printWinScreen(); // prints win screen
    }

    private void createStartingScreen() {
        canvas.setPenColor(Color.WHITE);
        canvas.drawImage(0, 0, "beeswithguns.jpg", canvas.getWidth(), canvas.getHeight()); // Background for starting screen.
        canvas.drawStringCentered(canvas.getWidth()/2, canvas.getHeight()/2, "Click to PROTECT THE HIVE!", (canvas.getWidth()+canvas.getHeight())/40);
        canvas.drawStringCentered(canvas.getWidth()/2, (canvas.getHeight()/2) + 50, "Controls: SPACE or LMB to shoot", (canvas.getWidth()+canvas.getHeight())/60);
        canvas.drawStringCentered(canvas.getWidth()/2, (canvas.getHeight()/2) + 100, "                WASD or Arrow Keys to Move", (canvas.getWidth()+canvas.getHeight())/60);
        canvas.update();
        canvas.show(); // Show starting screen
    }

    /** Print the win screen if the player accumulates enough points to win.*/
    private void printWinScreen() {
        canvas.clear(); // clear canvas.
        canvas.drawImage(0,0,"beecelebration.jpg", canvas.getWidth(), canvas.getHeight()); // load background win screen.
        canvas.setLineThickness(10);
        canvas.setPenColor(Color.WHITE);
        canvas.drawStringCentered(canvas.getWidth()/2, canvas.getHeight()/2, "You have defended the hive and won!", (canvas.getWidth()+canvas.getHeight())/40);
        canvas.update(); // update message to canvas.
    }

    /** Generate upgrades for the game. A max of 2 upgrades are allowed in the game at all times. */
    private void generateUpgrades() {
        int randNum = (int) (Math.random() * 300 + 1); // random 1 in 300 chance to generate upgrade every tick. (20 ticks/sec)
        if (randNum == 1 && upgrades.size() < 2) {
            Upgrade newUpgrade = new Upgrade();
            upgrades.add(newUpgrade);
        }
    }

    /** Draws the upgrades generated to the canvas so player can see them. */
    private void drawUpgrades() {
        for (Upgrade upgrade : upgrades) {
            upgrade.draw(canvas);
        }
    }

    /** Shows the death screen once a player has died. */
    private void runDeathScreen() {
        canvas.clear(); // clear canvas.
        canvas.setBackgroundColor(Color.RED);
        canvas.setPenColor(Color.BLACK);
        canvas.drawStringCentered(canvas.getWidth()/2, canvas.getHeight()/2, "You have died!", (canvas.getWidth()+canvas.getHeight())/40);
        canvas.update(); // update canvas with death message.
    }

    /** Checks if the bee is dead. If the bee is not dead, it returns false and true for being alive. */
    private boolean checkForDeathOfBee() {
        return mainBee.getHealth() <= 0;
    }

    /** Checks for any object overlapping with other objects. Enemies will be damaged from bullets, enemies damage the bee
     * and be pushed away from the bee. Upgrades will be picked up if the bee goes over them. Upgrades are also kept here
     * in terms of which upgrades are possible.
     */
    private void checkForCollisions() {
        ArrayList<Bullet> bullets = mainBee.getBullets(); // Get array of current bullets.
        for (Enemy enemy : enemies) {
            if (mainBee.overlaps(enemy)) {
                enemy.pushBackEnemy(enemy.getLocation(), enemy.getPrevLocation()); // Push enemies back from bee.
                mainBee.setHealth(enemy.getDph());
            }
            for (int i = 0; i < bullets.size(); i++) { // Check for any bullets hitting enemies.
                if (enemy.overlaps(bullets.get(i))) {
                    enemy.setHealth(10);
                    bullets.remove(i);
                }
            }
        }
        for (int i = 0; i < upgrades.size(); i++) { // Get list of upgrades.
            if (mainBee.overlaps(upgrades.get(i))) {
                upgrades.remove(upgrades.get(i)); // Remove upgrade after bee runs over it.
                int randNum = (int) (Math.random() * 2 + 1); // Generate random number 1 or 2.
                if (randNum == 1) {
                    // Regen Health
                    mainBee.heal();
                }
                else if (randNum == 2) {
                    // Nuke enemies
                    for (Enemy enemy : enemies) {
                        points += enemy.getPoints();
                    }
                    enemies.clear(); // Kill all enemies.
                }
            }
        }
    }

    /** Removes any enemies that are dead. */
    private void removeCommonEnemies() {
        for (int i = 0; i < enemies.size(); i++) {
            if (enemies.get(i).getHealth() <= 0) { // If enemy health is 0 or below, enemy dies.
                points += enemies.get(i).getPoints();
                enemies.remove(i);
            }
        }
    }

    /** Generates common enemies. Only generates 1 every 20 ticks (~1 seconds). A max of 20 enemies can be "alive." */
    private void generateCommonEnemies() {
        if (gameCounter % 20 == 0 && enemies.size() < 20) {
            int randNum = (int) (Math.random() * 10 + 1); // random number generated in range of 1-10.
            if (randNum == 1) { // Basically means a 1 in 10 chance to spawn a stronger enemy than the red wasp.
                YellowJacket newEnemy = new YellowJacket();
                enemies.add(newEnemy);
            }
            else {
                RedWasp newEnemy = new RedWasp();
                enemies.add(newEnemy); // Adds basic red wasp 9 out of 10 times.
            }
        }
    }

    /** Makes all the enemies "move" on the canvas. Calls the function of the enemy class to move towards the player.
     * Keeps track of previous location in order to push enemy back once enemy damages player.
     */
    private void moveEnemies() {
        for (Enemy enemy : enemies) {
            enemy.getPrevLocation().setX(enemy.getLocation().getX());
            enemy.getPrevLocation().setY(enemy.getLocation().getY());
            enemy.moveTowardsPlayer(mainBee);
        }
    }

    /** Draws the enemies to the canvas. */
    private void drawEnemies() {
        for (int i = 0; i < enemies.size(); i++) {
            Enemy currentEnemy = enemies.get(i);
            currentEnemy.draw(canvas);
        }
    }

    /** Handles all keyboard inputs from player. */
    private void handleKeyboard() {
        if (canvas.isKeyPressed(KeyEvent.VK_UP) || canvas.isKeyPressed(KeyEvent.VK_W)) {
            mainBee.moveUp();
        }
        if (canvas.isKeyPressed(KeyEvent.VK_DOWN) || canvas.isKeyPressed(KeyEvent.VK_S)) {
            mainBee.moveDown();
        }
        if (canvas.isKeyPressed(KeyEvent.VK_LEFT) || canvas.isKeyPressed(KeyEvent.VK_A)) {
            mainBee.moveLeft();
        }
        if (canvas.isKeyPressed(KeyEvent.VK_RIGHT) || canvas.isKeyPressed(KeyEvent.VK_D)) {
            mainBee.moveRight();
        }
        if (mainBee.getReloadedStatus()) { // If the gun is reloaded, you can fire.
            if (canvas.isKeyPressed(KeyEvent.VK_SPACE)) {
                mainBee.fire();
            }
        }
        else {
            drawReloadStatusMessage(); // Draws message to reload if gun is out of ammo.
        }
        if (canvas.isKeyPressed(KeyEvent.VK_R)) {
            if (mainBee.getReloadCounter() == 0 && mainBee.getAmmo() < 30) {
                mainBee.reload();
            }
        }
    }

    /** Handles all the mouse clicking of the player. The mouse really only shoots a certain direction. I could not figure out
     * a way to update the mouse location continually as the player holds down the mouse and moves it, thus the player must
     * click for every bullet they want in a different direction.
      */
    private void handleMouse() {
        if (canvas.isMousePressed()) {
            double clickX = canvas.getMouseClickX();
            double clickY = canvas.getMouseClickY();
            mainBee.fireWithMouse(clickX, clickY);
        }
    }

    /** Moves all the bullets using their x and y velocities determined from either a set movement if using "SPACE" to shoot
     * or calculated velocities if the mouse is used to fire.
     */
    private void moveBullets() {
        ArrayList<Bullet> bullets = mainBee.getBullets();
        for (int w = 0; w < bullets.size(); w++) {
            Bullet currentBullet = bullets.get(w);
            currentBullet.setLocationX((int) (currentBullet.getCenterX() + currentBullet.getXVelocity())); // Moves the bullet one x coordinate per tick.
            currentBullet.setLocationY((int) (currentBullet.getCenterY() + currentBullet.getYVelocity())); // Moves the bullet one y coordinate per tick.
            if (!currentBullet.insideGameBoundaries()) { // If bullet leaves the canvas border, it is removed.
                bullets.remove(w);
            }
        }
    }

    /** Reduces the reload counter every tick. Once the required amount of ticks have passed, the gun is reloaded. */
    private void reduceReloadCounter() {
        mainBee.setReloadCounter(mainBee.getReloadCounter() - 1);
        if (mainBee.getReloadCounter() == 0) {
            mainBee.setReloadedStatus(true);
            mainBee.setAmmo(30);
        }
    }

    /** Draws the reload message once gun is out of ammo */
    private void drawReloadStatusMessage() {
        canvas.setPenColor(Color.BLACK);
        canvas.drawStringCentered(canvas.getWidth()/2, canvas.getHeight()/4, "PRESS R TO RELOAD", (canvas.getWidth()+canvas.getHeight())/40);
    }

    /** Constructs the HUD for the player. Health, Ammo, and Points are displayed at the top of the canvas. */
    private void constructHUD() {
        heightOfHUD = canvas.getHeight() / 15;
        mainBee.setTopBoundary(heightOfHUD);
        canvas.setPenColor(Color.DARK_GRAY);
        canvas.drawFilledRectangle(0, 0, canvas.getWidth(), heightOfHUD); // Top bar for HUD.
        int widthOfBoxes = canvas.getWidth() / 7;
        int heightOfBoxes = (heightOfHUD / 7) * 6;
        Location locationForHealth = new Location(widthOfBoxes, heightOfHUD / 7);
        Location locationForAmmo = new Location(widthOfBoxes * 3, heightOfHUD / 7);
        Location locationForPoints = new Location(widthOfBoxes * 5, heightOfHUD / 7);
        canvas.setPenColor(Color.ORANGE);
        canvas.drawFilledRectangle(locationForHealth.getX(), locationForHealth.getY(), widthOfBoxes, heightOfBoxes);
        canvas.drawFilledRectangle(locationForAmmo.getX(), locationForAmmo.getY(), widthOfBoxes, heightOfBoxes);
        canvas.drawFilledRectangle(locationForPoints.getX(), locationForPoints.getY(), widthOfBoxes, heightOfBoxes);
        canvas.setPenColor(Color.RED);
        canvas.drawStringCentered(widthOfBoxes / 2 + locationForHealth.getX(), heightOfBoxes / 2 + locationForHealth.getY(), "Health: " + String.valueOf(mainBee.getHealth()), (int) (heightOfBoxes * 0.7));
        canvas.drawStringCentered(widthOfBoxes / 2 + locationForAmmo.getX(), heightOfBoxes / 2 + locationForAmmo.getY(), "Ammo: " + String.valueOf(mainBee.getAmmo()), (int) (heightOfBoxes * 0.7));
        canvas.drawStringCentered(widthOfBoxes / 2 + locationForPoints.getX(), heightOfBoxes / 2 + locationForPoints.getY(), "Points: " + String.valueOf(points), (int) (heightOfBoxes * 0.7));
    }
}