package com.example.stylishsloth.entities;

import java.io.Serializable;

public class Subscription implements Serializable {

    private String type;

    public Subscription(String id, String type, String topSize, String bottomSize, String footSize, String adress, String payed) {
        this.type = type;
        this.topSize = topSize;
        this.bottomSize = bottomSize;

        this.footSize = footSize;
        Adress = adress;
        Payed = payed;
        this.id = id;
    }

    private String topSize;
    private String bottomSize;
    private String Height;
    private String footSize;
    private String Adress;
    public String Payed;

    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTopSize() {
        return topSize;
    }

    public void setTopSize(String topSize) {
        this.topSize = topSize;
    }

    public String getBottomSize() {
        return bottomSize;
    }

    public void setBottomSize(String bottomSize) {
        this.bottomSize = bottomSize;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public String getFootSize() {
        return footSize;
    }

    public void setFootSize(String footSize) {
        this.footSize = footSize;
    }

    public boolean  isPayed() {
        if (Payed.equals("true"))
        return true;
        else return false;
    }

    public void setPayed(String payed) {
        Payed = payed;
    }

    public String getAdress() {
        return Adress;
    }

    public void setAdress(String adress) {
        Adress = adress;
    }

    public Subscription() {
    }

    public Subscription(String type, String topSize, String bottomSize, String footSize, String adress) {
        this.type = type;
        this.topSize = topSize;
        this.bottomSize = bottomSize;
        this.footSize = footSize;

    }
}
