# Platformer Game

A 2D platformer game built using Java and the CityEngine physics library.

## Game Features

- Multiple levels with unique themes and challenges
- Various enemy types with different behaviors
- Collectible items (coins, keys, power-ups)
- Interactive platforms (moving, disappearing, falling)
- Player progression system with unlockable levels
- Game statistics tracking (score, lives, collected items)
- Sound effects and background music

## Controls

- **Left/Right Arrow Keys**: Move player left/right
- **Up Arrow Key**: Jump
- **Space**: Attack (when sword power-up is collected)
- **Escape**: Pause game / Access menu
- **Enter**: Confirm selection in menus

## Game Objects

### Player
The main character controlled by the player. Can run, jump, and attack enemies when powered up.

### Enemies
- **Basic Patrol Enemy**: Moves back and forth on platforms
- **Pursuer Enemy**: Follows the player when in range
- **Spiked Enemy**: Causes damage on contact
- **Armored Enemy**: Requires multiple hits to defeat
- **Slime**: Bounces around unpredictably
- **Turret**: Shoots projectiles at the player

### Platforms
- **Static Platforms**: Standard platforms for navigation
- **Moving Platforms**: Platforms that follow predetermined paths
- **Intermittent Platforms**: Appear and disappear on a timer
- **Falling Platforms**: Collapse after the player stands on them

### Collectibles
- **Coins**: Increase score
- **Keys**: Required to open doors
- **Sword Power-up**: Enables attack ability

### Interactive Objects
- **Doors**: Require keys to open
- **Electric Portals**: Transport to next level

## Setup Instructions

1. Ensure Java 8 or higher is installed
2. Clone the repository
3. Open the project in IntelliJ IDEA:
   - Select "Open" or "Import Project"
   - Navigate to the project directory and select it
   - Choose "Open as Project"
   - Wait for IntelliJ to index the project files
4. Run the game:
   - Navigate to `src/ui/Game.java`
   - Right-click on the file and select "Run Game.main()"
   - Alternatively, open the file and click the green "Run" button next to the main method

## Development

The game is built using:
- Java
- CityEngine physics library (based on JBox2D)
- Custom game engine components

## Credits

Developed as part of a programming coursework project.
