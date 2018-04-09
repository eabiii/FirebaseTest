package com.example.eabiii.firebasetest;

/**
 * Created by eabiii on 11/03/2018.
 */

public class RateModel {

    private String username,comment;
    private float rating;
    private Long time;

    public RateModel() {
    }

    public RateModel(String username, String comment, float rating, Long time) {
        this.username = username;
        this.comment = comment;
        this.rating = rating;
        this.time=time;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
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
