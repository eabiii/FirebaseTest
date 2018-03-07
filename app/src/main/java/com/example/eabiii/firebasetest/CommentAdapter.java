package com.example.eabiii.firebasetest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eabiii on 04/03/2018.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {


    private List<CommentModel>modelList;

    public CommentAdapter(ArrayList<CommentModel> model) {
        this.modelList = model;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment,parent,false);

        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {

        CommentModel model=this.modelList.get(position);
        holder.getTxtUser().setText(model.getUsername());
        holder.getTxtComment().setText(model.getComment());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
