package com.project.accordo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.accordo.Entity.Channel;
import com.project.accordo.Model.MyModel;
import com.project.accordo.Service.AdapterChannel;
import com.project.accordo.Service.AppDatabase;
import com.project.accordo.Service.Helper;
import com.project.accordo.Service.OnRecyclerViewClickListener;
import com.project.accordo.Service.RequestController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class WallActivity extends BaseActivity implements OnRecyclerViewClickListener, View.OnClickListener{
    private RequestController requestController;
    private FloatingActionButton fabSponsor;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestController = new RequestController(this);


        LinearLayout dynamicContent = findViewById(R.id.dynamicContent);
        View view = getLayoutInflater().inflate(R.layout.activity_wall,null);
        dynamicContent.addView(view);
        fabSponsor = findViewById(R.id.sponsor);
        fabSponsor.setOnClickListener(this);


        /*new Thread(() -> {
            AppDatabase.getInstance(this).profilePictureDao().deleteAllProfilePictures();
            AppDatabase.getInstance(this).pictureDao().deleteAll(); }).start();
*/
        Log.d(TAG, "ONCreate");
        if (Helper.checkFirstAccess(this)){
            requestController.register(
                    this::saveSidAndGoToSetProfile,
                    this::simpleError);
        }else {
            Log.d(TAG,"SID: "+Helper.getSid(this));
            requestController.getWall(
                    this::showWall,
                    this::simpleError);
        }

    }

    @Override
    int getBottomNavigationMenuItemId() {
        return R.id.home;
    }


    private void showWall(JSONObject response) {
        try {
            JSONArray jsonArray = response.getJSONArray("channels");
            for (int i=1; i<jsonArray.length(); i++){
                JSONObject c = jsonArray.getJSONObject(i);
                Channel channel = new Channel(c.getString("ctitle"), c.getString("mine").equals("t"));
                Log.d(TAG,channel.toString());
                MyModel.getInstance().addChannel(channel);
            }
            Log.d(TAG,"Mostro i canali");
            showRecyclerViewChannels();
            Log.d(TAG,"Wall loaded");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showRecyclerViewChannels() {
        MyModel.getInstance().getChannels().add(0, new Channel("Sponsor", false));
        RecyclerView recyclerView = findViewById(R.id.channel_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdapterChannel adapter = new AdapterChannel(this,this);
        recyclerView.setAdapter(adapter);
    }

    private void simpleError(VolleyError error) {
        Toast.makeText(this, "Failed " + error.networkResponse, Toast.LENGTH_LONG).show();
    }

    private void saveSidAndGoToSetProfile(JSONObject response) {
        try {
            Helper.saveSid(this,response.getString("sid"));
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        Helper.setFirstAccess(this);
        Log.d(TAG,"SID: "+Helper.getSid(this));
        Helper.changeActivity(this,ChangeProfileActivity.class);
    }

    @Override
    public void onRecyclerViewClick(View view, int position) {
        if (position==0){
            Helper.changeActivity(this, SponsorActivity.class);
        }else{
            Bundle b = new Bundle();
            b.putString("ctitle", MyModel.getInstance().getChannel(position).getCtitle());
            Log.d(TAG, "Ctitle: "+b.getString("ctitle"));
            Helper.changeActivityWithArguments(this,ChannelActivity.class,b);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.sponsor){
            Helper.changeActivity(this, SponsorActivity.class);
        }
    }
}