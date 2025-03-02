package service;

import java.io.File;
import java.util.Date;
import java.util.List;

import entity.BasicPatrolEnemy;
import game.Level;
import org.jbox2d.common.Vec2;

import game.GameWorld;
import object.Coin;
import object.IntermittentPlatform;
import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.DynamicBody;
import city.cs.engine.Shape;
import city.cs.engine.StaticBody;
import assets.GameFileManager;

/**
 * LevelFileParser handles loading and parsing level data from text files.
 * It reads level files, interprets the data, and creates the corresponding game
 * objects.
 */
public class LevelFileParser {
    /**
     * Loads a level from a specified file path
     * 
     * @param w The game world to load the level into
     * @param f The file path of the level file to load
     */
    public static void loadLevel(GameWorld w, String f) {
        List<String> str = GameFileManager.ReadTextLevel(f);
        for (String line : str) {
            if (!line.equals("")) {
                analyseElement(GameFileManager.AnalyseLine(line), w);
            }
        }
    }

    /**
     * Analyzes a list of parameters and creates the corresponding game elements
     * 
     * @param params The parameters to analyze
     * @param w      The game world to add the elements to
     */
    public static void analyseElement(List<String> params, GameWorld w) {
        switch (params.get(0)) {
            case "platform":
                String nameFile = params.get(1);
                float position_x = Float.valueOf(params.get(2));
                float position_y = Float.valueOf(params.get(3));
                float width = Float.valueOf(params.get(4));
                float height = Float.valueOf(params.get(5));
                Shape s = new BoxShape(width, height);
                StaticBody platform = new StaticBody(w, s);
                platform.setPosition(new Vec2(position_x, position_y));
                platform.addImage(new BodyImage(nameFile, 2 * height));
                break;
            case "coin":
                float position_xcoin = Float.valueOf(params.get(1));
                float position_ycoin = Float.valueOf(params.get(2));
                Coin c = new Coin(w);
                c.setPosition(new Vec2(position_xcoin, position_ycoin));
                break;
            case "box":
                String nameFileBox = params.get(1);
                float position_xbox = Float.valueOf(params.get(2));
                float position_ybox = Float.valueOf(params.get(3));
                float widthbox = Float.valueOf(params.get(4));
                float heightbox = Float.valueOf(params.get(5));
                Shape sbox = new BoxShape(widthbox, heightbox);
                DynamicBody db = new DynamicBody(w, sbox);
                db.setPosition(new Vec2(position_xbox, position_ybox));
                db.addImage(new BodyImage(nameFileBox, 2 * heightbox));
                break;
            case "intermittentplatform":
                float position_xdp = Float.valueOf(params.get(1));
                float position_ydp = Float.valueOf(params.get(2));
                IntermittentPlatform dp = new IntermittentPlatform(w, new Vec2(position_xdp, position_ydp), w.camera);
                w.staticObjects.add(dp);
                break;
            case "enemy":
                float position_xenemy = Float.valueOf(params.get(1));
                float position_yenemy = Float.valueOf(params.get(2));
                BasicPatrolEnemy el1 = new BasicPatrolEnemy(w);
                el1.setPosition(new Vec2(position_xenemy, position_yenemy));
                w.movableObjects.add(el1);
                break;
            case "length":
                Level.lengthLevelOpenLevel = Integer.valueOf(params.get(1));
                break;
            case "startingPosition":
                Level.startingPositionOpenLevel = new Vec2(Float.valueOf(params.get(1)), Float.valueOf(params.get(2)));
                break;
            case "level":
                w.currentLevelNumber = Integer.valueOf(params.get(1));
                w.loadLevel(Integer.valueOf(params.get(1)));
                break;
            case "score":
                GameWorld.score = Integer.valueOf(params.get(1));
                break;
            case "time":
                GameWorld.previousElapsedTimeSeconds = Integer.valueOf(params.get(1));
                GameWorld.currentElapsedTimeSeconds = Integer.valueOf(params.get(1));
                GameWorld.levelStartTimeMillis = (new Date()).getTime() / 1000 - Integer.valueOf(params.get(1));
                break;
            case "player":
                w.player.setPosition(new Vec2(Float.valueOf(params.get(1)), Float.valueOf(params.get(2))));
                w.player.hasSword = Boolean.valueOf(params.get(3));
                break;
            case "camera":
                w.player.move(new Vec2(Float.valueOf(params.get(1)), 0));
                break;
        }
    }
}