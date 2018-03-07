package com.example.eabiii.firebasetest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by eabiii on 06/03/2018.
 */

public class PoliticianViewHolder extends RecyclerView.ViewHolder {


    private TextView txtPoli,txtPos,txtParty;
    View mView;

    public PoliticianViewHolder(View itemView) {
        super(itemView);
        this.txtPoli = itemView.findViewById(R.id.txtPoliName);
        this.txtPos = itemView.findViewById(R.id.txtPosition);
        this.txtParty = itemView.findViewById(R.id.txtPartyList);
        this.mView = itemView;
    }

    public TextView getTxtPoli() {
        return txtPoli;
    }

    public void setTxtPoli(TextView txtPoli) {
        this.txtPoli = txtPoli;
    }

    public TextView getTxtPos() {
        return txtPos;
    }

    public void setTxtPos(TextView txtPos) {
        this.txtPos = txtPos;
    }

    public TextView getTxtParty() {
        return txtParty;
    }

    public void setTxtParty(TextView txtParty) {
        this.txtParty = txtParty;
    }
}
