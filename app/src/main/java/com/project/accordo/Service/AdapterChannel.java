package com.project.accordo.Service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.accordo.Entity.Channel;
import com.project.accordo.Model.MyModel;
import com.project.accordo.R;

public class AdapterChannel extends RecyclerView.Adapter<ViewHolderChannel> {
    private LayoutInflater layoutInflater;
    private OnRecyclerViewClickListener onRecyclerViewClickListener;

    public AdapterChannel(Context context, OnRecyclerViewClickListener recyclerViewClickListener){
        layoutInflater = LayoutInflater.from(context);
        onRecyclerViewClickListener = recyclerViewClickListener;
    }
    @NonNull
    @Override
    public ViewHolderChannel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.channel_single_row_cw,parent,false);
        return new ViewHolderChannel(view,onRecyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderChannel holder, int position) {
        Channel channel = MyModel.getInstance().getChannel(position);
        holder.updateContent(channel);
    }

    @Override
    public int getItemCount() {
        return MyModel.getInstance().getWallSize();
    }

}
