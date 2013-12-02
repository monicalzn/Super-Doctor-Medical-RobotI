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
    public void setScore(int badguy, int map){
        if(map == 2){
            if(badguy == 1){
                points += 3;
            }
            else if(badguy == 2){
                points += 5;
            }else if(badguy == 3){
            points += 1000;
            }
        }else if(map == 3){
            if(badguy == 1){
                points += 6;
            }
            else if(badguy == 2){
                points += 10;
            }else if(badguy == 4){
            points += 1500;
            }
        }
        else if(map == 4){
            if(badguy == 1){
                points += 9;
            }
            else if(badguy == 2){
                points += 15;
            }else if(badguy == 5){
            points += 2000;
            }
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
    
    public void setScoreUp(int score){
        points += score;
    }
    
}
