package com.example.eabiii.firebasetest;

/**
 * Created by eabiii on 26/02/2018.
 */

public class PostModel {

    private String title,desc,username,image;

    public PostModel(){}

    public PostModel(String title, String desc, String username,String image){
        this.title=title;
        this.desc=desc;
        this.username=username;
        this.image=image;


    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
