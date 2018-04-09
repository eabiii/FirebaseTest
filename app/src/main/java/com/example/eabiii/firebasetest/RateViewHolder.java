package com.example.eabiii.firebasetest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by eabiii on 11/03/2018.
 */

public class RateViewHolder extends RecyclerView.ViewHolder {

    TextView txtUsername, txtComment, txtRating,txtTime;
    View mView;

    public RateViewHolder(View itemView) {
        super(itemView);
        this.txtUsername=itemView.findViewById(R.id.userName);
        this.txtComment=itemView.findViewById(R.id.comment);
        this.txtRating=itemView.findViewById(R.id.rating);
        this.txtTime=itemView.findViewById(R.id.txtTime);
        this.mView=itemView;
    }
    public TextView getTxtTime() {
        return txtTime;
    }

    public void setTxtTime(Long txttime) {
        SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try{
            txtTime.setText(format.format(new Date(txttime)));
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public TextView getTxtUsername() {
        return txtUsername;
    }

    public void setTxtUsername(TextView txtUsername) {
        this.txtUsername = txtUsername;
    }

    public TextView getTxtComment() {
        return txtComment;
    }

    public void setTxtComment(TextView txtComment) {
        this.txtComment = txtComment;
    }

    public TextView getTxtRating() {
        return txtRating;
    }

    public void setTxtRating(TextView txtRating) {
        this.txtRating = txtRating;
    }

    public View getmView() {
        return mView;
    }

    public void setmView(View mView) {
        this.mView = mView;
    }
}
