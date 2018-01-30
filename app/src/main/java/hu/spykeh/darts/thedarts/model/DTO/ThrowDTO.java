package hu.spykeh.darts.thedarts.model.DTO;

import hu.spykeh.darts.thedarts.model.Throw;

/**
 * Created by spykeh on 2018. 01. 22..
 */

public class ThrowDTO {
    private int score;
    private int round;
    private int playerid;

    public ThrowDTO(Throw throwObj){
        setScore(throwObj.getScore());
        setRound(throwObj.getRoundNumber());
        setPlayerid(throwObj.getPlayerId());
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getPlayerid() {
        return playerid;
    }

    public void setPlayerid(int playerid) {
        this.playerid = playerid;
    }
}
