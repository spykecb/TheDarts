package hu.spykeh.darts.thedarts.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by spykeh on 2017. 10. 22..
 * A shot is always consisting of 3 throws
 */

public class Shot implements Serializable{
    private Player player;
    private List<Throw> throwList;
    private int roundNumber;

    public Shot(){

    }

    public Shot(List<Throw> throwList, int roundNumber){
        this.throwList = throwList;
        this.roundNumber = roundNumber;
        if(throwList.size()>0) {
            this.player = throwList.get(0).getPlayer();
        }
    }

    public List<Throw> getThrowList() {
        if(throwList == null){
            throwList = new ArrayList<>();
        }
        return throwList;
    }

    public void setThrowList(List<Throw> throwList) {
        this.throwList = throwList;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public void setPlayer(Player player ){
        this.player = player;
    }

    public Player getPlayer() {
        if(player == null){
            if(throwList.size() > 0)
                return throwList.get(0).getPlayer();
            else
                return null;
        }

        return player;
    }

    public int getScore(){
        int score = 0;
        for(Throw t: getThrowList()){
            score += t.getScore();
        }
        return score;
    }

    public int getMarkCount(){
        int count = 0;
        for(Throw t : getThrowList()){
            if(t.getScore() >= 15){
                count++;
            }
        }
        return count;
    }

    private HashMap<Integer, Integer> createHitHashMap(){
        HashMap<Integer, Integer> hits = new HashMap<>();
        for(Throw t : throwList){
            if(hits.get(t.getScore()) != null){
                hits.put(t.getScore(), hits.get(t.getScore()) + 1);
            }else{
                hits.put(t.getScore(), 1);
            }
        }
        return hits;
    }

    public String toString(){
        String str = "";
        for(Map.Entry<Integer, Integer> hit: createHitHashMap().entrySet()){
            str += hit.getKey() + "x" + hit.getValue() + " ";
        }
        return str;
    }
}
