package com.example.eabiii.firebasetest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eabiii on 26/02/2018.
 */

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder>{

    private List<PostModel> model;

    public PostAdapter(ArrayList<PostModel> model){
        this.model=model;

    }
    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post,parent,false);

        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        PostModel pModel=model.get(position);
        holder.getTxtTitle().setText(pModel.getTitle());
        holder.getTxtDesc().setText(pModel.getDesc());
        holder.getTxtUser().setText(pModel.getUsername());


    }

    @Override
    public int getItemCount() {
        return this.model.size();
    }
}
