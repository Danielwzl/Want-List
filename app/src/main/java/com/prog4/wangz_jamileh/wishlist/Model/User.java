package com.prog4.wangz_jamileh.wishlist.Model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.prog4.wangz_jamileh.wishlist.BR;
import com.prog4.wangz_jamileh.wishlist.magic.Ajax;

public class User extends BaseObservable {
    public static User user = new User();

    public String username = "daniel", fname = "Daniel", lname = "Wang", dob = "1988/12/02", phone, email, session, gender = "female", fullName = "Daniel Wang";

    public User() {

    }

    public static User getInstance() {
        if (user != null) return user;
        else return user = new User();
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
        notifyPropertyChanged(BR.fname);
    }

    @Bindable
    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
        notifyPropertyChanged(BR.lname);
    }

    @Bindable
    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
        notifyPropertyChanged(BR.dob);
    }

    public String getSession() {
        return session;
    }

    @Bindable
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
        notifyPropertyChanged(BR.gender);
    }

    @Bindable
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String lname, String fname) {
        this.fullName = fullName;
        this.lname = lname;
        this.fname = fname;
        notifyPropertyChanged(BR.fullName);
    }

    public int[] getAllfieldsFromDOB() {
        int[] fields = {2017, 1, 1};
        if (dob != null) {
            String[] dobAry = dob.split("/");
            fields[0] = Integer.parseInt(dobAry[0]);
            fields[1] = Integer.parseInt(dobAry[1]) - 1;
            fields[2] = Integer.parseInt(dobAry[2]);
        }
        return fields;
    }

    public boolean isMale(){
        return gender.equals("male");
    }

    private void getData() {
        Ajax a = new Ajax();

    }
}
