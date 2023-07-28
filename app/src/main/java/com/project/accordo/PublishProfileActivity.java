package com.project.accordo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.project.accordo.Model.MyModel;
import com.project.accordo.Service.Helper;

public class PublishProfileActivity extends BaseActivity implements View.OnClickListener{
    private ExtendedFloatingActionButton btnChange;
    private TextView tvUsername;
    private ImageView ivPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_publish_profile);

        LinearLayout dynamicContent = findViewById(R.id.dynamicContent);
        View view = getLayoutInflater().inflate(R.layout.activity_publish_profile,null);
        dynamicContent.addView(view);

        btnChange = findViewById(R.id.modify);
        btnChange.setOnClickListener(this);

        tvUsername = findViewById(R.id.channel_name);
        ivPicture = findViewById(R.id.picture);

        if (Helper.getName(this)!=null) {
            tvUsername.setText(Helper.getName(this));
        }
        if (Helper.getPicture(this)!=null){
            ivPicture.setImageBitmap(Helper.convertFromBase64toBitmap(Helper.getPicture(this)));
            Log.d(TAG, "Immagine profilo: "+Helper.getPicture(this));
        }
        Log.d(TAG,"Profile loaded");
        Log.d(TAG, "Immagine profilo: "+Helper.getPicture(this));
    }

    @Override
    int getBottomNavigationMenuItemId() {
        return R.id.profilePicture;
    }

    @Override
    public void onClick(View v) {
        Intent intentUser = new Intent(this,ChangeProfileActivity.class);
        startActivity(intentUser);
    }
}