package com.project.accordo.Service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.accordo.Entity.Sponsor;
import com.project.accordo.Model.MyModel;
import com.project.accordo.R;

public class AdapterSponsor extends RecyclerView.Adapter<ViewHolderSponsor>{
    private LayoutInflater layoutInflater;
    private OnRecyclerViewClickListener onRecyclerViewClickListener;

    public AdapterSponsor(Context context, OnRecyclerViewClickListener recyclerViewClickListener){
        layoutInflater = LayoutInflater.from(context);
        onRecyclerViewClickListener = recyclerViewClickListener;
    }
    @NonNull
    @Override
    public ViewHolderSponsor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.sponsor_single_row,parent,false);
        return new ViewHolderSponsor(view, onRecyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSponsor holder, int position) {
        Sponsor sponsor = MyModel.getInstance().getSponsorByIndex(position);
        holder.updateContent(sponsor);
    }

    @Override
    public int getItemCount() {
        return MyModel.getInstance().getSponsorsSize();
    }
}
