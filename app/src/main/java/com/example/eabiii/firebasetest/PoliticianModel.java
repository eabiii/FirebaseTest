package com.example.eabiii.firebasetest;

/**
 * Created by eabiii on 04/03/2018.
 */

public class PoliticianModel {

    private String name,position,partylist,image;

    public PoliticianModel(){}

    public PoliticianModel(String name, String position, String partylist,String image) {
        this.name = name;
        this.position = position;
        this.partylist = partylist;
        this.image=image;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPartylist() {
        return partylist;
    }

    public void setPartylist(String partylist) {
        this.partylist = partylist;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
