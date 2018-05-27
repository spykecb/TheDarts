package hu.spykeh.darts.thedarts.model;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import hu.spykeh.darts.thedarts.model.DTO.ThrowDTO;

/**
 * Created by spykeh on 2017. 10. 22..
 */

public class Match implements Serializable{
    private int id;
    protected ArrayList<Player> players;
    protected ArrayList<Team> teams;

    @JsonIgnore
    protected MatchSettings settings;

    public Match(MatchSettings settings, ArrayList<Player> players) {
        this.settings = settings;
        setPlayers(players);
        String playerList = "";
        Collections.shuffle(players, new Random(System.nanoTime()));
        Team team1 = new Team(Team.TeamColor.RED);
        Team team2 = new Team(Team.TeamColor.BLUE);
        if(players.size() >= 2) {
            for (int i = 0; i < players.size(); i++) {
                playerList += players.get(i).getName() + ", ";
            }
            playerList = playerList.substring(0, playerList.length() - 2);
            Log.d("myTag", "Match started with players: " + playerList);
            int i = 0;
            for (i = 0; i < players.size() / 2; i++) {
                team1.addPlayer(players.get(i));
                Log.d("myTag", players.get(i).getName() + " added to team 1");
            }

            for (; i < players.size(); i++) {
                team2.addPlayer(players.get(i));
                Log.d("myTag", players.get(i).getName() + " added to team 2");
            }

        }
        teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
    }

    public Match(MatchSettings settings, Team team1, Team team2) {
        this.settings = settings;
        team1.setColor(Team.TeamColor.RED);
        team2.setColor(Team.TeamColor.BLUE);
        String playerList = "";
        players = new ArrayList<>();
        players.addAll(team1.getPlayers());
        players.addAll(team2.getPlayers());
        setPlayers(players);
        if(players.size() >= 2) {
            for (int i = 0; i < players.size(); i++) {
                playerList += players.get(i).getName() + ", ";
            }
            playerList = playerList.substring(0, playerList.length() - 2);
            Log.d("myTag", "Match started with players: " + playerList);
        }
        teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
    }

    public void addPlayer(Player p ){
        if(this.players == null){
            this.players = new ArrayList<>();
        }
        this.players.add(p);
    }

    public void addTeam(Team t){
        if(this.teams == null){
            this.teams = new ArrayList<>();
        }
        this.teams.add(t);
        for(Player player : t.getPlayers()) {
            addPlayer(player);
        }
    }



    public MatchSettings getSettings() {
        return settings;
    }

    public void setSettings(MatchSettings settings) {
        this.settings = settings;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public Team getTeamOfPlayer(Player player){
        for(Team team: getTeams()){
            if(team.getPlayers().contains(player)){
                return team;
            }
        }
        return null;
    }

    public void movePlayerToTeam(Player p, Team.TeamColor color){
        if(color == Team.TeamColor.RED){
            teams.get(1).getPlayers().remove(p);
            teams.get(0).addPlayer(p);
        }else{
            teams.get(0).getPlayers().remove(p);
            teams.get(1).addPlayer(p);
        }
    }

    public ArrayList<Player> getPlayersOfTeam(Team.TeamColor color){
        ArrayList<Player> players = new ArrayList<Player>();
        for(Player p : getPlayers()){
            if(getTeamOfPlayer(p).getColor() == color){
                players.add(p);
            }
        }
        return players;
    }

    public ArrayList<Shot> getShotsOfTeam(Team.TeamColor color){
        ArrayList<Shot> shots = new ArrayList<>();
        for(Player p : getPlayersOfTeam(color)){
            if(p.getShots() == null){
                return shots;
            }
            for(Shot s: p.getShots()){
                if(getTeamOfPlayer(s.getPlayer()).getColor() == color){
                    shots.add(s);
                }
            }
        }
        Collections.sort(shots, new Comparator<Shot>() {
            public int compare(Shot s1, Shot s2){
                return Integer.valueOf(s1.getRoundNumber()).compareTo(Integer.valueOf(s2.getRoundNumber()));
            }
        });
        return shots;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
