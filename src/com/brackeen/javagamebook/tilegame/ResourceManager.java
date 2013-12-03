package com.brackeen.javagamebook.tilegame;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;

import com.brackeen.javagamebook.graphics.*;
import static com.brackeen.javagamebook.tilegame.ResourceManager.getMirrorImage;
import static com.brackeen.javagamebook.tilegame.ResourceManager.loadImage;
import com.brackeen.javagamebook.tilegame.sprites.*;
import java.net.URL;


/**
    The ResourceManager class loads and manages tile Images and
    "host" Sprites used in the game. Game Sprites are cloned from
    "host" Sprites.
*/
public class ResourceManager {

    private ArrayList tiles;
    private int currentMap;
    private static GraphicsConfiguration gc;

    // host sprites used for cloning
    private Sprite playerSprite;
    private Sprite shipSprite;
    private Sprite playerBusterSprite;
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
    private Bullet bulletSprite;

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
    
    public int getCurrentM(){
        return currentMap;
    }


    /**
        Gets an image from the images/ directory.
    */
    public static Image loadImage(String name) {
        String filename = "/images/" + name;
        URL urlImg = ResourceManager.class.getResource(filename);
        return new ImageIcon(urlImg).getImage();
    }

    /**
* Obtain mirror image
*
* @param image image to be mirror
* @return Image
*/

    public static Image getMirrorImage(Image image) {
        return getScaledImage(image, -1, 1);
    }

    /**
* Obtain Flipped image
*
* @param image
* @return
*/

    public Image getFlippedImage(Image image) {
        return getScaledImage(image, 1, -1);
    }

    /**
* Transform the Image
*
* @param image
* @param x
* @param y
* @return Image
*/

    private static Image getScaledImage(Image image, float x, float y) {

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

    /**
* Load Next Map
*
* @return TileMap
*/

    public TileMap loadNextMap() {
        TileMap map = null;
        while (map == null) {
            currentMap++;
            try {
                map = loadMap(
                    "/maps/map" + currentMap + ".txt");
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
  /**
* Reload Map
*
* @return Tile Map
*/

   public TileMap loadNextMapSpecial(int Cmap) {
        TileMap map = null;
        while (map == null) {
            currentMap = Cmap;
            try {
                map = loadMap(
                    "/maps/map" + currentMap + ".txt");
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

    /**
* Reload Map
*
* @return Tile Map
*/

    public TileMap reloadMap() {
        try {
            return loadMap("/maps/map" + currentMap + ".txt");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
* Load Map
*
* @param filename
* @return TileMap
* @throws IOException
*/

    private TileMap loadMap(String filename)
        throws IOException
    {
        ArrayList lines = new ArrayList();
        int width = 0;
        int height = 0;
        InputStream ipStrm = ResourceManager.class.getResourceAsStream(filename);

        InputStreamReader ipStrmRdr = new InputStreamReader(ipStrm);
        BufferedReader reader = new BufferedReader(ipStrmRdr);
            // read every line in the text file into the list
        //BufferedReader reader = new BufferedReader(
        // new FileReader(urlMap.toString()));
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
        Sprite playerBuster = (Sprite)playerBusterSprite.clone();
        playerBuster.setX(0);
        playerBuster.setY(0);
        Sprite ship = (Sprite)shipSprite.clone();
        ship.setX(0);
        ship.setY(500);
        newMap.setPlayer(player);
        newMap.setPlayerBuster(playerBuster);
        newMap.setShip(ship);

        return newMap;
    }

    /**
* Add Sprite
*
* @param map
* @param hostSprite
* @param tileX
* @param tileY
*/

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
            URL urlImg = ResourceManager.class.getResource("/images/" + name);
            if (urlImg==null){
                break;
            }
            tiles.add(loadImage(name));
            ch++;
        }
    }


    public void loadCreatureSprites() {

        Image[][] images = new Image[8][];

        // load left-facing images
        images[0] = new Image[] {
            loadImage("Rocket-01.png"),
            loadImage("Rocket-02.png"),
            loadImage("Rocket-03.png"),
            loadImage("Rocket-04.png"), 
            //4 al 13 left ACTIVATED
            loadImage("robot13buster.png"),
            loadImage("robot14buster.png"),
            loadImage("robot15buster.png"),
            loadImage("robot16buster.png"),
            loadImage("robot17buster.png"),
            loadImage("robot18buster.png"),
            loadImage("robot19buster.png"),
            loadImage("robot20buster.png"),
            loadImage("robot21buster.png"),
            loadImage("robot22buster.png"),
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
            loadImage("Boss1-01.png"),
            //25
            loadImage("robot23buster.png"),
            loadImage("robot24buster.png"),
            //27 robot ACTIVATED buster running 
            loadImage("robotbuster.png"),
            loadImage("robot2buster.png"),
            loadImage("robot3buster.png"),
            loadImage("robot4buster.png"),
            loadImage("robot5buster.png"),
            loadImage("robot6buster.png"),
            loadImage("robot7buster.png"),
            loadImage("robot8buster.png"),
            loadImage("robot9buster.png"),
            loadImage("robot10buster.png"),
            loadImage("robot11buster.png"),
            loadImage("robot12buster.png"),
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
            //53
            loadImage("GM3_12.png"),
            loadImage("GM3_12.png"),
            loadImage("GM3_12.png"),
            loadImage("GM3_12.png"),
            //57            parado RIGHT NO BUSTER
            loadImage("robotparado.png"),
            loadImage("robotparado2.png"),
            // 59 LEFT NO BUSTER            
            loadImage("robot13.png"),
            loadImage("robot14.png"),
            loadImage("robot15.png"),            
            loadImage("robot16.png"),
            loadImage("robot17.png"),
            loadImage("robot18.png"),
            loadImage("robot19.png"),
            loadImage("robot20.png"),
            loadImage("robot21.png"),
            loadImage("robot22.png"),
            loadImage("robot23.png"),
            loadImage("robot24.png"),
            //71 RIGHT NO BUSTER
            loadImage("robot.png"),
            loadImage("robot2.png"),
            loadImage("robot3.png"),            
            loadImage("robot4.png"),
            loadImage("robot5.png"),
            loadImage("robot6.png"),
            loadImage("robot7.png"),
            loadImage("robot8.png"),
            loadImage("robot9.png"),
            loadImage("robot10.png"),
            loadImage("robot11.png"),
            loadImage("robot12.png"),
            //83 stand LEFT NO BUSTER 
            loadImage("robotparado3.png"),
            loadImage("robotparado4.png"),
            //85 jump LEFT NO BUSTER
            loadImage("robotsaltando7.png"),
            loadImage("robotsaltando8.png"),
            loadImage("robotsaltando9.png"),
            loadImage("robotsaltando10.png"),
            loadImage("robotsaltando11.png"),
            loadImage("robotsaltando12.png"),
            //91
            loadImage("robotsaltando.png"),
            loadImage("robotsaltando2.png"),
            loadImage("robotsaltando3.png"),
            loadImage("robotsaltan4.png"),
            loadImage("robotsaltando5.png"),
            loadImage("robotsaltando6.png"),
            //97 BOSS ONE
            loadImage("Boss1-02.png"),
            loadImage("Boss1-03.png"),
            loadImage("Boss1-04.png"),
            loadImage("Boss1-05.png"),
            loadImage("Boss1-06.png"),
            loadImage("Boss1-07.png"),
            loadImage("Boss1-08.png"),
            loadImage("Boss1-09.png"),
            //105 BOSS TWO
            loadImage("Boss2-01.png"),
            loadImage("Boss2-02.png"),
            loadImage("Boss2-06.png"),
            loadImage("Boss2-07.png"),
            //109 BOSS THREE
            loadImage("Boss3-01.png"),
            loadImage("Boss3-02.png"),
            //111 left STAND ACTIVATED
            loadImage("robotparadobuster3.png"),
            loadImage("robotparado2buster4.png"),
            //113 Right STAND ACTIVATED
            loadImage("robotparadobuster.png"),
            loadImage("robotparado2buster.png"),
            //115 left jump ACTIVATED
            loadImage("robotsaltandobuster7.png"),
            loadImage("robotsaltandobuster8.png"),
            loadImage("robotsaltandobuster9.png"),
            loadImage("robotsaltandobuster10.png"),
            loadImage("robotsaltandobuster11.png"),
            loadImage("robotsaltandobuster12.png"),
            //121 right jump ACTIVATED
            loadImage("robotsaltandobuster.png"),
            loadImage("robotsaltandobuster2.png"),
            loadImage("robotsaltandobuster3.png"),
            loadImage("robotsaltandobuster4.png"),
            loadImage("robotsaltandobuster5.png"),
            loadImage("robotsaltandobuster6.png"),
            
        };

        images[1] = new Image[images[0].length];
        images[2] = new Image[images[0].length];
        images[3] = new Image[images[0].length];
        images[4] = new Image[images[0].length];
        images[5] = new Image[images[0].length];
        images[6] = new Image[images[0].length];
        images[7] = new Image[images[0].length];
        for (int i=0; i<images[0].length; i++) {
            // right-facing images
            images[1][i] = getMirrorImage(images[0][i]);
            // left-facing "dead" images
            images[2][i] = getFlippedImage(images[0][i]);
            // right-facing "dead" images
            images[3][i] = getFlippedImage(images[1][i]);
            // jump
            images[4][i] = images[1][i];
            images[5][i] = images[1][i];
            images[6][i] = images[1][i];
            images[7][i] = images[1][i];
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
        Animation[] playerBusterAnim = new Animation[10];
        
        for (int i=0; i<8; i++) {
            shipAnim[i] = createPlayerAnim(images[i][0], images[i][1], images[i][2], images[i][3], 
                    images[i][0], images[i][1], images[i][2], images[i][3], images[i][0], images[i][1], images[i][2], images[i][3]);
            
            //first world bad guys
            flyAnim[i] = createFlyAnim(images[i][14], images[i][15], images[i][16], images[i][17], images[i][18], images[i][19]);
            grubAnim[i] = createGrubAnim(images[i][20], images[i][21], images[i][22], images[i][23]); 
            bossAnim[i] = createBossOneAnim(images[i][24], images[i][97], images[i][98], images[i][99], images[i][100], images[i][101],
                    images[i][102], images[i][103], images[i][104]);
            
            //second world bad guy
            FM2Anim[i] = createFlyAnim(images[i][39], images[i][40], images[i][39], images[i][40], images[i][39], images[i][40]);
            GM2Anim[i] = createGrubAnim(images[i][41], images[i][42], images[i][43], images[i][44]); 
            boss2Anim[i] = createBossAnim(images[i][105], images[i][105], images[i][106], images[i][106]);
            
            //third world bad guy
            FM3Anim[i] = createFlyAnim(images[i][45], images[i][46], images[i][45], images[i][46], images[i][45], images[i][46]);
            GM3Anim[i] = createPlayJumAnim(images[i][47], images[i][48], images[i][49], images[i][50], images[i][51], images[i][52]); 
            boss3Anim[i] = createBossAnim(images[i][109], images[i][109], images[i][110], images[i][110]);
        }
        
        for (int i=0; i<images[0].length; i++) {
            // right-facing images
            images[1][i] = getMirrorImage(images[1][i]);
            images[5][i] = getMirrorImage(images[5][i]);
            images[4][i] = getMirrorImage(images[4][i]);
        }
        //left-facing NO BUSTER
        playerAnim[0] = createPlayerAnim(images[0][59], images[0][60], images[0][61], images[0][62],
                    images[0][63], images[0][64], images[0][65], images[0][66], images[0][67], images[0][68], images[0][69], images[0][70]);
        //right-facing NO BUSTER
        playerAnim[1] = createPlayerAnim(images[1][71], images[1][72], images[1][73], images[1][74],
                    images[1][75], images[1][76], images[1][77], images[1][78], images[1][79], images[1][80], images[1][81], images[1][82]);
        //dead left NO BUSTER
        playerAnim[2] = createPlayerAnim(images[2][59], images[2][60], images[2][61], images[2][62],
                    images[2][63], images[2][64], images[2][65], images[2][66], images[2][67], images[2][68], images[2][69], images[2][70]);
        //dead right NO BUSTER
        playerAnim[3] = createPlayerAnim(images[3][71], images[3][72], images[3][73], images[3][74],
                    images[3][75], images[3][76], images[3][77], images[3][78], images[3][79], images[3][80], images[3][81], images[3][82]);
        //Stand Still left NO BUSTER   
        playerAnim[4] = createPlayerAnim(images[4][83], images[4][83], images[4][84], images[4][84],
                    images[4][83], images[4][83], images[4][84], images[4][84], images[4][83], images[4][83], images[4][84], images[4][84]);
        //Stand Still right NO BUSTER   
        playerAnim[5] = createPlayerAnim(images[5][57], images[5][57], images[5][58], images[5][58],
                    images[5][57], images[5][57], images[5][58], images[5][58], images[5][57], images[5][57], images[5][58], images[5][58]);
        //Jump left NO BUSTER
        playerAnim[6] = createPlayJumAnim(images[6][85], images[6][86], images[6][87], images[6][88],
                    images[6][89], images[6][90]);
        //Jump Right NO BUSTER
        playerAnim[7] = createPlayJumAnim(images[7][91], images[7][92], images[7][93], images[7][94],
                    images[7][95], images[7][96]);
        
       /* for (int i=0; i<images[0].length; i++) {
            // right-facing images
            images[4][i] = getMirrorImage(images[4][i]);
        }*/
        //left-facing ACTIVATED
        playerBusterAnim[0] = createPlayerAnim(images[0][4], images[0][5], images[0][6], images[0][7],
                    images[0][8], images[0][9], images[0][10], images[0][11], images[0][12], images[0][13], images[0][25], images[0][26]);
        //right-facing ACTIVATED
        playerBusterAnim[1] = createPlayerAnim(images[1][27], images[1][28], images[1][29], images[1][30],
                    images[1][31], images[1][32], images[1][33], images[1][34], images[1][35], images[1][36], images[1][37], images[1][38]);
        //dead left ACTIVATED
        playerBusterAnim[2] = createPlayerAnim(images[2][4], images[2][5], images[2][6], images[2][7],
                    images[2][8], images[2][9], images[2][10], images[2][11], images[2][12], images[2][13], images[2][25], images[2][26]);
        //dead right ACTIVATED
        playerBusterAnim[3] = createPlayerAnim(images[3][27], images[3][28], images[3][29], images[3][30],
                    images[3][31], images[3][32], images[3][33], images[3][34], images[3][35], images[3][36], images[3][37], images[3][38]);
        //Stand Still left ACTIVATED 
        playerBusterAnim[4] = createPlayerAnim(images[4][111], images[4][111], images[4][112], images[4][112],
                    images[4][111], images[4][111], images[4][112], images[4][112], images[4][111], images[4][111], images[4][112], images[4][112]);
        //Stand Still right ACTIVATED 
        playerBusterAnim[5] = createPlayerAnim(images[5][113], images[5][113], images[5][114], images[5][114],
                    images[5][113], images[5][113], images[5][114], images[5][114], images[5][113], images[5][113], images[5][114], images[5][114]);
        //Jump left ACTIVATED
        playerBusterAnim[6] = createPlayJumAnim(images[6][115], images[6][116], images[6][117], images[6][118],
                    images[6][119], images[6][120]);
        //Jump Right ACTIVATED
        playerBusterAnim[7] = createPlayJumAnim(images[7][121], images[7][122], images[7][123], images[7][124],
                    images[7][125], images[7][126]);
        
        for (int i=0; i<images[0].length; i++) {
            // right-facing images
            images[0][i] = getMirrorImage(images[0][i]);
        }
        boss3Anim[0] = createBossAnim(images[0][109], images[0][109], images[0][110], images[0][110]);
        
        // create creature sprites
        playerSprite = new Player(playerAnim[0], playerAnim[1], playerAnim[2], playerAnim[3], playerAnim[4], playerAnim[5], playerAnim[7], playerAnim[6]);
        playerBusterSprite = new Player(playerBusterAnim[0], playerBusterAnim[1], playerBusterAnim[2], playerBusterAnim[3], playerBusterAnim[4], 
                playerBusterAnim[5], playerBusterAnim[7], playerBusterAnim[6]);
        shipSprite = new Player(shipAnim[0], shipAnim[1], shipAnim[2], shipAnim[3], shipAnim[2], shipAnim[3], shipAnim[2], shipAnim[3]);
        
        FM1Sprite = new Fly(flyAnim[0], flyAnim[1], flyAnim[2], flyAnim[3], flyAnim[0], flyAnim[1], flyAnim[0], flyAnim[1]);
        GM1Sprite = new Grub(grubAnim[0], grubAnim[1], grubAnim[2], grubAnim[3], grubAnim[0], grubAnim[1], grubAnim[0], grubAnim[1]);
        boss1Sprite = new Boss1(bossAnim[0], bossAnim[1], bossAnim[2], bossAnim[3], bossAnim[0], bossAnim[1], bossAnim[0], bossAnim[1]);
        
        GM2Sprite = new Grub(GM2Anim[0], GM2Anim[1], GM2Anim[2], GM2Anim[3], GM2Anim[0], GM2Anim[1], GM2Anim[0], GM2Anim[1]);
        FM2Sprite = new Fly(FM2Anim[0], FM2Anim[1], FM2Anim[2], FM2Anim[3], FM2Anim[0], FM2Anim[1], FM2Anim[0], FM2Anim[1]);
        boss2Sprite = new Boss2(boss2Anim[0], boss2Anim[1], boss2Anim[2], boss2Anim[3], boss2Anim[0], boss2Anim[1], boss2Anim[0], boss2Anim[1]);
        
        GM3Sprite = new Grub(GM3Anim[0], GM3Anim[1], GM3Anim[2], GM3Anim[3], GM3Anim[0], GM3Anim[1], GM3Anim[0], GM3Anim[1]);
        FM3Sprite = new Fly(FM3Anim[0], FM3Anim[1], FM3Anim[2], FM3Anim[3], FM3Anim[0], FM3Anim[1], FM3Anim[0], FM3Anim[1]);
        boss3Sprite = new Boss3(boss3Anim[0], boss3Anim[0], boss3Anim[2], boss3Anim[3], boss3Anim[0], boss3Anim[1], boss3Anim[0], boss3Anim[1]);
   }


    private Animation createPlayerAnim(Image player1,
        Image player2, Image player3, Image player4, Image player5,
        Image player6, Image player7, Image player8, Image player9, Image player10, Image player11, Image player12)
    {
        Animation anim = new Animation();
        anim.addFrame(player1, 100);
        anim.addFrame(player2, 100);
        anim.addFrame(player3, 100);
        anim.addFrame(player4, 100);
        anim.addFrame(player5, 100);
        anim.addFrame(player6, 100);
        anim.addFrame(player7, 100);
        anim.addFrame(player8, 100);
        anim.addFrame(player9, 100);
        anim.addFrame(player10, 100);
        anim.addFrame(player11, 100);
        anim.addFrame(player12, 100);
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
    private Animation createBossOneAnim(Image img1, Image img2, Image img3, Image img4, Image img5, Image img6, Image img7, Image img8, Image img9) { 
        Animation anim = new Animation();
        anim.addFrame(img1, 300);
        anim.addFrame(img2, 300);
        anim.addFrame(img3, 300);
        anim.addFrame(img4, 300);
        anim.addFrame(img5, 300);
        anim.addFrame(img6, 300);
        anim.addFrame(img7, 300);
        anim.addFrame(img8, 300);
        anim.addFrame(img9, 300);
        anim.addFrame(img8, 300);
        anim.addFrame(img7, 300);
        anim.addFrame(img6, 300);
        anim.addFrame(img5, 300);
        anim.addFrame(img4, 300);
        anim.addFrame(img3, 300);
        anim.addFrame(img2, 300);
        anim.addFrame(img1, 300);
        return anim;
    }
    
    private Animation createPlayJumAnim(Image img1, Image img2, Image img3, Image img4, Image img5, Image img6) { 
        Animation anim = new Animation();
        anim.addFrame(img1, 125);
        anim.addFrame(img2, 125);
        anim.addFrame(img3, 125);
        anim.addFrame(img4, 125);
        anim.addFrame(img5, 125);
        anim.addFrame(img6, 125);
        return anim;
    }

    private void loadPowerUpSprites() {
        // create "goal" sprite
        Animation anim = new Animation();
        anim.addFrame(loadImage("buster.png"), 150);
        anim.addFrame(loadImage("buster.png"), 150);
        anim.addFrame(loadImage("buster.png"), 150);
        anim.addFrame(loadImage("buster.png"), 150);
        goalSprite = new PowerUp.Goal(anim);

        // create "star" sprite
        anim = new Animation();
        anim.addFrame(loadImage("star.png"), 100);
        anim.addFrame(loadImage("star.png"), 100);
        anim.addFrame(loadImage("star.png"), 100);
        coinSprite = new PowerUp.Star(anim);

        
    }

    public static Animation bulletAnimationLeft(){
        Animation anim = new Animation();
        
        anim.addFrame(getMirrorImage(loadImage("Disparo-01.png")), 50);
        anim.addFrame(getMirrorImage(loadImage("Disparo-02.png")), 50);
        anim.addFrame(getMirrorImage(loadImage("Disparo-03.png")), 50);
        anim.addFrame(getMirrorImage(loadImage("Disparo-04.png")), 50);
        
        return anim;
}
    public static Animation bulletAnimationRight(){
        Animation anim = new Animation();
        
        anim.addFrame((loadImage("Disparo-01.png")), 50);
        anim.addFrame((loadImage("Disparo-02.png")), 50);
        anim.addFrame((loadImage("Disparo-03.png")), 50);
        anim.addFrame((loadImage("Disparo-04.png")), 50);
        
        return anim;
    }
    
    public static Animation bulletAnimationBossOne(){
        Animation anim = new Animation();
        
        anim.addFrame(loadImage("Boss_1-D-1.png"), 50);
        anim.addFrame(loadImage("Boss_1-D-2.png"), 50);
        anim.addFrame(loadImage("Boss_1-D-3.png"), 50);
        anim.addFrame(loadImage("Boss_1-D-4.png"), 50);
        
        return anim;
    }

}
