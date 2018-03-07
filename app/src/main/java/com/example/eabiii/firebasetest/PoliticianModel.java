package com.example.eabiii.firebasetest;

/**
 * Created by eabiii on 04/03/2018.
 */

public class PoliticianModel {

    private String name,position,partylist;

    public PoliticianModel(){}

    public PoliticianModel(String name, String position, String partylist) {
        this.name = name;
        this.position = position;
        this.partylist = partylist;
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
}
