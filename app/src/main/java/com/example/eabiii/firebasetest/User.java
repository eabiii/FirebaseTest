package com.example.eabiii.firebasetest;

/**
 * Created by eabiii on 02/03/2018.
 */

public class User {

    public String username;
    public String fName;
    public String lName;

    public User(String username, String fName, String lName) {
        this.username = username;
        this.fName = fName;
        this.lName = lName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }
}
