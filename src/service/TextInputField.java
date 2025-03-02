package service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import ui.GameView;

/**
 * TextInputField handles the rendering and management of user-entered text in
 * input fields.
 * It is used primarily for the name entry screen where players enter their
 * username.
 */
public class TextInputField {

    // The text content to display
    private String textContent;

    // Position and dimensions of the text input box
    private Rectangle textBoxBounds = new Rectangle(283, 217);

    // Text rendering position coordinates
    private static final int TEXT_X_POSITION = 308;
    private static final int TEXT_Y_POSITION = 256;

    /**
     * Creates a new text input field with the specified initial content
     * 
     * @param initialContent The initial text to display
     */
    public TextInputField(String initialContent) {
        textContent = initialContent;
    }

    /**
     * Updates the displayed text content
     * 
     * @param newText The new text to display
     */
    public void setValue(String newText) {
        textContent = newText;
    }

    /**
     * Retrieves the current text content
     * 
     * @return The current text being displayed
     */
    public String getValue() {
        return textContent;
    }

    /**
     * Renders the text content on the screen
     * 
     * @param graphics The Graphics2D context used for drawing
     */
    public void draw(Graphics2D graphics) {
        graphics.setColor(Color.WHITE);
        graphics.setFont(GameView.GAME_FONT);
        graphics.drawString(textContent, TEXT_X_POSITION, TEXT_Y_POSITION);
    }
}