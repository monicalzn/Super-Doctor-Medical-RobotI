package com.brackeen.javagamebook.tilegame.sprites;

import com.brackeen.javagamebook.tilegame.GameManager;
import com.brackeen.javagamebook.graphics.Animation;
import com.brackeen.javagamebook.tilegame.TileMapRenderer;
import java.awt.Color;
import java.awt.Graphics2D;
import com.brackeen.javagamebook.graphics.Sprite;

 
public class Bullet extends Sprite{
 
    private int r;
    
    private double ddx;
    private double ddy;
    private double rad;
    private double speed;
    private boolean live;
    private int timer = 0;
    private Color color1;
    
    //Constructor
    public Bullet(Animation anim, double angle, float x, float y){
        
        super(anim, x, y);
        /*
        while(x-GameManager.screen.getWidth() > 0){
            x -= GameManager.screen.getWidth(); 
        } */
    
        
    
        r=6;

        rad = Math.toRadians(angle);
        speed = 3;
        ddx = Math.cos(rad) * speed;
        ddy = Math.sin(rad) * speed;
    
        color1 = Color.BLUE;
        live=false;
         
    }
        
    public boolean updateBullet(long elapsedTime){
        this.setX(this.getX()+(float)ddx);
        this.setY(this.getY()+(float)ddy);
        
        timer++;
        if(timer > 1000){
            timer = 0;
            return true;
        }
        //anim.update(elapsedTime);
    
       /* if(this.getX() < -r || this.getX() > GameManager.screen.getWidth() - TileMapRenderer.offsetX + r ||
                this.getY() < -r || this.getY() > GameManager.screen.getHeight() + r){
            return true;
        } */
        return false;
    }
    
    public double getSpeed(){
        return this.speed;
    }
    public void setLive(boolean l){
        this.live = l;
    }
    
    public void draw(Graphics2D g){
        //g.setColor(color1);
        //g.fillOval((int)(x - r), (int)(y - r), 2 * r, 2 * r);
        g.drawImage(this.getImage(), Math.round(this.getX()), Math.round(this.getY()), null);
    }
    
}
