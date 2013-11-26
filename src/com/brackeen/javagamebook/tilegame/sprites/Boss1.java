package com.brackeen.javagamebook.tilegame.sprites;

import com.brackeen.javagamebook.graphics.Animation;

/**
 *
 * @author moni
 */
public class Boss1 extends Creature {
    private long timer;
    public static int type = 3;
    private static final float JUMP_SPEED = -.95f;

    public static boolean onGround;
    
    private boolean firing;
    private long bulletTimer;
    private long bulletDelay;

    /**
* Constructor
* @param left
* @param right
* @param deadLeft
* @param deadRight
* @param standingLeft
* @param standingRight
* @param jumpingLeft
* @param jumpingRight
*/
    
    public Boss1(Animation left, Animation right,
        Animation deadLeft, Animation deadRight, Animation standingLeft,
        Animation standingRight, Animation jumpingLeft, Animation jumpingRight)
    {
        super(left, right, deadLeft, deadRight, standingLeft, standingRight, jumpingLeft, jumpingRight);
        firing = true;
        bulletTimer = System.nanoTime();
        bulletDelay = 500;
        timer = 0;
    }
    
    /**
* Set Velocity after collide horizontal
*/

    public void collideHorizontal() {
        setVelocityX(0);
    }
    
            
    public void collideVertical() {
        // check if collided with ground
        if (getVelocityY() > 0) {
            onGround = true;
        }
        setVelocityY(0);
    }

    
    /**
* Start player
*/

    public void wakeUp() {
        // do nothing
    }


    public int getType(){
        return type;
    }

    /**
* Sets bullet Timer
* @param timer
*/
    public void setBulletTimer(long timer){
        this.bulletTimer = timer;
    }
    /**
* Get bullet Timer
* @return bullet timer
*/
    public long getBulletTimer(){
        return this.bulletTimer;
    }

    /**
* Get bullet Delay
* @return bullet timer
*/
    public long getBulletDelay(){
        return this.bulletDelay;
    }
    
    /**
* Is player firing?
* @return firing
*/
    public boolean isFiring(){
        return this.firing;
    }
    
    /**
* Makes the player fire
* @param fire
*/
    public void fire(boolean fire) {
        this.firing = fire;
    }

    /**
* Max Speed
* @return float
*/
    
    public float getMaxSpeed() {
        return 0.0f;
    }

}
