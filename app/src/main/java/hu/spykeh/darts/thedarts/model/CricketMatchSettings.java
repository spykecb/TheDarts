package hu.spykeh.darts.thedarts.model;

/**
 * Created by spykeh on 2017. 10. 22..
 */

public class CricketMatchSettings extends MatchSettings {
    public enum EndsWhen{
        REACHED_MAX_ROUND,
        ALL_CLOSED
    }

    private EndsWhen endsWhen;
    private int maxRound;

    public CricketMatchSettings() {
        setMaxRound(20);
        setEndsWhen(EndsWhen.REACHED_MAX_ROUND);
    }

    public EndsWhen getEndsWhen() {
        return endsWhen;
    }

    public void setEndsWhen(EndsWhen endsWhen) {
        this.endsWhen = endsWhen;
    }

    public void setMaxRound(int maxRound) {
        this.maxRound = maxRound;
    }

    public int getMaxRound() {
        return maxRound;
    }
}
