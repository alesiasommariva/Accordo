package com.project.accordo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.project.accordo.Entity.PicturePost;
import com.project.accordo.Model.MyModel;
import com.project.accordo.Service.Helper;

public class DataSponsorActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tvText;
    private ImageView ivSponsor;
    private TextView tvUrl;
    private ExtendedFloatingActionButton fab_url;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_sponsor);

        tvText = findViewById(R.id.tvText);
        ivSponsor = findViewById(R.id.ivSponsor);
        tvUrl = findViewById(R.id.tvUrl);

        fab_url = findViewById(R.id.fab_url);
        fab_url.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            tvText.setText(bundle.getString("text"));
            tvUrl.setText(bundle.getString("url"));
            url = bundle.getString("url");
            Bitmap pic = Helper.convertFromBase64toBitmap(bundle.getString("image64"));
            ivSponsor.setImageBitmap(pic);
        }
    }

    @Override
    public void onClick(View v) {
        String url = "http://"+this.url;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}