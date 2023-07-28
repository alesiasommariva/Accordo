package com.project.accordo.Service;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.VolleyError;
import com.project.accordo.Model.MyModel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Helper {
    public static int MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 0;

    public static boolean checkFirstAccess(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Values.FIRST_ACCESS, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("first_access",true);
    }

    public static void setFirstAccess(Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Values.FIRST_ACCESS,Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("first_access", false).apply();
    }

    public static String getSid(Activity activity){
        return activity.getSharedPreferences(Values.SID, Context.MODE_PRIVATE).getString("sid",null);
    }

    public static String getName(Activity activity){
        return activity.getSharedPreferences(Values.PREFERENCES_PROFILE,Context.MODE_PRIVATE).getString("name",null);
    }

    public static String getPicture(Activity activity){
        return activity.getSharedPreferences(Values.PREFERENCES_PROFILE,Context.MODE_PRIVATE).getString("picture",null);
    }

    public static String getPversion(Activity activity){
        return activity.getSharedPreferences(Values.PREFERENCES_PROFILE,Context.MODE_PRIVATE).getString("pversion",null);
    }

    public static void saveName(Activity activity, String name){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Values.PREFERENCES_PROFILE, Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putString("name", name)
                .apply();
    }

    public static void savePicture(Activity activity, String picture){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Values.PREFERENCES_PROFILE, Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putString("picture", picture)
                .apply();
    }

    public static void saveSid(Activity activity, String sid){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Values.SID, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("sid",sid).apply();
    }

    public static String convertToBase64(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap convertFromBase64toBitmap(String s) throws IllegalArgumentException{
        InputStream stream = new ByteArrayInputStream(Base64.decode(s.getBytes(), Base64.DEFAULT));
        return BitmapFactory.decodeStream(stream);
    }

    public static String convertFromPathToBase64(String path){
        File imagefile = new File(path);
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(imagefile);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        return convertToBase64(bm);
    }

    public static String buildSimpleUrl(String url) {
        return Values.URL_BASE + url;
    }

    public static boolean checkStoragePermission(Activity activity){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.READ_EXTERNAL_STORAGE)){
                new AlertDialog.Builder(activity)
                        .setTitle("Permission needed")
                        .setMessage("External storage permission is necessary.")
                        .setPositiveButton("ok", (dialog, which) ->
                                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE))
                        .setNegativeButton("cancel", (dialog, which) ->
                                dialog.dismiss())
                        .create().show();

            }else {
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
            }
            return false;
        }else{
            Toast.makeText(activity,"You have already grant the permission!",Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public static void requestError(Activity activity, String TAG, VolleyError error, String customMsg) {
        if (error.networkResponse.statusCode == 400) {
            Log.d(TAG, "Incorrect parameters. " + error.toString()+ " "+customMsg);
            Toast.makeText(activity, "Incorrect parameters", Toast.LENGTH_LONG).show();
        }
        if (error.networkResponse.statusCode == 401) {
            Log.d(TAG, "SID is not valid. " + error.toString()+ " "+customMsg);
            Toast.makeText(activity, "SID is not valid.", Toast.LENGTH_LONG).show();
        }
        Log.d(TAG, "Failed " + error.toString()+ " "+customMsg);
    }

    public static void changeActivity(Activity from, Class to){
        Intent intent = new Intent(from,to);
        from.startActivity(intent);
        from.finish();
    }

    public static void changeActivityWithArguments(Activity from, Class to, Bundle bundle){
        Intent intent = new Intent(from,to);
        intent.putExtras(bundle);
        from.startActivity(intent);
        from.finish();
    }

}
