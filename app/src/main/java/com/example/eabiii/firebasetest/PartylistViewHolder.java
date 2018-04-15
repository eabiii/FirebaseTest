package com.example.eabiii.firebasetest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by eabiii on 10/03/2018.
 */

public class PartylistViewHolder extends RecyclerView.ViewHolder{

    private TextView txtParty;
    ImageView imgView;
    View mView;


    public PartylistViewHolder(View itemView) {
        super(itemView);
        this.txtParty=itemView.findViewById(R.id.txtParty);
        this.imgView=itemView.findViewById(R.id.imageParty);
        this.mView=itemView;
    }

    public TextView getTxtParty() {
        return txtParty;
    }

    public void setTxtParty(TextView txtParty) {
        this.txtParty = txtParty;
    }

    public View getmView() {
        return mView;
    }

    public void setmView(View mView) {
        this.mView = mView;
    }

    public ImageView getImgView(){return imgView;}
}
