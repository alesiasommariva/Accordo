package com.project.accordo.Service;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.accordo.Entity.Sponsor;
import com.project.accordo.R;

public class ViewHolderSponsor extends RecyclerView.ViewHolder implements View.OnClickListener{
    private final TextView tvSponsor;
    private final ImageView ivSponsor;
    private final OnRecyclerViewClickListener onRecyclerViewClickListener;

    public ViewHolderSponsor(@NonNull View itemView, OnRecyclerViewClickListener recyclerViewClickListener) {
        super(itemView);
        tvSponsor = itemView.findViewById(R.id.sponsor_name);
        ivSponsor = itemView.findViewById(R.id.sponsor_image);
        onRecyclerViewClickListener = recyclerViewClickListener;
        itemView.setOnClickListener(this);
    }

    public void updateContent(Sponsor sponsor){
        tvSponsor.setText(sponsor.getText());
        ivSponsor.setImageBitmap(Helper.convertFromBase64toBitmap(sponsor.getImage64()));
    }

    @Override
    public void onClick(View v) {
        onRecyclerViewClickListener.onRecyclerViewClick(v,getAdapterPosition());
    }
}
