package com.brackeen.javagamebook.tilegame;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;

import com.brackeen.javagamebook.graphics.*;
import com.brackeen.javagamebook.tilegame.sprites.*;


/**
    The ResourceManager class loads and manages tile Images and
    "host" Sprites used in the game. Game Sprites are cloned from
    "host" Sprites.
*/
public class ResourceManager {

    private ArrayList tiles;
    private int currentMap;
    private GraphicsConfiguration gc;

    // host sprites used for cloning
    private Sprite playerSprite;
    private Sprite shipSprite;
    private Sprite musicSprite;
    private Sprite coinSprite;
    private Sprite goalSprite;
    private Sprite GM1Sprite;
    private Sprite FM1Sprite;
    private Sprite boss1Sprite;
    private Sprite GM2Sprite;
    private Sprite FM2Sprite;
    private Sprite boss2Sprite;
    private Sprite GM3Sprite;
    private Sprite FM3Sprite;
    private Sprite boss3Sprite;

    /**
        Creates a new ResourceManager with the specified
        GraphicsConfiguration.
    */
    public ResourceManager(GraphicsConfiguration gc) {
        this.gc = gc;
        loadTileImages();
        loadCreatureSprites();
        loadPowerUpSprites();
    }
    public ResourceManager(){
        return;
    }
    
    public int getCurrentM(){
        return currentMap;
    }


    /**
        Gets an image from the images/ directory.
    */
    public Image loadImage(String name) {
        String filename = "images/" + name;
        return new ImageIcon(filename).getImage();
    }


    public Image getMirrorImage(Image image) {
        return getScaledImage(image, -1, 1);
    }


    public Image getFlippedImage(Image image) {
        return getScaledImage(image, 1, -1);
    }


    private Image getScaledImage(Image image, float x, float y) {

        // set up the transform
        AffineTransform transform = new AffineTransform();
        transform.scale(x, y);
        transform.translate(
            (x-1) * image.getWidth(null) / 2,
            (y-1) * image.getHeight(null) / 2);

        // create a transparent (not translucent) image
        Image newImage = gc.createCompatibleImage(
            image.getWidth(null),
            image.getHeight(null),
            Transparency.BITMASK);

        // draw the transformed image
        Graphics2D g = (Graphics2D)newImage.getGraphics();
        g.drawImage(image, transform, null);
        g.dispose();

        return newImage;
    }


    public TileMap loadNextMap() {
        TileMap map = null;
        while (map == null) {
            currentMap++;
            try {
                map = loadMap(
                    "maps/map" + currentMap + ".txt");
            }
            catch (IOException ex) {
                if (currentMap == 1) {
                    // no maps to load!
                    return null;
                }
                currentMap = 0;
                map = null;
            }
        }

        return map;
    }


    public TileMap reloadMap() {
        try {
            return loadMap("maps/map" + currentMap + ".txt");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    private TileMap loadMap(String filename)
        throws IOException
    {
        ArrayList lines = new ArrayList();
        int width = 0;
        int height = 0;

        // read every line in the text file into the list
        BufferedReader reader = new BufferedReader(
            new FileReader(filename));
        while (true) {
            String line = reader.readLine();
            // no more lines to read
            if (line == null) {
                reader.close();
                break;
            }

            // add every line except for comments
            if (!line.startsWith("#")) {
                lines.add(line);
                width = Math.max(width, line.length());
            }
        }

        // parse the lines to create a TileEngine
        height = lines.size();
        TileMap newMap = new TileMap(width, height);
        for (int y=0; y<height; y++) {
            String line = (String)lines.get(y);
            for (int x=0; x<line.length(); x++) {
                char ch = line.charAt(x);

                // check if the char represents tile A, B, C etc.
                int tile = ch - 'A';
                if (tile >= 0 && tile < tiles.size()) {
                    newMap.setTile(x, y, (Image)tiles.get(tile));
                }

                // check if the char represents a sprite
                else if (ch == 'o') {
                    addSprite(newMap, coinSprite, x, y);
                }
                else if (ch == '!') {
                    addSprite(newMap, musicSprite, x, y);
                }
                else if (ch == '*') {
                    addSprite(newMap, goalSprite, x, y);
                }
                else if (ch == '1') {
                    addSprite(newMap, GM1Sprite, x, y);
                }
                else if (ch == '2') {
                    addSprite(newMap, FM1Sprite, x, y);
                }
                else if (ch == '3'){
                    addSprite(newMap, boss1Sprite, x, y);
                }
                else if (ch == '4') {
                    addSprite(newMap, GM2Sprite, x, y);
                }
                else if (ch == '5') {
                    addSprite(newMap, FM2Sprite, x, y);
                }
                else if (ch == '6') {
                    addSprite(newMap, boss2Sprite, x, y);
                }
                else if (ch == '7') {
                    addSprite(newMap, GM3Sprite, x, y);
                }
                else if (ch == '8') {
                    addSprite(newMap, FM3Sprite, x, y);
                }
                else if (ch == '9'){
                    addSprite(newMap, boss3Sprite, x, y);
                }
            }
        }

        // add the player to the map
        Sprite player = (Sprite)playerSprite.clone();
        //player.setX(TileMapRenderer.tilesToPixels(3));
        player.setX(0);
        player.setY(0);
        Sprite ship = (Sprite)shipSprite.clone();
        ship.setX(0);
        ship.setY(500);
        newMap.setPlayer(player);
        newMap.setShip(ship);

        return newMap;
    }


    private void addSprite(TileMap map,
        Sprite hostSprite, int tileX, int tileY)
    {
        if (hostSprite != null) {
            // clone the sprite from the "host"
            Sprite sprite = (Sprite)hostSprite.clone();

            // center the sprite
            sprite.setX(
                TileMapRenderer.tilesToPixels(tileX) +
                (TileMapRenderer.tilesToPixels(1) -
                sprite.getWidth()) / 2);

            // bottom-justify the sprite
            sprite.setY(
                TileMapRenderer.tilesToPixels(tileY + 1) -
                sprite.getHeight());

            // add it to the map
            map.addSprite(sprite);
        }
    }


    // -----------------------------------------------------------
    // code for loading sprites and images
    // -----------------------------------------------------------


    public void loadTileImages() {
        // keep looking for tile A,B,C, etc. this makes it
        // easy to drop new tiles in the images/ directory
        tiles = new ArrayList();
        char ch = 'A';
        while (true) {
            String name = "tile_" + ch + ".png";
            File file = new File("images/" + name);
            if (!file.exists()) {
                break;
            }
            tiles.add(loadImage(name));
            ch++;
        }
    }


    public void loadCreatureSprites() {

        Image[][] images = new Image[4][];

        // load left-facing images
        images[0] = new Image[] {
            loadImage("Rocket-01.png"),
            loadImage("Rocket-02.png"),
            loadImage("Rocket-03.png"),
            loadImage("Rocket-04.png"), 
            //4 al 13
            loadImage("robot13busterna.png"),
            loadImage("robot14busterna.png"),
            loadImage("robot15busterna.png"),            
            loadImage("robot16busterna.png"),
            loadImage("robot17busterna.png"),
            loadImage("robot18busterna.png"),
            loadImage("robot19busterna.png"),
            loadImage("robot20busterna.png"),
            loadImage("robot21busterna.png"),
            loadImage("robot22busterna.png"),
            //14 a 19 First World flying bad guy
            loadImage("FM1-1.png"),
            loadImage("FM1-2.png"),
            loadImage("FM1-3.png"),
            loadImage("FM1-4.png"),
            loadImage("FM1-5.png"),
            loadImage("FM1-6.png"),
            //20 first worlds ground bad guy
            loadImage("GM1_1.png"),
            loadImage("GM1_2.png"),
            loadImage("GM1_3.png"),
            loadImage("GM1_4.png"),
            //24
            loadImage("Boss_1-01.png"),
            //25
            loadImage("robot23busterna.png"),
            loadImage("robot24busterna.png"),
            //27 robot WITHOUT buster running 
            loadImage("robotbusterna.png"),
            loadImage("robot2busterna.png"),
            loadImage("robot3busterna.png"),            
            loadImage("robot4busterna.png"),
            loadImage("robot5busterna.png"),
            loadImage("robot6busterna.png"),
            loadImage("robot7busterna.png"),
            loadImage("robot8busterna.png"),
            loadImage("robot9busterna.png"),
            loadImage("robot10busterna.png"),
            loadImage("robot11busterna.png"),
            loadImage("robot12busterna.png"),
            //39 second world flying bad guy
            loadImage("FM2-5.png"),
            loadImage("FM2-6.png"),
            //41 second world ground bad guy
            loadImage("GM2_5.png"),
            loadImage("GM2_6.png"),
            loadImage("GM2_7.png"),
            loadImage("GM2_8.png"),
            //45 third world flying bad guy
            loadImage("FM3-6.png"),
            loadImage("FM3-5.png"),
            //47 third world ground bad guy
            loadImage("GM3_7.png"),
            loadImage("GM3_8.png"),
            loadImage("GM3_9.png"),
            loadImage("GM3_10.png"),
            loadImage("GM3_11.png"),
            loadImage("GM3_12.png"),
            
        };

        images[1] = new Image[images[0].length];
        images[2] = new Image[images[0].length];
        images[3] = new Image[images[0].length];
        for (int i=0; i<images[0].length; i++) {
            // right-facing images
            images[1][i] = getMirrorImage(images[0][i]);
            // left-facing "dead" images
            images[2][i] = getFlippedImage(images[0][i]);
            // right-facing "dead" images
            images[3][i] = getFlippedImage(images[1][i]);
        }

        // create creature animations
        Animation[] shipAnim = new Animation[10];
        Animation[] playerAnim = new Animation[10];
        Animation[] flyAnim = new Animation[10];
        Animation[] grubAnim = new Animation[10];
        Animation[] bossAnim = new Animation[10];
        Animation[] FM2Anim = new Animation[10];
        Animation[] GM2Anim = new Animation[10];
        Animation[] boss2Anim = new Animation[10];
        Animation[] FM3Anim = new Animation[10];
        Animation[] GM3Anim = new Animation[10];
        Animation[] boss3Anim = new Animation[10];
        
        for (int i=0; i<4; i++) {
            shipAnim[i] = createPlayerAnim(images[i][0], images[i][1], images[i][2], images[i][3], 
                    images[i][0], images[i][1], images[i][2], images[i][3], images[i][0], images[i][1], images[i][2]);
            playerAnim[i] = createPlayerAnim(images[i][4], images[i][5], images[i][6], images[i][7],
                    images[i][8], images[i][9], images[i][10], images[i][11], images[i][12], images[i][25], images[i][26]);
            
            //first world bad guys
            flyAnim[i] = createFlyAnim(images[i][14], images[i][15], images[i][16], images[i][17], images[i][18], images[i][19]);
            grubAnim[i] = createGrubAnim(images[i][20], images[i][21], images[i][22], images[i][23]); 
            bossAnim[i] = createBossAnim(images[i][24], images[i][24], images[i][24], images[i][24]);
            
            //second world bad guy
            FM2Anim[i] = createFlyAnim(images[i][39], images[i][40], images[i][39], images[i][40], images[i][39], images[i][40]);
            GM2Anim[i] = createGrubAnim(images[i][41], images[i][42], images[i][43], images[i][44]); 
            boss2Anim[i] = createBossAnim(images[i][24], images[i][24], images[i][24], images[i][24]);
            
            //third world bad guy
            FM3Anim[i] = createFlyAnim(images[i][45], images[i][46], images[i][45], images[i][46], images[i][45], images[i][46]);
            GM3Anim[i] = createGrubAnim(images[i][47], images[i][48], images[i][49], images[i][50]); 
            boss3Anim[i] = createBossAnim(images[i][24], images[i][24], images[i][24], images[i][24]);
        }
        //right-facing 
       /* playerAnim[1] = createPlayerAnim(images[1][4], images[1][5], images[1][6], images[1][7],
                    images[1][8], images[1][9], images[1][10], images[1][11], images[1][12], images[1][25], images[1][26]);
        */
        // create creature sprites
        playerSprite = new Player(playerAnim[0], playerAnim[1], playerAnim[2], playerAnim[3]);
        FM1Sprite = new Fly(flyAnim[0], flyAnim[1], flyAnim[2], flyAnim[3]);
        GM1Sprite = new Grub(grubAnim[0], grubAnim[1], grubAnim[2], grubAnim[3]);
        shipSprite = new Player(shipAnim[0], shipAnim[1], shipAnim[2], shipAnim[3]);
        boss1Sprite = new Boss1(bossAnim[0], bossAnim[1], bossAnim[2], bossAnim[3]);
        GM2Sprite = new Grub(GM2Anim[0], GM2Anim[1], GM2Anim[2], GM2Anim[3]);
        FM2Sprite = new Fly(FM2Anim[0], FM2Anim[1], FM2Anim[2], FM2Anim[3]);;
        boss2Sprite = new Boss1(bossAnim[0], bossAnim[1], bossAnim[2], bossAnim[3]);;
        GM3Sprite = new Grub(GM3Anim[0], GM3Anim[1], GM3Anim[2], GM3Anim[3]);;
        FM3Sprite = new Fly(FM3Anim[0], FM3Anim[1], FM3Anim[2], FM3Anim[3]);;
        boss3Sprite = new Boss1(bossAnim[0], bossAnim[1], bossAnim[2], bossAnim[3]);;
    }


    private Animation createPlayerAnim(Image player1,
        Image player2, Image player3, Image player4, Image player5,
        Image player6, Image player7, Image player8, Image player9, Image player10, Image player11)
    {
        Animation anim = new Animation();
        anim.addFrame(player1, 150);
        anim.addFrame(player2, 150);
        anim.addFrame(player3, 150);
        anim.addFrame(player4, 150);
        anim.addFrame(player5, 250);
        anim.addFrame(player6, 150);
        anim.addFrame(player7, 150);
        anim.addFrame(player8, 150);
        anim.addFrame(player9, 150);
        anim.addFrame(player10, 150);
        anim.addFrame(player11, 150);
        return anim;
    }


    private Animation createFlyAnim(Image img1, Image img2,
        Image img3, Image img4, Image img5, Image img6)
    {
        Animation anim = new Animation();
        anim.addFrame(img1, 200);
        anim.addFrame(img2, 300);
        anim.addFrame(img3, 300);
        anim.addFrame(img4, 300);
        anim.addFrame(img5, 300);
        anim.addFrame(img6, 300);
        return anim;
    }


    private Animation createGrubAnim(Image img1, Image img2, Image img3, Image img4) { 
        Animation anim = new Animation();
        anim.addFrame(img1, 125);
        anim.addFrame(img2, 125);
        anim.addFrame(img3, 125);
        anim.addFrame(img4, 125);
        return anim;
    }

    private Animation createBossAnim(Image img1, Image img2, Image img3, Image img4) { 
        Animation anim = new Animation();
        anim.addFrame(img1, 125);
        anim.addFrame(img2, 125);
        anim.addFrame(img3, 125);
        anim.addFrame(img4, 125);
        return anim;
    }

    private void loadPowerUpSprites() {
        // create "goal" sprite
        Animation anim = new Animation();
        anim.addFrame(loadImage("heart1.png"), 150);
        anim.addFrame(loadImage("heart2.png"), 150);
        anim.addFrame(loadImage("heart3.png"), 150);
        anim.addFrame(loadImage("heart2.png"), 150);
        goalSprite = new PowerUp.Goal(anim);

        // create "star" sprite
        anim = new Animation();
        anim.addFrame(loadImage("star1.png"), 100);
        anim.addFrame(loadImage("star2.png"), 100);
        anim.addFrame(loadImage("star3.png"), 100);
        anim.addFrame(loadImage("star4.png"), 100);
        coinSprite = new PowerUp.Star(anim);

        // create "music" sprite
        anim = new Animation();
        anim.addFrame(loadImage("music1.png"), 150);
        anim.addFrame(loadImage("music2.png"), 150);
        anim.addFrame(loadImage("music3.png"), 150);
        anim.addFrame(loadImage("music2.png"), 150);
        musicSprite = new PowerUp.Music(anim);
        
    }

}
