package hu.spykeh.darts.thedarts.model;

import java.util.ArrayList;

/**
 * Created by spykeh on 2017. 10. 22..
 */

public class X01Match extends Match {
    private int startingScore;

    public X01Match(MatchSettings settings, ArrayList<Player> players){
        super(settings, players);
    }

    public int getStartingScore() {
        return startingScore;
    }

    public void setStartingScore(int startingScore) {
        this.startingScore = startingScore;
    }
}
