package com.project.accordo.Service;


import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.accordo.Entity.Channel;
import com.project.accordo.R;

public class ViewHolderChannel extends RecyclerView.ViewHolder implements View.OnClickListener{
    private TextView tvChannel;
    private OnRecyclerViewClickListener onRecyclerViewClickListener;
    private CardView cardView;

    public ViewHolderChannel(@NonNull View itemView, OnRecyclerViewClickListener recyclerViewClickListener) {
        super(itemView);
        tvChannel = itemView.findViewById(R.id.channel);
        onRecyclerViewClickListener = recyclerViewClickListener;
        cardView = itemView.findViewById(R.id.card_view);
        itemView.setOnClickListener(this);
    }

    public void updateContent(Channel channel){
        if (channel.isMine()) {
            cardView.setCardBackgroundColor(Color.parseColor("#90caf9"));
            Log.d("ViewHolderChannel", "MIO CANALE");
        }else{
            cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        tvChannel.setText(channel.getCtitle());

    }

    @Override
    public void onClick(View v) {
        onRecyclerViewClickListener.onRecyclerViewClick(v, getAdapterPosition());
    }
}
