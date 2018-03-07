package com.example.eabiii.firebasetest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by eabiii on 04/03/2018.
 */

public class CommentViewHolder extends RecyclerView.ViewHolder {

    TextView txtUser,txtCom;
    View v;

    public CommentViewHolder(View itemView) {
        super(itemView);
        v=itemView;

        txtUser=itemView.findViewById(R.id.txtUsername);
        txtCom=itemView.findViewById(R.id.txtComment);

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
