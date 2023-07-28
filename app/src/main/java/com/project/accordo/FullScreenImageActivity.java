package com.project.accordo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.accordo.Entity.PicturePost;
import com.project.accordo.Model.MyModel;
import com.project.accordo.Service.Helper;

import java.util.Objects;

public class FullScreenImageActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivFullScreen;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        ivFullScreen = findViewById(R.id.imageFullScreen);
        bundle = getIntent().getExtras();
        FloatingActionButton fab = findViewById(R.id.back);
        fab.setOnClickListener(this);


        if (bundle != null) {
            Bitmap pic = ((PicturePost) MyModel.getInstance().getPostByIndex(bundle.getInt("postIndex"))).getPicture();
            ivFullScreen.setImageBitmap(pic);
            //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //Objects.requireNonNull(getSupportActionBar()).hide();

            //ivFullScreen.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            //ivFullScreen.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            //ivFullScreen.setAdjustViewBounds(true);
            //ivFullScreen.setScaleType(ImageView.ScaleType.FIT_XY);
        }else{
            ivFullScreen.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            ivFullScreen.setImageResource(R.drawable.baseline_image_not_supported_24);
        }

    }

    @Override
    public void onClick(View view) {
        Bundle b = new Bundle();
        b.putString("ctitle", bundle.getString("ctitle"));
        //MyModel.getInstance().setPostsNull();MyModel.getInstance().setChannelsNull();
        Helper.changeActivityWithArguments(this, ChannelActivity.class,b);
    }
}