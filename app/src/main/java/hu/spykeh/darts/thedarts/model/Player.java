package hu.spykeh.darts.thedarts.model;

import java.io.Serializable;

/**
 * Created by spykeh on 2017. 10. 22..
 */

public class Player implements Serializable{
    int id;
    private String name;
    private float mpr;

    public Player(String name){
        this.name = name;
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
        return mpr;
    }

    public void setMpr(float mpr) {
        this.mpr = mpr;
    }
}
