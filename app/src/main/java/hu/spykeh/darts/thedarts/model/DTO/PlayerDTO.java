package hu.spykeh.darts.thedarts.model.DTO;

import java.util.ArrayList;

import hu.spykeh.darts.thedarts.model.Player;
import hu.spykeh.darts.thedarts.model.Shot;
import hu.spykeh.darts.thedarts.model.Throw;

/**
 * Created by spykeh on 2018. 01. 28..
 */

public class PlayerDTO {
    int id;
    private String name;
    private String username;
    private String qrcodeid;
    private int team;

    public PlayerDTO(){

    }

    public PlayerDTO(Player playerObj){
       setId(playerObj.getId());
       setName(playerObj.getName());
       setUsername(playerObj.getUsername());
       setQrcodeid(playerObj.getQrcodeid());
       setTeam(playerObj.getTeam());
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


}
