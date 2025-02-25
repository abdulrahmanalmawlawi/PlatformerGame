package ui;

import javax.swing.JFrame;

import game.GameWorld;
import game.Menu;
import assets.GameAssetManager;

/**
 * Main class that initializes and sets up the game window and components.
 * This class serves as the entry point for the application and creates the
 * main game window, initializes the game world, and starts the game loop.
 */
public class Game {
    /**
     * Constructor function that initializes the game components and UI
     */
    public Game() {
        // Initialization of the world, the menu, the view and the jframe
        GameWorld world = new GameWorld();
        Menu menu = new Menu(world);
        GameView view = new GameView(world, 783, 561);
        JFrame frame = new JFrame("game");
        frame.addKeyListener(world);
        frame.addMouseListener(menu);
        frame.add(view);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        world.start();
    }

    /**
     * Main method that starts the application
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        GameAssetManager.loadResources();
        new Game();
    }
}