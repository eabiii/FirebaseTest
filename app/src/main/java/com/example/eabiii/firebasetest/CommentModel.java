package com.example.eabiii.firebasetest;

/**
 * Created by eabiii on 04/03/2018.
 */

public class CommentModel {

    private String username,comment;

    public CommentModel(){}

    public CommentModel(String username,String comment) {
        this.username=username;
        this.comment=comment;
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
