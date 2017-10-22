package com.prog4.wangz_jamileh.wishlist.Model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.prog4.wangz_jamileh.wishlist.magic.Ajax;

public class User extends BaseObservable{
    public static User user = new User();

    public String username = "daniel", fname, lname, dob = "1988/12/02", phone, email, session, gender, fullName = "Daniel Wang";

    public User(){

    }

    public static User getInstance(){
        if(user != null) return user;
        else return user = new User();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    @Bindable
    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
//        notifyPropertyChanged(BR.dob);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSession() {
        return session;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFullName() {
        return fullName;
    }

    public int[] getAllfieldsFromDOB(){
        int[] fields = {2017, 1, 1};
        if(dob != null) {
            String[] dobAry = dob.split("/");
            fields[0] = Integer.parseInt(dobAry[0]) - 1;
            fields[1] = Integer.parseInt(dobAry[1]);
            fields[2] = Integer.parseInt(dobAry[2]);
        }
        return fields;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private void getData(){
        Ajax a = new Ajax();

    }
}
