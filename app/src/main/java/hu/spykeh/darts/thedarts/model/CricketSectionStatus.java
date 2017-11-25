package hu.spykeh.darts.thedarts.model;

import java.io.Serializable;

/**
 * Created by spykeh on 2017. 10. 22..
 */

public class CricketSectionStatus implements Serializable {

    private static final long serialVersionUID = 0L;

    private int score1;
    private int score2;
    private int section;
    private int curRoundThrowCount;

    public CricketSectionStatus(int section, int score1, int score2){
        this.section = section;
        this.score1 = score1;
        this.score2 = score2;
    }

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public int getCurRoundThrowCount() {
        return curRoundThrowCount;
    }

    public void setCurRoundThrowCount(int curRoundThrowCount) {
        this.curRoundThrowCount = curRoundThrowCount;
    }
}
