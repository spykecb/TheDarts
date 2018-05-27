package hu.spykeh.darts.thedarts.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by spykeh on 2017. 10. 22..
 */

public class Team implements Serializable{
    private List<Player> players;

    public enum TeamColor{
        RED,
        BLUE
    }

    private TeamColor color;

    public Team(TeamColor color){
        this.color = color;
        this.players = new ArrayList<>();
    }
    public Team(List<Player> players, TeamColor color)  {
        this.color = color;
        this.players = players;
    }

    public TeamColor getColor() {
        return color;
    }

    public void setColor(TeamColor color) {
        this.color = color;
    }

    public void addPlayer(Player player){
        if(players == null){
            players = new ArrayList<>();
        }
        this.players.add(player);
    }

    public List<Player> getPlayers() {
        if(players == null){
            players = new ArrayList<>();
        }
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @Override
    public String toString(){
        String s = "";
        for(Player p : getPlayers()){
            s += p.getName() + ", ";
        }
        s = s.substring(0, s.length() - 2);
        return s;
    }
}
