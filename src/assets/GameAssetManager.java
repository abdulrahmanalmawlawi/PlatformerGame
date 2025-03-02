package assets;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * GameAssetManager is responsible for loading and providing access to all game
 * image assets.
 * It loads images for backgrounds, UI elements, characters, and other visual
 * components
 * used throughout the game.
 */
public class GameAssetManager {

    // Background layers for parallax scrolling
    public static Image[] background1 = new Image[3];
    public static Image[] background2 = new Image[3];
    public static Image[] background3 = new Image[3];
    public static Image[] background4 = new Image[3];

    // Level-specific backgrounds and foregrounds
    public static Image backgroundLevel1;
    public static Image foregroundLevel1;
    public static Image backgroundLevel2;
    public static Image foregroundLevel2;
    public static Image backgroundLevel3;
    public static Image foregroundLevel3;

    // Visual effects
    public static Image[] gradient = new Image[3];

    // UI icons
    public static Image sword;
    public static Image heart;
    public static Image coin;
    public static Image pauseIcon;
    public static Image playIcon;
    public static Image menuIcon;
    public static Image key;

    // UI backgrounds and overlays
    public static Image darkforeground;
    public static Image backgroundmenu;
    public static Image namebackground;
    public static Image backgroundleaderboard;

    // UI buttons and states
    public static Image playButton;
    public static Image playButton2;
    public static Image leaderBoardButton;
    public static Image leaderBoardButton2;
    public static Image nameBox;
    public static Image okbutton;
    public static Image okbutton2;
    public static Image level1button;
    public static Image level1button2;
    public static Image level1buttonlocked;
    public static Image level2button;
    public static Image level2button2;
    public static Image level2buttonlocked;
    public static Image level3button;
    public static Image level3button2;
    public static Image level3buttonlocked;
    public static Image savebutton;
    public static Image loadPreviousGame;
    public static Image loadPreviousGame2;
    public static Image buttonFile;

    // Game state messages
    public static Image congratulations;
    public static Image gameOver;

    /**
     * Loads all the game image assets from their respective files.
     * This method should be called once at the start of the game to ensure
     * all visual assets are available when needed.
     */
    public static void loadResources() {
        try {
            // Load background layers
            background1[0] = ImageIO.read(new File("resources/backgrounds/background_layer1.png"));
            background2[0] = ImageIO.read(new File("resources/backgrounds/background_layer2.png"));
            background3[0] = ImageIO.read(new File("resources/backgrounds/background_layer3.png"));
            background4[0] = ImageIO.read(new File("resources/backgrounds/background_layer4.png"));
            background1[1] = ImageIO.read(new File("resources/backgrounds/background_layer1.png"));
            background2[1] = ImageIO.read(new File("resources/backgrounds/background_layer2.png"));
            background3[1] = ImageIO.read(new File("resources/backgrounds/background_layer3.png"));
            background4[1] = ImageIO.read(new File("resources/backgrounds/background_layer4.png"));
            background1[2] = ImageIO.read(new File("resources/backgrounds/background_layer5.png"));

            // Load level backgrounds and foregrounds
            backgroundLevel1 = ImageIO.read(new File("resources/levels/level1_background.png"));
            foregroundLevel1 = ImageIO.read(new File("resources/levels/level1_foreground.png"));
            backgroundLevel2 = ImageIO.read(new File("resources/levels/level2_background.png"));
            foregroundLevel2 = ImageIO.read(new File("resources/levels/level2_foreground.png"));
            backgroundLevel3 = ImageIO.read(new File("resources/levels/level3_background.png"));
            foregroundLevel3 = ImageIO.read(new File("resources/levels/level3_foreground.png"));

            // Load UI icons
            heart = ImageIO.read(new File("resources/ui/icons/ui_heart_icon.png"));
            coin = ImageIO.read(new File("resources/collectibles/collectible_coin.png"));
            sword = ImageIO.read(new File("resources/ui/icons/ui_sword_icon.png"));
            pauseIcon = ImageIO.read(new File("resources/ui/icons/ui_icon_pause.png"));
            playIcon = ImageIO.read(new File("resources/ui/icons/ui_icon_play.png"));
            menuIcon = ImageIO.read(new File("resources/ui/icons/ui_icon_menu.png"));
            key = ImageIO.read(new File("resources/ui/icons/ui_key_icon.png"));

            // Load visual effects
            gradient[0] = ImageIO.read(new File("resources/ui/effects/ui_effect_gradient.png"));
            gradient[1] = ImageIO.read(new File("resources/ui/effects/ui_effect_gradient.png"));
            gradient[2] = ImageIO.read(new File("resources/ui/effects/ui_effect_gradient_alt.png"));

            // Load UI backgrounds and overlays
            darkforeground = ImageIO.read(new File("resources/ui/overlays/ui_overlay_dark.png"));
            backgroundmenu = ImageIO.read(new File("resources/ui/backgrounds/ui_background_menu.png"));
            namebackground = ImageIO.read(new File("resources/ui/backgrounds/ui_background_name_input.png"));
            backgroundleaderboard = ImageIO.read(new File("resources/ui/backgrounds/ui_background_leaderboard.png"));

            // Load UI buttons and their states
            playButton = ImageIO.read(new File("resources/ui/buttons/ui_button_play.png"));
            playButton2 = ImageIO.read(new File("resources/ui/buttons/ui_button_play_hover.png"));
            leaderBoardButton = ImageIO.read(new File("resources/ui/buttons/ui_button_leaderboard.png"));
            leaderBoardButton2 = ImageIO.read(new File("resources/ui/buttons/ui_button_leaderboard_hover.png"));
            nameBox = ImageIO.read(new File("resources/ui/inputs/ui_input_name.png"));
            okbutton = ImageIO.read(new File("resources/ui/buttons/ui_button_ok.png"));
            okbutton2 = ImageIO.read(new File("resources/ui/buttons/ui_button_ok_hover.png"));
            level1button = ImageIO.read(new File("resources/ui/buttons/ui_button_level1.png"));
            level1button2 = ImageIO.read(new File("resources/ui/buttons/ui_button_level1_hover.png"));
            level2button = ImageIO.read(new File("resources/ui/buttons/ui_button_level2.png"));
            level2button2 = ImageIO.read(new File("resources/ui/buttons/ui_button_level2_hover.png"));
            level3button = ImageIO.read(new File("resources/ui/buttons/ui_button_level3.png"));
            level3button2 = ImageIO.read(new File("resources/ui/buttons/ui_button_level3_hover.png"));
            level1buttonlocked = ImageIO.read(new File("resources/ui/buttons/ui_button_level1_locked.png"));
            level2buttonlocked = ImageIO.read(new File("resources/ui/buttons/ui_button_level2_locked.png"));
            level3buttonlocked = ImageIO.read(new File("resources/ui/buttons/ui_button_level3_locked.png"));
            savebutton = ImageIO.read(new File("resources/ui/buttons/ui_button_save.png"));
            loadPreviousGame = ImageIO.read(new File("resources/ui/buttons/ui_button_load.png"));
            loadPreviousGame2 = ImageIO.read(new File("resources/ui/buttons/ui_button_load_hover.png"));
            buttonFile = ImageIO.read(new File("resources/ui/buttons/ui_button_file.png"));

            // Load game state messages
            gameOver = ImageIO.read(new File("resources/ui/messages/ui_message_game_over.png"));
            congratulations = ImageIO.read(new File("resources/ui/messages/ui_message_congratulations.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}