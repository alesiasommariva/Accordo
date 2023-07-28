package com.project.accordo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.project.accordo.Entity.LocationPost;
import com.project.accordo.Entity.Post;
import com.project.accordo.Model.MyModel;
import com.project.accordo.Service.Helper;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

public class FullScreenMapActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";
    private MapView mapView;
    private String TAG = this.getClass().getCanonicalName();
    private Bundle bundle;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_full_screen_map);

        bundle = getIntent().getExtras();
        FloatingActionButton fab = findViewById(R.id.back);
        fab.setOnClickListener(this);

        int postIndex = bundle.getInt("postIndex");

        post = MyModel.getInstance().getPostByIndex(postIndex);
        mapView = findViewById(R.id.fullMapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Log.d(TAG,postIndex+" "+post.getPid());

    }

    @Override
    public void onClick(View view) {
        Bundle b = new Bundle();
        b.putString("ctitle", bundle.getString("ctitle"));
        //MyModel.getInstance().setPostsNull();
        Helper.changeActivityWithArguments(this, ChannelActivity.class,b);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        double lat = Double.parseDouble(((LocationPost)post).getLat());
        double lon = Double.parseDouble(((LocationPost)post).getLon());
        Log.d(TAG, lon+" "+lat);

        List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                Point.fromLngLat(lon, lat)));

        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")
                // Add the SymbolLayer icon image to the map style
                .withImage(ICON_ID, BitmapFactory.decodeResource(
                        FullScreenMapActivity.this.getResources(), R.drawable.mapbox_marker_icon_default))
                // Adding a GeoJson source for the SymbolLayer icons.
                .withSource(new GeoJsonSource(SOURCE_ID,
                        FeatureCollection.fromFeatures(symbolLayerIconFeatureList)))
                // Adding the actual SymbolLayer to the map style. An offset is added that the bottom of the red
                // marker icon gets fixed to the coordinate, rather than the middle of the icon being fixed to
                // the coordinate point. This is offset is not always needed and is dependent on the image
                // that you use for the SymbolLayer icon.
                .withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                        .withProperties(
                                iconImage(ICON_ID),
                                iconAllowOverlap(true),
                                iconIgnorePlacement(true)
                        )
                ), style -> {
                    // Map is set up and the style has loaded. Now you can add additional data or make other map adjustments.
            try {
                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(lat, lon))
                        .zoom(12)
                        .tilt(30)
                        .build();

                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);
            }catch (IllegalArgumentException exception){
                Toast.makeText(this, "Incorrect Coordinates!", Toast.LENGTH_LONG).show();
            }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}