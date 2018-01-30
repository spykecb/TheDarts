package hu.spykeh.darts.thedarts.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by spykeh on 2017. 10. 22..
 */

public class Player implements Serializable{
    int id;
    private String name;
    private String username;
    private String qrcodeid;
    private int team;
    protected ArrayList<Shot> shots;

    public Player(){
        shots = new ArrayList<>();
    }

    public Player(String name){
        this();
        this.name = name;
    }

    public void addShot(Shot shot){
        if(this.shots == null){
            this.shots = new ArrayList<>();
        }
        shot.setPlayer(this);
        this.shots.add(shot);
    }

    public ArrayList<Shot> getShots() {
        return shots;
    }

    public ArrayList<Throw> getThrows(){
        ArrayList<Throw> t = new ArrayList<Throw>();
        for(Shot s : shots){
            t.addAll(s.getThrowList());
        }
        return t;
    }

    public void setShots(ArrayList<Shot> shots) {
        this.shots = shots;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getMpr() {
        int marks = 0;
        int q = 0;
        for(Shot s : getShots()){
            marks += s.getMarkCount();
            q++;
        }
        if(q > 0)
            return ((float)marks / (float)q);
        else
        return 0;

    }

    public String getQrcodeid() {
        return qrcodeid;
    }

    public void setQrcodeid(String qrcodeid) {
        this.qrcodeid = qrcodeid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public String toString(){
        return getName() + " " + getQrcodeid();
    }
}
