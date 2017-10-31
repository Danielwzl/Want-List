package com.prog4.wangz_jamileh.wishlist.Model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.prog4.wangz_jamileh.wishlist.BR;

public class Post extends BaseObservable {
    private String name,
            desc, updateAt;

    private float desire, cost;

    private boolean marked;
    private Bitmap image;
    private String id;
    private Drawable imageDrawable;

    public Post(String id, String name, String desc, float desire, float cost, boolean marked, Bitmap image, String updateAt) {
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

    @Bindable
    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public java.lang.String getDesc() {
        return desc;
    }

    public void setDesc(java.lang.String desc) {
        this.desc = desc;
        notifyPropertyChanged(BR.desc);
    }

    @Bindable
    public java.lang.String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(java.lang.String updateAt) {
        this.updateAt = updateAt;
        notifyPropertyChanged(BR.updateAt);
    }

    @Bindable
    public float getDesire() {
        return desire;
    }

    public void setDesire(float desire) {
        this.desire = desire;
        notifyPropertyChanged(BR.desire);
    }

    @Bindable
    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
        notifyPropertyChanged(BR.cost);
    }

    @Bindable
    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
        notifyPropertyChanged(BR.marked);
    }

    @Bindable
    public Bitmap getImage() {
        return image;
    }


    public void setImage(Bitmap image) {
        this.image = image;
        notifyPropertyChanged(BR.image);
    }

}

