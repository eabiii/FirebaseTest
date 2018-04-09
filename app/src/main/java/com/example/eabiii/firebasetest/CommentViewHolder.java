package com.example.eabiii.firebasetest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by eabiii on 04/03/2018.
 */

public class CommentViewHolder extends RecyclerView.ViewHolder {

    TextView txtUser,txtCom,txtTime;
    View v;

    public CommentViewHolder(View itemView) {
        super(itemView);
        v=itemView;

        txtUser=itemView.findViewById(R.id.txtUsername);
        txtCom=itemView.findViewById(R.id.txtComment);
        txtTime=itemView.findViewById(R.id.txtTime);

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


    public TextView getTxtUser() {
        return txtUser;
    }

    public void setUsername(String username) {
        txtUser.setText(username);
        // this.username = username;
    }

    public TextView getTxtComment() {
        return txtCom;
    }

    public void setComment(String comment) {
        txtCom.setText(comment);
    }
}
