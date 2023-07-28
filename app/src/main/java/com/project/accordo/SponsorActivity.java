package com.project.accordo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.accordo.Entity.Sponsor;
import com.project.accordo.Model.MyModel;
import com.project.accordo.Service.AdapterPost;
import com.project.accordo.Service.AdapterSponsor;
import com.project.accordo.Service.Helper;
import com.project.accordo.Service.OnRecyclerViewClickListener;
import com.project.accordo.Service.RequestController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SponsorActivity extends AppCompatActivity implements OnRecyclerViewClickListener, View.OnClickListener {

    private RequestController requestController;
    private static final String TAG = SponsorActivity.class.getCanonicalName();
    private Activity thisActivity = this;
    private RecyclerView recyclerView;
    private AdapterSponsor adapterSponsor;
    private FloatingActionButton fabBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor);
        recyclerView = findViewById(R.id.sponsor_list);

        MyModel.getInstance().setSponsorsNull();

        fabBack = findViewById(R.id.back_wall);
        fabBack.setOnClickListener(this);


        requestController = new RequestController(this);

        requestController.sponsors(
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("sponsors");
                        Log.d(TAG, "sponsors number: "+jsonArray.length());
                        for (int i = 0; i<jsonArray.length(); i++){
                            JSONObject s = jsonArray.getJSONObject(i);
                            Sponsor sponsor = new Sponsor(s.getString("url"),s.getString("text"), s.getString("image"));
                            MyModel.getInstance().addSponsor(sponsor);
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                        adapterSponsor = new AdapterSponsor(thisActivity, this);
                        recyclerView.setAdapter(adapterSponsor);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Helper.requestError(thisActivity, TAG, error, null));
    }

    @Override
    public void onRecyclerViewClick(View view, int position) {
        Sponsor sponsor = MyModel.getInstance().getSponsorByIndex(position);
        Bundle bundle = new Bundle();
        bundle.putString("text", sponsor.getText());
        bundle.putString("url", sponsor.getUrl());
        bundle.putString("image64", sponsor.getImage64());
        Helper.changeActivityWithArguments(this, DataSponsorActivity.class, bundle);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.back_wall)
            MyModel.getInstance().setChannelsNull();
            Helper.changeActivity(thisActivity, WallActivity.class);
    }
}