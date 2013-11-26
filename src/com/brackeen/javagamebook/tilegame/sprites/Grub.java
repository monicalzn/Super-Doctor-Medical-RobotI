package com.brackeen.javagamebook.tilegame.sprites;

import com.brackeen.javagamebook.graphics.Animation;

/**
    A Grub is a Creature that moves slowly on the ground.
*/
public class Grub extends Creature {
    public static int type = 1;
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
    * @param type
    */
    public Grub(Animation left, Animation right,
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
        return 0.05f;
    }

    public int getType(){
        return type;
    }
}
