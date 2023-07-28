package com.project.accordo;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.accordo.Entity.Channel;
import com.project.accordo.Model.MyModel;
import com.project.accordo.Service.Helper;

import java.util.List;
import java.util.stream.Collectors;

public class AddPostActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Spinner schannel;
    private Spinner stypepost;
    private AddPicturePostFragment addPicturePostFragment;

    private AddTextPostFragment addTextPostFragment;
    private ExtendedFloatingActionButton btnPost;
    private ExtendedFloatingActionButton btnDelete;
    private static final int MAX_LENGTH = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout dynamicContent = findViewById(R.id.dynamicContent);
        View view = getLayoutInflater().inflate(R.layout.activity_add_post,null);
        dynamicContent.addView(view);

        schannel = findViewById(R.id.spinner_channels);
        stypepost = findViewById(R.id.spinenr_typepost);
        stypepost.setOnItemSelectedListener(this);

        addPicturePostFragment = new AddPicturePostFragment();
        addTextPostFragment = new AddTextPostFragment();

        btnPost = findViewById(R.id.btn_post);
        btnDelete = findViewById(R.id.btn_delete);

        btnPost.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        List<String> channels = MyModel.getInstance().getChannels().stream()
                .map(Channel::getCtitle)
                .collect(Collectors.toList());

        ArrayAdapter<String> channels_adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, channels);
        channels_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schannel.setAdapter(channels_adapter);

    }

    @Override
    int getBottomNavigationMenuItemId() {
        return R.id.add;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (position){
            case 0://picture
                Log.d(TAG, "picture post selected");
                fragmentTransaction.replace(R.id.fragment_container, addPicturePostFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case 1://location
                String channel = schannel.getSelectedItem().toString();
                Bundle bundle = new Bundle();
                bundle.putString("ctitle", channel);
                Helper.changeActivityWithArguments(this, AddLocationPostActivity.class, bundle);
                break;
            case 2://text
                Log.d(TAG, "text post selected");
                fragmentTransaction.replace(R.id.fragment_container, addTextPostFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_post:
                String channel = schannel.getSelectedItem().toString();
                if (stypepost.getSelectedItem().toString().equals("Text"))
                    textPost(channel);
                else if (stypepost.getSelectedItem().toString().equals("Picture"))
                    picturePost(channel);
                break;
            case R.id.btn_delete:
                new AlertDialog.Builder(this)
                        .setMessage("Do you want to delete this post?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            dialog.dismiss();
                            Helper.changeActivity(this, WallActivity.class);
                        })
                        .setNegativeButton("No", (dialog, which) ->
                                dialog.dismiss())
                        .create().show();
                break;

        }
    }

    private void picturePost(String channel) {
        String picture = addPicturePostFragment.getPicture();
            requestController.addPost(
                    channel,
                    "i",
                    picture,
                    null,
                    null,
                    response -> {
                        Toast.makeText(this,"The post was upload successfully.",Toast.LENGTH_LONG).show();
                        //MyModel.getInstance().setPostsNull();
                        backToChannel(channel);
                    },
                    error -> Helper.requestError(this, TAG, error, null)
            );

    }

    private void textPost(String channel){
        String textPost = addTextPostFragment.getText();
        if (textPost.length() > MAX_LENGTH){
            new AlertDialog.Builder(this)
                    .setTitle("Text too long!")
                    .setMessage("Text must be at most 100 characters length.")
                    .setPositiveButton("ok", (dialog, which) ->
                            dialog.dismiss())
                    .create().show();
        }else{
            requestController.addPost(
                    channel,
                    "t",
                    textPost,
                    null,
                    null,
                    response -> {
                        Toast.makeText(this,"The post was upload successfully.",Toast.LENGTH_LONG).show();
                        //MyModel.getInstance().setPostsNull();
                        backToChannel(channel);
                    },
                    error -> Helper.requestError(this, TAG, error, null));
        }
    }

    private void backToChannel(String channel) {
        Bundle bundle = new Bundle();
        bundle.putString("ctitle", channel);
        Helper.changeActivityWithArguments(this, ChannelActivity.class, bundle);
    }
}