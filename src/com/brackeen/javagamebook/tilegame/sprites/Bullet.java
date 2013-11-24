package com.brackeen.javagamebook.tilegame.sprites;

import com.brackeen.javagamebook.tilegame.GameManager;
import com.brackeen.javagamebook.graphics.Animation;

import com.brackeen.javagamebook.tilegame.*;

import com.brackeen.javagamebook.input.*;
import com.brackeen.javagamebook.test.GameCore;

import java.awt.*;
import javax.swing.ImageIcon;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.util.ArrayList;

import com.brackeen.javagamebook.graphics.*;
import com.brackeen.javagamebook.tilegame.sprites.*;
import java.awt.image.BufferedImage;

 
public class Bullet {
 
        // For creating new bullets.
    public long timeBetweenNewBullets = 10;
    public long timeOfLastCreatedBullet = 0;
    
    // Damage that is made to an enemy helicopter when it is hit with a bullet.
    public int damagePower = 20;
    
    // Position of the bullet on the screen. Must be of type double because movingXspeed and movingYspeed will not be a whole number.
    public double xCoordinate;
    public double yCoordinate;
    
    public double x;
    public double y;
    
    // Moving speed and direction.
    private double movingXspeed = 2;
    private double movingYspeed = 0;
    
    // Images of helicopter machine gun bullet. Image is loaded and set in Game class in LoadContent() method.
    public Image bulletImg;
    
    
    /**
     * Creates new machine gun bullet.
     * 
     * @param xCoordinate From which x coordinate was bullet fired?
     * @param yCoordinate From which y coordinate was bullet fired?
     * @param mousePosition Position of the mouse at the time of the shot.
     */
    public Bullet(int xCoordinate, int yCoordinate, int x)
    {
        this.xCoordinate = xCoordinate;
        this.y = this.yCoordinate = yCoordinate;
        this.x = x;
         
        
    }
  
    
    public void setBulIm(Image Bull){
        this.bulletImg = Bull;
    }
    /*
     * Checks if the bullet is left the screen.
     * 
     * @return true if the bullet left the screen, false otherwise.
     */
    
      public int getXS(){
        return (int)x;
    }
    
    public int getYS(){
        return (int)y;
    }
    
    /*
     * Moves the bullet.
     */
    public void Update()
    {
        xCoordinate += movingXspeed;
        x += movingXspeed;
    }
    public int getX(){
        return (int)xCoordinate;
    }
    
    public int getY(){
        return (int)yCoordinate;
    }
    /**
     * Draws the bullet to the screen.
     * 
     * @param g2d Graphics2D
     */
    public void DrawB(Graphics2D g2d)
    {
        g2d.drawImage(bulletImg, (int)xCoordinate, (int)yCoordinate, null);
    }
}
