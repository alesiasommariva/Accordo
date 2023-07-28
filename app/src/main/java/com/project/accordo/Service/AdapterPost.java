package com.project.accordo.Service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.accordo.Entity.Post;
import com.project.accordo.Model.MyModel;
import com.project.accordo.R;

public class AdapterPost extends RecyclerView.Adapter<ViewHolderPost> {
    private LayoutInflater layoutInflater;
    private OnRecyclerViewClickListener onRecyclerViewClickListener;
    private static final String TAG = AdapterPost.class.getCanonicalName();

    public AdapterPost(Context context,OnRecyclerViewClickListener recyclerViewClickListener){
        layoutInflater = LayoutInflater.from(context);
        onRecyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public ViewHolderPost onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.post_single_row_cv,parent,false);
        return new ViewHolderPost(view,onRecyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPost holder, int position) {
        Post post = MyModel.getInstance().getPostByIndex(position);
        holder.updateContent(post);
    }

    @Override
    public int getItemCount() {
       return MyModel.getInstance().getChannelSize();
    }
}
