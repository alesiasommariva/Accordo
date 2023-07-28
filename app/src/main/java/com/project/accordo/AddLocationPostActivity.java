package com.project.accordo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.project.accordo.Entity.LocationPost;
import com.project.accordo.Model.MyModel;
import com.project.accordo.Service.Helper;
import com.project.accordo.Service.RequestController;

import java.lang.ref.WeakReference;
import java.util.List;

public class AddLocationPostActivity extends AppCompatActivity implements
        View.OnClickListener, OnMapReadyCallback, PermissionsListener {
    private static final int MY_PERMISSION_REQUEST_LOCATION = 1;
    private String TAG = this.getClass().getCanonicalName();
    private MapView mapView;
    private MapboxMap mMapboxMap;
    private PermissionsManager permissionsManager;
    private RequestController requestController;
    private Bundle bundle;
    public Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_add_location_post);

        requestController = new RequestController(this);

        FloatingActionButton fabSetCurrentPosition = findViewById(R.id.postLocation);
        fabSetCurrentPosition.setOnClickListener(this);
        FloatingActionButton fabBack = findViewById(R.id.back);
        fabBack.setOnClickListener(this);
        FloatingActionButton fabMyLocation = findViewById(R.id.myLocation);
        fabMyLocation.setOnClickListener(this);

        mapView = findViewById(R.id.fullMapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        bundle = getIntent().getExtras();

        checkGPSUpdate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission needed")
                        .setMessage("Location permission is necessary.")
                        .setPositiveButton("ok", (dialog, which) ->
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION))
                        .setNegativeButton("cancel", (dialog, which) ->
                                dialog.dismiss())
                        .create().show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
            }
            return true;
        } else {
            //Toast.makeText(this, "You have already grant the permission!", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    private void checkGPSUpdate() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int resultCode = api.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {

            // show your own AlertDialog for example:
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // set the message
            builder.setMessage("This app use google play services only for optional features")
                    .setTitle("Do you want to update?"); // set a title

            builder.setPositiveButton("OK", (dialog, id) -> {
                // User clicked OK button

                final String appPackageName = "com.google.android.gms";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            });
            builder.setNegativeButton("CANCEL", (dialog, id) -> {
                dialog.dismiss();
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.postLocation:
                if (checkLocationPermission()) {
                    requestController.addPost(
                            bundle.getString("ctitle"),
                            "l",
                            null,
                            String.valueOf(mMapboxMap.getCameraPosition().target.getLatitude()),
                            String.valueOf(mMapboxMap.getCameraPosition().target.getLongitude()),
                            response -> {
                                Toast.makeText(this, "The post was upload successfully.", Toast.LENGTH_LONG).show();
                                Helper.changeActivityWithArguments(this, ChannelActivity.class, bundle);
                            },
                            error -> Helper.requestError(this, TAG, error, null)
                    );
                }
                break;
            case R.id.back:
                new AlertDialog.Builder(this)
                        .setMessage("Do you want to delete this post?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            dialog.dismiss();
                            //MyModel.getInstance().setPostsNull();
                            Helper.changeActivityWithArguments(this, ChannelActivity.class, bundle);
                        })
                        .setNegativeButton("No", (dialog, which) ->
                                dialog.dismiss())
                        .create().show();
                break;
            case R.id.myLocation:
                if(location!=null) {
                    CameraPosition position = new CameraPosition.Builder()
                            .target(new LatLng(location.getLatitude(), location.getLongitude()))
                            .zoom(12)
                            .tilt(30)
                            .build();
                    mMapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);
                }
                break;

        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mMapboxMap.getStyle(this::enableLocationComponent);
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            //finish();
        }
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        AddLocationPostActivity.this.mMapboxMap = mapboxMap;
        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41"),
                this::enableLocationComponent);

    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Get an instance of the component
            LocationComponent locationComponent = mMapboxMap.getLocationComponent();
            // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());
            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);
            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}