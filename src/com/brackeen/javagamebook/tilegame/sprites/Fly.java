package com.brackeen.javagamebook.tilegame.sprites;

import com.brackeen.javagamebook.graphics.Animation;

/**
    A Fly is a Creature that fly slowly in the air.
*/
public class Fly extends Creature {
    private int type = 2;

    public Fly(Animation left, Animation right, Animation deadLeft, Animation deadRight)
    {
        super(left, right, deadLeft, deadRight);
    }


    public float getMaxSpeed() {
        return 0.1f;
    }


    public boolean isFlying() {
        return isAlive();
    }
    
    public int getType(){
        return type;
    }

}
