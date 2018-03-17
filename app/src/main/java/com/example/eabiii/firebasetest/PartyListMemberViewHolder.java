package com.example.eabiii.firebasetest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by eabiii on 10/03/2018.
 */

public class PartyListMemberViewHolder extends RecyclerView.ViewHolder {


    TextView txtParty,txtPosition;
    View mView;
    public PartyListMemberViewHolder(View itemView) {
        super(itemView);
        this.txtParty=itemView.findViewById(R.id.memberName);
        this.txtPosition=itemView.findViewById(R.id.memberPosition);
        this.mView=itemView;
    }

    public TextView getTxtPosition() {
        return txtPosition;
    }

    public void setTxtPosition(TextView txtPosition) {
        this.txtPosition = txtPosition;
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
}
