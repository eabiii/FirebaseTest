package com.example.eabiii.firebasetest;

/**
 * Created by eabiii on 11/03/2018.
 */

public class RateModel {

    private String username,comment;
    private float rating;

    public RateModel() {
    }

    public RateModel(String username, String comment, float rating) {
        this.username = username;
        this.comment = comment;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
