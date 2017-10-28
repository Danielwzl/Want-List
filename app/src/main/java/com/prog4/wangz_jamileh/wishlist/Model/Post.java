package com.prog4.wangz_jamileh.wishlist.Model;


import android.graphics.Bitmap;

import java.io.Serializable;

public class Post implements Serializable {
    private String name, String,
            desc, updateAt;

    private int desire, cost;


    private boolean marked;
    private Bitmap image;
    private String id;

    public Post() {

    }

    public Post(String id, String name, String desc, int desire, int cost, boolean marked, Bitmap image, String updateAt) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.desire = desire;
        this.cost = cost;
        this.marked = marked;
        this.image = image;
        this.updateAt = updateAt;
    }

    public java.lang.String getId() {
        return id;
    }

    public void setId(java.lang.String id) {
        this.id = id;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getString() {
        return String;
    }

    public void setString(java.lang.String string) {
        String = string;
    }

    public java.lang.String getDesc() {
        return desc;
    }

    public void setDesc(java.lang.String desc) {
        this.desc = desc;
    }

    public java.lang.String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(java.lang.String updateAt) {
        this.updateAt = updateAt;
    }

    public int getDesire() {
        return desire;
    }

    public void setDesire(int desire) {
        this.desire = desire;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}

