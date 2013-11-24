/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brackeen.javagamebook.tilegame;
import java.awt.*;
import java.util.Iterator;
import com.brackeen.javagamebook.tilegame.*;

/**
 *
 * @author moni
 */
public class score {
    private int points = 0;
    public void score(){
        points = 0;
    }
    
    public int getScore(){
        return points;
    }
    public void properSetScore(int score){
        points = score;
    }
    public void setScore(int badguy){
        if(badguy == 1){
            points += 5;
        }
        else if(badguy == 2){
            points += 10;
        }
    }
    
    public void setScoreNewMap(int Map){
        if(Map == 1){
            points += 1000;
        }
        else {
            points += 100;
        }
    }
    
}
