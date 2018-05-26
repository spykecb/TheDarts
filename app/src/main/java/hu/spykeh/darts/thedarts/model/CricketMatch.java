package hu.spykeh.darts.thedarts.model;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by spykeh on 2017. 10. 22..
 */

public class CricketMatch extends Match {

    @JsonIgnore
    private Team teamToThrow;
    @JsonIgnore
    private Player playerTothrow;
    @JsonIgnore
    private int roundNumber;
    private int shotNum;
    @JsonIgnore
    private ArrayList<CricketSectionStatus> scoreBoard;
    @JsonIgnore
    private HashMap<Integer, ArrayList<CricketSectionStatus>> sbHistory;

    public CricketMatch(CricketMatchSettings settings, ArrayList<Player> players){
        super(settings, players);
    }

    public void initBoard(){
        scoreBoard = new ArrayList<>();
        List<Integer> sections = new ArrayList<>();
        sections.add(20);
        sections.add(19);
        sections.add(18);
        sections.add(17);
        sections.add(16);
        sections.add(15);
        addSections(sections);
        scoreBoard.add(new CricketSectionStatus(25, 0, 0));
        addSbToSbHistory(0, copyBoard(scoreBoard));
        teamToThrow = getTeams().get(0);
        playerTothrow = teamToThrow.getPlayers().get(0);
        roundNumber = 0;
    }

    public void initBoard(List<Integer> sections){
        scoreBoard = new ArrayList<>();
        addSections(sections);
        scoreBoard.add(new CricketSectionStatus(25, 0, 0));
        addSbToSbHistory(0, copyBoard(scoreBoard));
        teamToThrow = getTeams().get(0);
        playerTothrow = teamToThrow.getPlayers().get(0);
        roundNumber = 0;
    }

    private void addSections(List<Integer> sections){
        for (int i = 0; i < 6; i++) {
            scoreBoard.add(new CricketSectionStatus(sections.get(i), 0, 0));
        }
    }

    public int getType(){
        return 1;
    }

    private ArrayList<CricketSectionStatus> copyBoard(ArrayList<CricketSectionStatus> src){
        ArrayList<CricketSectionStatus> dst = new ArrayList<>();
        for(CricketSectionStatus stat : src){
            dst.add(new CricketSectionStatus(stat.getSection(), stat.getScore1(), stat.getScore2()));
        }
        return dst;
    }

    public int getTeamScore(Team.TeamColor color){
        ArrayList<Shot> shots = new ArrayList<>();
        for(Player p : getPlayersOfTeam(color)) {
            if(p.getShots() != null) {
                shots.addAll(p.getShots());
            }
        }
        if(shots.size() == 0){
            return 0;
        }
        Collections.sort(shots, new Comparator<Shot>() {
            @Override
            public int compare(Shot shot1, Shot shot2) {
                Integer roundNum1 = shot1.getRoundNumber();
                Integer roundNum2 = shot1.getRoundNumber();
                return roundNum1.compareTo(roundNum2);
            }
        });
        int score = 0;
        for(CricketSectionStatus stat : scoreBoard){
            int timesHit;
            if(color == Team.TeamColor.RED){
                timesHit = stat.getScore1();
            }else{
                timesHit = stat.getScore2();
            }

            if(timesHit > 3){
                score += stat.getSection() * (timesHit - 3);
            }
        }
        return score;

    }

    public Team getTeam(Team.TeamColor color){
        for(Team t: getTeams()){
            if(t.getColor().equals(color)){
                return t;
            }
        }
        return null;

    }

    public CricketSectionStatus getStatusOfSection(int section){
        for(CricketSectionStatus secStat : scoreBoard){
            if(secStat.getSection() == section){
                return secStat;
            }
        }
        return new CricketSectionStatus(section, 0, 0);
    }

    public void hitSection(int section, int amount){
        for(CricketSectionStatus secStat : scoreBoard){
            if(secStat.getSection() == section){
                if(getStatusOfSection(section).getScore1() >= 3 && getStatusOfSection(section).getScore2() >= 3){
                    continue;
                }
                if(teamToThrow.getColor() == Team.TeamColor.RED) {
                    secStat.setScore1(secStat.getScore1() + amount);
                    Log.d("myTag", section + " increased to " + secStat.getScore1());
                }else if (teamToThrow.getColor() == Team.TeamColor.BLUE){
                    secStat.setScore2(secStat.getScore2() + amount);
                    Log.d("myTag", section + " increased to " + secStat.getScore2());
                }
            }
        }
    }

    public ArrayList<CricketSectionStatus> getScoreBoard() {
        return scoreBoard;
    }

    public void revertScoreBoard(){
        scoreBoard = copyBoard(sbHistory.get(shotNum));
    }

    public void undoScoreBoard(){
        if(shotNum > 0){
            scoreBoard = copyBoard(sbHistory.get(shotNum - 1));
            sbHistory.remove(sbHistory.get(shotNum));
            pickPrevPlayerToThrow();
            playerTothrow.getShots().remove(playerTothrow.getShots().size() - 1);
            shotNum--;

        }
    }

    public Team getWinner(){
        boolean allClosed1 = true;
        boolean allClosed2 = true;
        for(CricketSectionStatus stat : getScoreBoard()){
            if(stat.getScore1() <  3){
                allClosed1 = false;
            }
            if(stat.getScore2() <  3){
                allClosed2 = false;
            }
        }
        if(roundNumber >= 20){
            if(getTeamScore(Team.TeamColor.RED) > getTeamScore(Team.TeamColor.BLUE)){
                return getTeam(Team.TeamColor.RED);
            }else if(getTeamScore(Team.TeamColor.RED) < getTeamScore(Team.TeamColor.BLUE)){
                return getTeam(Team.TeamColor.BLUE);
            }else{
                //TODO:draw
                return null;
            }
        }
        if(allClosed1 && getTeamScore(Team.TeamColor.RED) > getTeamScore(Team.TeamColor.BLUE)){
            return getTeam(Team.TeamColor.RED);
        }else if(allClosed1 && !allClosed2 &&
                getTeamScore(Team.TeamColor.RED) == getTeamScore(Team.TeamColor.BLUE) ){
            return getTeam(Team.TeamColor.RED);
        }
        if(allClosed2 && getTeamScore(Team.TeamColor.BLUE) > getTeamScore(Team.TeamColor.RED)){
            return getTeam(Team.TeamColor.BLUE);
        }else if(allClosed2 && !allClosed1 &&
                getTeamScore(Team.TeamColor.RED) == getTeamScore(Team.TeamColor.BLUE) ){
            return getTeam(Team.TeamColor.BLUE);
        }
        return null;
    }

    public void addSbToSbHistory(int round, ArrayList<CricketSectionStatus> sb){
        if(sbHistory == null){
            sbHistory = new HashMap<>();
        }
        sbHistory.put(round, copyBoard(sb));
    }

    public void addShot(Player p, Shot shot){
        p.addShot(shot);
        for(int i = 0; i <this.scoreBoard.size() ; i++){
            this.scoreBoard.get(i).setCurRoundThrowCount(0);
        }
        addSbToSbHistory(++shotNum, scoreBoard);
    }

    public void pickNextPlayerToThrow(){
        if(getTeams().get(0) == teamToThrow){
            teamToThrow = getTeams().get(1);
            playerTothrow = teamToThrow.getPlayers().get(roundNumber % teamToThrow.getPlayers().size());
        }else{
            roundNumber++;
            teamToThrow = getTeams().get(0);
            playerTothrow = teamToThrow.getPlayers().get(roundNumber % teamToThrow.getPlayers().size());
        }
    }

    private void pickPrevPlayerToThrow(){
        if(getTeams().get(0) == teamToThrow){
            roundNumber--;
            teamToThrow = getTeams().get(1);
            playerTothrow = teamToThrow.getPlayers().get(roundNumber % teamToThrow.getPlayers().size());
        }else{
            teamToThrow = getTeams().get(0);
            playerTothrow = teamToThrow.getPlayers().get(roundNumber % teamToThrow.getPlayers().size());
        }
    }

    public Player getPlayerTothrow() {
        return playerTothrow;
    }


    public Team getTeamToThrow() {
        return teamToThrow;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

}
