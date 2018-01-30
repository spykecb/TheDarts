package hu.spykeh.darts.thedarts.model.DTO;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import hu.spykeh.darts.thedarts.model.Match;
import hu.spykeh.darts.thedarts.model.MatchSettings;
import hu.spykeh.darts.thedarts.model.Player;
import hu.spykeh.darts.thedarts.model.Shot;
import hu.spykeh.darts.thedarts.model.Team;
import hu.spykeh.darts.thedarts.model.Throw;

/**
 * Created by spykeh on 2018. 01. 22..
 */

public class MatchDTO {
    private int id;
    private ArrayList<PlayerDTO> players;
    private int type;
    protected ArrayList<ThrowDTO> throwList;
    public MatchDTO(Match match){
        type = 1;
        setId(match.getId());
        players = new ArrayList<>();
        throwList = new ArrayList<>();
        for(Player p : match.getPlayers()){
            players.add(new PlayerDTO(p));
            for(Shot s : p.getShots()){
                for(Throw t : s.getThrowList()){
                    throwList.add(new ThrowDTO(t));
                }
            }
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<PlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<PlayerDTO> players) {
        this.players = players;
    }

    public int getType(){
        return 1;
    }
}
