package com.luns.neuro.mlkn.library;

/**
 * Created by neuro on 2/24/2018.
 */
public class User {

    private String phonenumber, firebaseid, user_priviledge, user_fullname, user_address, user_latitude, user_longitude;

    public User(String phonenumber, String firebaseid, String user_priviledge, String user_fullname, String user_address, String user_latitude, String user_longitude) {
        this.phonenumber = phonenumber;
        this.firebaseid = firebaseid;
        this.user_priviledge=user_priviledge;
        this.user_fullname = user_fullname;
        this.user_address=user_address;
        this.user_latitude=user_latitude;
        this.user_longitude=user_longitude;

    }


    public String getPhonenumber() {
        return phonenumber;
    }
    public String getFirebaseid() {
        return firebaseid;
    }

    public String getUser_fullname() {
        return user_fullname;
    }
    public String getUser_priviledge() {
        return user_priviledge;
    }
    public String getUser_address() {
        return user_address;
    }
    public String getUser_latitude() {
        return user_latitude;
    }
    public String getUser_longitude() {
        return user_longitude;
    }


}