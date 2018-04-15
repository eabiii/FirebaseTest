package com.example.eabiii.firebasetest;

/**
 * Created by eabiii on 10/03/2018.
 */

public class PartyListMemberModel {

    private String name, position, image;


    public PartyListMemberModel(){}

    public PartyListMemberModel(String name, String position) {

        this.name = name;
        this.position=position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
