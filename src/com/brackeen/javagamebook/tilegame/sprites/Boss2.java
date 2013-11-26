package com.brackeen.javagamebook.tilegame.sprites;

import com.brackeen.javagamebook.graphics.Animation;

/**
 *
 * @author moni
 */
public class Boss2 extends Creature {
    private int type = 4;
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
    public Boss2(Animation left, Animation right,
        Animation deadLeft, Animation deadRight, Animation standingLeft, Animation standingRight,
        Animation jumpingLeft, Animation jumpingRight)
    {
        super(left, right, deadLeft, deadRight, standingLeft, standingRight, jumpingLeft, jumpingRight);
    }
    
    /**
* Get Max Speed
* @return float
*/

    public float getMaxSpeed() {
        return 0.1f;
}

    /**
* Is it alive/flying
* @return boolean
*/

    public boolean isFlying() {
        return isAlive();
    }
    
    public int getType(){
        return type;
    }

}
