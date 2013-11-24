/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brackeen.javagamebook.tilegame.sprites;
import java.awt.*;
import com.brackeen.javagamebook.graphics.Animation;
import com.brackeen.javagamebook.tilegame.sprites.*;

/**
 *
 * @author moni
 */
public class Boss1 extends Creature {
    private int timer;
    
    public Boss1(Animation left, Animation right, Animation deadLeft, Animation deadRight)
    {
        super(left, right, deadLeft, deadRight);
    }
    
    public float getMaxSpeed() {
        return 0f;
    }
    
    public void shoot(){
        timer +=1;
        if(timer == 10){
            
        }
    }

}
