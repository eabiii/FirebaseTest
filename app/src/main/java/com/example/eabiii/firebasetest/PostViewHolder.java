package com.example.eabiii.firebasetest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by eabiii on 26/02/2018.
 */

public class PostViewHolder extends RecyclerView.ViewHolder {

    private TextView txtTitle,txtDesc,txtUser;
    View mView;

    public PostViewHolder(View itemView){
        super(itemView);
        mView=itemView;
        txtTitle=itemView.findViewById(R.id.post_title_txtview);
        txtDesc=itemView.findViewById(R.id.post_desc_txtview);
        txtUser=itemView.findViewById(R.id.post_user);
    }

    public void setTitle(String title){
        txtTitle.setText(title);

    }
    public void setDesc(String desc){
        txtDesc.setText(desc);

    }
    public void setUserName(String user){
        txtUser.setText(user);

    }

    public TextView getTxtTitle() {
        return txtTitle;
    }

    public TextView getTxtDesc() {
        return txtDesc;
    }

    public TextView getTxtUser() {
        return txtUser;
    }

    public void bindPost(PostModel model){


    }
}
