package com.example.eabiii.firebasetest;

import java.util.Map;

/**
 * Created by eabiii on 26/02/2018.
 */

public class PostModel {

    private String title,desc,username,image;
    private Long time;

    public PostModel(){}

    public PostModel(String title, String desc, String username,String image,Long time){
        this.title=title;
        this.desc=desc;
        this.username=username;
        this.image=image;
        this.time=time;


    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {

        this.time = time;
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
