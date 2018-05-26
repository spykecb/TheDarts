package hu.spykeh.darts.thedarts.model;

import java.io.Serializable;

/**
 * Created by spykeh on 2017. 10. 22..
 */

public class Throw implements Serializable{
    private Player player;
    private int score;
    private int multiplier;
    private int roundNumber;
    public Throw(Player player, int score){
        this.player = player;
        this.score = score;
    }
    public Throw(Player player, int score, int multiplier){
        this.player = player;
        this.score = score;
        this.multiplier = multiplier;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPlayerId(){
        return player.getId();
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }
}
