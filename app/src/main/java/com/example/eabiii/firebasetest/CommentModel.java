package com.example.eabiii.firebasetest;

/**
 * Created by eabiii on 04/03/2018.
 */

public class CommentModel {

    private String username,comment;
    private Long time;


    public CommentModel(){}

    public CommentModel(String username,String comment, Long time) {
        this.username=username;
        this.comment=comment;
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
}
