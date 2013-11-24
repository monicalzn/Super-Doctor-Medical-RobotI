package com.brackeen.javagamebook.tilegame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioFormat;

import com.brackeen.javagamebook.graphics.*;
import com.brackeen.javagamebook.sound.*;
import com.brackeen.javagamebook.input.*;
import com.brackeen.javagamebook.test.GameCore;
import com.brackeen.javagamebook.tilegame.sprites.*;
import com.brackeen.javagamebook.tilegame.*;

/**
    GameManager manages all parts of the game.
*/
public class GameManager extends GameCore {

    public static void main(String[] args) {
        new GameManager().run();
    }

    // uncompressed, 44100Hz, 16-bit, mono, signed, little-endian
    private static final AudioFormat PLAYBACK_FORMAT =
        new AudioFormat(44100, 16, 1, true, false);

    private static final int DRUM_TRACK = 1;

    public static final float GRAVITY = 0.002f;

    private Point pointCache = new Point();
    private TileMap map;
    private MidiPlayer midiPlayer;
    private SoundManager soundManager;
    private ResourceManager resourceManager;
    private Sound prizeSound;
    private Sound boopSound;
    private InputManager inputManager;
    private TileMapRenderer renderer;
    private TileMapRenderer menu;

    private GameAction moveLeft;
    private GameAction moveRight;
    private GameAction moveUp;
    private GameAction moveDown;
    private GameAction jump;
    private GameAction exit;
    private GameAction menus;
    private GameAction shoot;
    private GameAction Gpause;
    private GameAction inst;
    private GameAction Gstart;
    private GameAction Gnext;
    private GameAction credits;
    
    private boolean CurMap;
    private long HitTimer;
    private boolean HitBool;
    private boolean Menu;
    private boolean BStart;
    private boolean pause;
    private boolean instructions;
    private boolean Bcredits;
    private boolean story;
    
    private ArrayList<Bullet> bulletsList;
    
    private score score;
    private int Inext;
    private int help;
    
    static int v = 172;


    public void init() {
        
        super.init();
        score = new score();

        bulletsList = new ArrayList<Bullet>();
        // set up input manager
        initInput();
        Menu = true;
        BStart = true;
        instructions = false;
        story = false;
 
        Inext = 0;
        // start resource manager
        resourceManager = new ResourceManager(screen.getFullScreenWindow().getGraphicsConfiguration());

        // load resources
        renderer = new TileMapRenderer();
        renderer.setBackground(resourceManager.loadImage("background.png"));
        renderer.setPause(resourceManager.loadImage("BlueMeth-03.png"));
        renderer.setInst(resourceManager.loadImage("backgroundM.png"));
        renderer.setCredits(resourceManager.loadImage("credits.png"));
        renderer.setStory(resourceManager.loadImage("Story.png"));
        renderer.setStory(resourceManager.loadImage("Story02.png"));
        renderer.setStory(resourceManager.loadImage("Story.png"));
        renderer.setStory(resourceManager.loadImage("Story02.png"));
        
        
        renderer.setBullet(resourceManager.loadImage("BlueMeth-03.png"));
        menu = new TileMapRenderer();
        menu.setBackground(resourceManager.loadImage("Menu.png"));
        menu.setStart(resourceManager.loadImage("Start.png"));

        // load first map
        map = resourceManager.loadNextMap();

        // load sounds
        soundManager = new SoundManager(PLAYBACK_FORMAT);
        prizeSound = soundManager.getSound("sounds/prize.wav");
        boopSound = soundManager.getSound("sounds/boop2.wav");

        // start music
        midiPlayer = new MidiPlayer();
        Sequence sequence = midiPlayer.getSequence("sounds/music.midi");
        midiPlayer.play(sequence, true);
        toggleDrumPlayback();
        
    }

    public void initSpecial() {
        score.properSetScore(0);
        bulletsList = new ArrayList<Bullet>();
        
        // set up input manager
        initInput();
        Menu = true;
        instructions = false;
        Bcredits = false;
        BStart = false;
        story = false;
        // start resource manager
        resourceManager = new ResourceManager(screen.getFullScreenWindow().getGraphicsConfiguration());

        // load resources
        renderer = new TileMapRenderer();
        renderer.setBackground(resourceManager.loadImage("background.png"));
        renderer.setPause(resourceManager.loadImage("BlueMeth-03.png"));
        renderer.setInst(resourceManager.loadImage("backgroundM.png"));
        renderer.setCredits(resourceManager.loadImage("credits.png"));
        renderer.setStory(resourceManager.loadImage("Story.png"));
        renderer.setStory(resourceManager.loadImage("Story02.png"));
        renderer.setStory(resourceManager.loadImage("Story.png"));
        renderer.setStory(resourceManager.loadImage("Story02.png"));
        
        menu = new TileMapRenderer();
        menu.setBackground(resourceManager.loadImage("Menu.png"));
        
        // load first map
        map = resourceManager.loadNextMap();

        // load sounds
        soundManager = new SoundManager(PLAYBACK_FORMAT);
        prizeSound = soundManager.getSound("sounds/prize.wav");
        boopSound = soundManager.getSound("sounds/boop2.wav");
        
        
        // start music
        midiPlayer = new MidiPlayer();
        Sequence sequence =
            midiPlayer.getSequence("sounds/music.midi");
        midiPlayer.play(sequence, true);
        toggleDrumPlayback();
    }
    
    /**
        Closes any resurces used by the GameManager.
    */
    public void stop() {
        super.stop();
        midiPlayer.close();
        soundManager.close();
    }

    private void initInput() {
        menus = new GameAction("menus", GameAction.DETECT_INITAL_PRESS_ONLY);
        exit = new GameAction("exit", GameAction.DETECT_INITAL_PRESS_ONLY);
        inst = new GameAction("inst", GameAction.DETECT_INITAL_PRESS_ONLY);
        Gstart = new GameAction("start", GameAction.DETECT_INITAL_PRESS_ONLY);
        credits = new GameAction("credits", GameAction.DETECT_INITAL_PRESS_ONLY);
        Gnext = new GameAction("next", GameAction.DETECT_INITAL_PRESS_ONLY);

        inputManager = new InputManager(screen.getFullScreenWindow());
        inputManager.setCursor(InputManager.INVISIBLE_CURSOR);

        inputManager.mapToKey(menus, KeyEvent.VK_SPACE);
        inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);
        inputManager.mapToKey(inst, KeyEvent.VK_I);
        inputManager.mapToKey(Gstart, KeyEvent.VK_ENTER);
        inputManager.mapToKey(credits, KeyEvent.VK_C);
        inputManager.mapToKey(Gnext, KeyEvent.VK_RIGHT);
    }
    
    private void initInputS() {
         
        inputManager.resetAllGameActions();
        moveLeft = new GameAction("moveLeft");
        moveRight = new GameAction("moveRight");
        moveUp = new GameAction("moveUp");
        moveDown = new GameAction("moveDown");
        jump = new GameAction("jump", GameAction.DETECT_INITAL_PRESS_ONLY);
        Gpause = new GameAction("Gpause", GameAction.DETECT_INITAL_PRESS_ONLY);        
        shoot = new GameAction("shoot", GameAction.DETECT_INITAL_PRESS_ONLY);

         
        inputManager.mapToKey(moveLeft, KeyEvent.VK_LEFT);
        inputManager.mapToKey(moveRight, KeyEvent.VK_RIGHT);
        inputManager.mapToKey(moveUp, KeyEvent.VK_UP);
        inputManager.mapToKey(moveDown, KeyEvent.VK_DOWN);
        inputManager.mapToKey(jump, KeyEvent.VK_SPACE);
        inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);        
        inputManager.mapToKey(shoot, KeyEvent.VK_A);        
        inputManager.mapToKey(Gpause, KeyEvent.VK_P);
        inputManager.mapToKey(inst, KeyEvent.VK_I);
    }
    
    
    
    private void checkInput(long elapsedTime) {
        
        if (exit.isPressed()) {
            stop();
        }
        if(inst.isPressed()){
            if(!pause){
                instructions = !instructions;
            }
        }
        
        if(Gpause.isPressed()){
            if(!instructions){
                pause = !pause;
            }
        }
            Player player = (Player)map.getPlayer(resourceManager.getCurrentM());
            Player ship = (Player)map.getPlayer(resourceManager.getCurrentM());
            if (player.isAlive()) {
                float velocityX;
                if(CurMap){
                    velocityX = 0.2f;
                }
                else{
                    velocityX = 0;
                }
                float velocityY = 0;
                if(!CurMap){
                    if (moveLeft.isPressed()) {
                        velocityX-=player.getMaxSpeed();
                    }
                    if (moveRight.isPressed()) {
                        velocityX+=player.getMaxSpeed();
                    }
                    if (jump.isPressed()) {
                        player.jump(false);
                    }
                    if(shoot.isPressed()){
                        if(!pause && !instructions){
                            /*player.numberOfAmmo--;*/
                            /*if(Math.round(player.getX()) > screen.getWidth()){*/
                            Bullet b = new Bullet((Math.round(player.getX()) - (player.getWidth()/2) ), 
                                    (Math.round(player.getY())- player.getHeight()/2),(Math.round(player.getX()) - (player.getWidth()/2)));
                            bulletsList.add(b);
                            //}
                        }
                    }
                }else{
                    if (moveUp.isPressed()) {
                        velocityY -=player.getMaxSpeed();
                    }
                    if (moveDown.isPressed()) {
                        velocityY +=player.getMaxSpeed();
                    }
                    player.setVelocityY(velocityY);
                }

                player.setVelocityX(velocityX);
            }
       // }
        //}
    }
    
     private void checkInputStart(long elapsedTime) {
        
        if (exit.isPressed()) {
            stop();
        }
        if(Gstart.isPressed()){
            BStart = false;
        }
        if(menus.isPressed()){
            if(!BStart){
                Menu = false;
                story = true;
            }
        }
        if(inst.isPressed()){
            if(!BStart && !Bcredits){
                instructions = !instructions;
            }
        }
        if(credits.isPressed()){
            if(!BStart && !instructions){
                Bcredits = !Bcredits;
            }
        }
    }
     
      private void checkInputStory(long elapsedTime) {
        
        if (exit.isPressed()) {
            stop();
        }
        if(Gnext.isPressed()){
            Inext++;
            if(Inext >= 4){
                story = false;
                initInputS();
            }
        }
    }


    public void draw(Graphics2D g) {
        Graphics2D g2d = (Graphics2D) g;
        if(Menu){
            menu.drawMenu(g, screen.getWidth(), screen.getHeight(), BStart);
            
        }
        else if(story && Inext <= 4){
            renderer.drawStory(g, map, screen.getWidth(), screen.getHeight(), Inext);
        }
        else{
            
            renderer.draw(g, map, screen.getWidth(), screen.getHeight(), resourceManager.getCurrentM(), score.getScore());
            if(!bulletsList.isEmpty()){
                for(int j = 0; j < bulletsList.size(); j++){
                    renderer.drawBullet(g, bulletsList.get(j).getX(), bulletsList.get(j).getY());
                }
            }
        }
        if(Bcredits || instructions && !pause){
            renderer.drawInstCre(g, map, screen.getWidth(), screen.getHeight(), instructions, Bcredits);
        }
        if(pause && !instructions){
            renderer.drawPause(g, map, screen.getWidth(), screen.getHeight(), resourceManager.getCurrentM(), score.getScore());
        }
        
            
    }


    /**
        Gets the current map.
    */
    public TileMap getMap() {
        return map;
    }


    /**
        Turns on/off drum playback in the midi music (track 1).
    */
    public void toggleDrumPlayback() {
        Sequencer sequencer = midiPlayer.getSequencer();
        if (sequencer != null) {
            sequencer.setTrackMute(DRUM_TRACK,!sequencer.getTrackMute(DRUM_TRACK));
        }
    }

    /**
        Gets the tile that a Sprites collides with. Only the
        Sprite's X or Y should be changed, not both. Returns null
        if no collision is detected.
    */
    public Point getTileCollision(Sprite sprite, float newX, float newY){
        float fromX = Math.min(sprite.getX(), newX);
        float fromY = Math.min(sprite.getY(), newY);
        float toX = Math.max(sprite.getX(), newX);
        float toY = Math.max(sprite.getY(), newY);

        // get the tile locations
        int fromTileX = TileMapRenderer.pixelsToTiles(fromX);
        int fromTileY = TileMapRenderer.pixelsToTiles(fromY);
        int toTileX = TileMapRenderer.pixelsToTiles(toX + sprite.getWidth() - 1);
        int toTileY = TileMapRenderer.pixelsToTiles(toY + sprite.getHeight() - 1);

        // check each tile for a collision
        for (int x=fromTileX; x<=toTileX; x++) {
            for (int y=fromTileY; y<=toTileY; y++) {
                if (x < 0 || x >= map.getWidth() || map.getTile(x, y) != null){
                    // collision found, return the tile
                    pointCache.setLocation(x, y);
                    return pointCache;
                }
            }
        }

        // no collision found
        return null;
    }


    /**
        Checks if two Sprites collide with one another. Returns
        false if the two Sprites are the same. Returns false if
        one of the Sprites is a Creature that is not alive.
    */
    public boolean isCollision(Sprite s1, Sprite s2) {
        // if the Sprites are the same, return false
        if (s1 == s2) {
            return false;
        }

        // if one of the Sprites is a dead Creature, return false
        if (s1 instanceof Creature && !((Creature)s1).isAlive()) {
            return false;
        }
        if (s2 instanceof Creature && !((Creature)s2).isAlive()) {
            return false;
        }

        // get the pixel location of the Sprites
        int s1x = Math.round(s1.getX());
        int s1y = Math.round(s1.getY());
        int s2x = Math.round(s2.getX());
        int s2y = Math.round(s2.getY());

        // check if the two sprites' boundaries intersect
        return (s1x < s2x + s2.getWidth() &&
            s2x < s1x + s1.getWidth() &&
            s1y < s2y + s2.getHeight() &&
            s2y < s1y + s1.getHeight());
    }


    /**
        Gets the Sprite that collides with the specified Sprite,
        or null if no Sprite collides with the specified Sprite.
    */
    public Sprite getSpriteCollision(Sprite sprite) {

        // run through the list of Sprites
        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite otherSprite = (Sprite)i.next();
            if (isCollision(sprite, otherSprite)) {
                // collision found, return the Sprite
                return otherSprite;
            }
        }

        // no collision found
        return null;
    }
    
    /**
        Updates Animation, position, and velocity of all Sprites
        in the current map.
    */
    public void update(long elapsedTime) {
        if(Menu){
           
            checkInputStart(elapsedTime);
        }
        else if(story){
            checkInputStory(elapsedTime);
        }
        else{
            checkInput(elapsedTime);
            if(pause || instructions) {
                /*checkInput(elapsedTime);*/
            }//let the games begin
            else{
                if(HitBool){                                                                     //le da un tiempo antes de poder vovler a chocar
                    HitTimer += 1;
                    if(HitTimer > 50){
                        HitBool = false;
                    }
                }

                Creature player = (Creature)map.getPlayer(resourceManager.getCurrentM());
                
                updateBullets();
                if(resourceManager.getCurrentM() == 1){
                    CurMap = true;
                    player.setisFlying(true);
                }
                else{
                    CurMap = false;
                    player.setisFlying(false);
                }

                // player is dead! start map over
                if (player.getState() >= Creature.STATE_DEAD) {
                    HitBool = false;
                    player.setState(0);
                    map = resourceManager.reloadMap();
                    Menu = true;
                    initSpecial();
                    return;
                }

                // update player
                updateCreature(player, elapsedTime);
                player.update(elapsedTime);

                // update other sprites
                Iterator i = map.getSprites();
                while (i.hasNext()) {
                    Sprite sprite = (Sprite)i.next();
                    if (sprite instanceof Creature) {
                        Creature creature = (Creature)sprite;
                        if (creature.getState() >= Creature.STATE_DEAD) {
                            i.remove();
                        }
                        else {
                            updateCreature(creature, elapsedTime);
                        }
                    }
                    // normal update
                    sprite.update(elapsedTime);
                }
            }  
        }
    }


    /**
        Updates the creature, applying gravity for creatures that
        aren't flying, and checks collisions.
    */
    private void updateCreature(Creature creature, long elapsedTime)
    {

        // apply gravity
        if (!creature.isFlying()) {
            creature.setVelocityY(creature.getVelocityY() + GRAVITY * elapsedTime);
        }
        

        // change x
        float dx = creature.getVelocityX();
        float oldX = creature.getX();
        float newX = oldX + dx * elapsedTime;
        Point tile = getTileCollision(creature, newX, creature.getY());
        if (tile == null) {
            creature.setX(newX);
        }
        else {
            // line up with the tile boundary
            
            if (dx > 0) {
                creature.setX(TileMapRenderer.tilesToPixels(tile.x) - creature.getWidth());
            }
            else if (dx < 0) {
                creature.setX(TileMapRenderer.tilesToPixels(tile.x + 1));
            }
            if(CurMap){                                                          //nave choca con un tile muere (:
                creature.HitTile();                
            }
            creature.collideHorizontal();
        }
        if (creature instanceof Player) {
            checkPlayerCollision((Player)creature, false);
        }

        // change y
        float dy = creature.getVelocityY();
        float oldY = creature.getY();
        float newY = oldY + dy * elapsedTime;
        tile = getTileCollision(creature, creature.getX(), newY);
        if (tile == null) {
            creature.setY(newY);
        }
        else {
            // line up with the tile boundary
            if (dy > 0) {
                creature.setY(TileMapRenderer.tilesToPixels(tile.y) - creature.getHeight());
            }
            else if (dy < 0) {
                creature.setY(TileMapRenderer.tilesToPixels(tile.y + 1));
            }
            creature.collideVertical();
        }
        if (creature instanceof Player) {
            boolean canKill = (oldY < creature.getY());
            checkPlayerCollision((Player)creature, canKill);
        }

    }


    /**
        Checks for Player collision with other Sprites. If
        canKill is true, collisions with Creatures will kill
        them.
    */
    public void checkPlayerCollision(Player player, boolean canKill)
    {
        if (!player.isAlive()) {
            return;
        }

        // check for player collision with other sprites
        Sprite collisionSprite = getSpriteCollision(player);
        if (collisionSprite instanceof PowerUp) {
            acquirePowerUp((PowerUp)collisionSprite);
        }
        else if (collisionSprite instanceof Creature) {
            Creature badguy = (Creature)collisionSprite;
            if (canKill) {
                // kill the badguy and make player bounce
                soundManager.play(boopSound);
                player.setY(badguy.getY() - player.getHeight());
                player.jump(true);
                badguy.setStateTwo();
                score.setScore(badguy.getType());
            }
            else {
                // player dies!
                player.setStateT();
                
            }
        }
    }


    /**
        Gives the player the speicifed power up and removes it
        from the map.
    */
    public void acquirePowerUp(PowerUp powerUp) {
        // remove it from the map
        map.removeSprite(powerUp);

        if (powerUp instanceof PowerUp.Star) {
            // do something here, like give the player points
            soundManager.play(prizeSound);
        }
        else if (powerUp instanceof PowerUp.Music) {
            // change the music
            soundManager.play(prizeSound);
            toggleDrumPlayback();
        }
        else if (powerUp instanceof PowerUp.Goal) {
            // advance to next map
            soundManager.play(prizeSound, new EchoFilter(2000, .7f), false);
            score.setScoreNewMap(resourceManager.getCurrentM());
            map = resourceManager.loadNextMap();
        }
    }
    /*
    public boolean isItLeftScreen(Bullet e)
    {
        if(e.getX() > 0 && e.getX() < screen.getWidth() &&
           e.getY() > 0 && e.getY() < screen.getHeight())
            return false;
        else
            return true;
    }*/
    /*
    public void DBullet(int x, int y){
        renderer.drawBullet(g,x, y);
    }
    */
    private void updateBullets()
    {
        for(int i = 0; i < bulletsList.size(); i++)
        {
            Bullet bullet = bulletsList.get(i);
            
            // Move the bullet.
            bullet.Update();
           /*
            // Is left the screen?
            if(isItLeftScreen(bullet)){
                bulletsList.remove(i);
                // Bullet have left the screen so we removed it from the list and now we can continue to the next bullet.
                continue;
            }*/
            
            // Did hit any enemy?
            // Rectangle of the bullet image.
            // Go trough all enemis.
            Iterator it = map.getSprites();
            while (it.hasNext()) {
                Sprite otherSprite = (Sprite)it.next();
                // Current enemy rectangle.
                if (otherSprite instanceof Creature && !((Creature)otherSprite).isAlive()) {
                    
                }
                Rectangle bulletRectangle = new Rectangle((int)bullet.getXS(), (int)bullet.getYS(),64, 64);
            
                // get the pixel location of the Sprites
                int s2x = Math.round(otherSprite.getX());
                int s2y = Math.round(otherSprite.getY());
                
                Rectangle otherRectangle = new Rectangle(s2x, s2y, otherSprite.getWidth(), otherSprite.getHeight());
                // check if the two sprites' boundaries intersect
                if(bulletRectangle.intersects(otherRectangle))
                {
                    // Bullet hit the enemy so we reduce his health.
                    /*eh.health -= Bullet.damagePower;*/
                    
                    // Bullet was also destroyed so we remove it.
                    bulletsList.remove(i);
                    
                    // That bullet hit enemy so we don't need to check other enemies.
                    break;
                }
            }
        }        
    }

}
