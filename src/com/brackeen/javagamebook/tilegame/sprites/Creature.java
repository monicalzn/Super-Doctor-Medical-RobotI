package com.brackeen.javagamebook.tilegame.sprites;

import java.lang.reflect.Constructor;
import com.brackeen.javagamebook.graphics.*;

/**
    A Creature is a Sprite that is affected by gravity and can
    die. It has four Animations: moving left, moving right,
    dying on the left, and dying on the right.
*/
public abstract class Creature extends Sprite {

    /**
        Amount of time to go from STATE_DYING to STATE_DEAD.
    */
    private static final int DIE_TIME = 100;

    public static final int STATE_NORMAL = 0;
    public static final int STATE_DYING = 1;
    public static final int STATE_DEAD = 2;

    private Animation left;
    private Animation right;
    private Animation deadLeft;
    private Animation deadRight;
    private Animation standingLeft;
    private Animation standingRight;
    private Animation jumpingLeft;
    private Animation jumpingRight;
    private int standing;
    private double state;
    private long stateTime;
    private int type = 0;
    
    private boolean fly = false;

    /**
* Creates a new Creature with the specified Animations.
* @param left
* @param right
* @param deadLeft
* @param deadRight
* @param standingLeft
* @param standingRight
* @param jumpingLeft
* @param jumpingRight
    */
    public Creature(Animation left, Animation right,
        Animation deadLeft, Animation deadRight, Animation standingLeft, Animation standingRight,
        Animation jumpingLeft, Animation jumpingRight)
    {
        super(right);
        this.left = left;
        this.right = right;
        this.deadLeft = deadLeft;
        this.deadRight = deadRight;
        this.standingLeft = standingLeft;
        this.standingRight = standingRight;
        this.jumpingLeft = jumpingLeft;
        this.jumpingRight = jumpingRight;
        state = STATE_NORMAL;
    }
    public Creature(Animation anim, float x, float y){
        super(anim, x, y);
    }

    /**
* Clone Creature
* @return Object
*/
    public Object clone() {
        // use reflection to create the correct subclass
        Constructor constructor = getClass().getConstructors()[0];
        try {
            return constructor.newInstance(new Object[] {
                (Animation)left.clone(),
                (Animation)right.clone(),
                (Animation)deadLeft.clone(),
                (Animation)deadRight.clone(),
                (Animation)standingLeft.clone(),
                (Animation)standingRight.clone(),
                (Animation)jumpingLeft.clone(),
                (Animation)jumpingRight.clone()
            });
        }
        catch (Exception ex) {
            // should never happen
            ex.printStackTrace();
            return null;
        }
    }


    /**
* Gets the maximum speed of this Creature.
* @return float
    */
    public float getMaxSpeed() {
        return 0;
    }


    /**
        Wakes up the creature when the Creature first appears
        on screen. Normally, the creature starts moving left.
    */
    public void wakeUp() {
        if (getState() < 1 && getVelocityX() == 0) {
            setVelocityX(-getMaxSpeed());
        }
    }


    /**
* Gets the state of this Creature. The state is either
* STATE_NORMAL, STATE_DYING, or STATE_DEAD.
* @return
    */
    public double getState() {
        return state;
    }


    /**
* Sets the state of this Creature to STATE_NORMAL,
* STATE_DYING, or STATE_DEAD.
* @param state
    */
    public void setState(double state) {
        if (this.state != state) {
            this.state = state;
            stateTime = 0;
            if (state >= STATE_DYING) {
                setVelocityX(0);
                setVelocityY(0);
            }
        }
    }
    
    public void setStateOne(int map) {
        if(map < 4){
            this.state += 1;
            stateTime = 0;
            if (state >= STATE_DYING) {
                setVelocityX(0);
                setVelocityY(0);
            }
        }
        else{
            this.state += .5;
            stateTime = 0;
            if (state >= STATE_DYING) {
                setVelocityX(0);
                setVelocityY(0);
            }
        }
    }
    
    /**
        Sets the state of this Creature to STATE_NORMAL,
        STATE_DYING, or STATE_DEAD.
    */
    public void setStateTwo(int map) {
        if(map < 3){
            if(this.type <= 2){
                this.state += .5;
                stateTime = 0;
                    if (state >= STATE_DYING) {
                        setVelocityX(0);
                        setVelocityY(0);
                    }
            } else if(this.type == 3){
                this.state += .1;
                stateTime = 0;
                    if (state >= STATE_DYING) {
                        setVelocityX(0);
                        setVelocityY(0);
                    }
            }
        }
        else{
            if(this.type <= 2){
            this.state += .4;
            stateTime = 0;
                if (state >= STATE_DYING) {
                    setVelocityX(0);
                    setVelocityY(0);
                }
            }else if(this.type == 4){
                this.state += .08;
                stateTime = 0;
                    if (state >= STATE_DYING) {
                        setVelocityX(0);
                        setVelocityY(0);
                    }
            }else if(this.type == 5){
                this.state += .05;
                stateTime = 0;
                    if (state >= STATE_DYING) {
                        setVelocityX(0);
                        setVelocityY(0);
                    }
            }
        }
    }
    
    /**
        Sets the state of this Creature to STATE_NORMAL,
        STATE_DYING, or STATE_DEAD.
    */
    public void setStateT() {
        stateTime = 0;
            if (state >= STATE_DYING) {
                setVelocityX(0);
                setVelocityY(0);
            }
    }
    /**
        Sets the state of this Creature.
    */
    public void HitTile() {
        this.state += 0.2;
        stateTime = 0;
        if (state >= 2) {
            state = STATE_DEAD;
            setVelocityX(0);
            setVelocityY(0);
        }
    }

    /**
* Checks if this creature is alive.
* @return boolean
    */
    public boolean isAlive() {
        return (state < STATE_DEAD);
    }


    /**
* Checks if this creature is flying.
* @return boolean
    */
    public boolean isFlying() {
        return fly;
    }
    
    public void setisFlying(boolean Fly){
        fly = Fly;
    }


    /**
        Called before update() if the creature collided with a
        tile horizontally.
    */
    public void collideHorizontal() {
        setVelocityX(-getVelocityX());
    }


    /**
        Called before update() if the creature collided with a
        tile vertically.
    */
    public void collideVertical() {
        setVelocityY(0);
    }

    public int getStanding(){
        return standing;
    }

    /**
* Updates the animaton for this creature.
* @param elapsedTime
    */
    public void update(long elapsedTime) {
        
        // select the correct Animation
        Animation newAnim = anim;
        if(Player.onGround == false && getVelocityX() < 0 ){
            newAnim = jumpingLeft;
            standing = 0;
        }else if(Player.onGround == false && getVelocityX() > 0){
            newAnim = jumpingRight;
            standing = 1;
        }else if(Player.onGround == false && getVelocityX() == 0 && standing == 0 && state < STATE_DEAD ){
            newAnim = jumpingLeft;
            standing = 0;
        }else if(Player.onGround == false && getVelocityX() == 0 && standing == 1 && state < STATE_DEAD ){
            newAnim = jumpingRight;
            standing = 1;
        }else if (getVelocityX() == 0 && standing == 0 ){
            newAnim = standingLeft;
        }else if (getVelocityX() == 0 && standing == 1 && state < STATE_DEAD){
            newAnim = standingRight;
        }
        else if (getVelocityX() < 0) {
            newAnim = left;
            standing = 0;
        }
        else if (getVelocityX() > 0) {
            newAnim = right;
            standing = 1;
        }
        if (state >= STATE_DYING && state >= STATE_DEAD && newAnim == left) {
            newAnim = deadLeft;
        }
        else if (state >= STATE_DYING && state >= STATE_DEAD && newAnim == right) {
            newAnim = deadRight;
        }

        // update the Animation
        if (anim != newAnim) {
            anim = newAnim;
            anim.start();
        }
        else {
            anim.update(elapsedTime);
        }

        // update to "dead" state
        stateTime += elapsedTime;
        if (state >= STATE_DYING && state <= STATE_DEAD && stateTime >= DIE_TIME) {
            setState(STATE_DEAD);
        }
    }

    public int getType(){
        return type;
    }
}
