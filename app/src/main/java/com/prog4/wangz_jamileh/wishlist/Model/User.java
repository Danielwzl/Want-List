package com.prog4.wangz_jamileh.wishlist.Model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import com.prog4.wangz_jamileh.wishlist.BR;
import com.google.gson.internal.LinkedTreeMap;

import java.util.Map;

public class User extends BaseObservable {
    public static User user;

    public String username, fname, lname, dob, phone, email, session, gender, fullName;
    public Bitmap avartar;

    public User() {

    }

    public User(String fullName, String dob, String session, String gender) {
        this.dob = dob;
        this.gender = gender;
        this.session = session;
        this.fullName = fullName;
    }

    public User(String username, String fname, String lname, String dob, String phone, String email, String session, String gender) {
        this.username = username;
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.session = session;
        this.fullName = fname + " " + lname;
    }

    public static User getInstance() {
        if (user != null) return user;
        else return user = new User();
    }

    public static void setUser(String username, String fname, String lname, String dob, String phone, String email, String session, String gender) {
        if (User.user == null)
            User.user = new User(username, fname, lname, dob, phone, email, session, gender);
    }

    @SuppressWarnings("unchecked")
    public static void generateUser(Map<String, String> data, Map<String, Object> res) {
        String session = res.get("token").toString();
        String email = data.get("email");
        String username = data.get("nick_name");
        String dob = data.get("dob");
        String gender = data.get("gender");
        String phone = data.get("phone");
        String fname = data.get("fName");
        String lname = data.get("lName");
        setUser(username, fname, lname, dob, phone, email, session, gender);
    }

    @SuppressWarnings("unchecked")
    public static void generateUser(Map<String, Object> data) {
        String session = data.get("token").toString();
        LinkedTreeMap<String, Object> user = (LinkedTreeMap<String, Object>) data.get("user");
        String email = user.get("email").toString();
        String username = user.get("nick_name").toString();
        String dob = user.get("dob").toString();
        String gender = user.get("gender").toString();
        String phone = user.get("phone").toString();
        LinkedTreeMap<String, Object> realname = (LinkedTreeMap<String, Object>) user.get("full_name");
        String fname = realname.get("fName").toString();
        String lname = realname.get("lName").toString();
        setUser(username, fname, lname, dob, phone, email, session, gender);
    }

    @Bindable
    public Bitmap getAvartar(){
        return avartar;
    }

    public void setAvartar(Bitmap avartar){
        this.avartar = avartar;
        notifyPropertyChanged(BR.avartar);
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
            this.fullName = lname + " "  + fname;
            notifyPropertyChanged(BR.fname);
            notifyPropertyChanged(BR.fullName);
    }

    @Bindable
    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
            this.lname = lname;
            this.fullName = lname + " " + fname;
            notifyPropertyChanged(BR.lname);
            notifyPropertyChanged(BR.fullName);
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

    public void setFullName(String fullName) {
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

    public static void logout(){
        user = null;
    }
}
