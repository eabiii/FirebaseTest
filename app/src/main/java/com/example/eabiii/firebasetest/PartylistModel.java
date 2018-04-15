package com.example.eabiii.firebasetest;

/**
 * Created by eabiii on 10/03/2018.
 */

public class PartylistModel {

    private String partylist,image;

    public PartylistModel(){}

    public PartylistModel(String partylist, String image) {
        this.partylist = partylist;
        this.image=image;
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
