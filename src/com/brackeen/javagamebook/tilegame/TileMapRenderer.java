package com.brackeen.javagamebook.tilegame;

import com.brackeen.javagamebook.graphics.ScreenManager;
import java.awt.*;
import java.util.Iterator;

import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.geom.AffineTransform;
import java.io.*;
import com.brackeen.javagamebook.tilegame.*;

import com.brackeen.javagamebook.graphics.Sprite;
import com.brackeen.javagamebook.tilegame.sprites.Bullet;
import com.brackeen.javagamebook.tilegame.sprites.Creature;
import com.brackeen.javagamebook.tilegame.score;

/**
    The TileMapRenderer class draws a TileMap on the screen.
    It draws all tiles, sprites, and an optional background image
    centered around the position of the player.

    <p>If the width of background image is smaller the width of
    the tile map, the background image will appear to move
    slowly, creating a parallax background effect.

    <p>Also, three static methods are provided to convert pixels
    to tile positions, and vice-versa.

    <p>This TileMapRender uses a tile size of 64.
*/
public class TileMapRenderer {
    
    private static final int TILE_SIZE = 64;
    // the size in bits of the tile
    // Math.pow(2, TILE_SIZE_BITS) == TILE_SIZE
    private static final int TILE_SIZE_BITS = 6;

    public static int offsetX;

    private Image background;
    private Image pause;
    private Image instructions;
    private Image StartImg;
    private Image Credits;
    private Image Story[] = new Image[10];
    private Image Bullet;
    private Image GameOver;
    private Image Win;
    
    private int cont = 0;
    /**
* Converts a pixel position to a tile position.
* @param pixels
* @return int
    */
    public static int pixelsToTiles(float pixels) {
        return pixelsToTiles(Math.round(pixels));
    }


    /**
* Converts a pixel position to a tile position.
* @param pixels
* @return int
    */
    public static int pixelsToTiles(int pixels) {
        // use shifting to get correct values for negative pixels
        return pixels >> TILE_SIZE_BITS;

        // or, for tile sizes that aren't a power of two,
        // use the floor function:
        //return (int)Math.floor((float)pixels / TILE_SIZE);
    }


    /**
* Converts a tile position to a pixel position.
* @param numTiles
* @return int
    */
    public static int tilesToPixels(int numTiles) {
        // no real reason to use shifting here.
        // it's slighty faster, but doesn't add up to much
        // on modern processors.
        return numTiles << TILE_SIZE_BITS;

        // use this if the tile size isn't a power of 2:
        //return numTiles * TILE_SIZE;
    }


    /**
* Sets the background to draw.
* @param background
    */
    public void setBackground(Image background) {
        this.background = background;
    }

    /**
        Sets the background to draw.
    */
    public void setPause(Image pause) {
        this.pause = pause;
    }
    /**
        Sets the background to draw.
    */
    public void setInst(Image inst) {
        this.instructions = inst;
    }

    public void setWin(Image win) {
        this.Win = win;
    }
    public void setStart(Image start){
        this.StartImg = start;
    }
    public void setCredits(Image credit){
        this.Credits = credit;
    }
    public void setGameOver(Image game){
        this.GameOver = game;
    }
    public void setStory(Image st){
        Story[cont] = st;
        cont++;
    }
    public void setBullet(Image bull){
        this.Bullet = bull;
    }
    /**
* Draws the specified TileMap.
* @param g
* @param map
* @param screenWidth
* @param screenHeight
    */
    public void draw(Graphics2D g, TileMap map, int screenWidth, int screenHeight, int currMap, int score, int lives)
    {
        Sprite player = map.getPlayer(currMap);
        int mapWidth = tilesToPixels(map.getWidth());

        // get the scrolling position of the map
        // based on player's position
        int offsetX = screenWidth / 2 - Math.round(player.getX()) - TILE_SIZE;
        offsetX = Math.min(offsetX, 0);
        offsetX = Math.max(offsetX, screenWidth - mapWidth);

        // get the y offset to draw all sprites and tiles
        int offsetY = screenHeight - tilesToPixels(map.getHeight());

        // draw black background, if needed
        if (background == null || screenHeight > background.getHeight(null))
        {
            g.setColor(Color.black);
            g.fillRect(0, 0, screenWidth, screenHeight);
        }

        // draw parallax background image
        if (background != null) {
            int x = offsetX *
                (screenWidth - background.getWidth(null)) /
                (screenWidth - mapWidth);
            int y = screenHeight - background.getHeight(null);

            g.drawImage(background, x, y, null);
        }

        // draw the visible tiles
        int firstTileX = pixelsToTiles(-offsetX);
        int lastTileX = firstTileX +
            pixelsToTiles(screenWidth) + 1;
        for (int y=0; y<map.getHeight(); y++) {
            for (int x=firstTileX; x <= lastTileX; x++) {
                Image image = map.getTile(x, y);
                if (image != null) {
                    g.drawImage(image,
                        tilesToPixels(x) + offsetX,
                        tilesToPixels(y) + offsetY,
                        null);
                }
            }
            
        }
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(Color.WHITE);
        g.drawString( "score: "  + score + " |lives left: " + lives, 50, 50);

        // draw player
        g.drawImage(player.getImage(),
            Math.round(player.getX()) + offsetX,
            Math.round(player.getY()) + offsetY,
            null);

        // draw sprites
        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite sprite = (Sprite)i.next();
            int x;
            int y;
            if(sprite instanceof Bullet){
                x = Math.round(sprite.getX()) + offsetX;
                y = Math.round(sprite.getY()) + offsetY;
            }else{
               x = Math.round(sprite.getX()) + offsetX;
               y = Math.round(sprite.getY()) + offsetY;
            }
            g.drawImage(sprite.getImage(), x, y, null);

            // wake up the creature when it's on screen
            if (sprite instanceof Creature &&
                x >= 0 && x < screenWidth)
            {
                ((Creature)sprite).wakeUp();
            }
        }
        if(lives <= 0){
            g.drawImage(GameOver,0, 0, null);
        }
    }
    
    public void drawMenu(Graphics2D g,int screenWidth, int screenHeight, boolean Start)  //metodo que pinta el menu
    {           
        
        // draw black background, if needed
        if (background == null || screenHeight > background.getHeight(null))
        {
            g.setColor(Color.black);
            g.fillRect(0, 0, screenWidth, screenHeight);
        }
        if (background != null) {
            int x = (screenWidth - background.getWidth(null));
            int y = screenHeight - background.getHeight(null);

            g.drawImage(background, x, y, null);
        }
        if(Start){
            g.drawImage(StartImg,0, 0, null);
            g.setFont(new Font("Arial", Font.BOLD, 26));
            g.setColor(Color.WHITE);
        }
    }
    
    public void drawPause(Graphics2D g, TileMap map, int screenWidth, int screenHeight, int currMap, int score)
    {
        g.drawImage(pause,0, 0, null);
    }
    
    public void drawInstCre(Graphics2D g, TileMap map, int screenWidth, int screenHeight, boolean inst, boolean credit)
    {
        if(inst){
            g.drawImage(instructions,(screenWidth/3)-50, (screenHeight/3)-40, null);
        }
        else{
            g.drawImage(Credits,0, 0, null);
        }
        
    }
    public void drawStory(Graphics2D g, TileMap map, int screenWidth, int screenHeight, int st){
        g.drawImage(Story[st],0, 0, null);
    }
    public void drawBullet(Graphics2D g, int x, int y){
        g.drawImage(Bullet,x, y, null);
    }
    public void GameOver(Graphics2D g, TileMap map, int screenWidth, int screenHeight, int score){
        g.drawImage(GameOver,0, 0, null);
        g.setFont(new Font("Arial", Font.BOLD, 26));
        g.drawString("Score final "+ score, screenWidth/3, screenHeight-100);
    }
    public void Win(Graphics2D g, TileMap map, int screenWidth, int screenHeight){
        g.drawImage(Win,0, 0, null);
    }
}

