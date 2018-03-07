package com.example.eabiii.firebasetest;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by eabiii on 06/03/2018.
 */

public class PoliticianAdapter extends RecyclerView.Adapter<PoliticianViewHolder> {

    private List<PoliticianModel>modelList;

    public PoliticianAdapter(List<PoliticianModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public PoliticianViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(PoliticianViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
