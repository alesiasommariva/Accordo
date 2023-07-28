package com.project.accordo.Service;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.accordo.Entity.PicturePost;
import com.project.accordo.Entity.LocationPost;
import com.project.accordo.Entity.Post;
import com.project.accordo.Entity.TextPost;
import com.project.accordo.R;

public class ViewHolderPost extends RecyclerView.ViewHolder implements View.OnClickListener{
    private final TextView tvUsername;
    private final ImageView ivProfilePic;
    private final TextView tvTextPost;
    private final ImageView ivImagePost;
    private final OnRecyclerViewClickListener onRecyclerViewClickListener;
    private static final String TAG = ViewHolderPost.class.getCanonicalName();

    public ViewHolderPost(@NonNull View itemView, OnRecyclerViewClickListener recyclerViewClickListener) {
        super(itemView);
        tvUsername = itemView.findViewById(R.id.profile_name);
        ivProfilePic = itemView.findViewById(R.id.profile);
        tvTextPost = itemView.findViewById(R.id.textPost);
        ivImagePost = itemView.findViewById(R.id.picturePost);
        onRecyclerViewClickListener = recyclerViewClickListener;
        itemView.setOnClickListener(this);
    }

    public void updateContent(Post post){
        if (post.getProfile_pic()!=null) {
                Log.d(TAG, "ProfilePicturePresent " + post.getName() + " " + post.getPid() + " " + post.getProfile_pic());
                ivProfilePic.setImageBitmap(post.getProfile_pic());
        }else {
                Log.d(TAG, "ProfilePictureNotPresent " + post.getName() + " " + post.getPid() + " " + post.getProfile_pic());
                ivProfilePic.setImageResource(R.drawable.outline_person_24);
        }

        if (post.getName()!=null){
            Log.d(TAG,"NamePresent "+post.getName()+" "+post.getPid());
            tvUsername.setText(post.getName());
        }else{
            Log.d(TAG,"NameNotPresent "+post.getName()+" "+post.getPid());
            tvUsername.setText(R.string.utente_anonimo);
        }

        if (post instanceof TextPost){
            ivImagePost.setVisibility(View.GONE);
            tvTextPost.setText(((TextPost) post).getContent());
            tvTextPost.setVisibility(View.VISIBLE);
            Log.d(TAG,"Show TextPost "+"! "+((TextPost) post).getContent()+" !");
        }else if (post instanceof PicturePost){
            if (((PicturePost)post).getPicture()!=null) {
                tvTextPost.setText(null);
                tvTextPost.setVisibility(View.GONE);
                ivImagePost.setVisibility(View.VISIBLE);
                ivImagePost.setImageBitmap(((PicturePost)post).getPicture());
            }else{
                tvTextPost.setVisibility(View.GONE);
                ivImagePost.setVisibility(View.VISIBLE);
                ivImagePost.setImageResource(R.drawable.baseline_image_not_supported_24);
            }
            Log.d(TAG,"Show ImagePost");
        }else {
            ivImagePost.setVisibility(View.GONE);
            tvTextPost.setVisibility(View.VISIBLE);
            tvTextPost.setText(R.string.shared_location);
            Log.d(TAG,"Show LocationPost "+"! "+((LocationPost)post).getLat()+" "+((LocationPost)post).getLon());
        }
    }

    @Override
    public void onClick(View v) {
        onRecyclerViewClickListener.onRecyclerViewClick(v,getAdapterPosition());
    }
}
