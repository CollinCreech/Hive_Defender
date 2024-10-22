package game;

public class RunDefaultGame {

    /** This is the main testing function for the game. It will instantiate a new game and run it. */
    public static void main(String[] args)
    {
        Game theGame = new Game(1280, 720);
        theGame.runGame();
    }
}