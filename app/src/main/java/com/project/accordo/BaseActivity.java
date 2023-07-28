package com.project.accordo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.accordo.Model.MyModel;
import com.project.accordo.Service.Helper;
import com.project.accordo.Service.RequestController;

public abstract class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    protected BottomNavigationView bNavBar;
    final String TAG = this.getClass().getCanonicalName();
    protected RequestController requestController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        requestController = new RequestController( this);


        bNavBar = findViewById(R.id.bottom_navigation);
        bNavBar.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    private void updateNavigationBarState() {
        int actionId = getBottomNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        MenuItem item = bNavBar.getMenu().findItem(itemId);
        item.setChecked(true);
    }

    abstract int getBottomNavigationMenuItemId();


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                MyModel.getInstance().setChannelsNull();
                Helper.changeActivity(this, WallActivity.class);
                return true;
            case R.id.profilePicture:
                Helper.changeActivity(this, PublishProfileActivity.class);
                return true;
            case R.id.add:
                showPopup(findViewById(R.id.add), this);
                return true;
        }
        return false;
    }

    @SuppressLint("RestrictedApi")
    public void showPopup(View v, Activity activity) {
        MenuBuilder menuBuilder = new MenuBuilder(this);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.add_post_menu, menuBuilder);
        MenuPopupHelper optionsMenu = new MenuPopupHelper(this, menuBuilder, v);
        optionsMenu.setForceShowIcon(true);

        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.newChannel:
                        newChannelDialog();
                        return true;
                    case R.id.new_post:
                        Helper.changeActivity(activity, AddPostActivity.class);
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onMenuModeChange(MenuBuilder menu) {}
        });
        optionsMenu.show();
    }


    protected void newChannelDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Channel");
        final View customLayout = getLayoutInflater().inflate(R.layout.new_channel_dialog, null);
        builder.setView(customLayout);

        builder.setPositiveButton("ADD CHANNEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the AlertDialog to the Activity
                EditText editText = customLayout.findViewById(R.id.channel_name);
                sendDialogDataToActivity(editText.getText().toString());
            }
        });
        builder.setNegativeButton("BACK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // do something with the data coming from the AlertDialog
    private void sendDialogDataToActivity(String ctitle) {
        Toast.makeText(this, ctitle, Toast.LENGTH_SHORT).show();

        Log.d(TAG,ctitle);
        if (ctitle.length()>20){
            Toast.makeText(this,"Title's length must be at most 20 characters.",Toast.LENGTH_LONG).show();
        }else if (ctitle.trim().isEmpty()){
            Toast.makeText(this,"Title's length must be at least 1 character.",Toast.LENGTH_LONG).show();
        }else{
            requestController.addChannel(
                    ctitle,
                    response -> {
                        MyModel.getInstance().setChannelsNull();
                        Helper.changeActivity(this,WallActivity.class);
                    },
                    error -> {
                        if (error.networkResponse.statusCode == 400){
                            new AlertDialog.Builder(this)
                                    .setTitle("This channel title already exists!")
                                    .setMessage("You must change your channle title.")
                                    .setPositiveButton("ok", (dialog, which) -> dialog.dismiss())
                                    .create().show();
                        }
                    }
            );
        }
    }
}