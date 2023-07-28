package com.project.accordo.Service;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestController {

    private RequestQueue requestQueue;
    private static final String TAG = RequestController.class.getCanonicalName();
    private Context context;

    public RequestController(Context context){
        this.context = context;
        requestQueue = Volley.newRequestQueue(this.context);
        Log.d(TAG,"Created queue.");
    }

    public void register(Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sid", Helper.getSid((Activity) context));
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        JsonObjectRequest registerRequest = new JsonObjectRequest(
                Helper.buildSimpleUrl(Values.REGISTER),
                null,
        responseListener,
        errorListener);
        requestQueue.add(registerRequest);
    }

    public void setProfile(String name, String picture, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sid", Helper.getSid((Activity) context));
            jsonObject.put("name", name);
            jsonObject.put("picture", picture);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        JsonObjectRequest setProfileRequest = new JsonObjectRequest(
                Request.Method.POST,
                Helper.buildSimpleUrl(Values.SET_PROFILE),
                jsonObject,
                responseListener,
                errorListener);
        requestQueue.add(setProfileRequest);
    }

    public void getWall(Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sid", Helper.getSid((Activity) context));
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        JsonObjectRequest getWallRequest = new JsonObjectRequest(
                Request.Method.POST,
                Helper.buildSimpleUrl(Values.GET_WALL),
                jsonObject,
                responseListener,
                errorListener);
        requestQueue.add(getWallRequest);
    }

    public void getChannel(String ctitle, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sid", Helper.getSid((Activity) context));
            jsonObject.put("ctitle",ctitle);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        JsonObjectRequest getChannelRequest = new JsonObjectRequest(
                Request.Method.POST,
                Helper.buildSimpleUrl(Values.GET_CHANNEL),
                jsonObject,
                responseListener,
                errorListener);
        requestQueue.add(getChannelRequest);
    }

    public void getUserPicture(String uid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sid", Helper.getSid((Activity) context));
            jsonObject.put("uid",uid);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        JsonObjectRequest getUserPictureRequest = new JsonObjectRequest(
                Request.Method.POST,
                Helper.buildSimpleUrl(Values.GET_USER_PICTURE),
                jsonObject,
                responseListener,
                errorListener);
        requestQueue.add(getUserPictureRequest);
    }

    public void getPostImage(String pid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sid",Helper.getSid((Activity) context));
            jsonObject.put("pid",pid);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        JsonObjectRequest getPostImageRequest = new JsonObjectRequest(
                Request.Method.POST,
                Helper.buildSimpleUrl(Values.GET_POST_IMAGE),
                jsonObject,
                responseListener,
                errorListener);
        requestQueue.add(getPostImageRequest);
    }

    public void addChannel(String ctitle, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sid",Helper.getSid((Activity) context));
            jsonObject.put("ctitle",ctitle);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        JsonObjectRequest addChannelRequest = new JsonObjectRequest(
                Request.Method.POST,
                Helper.buildSimpleUrl(Values.ADD_CHANNEL),
                jsonObject,
                responseListener,
                errorListener);
        requestQueue.add(addChannelRequest);
    }

    public void addPost(String ctitle, String type, String content, String lat, String lon, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sid",Helper.getSid((Activity) context));
            jsonObject.put("ctitle", ctitle);
            jsonObject.put("type", type);
            if (type.equals("l")){
                jsonObject.put("lat", lat);
                jsonObject.put("lon", lon);
            }else
                jsonObject.put("content", content);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        JsonObjectRequest addPostRequest = new JsonObjectRequest(
                Request.Method.POST,
                Helper.buildSimpleUrl(Values.ADD_POST),
                jsonObject,
                responseListener,
                errorListener);
        requestQueue.add(addPostRequest);
    }

    public void sponsors(Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("sid",Helper.getSid((Activity) context));
        }catch (JSONException jsonException){
            jsonException.printStackTrace();
        }
        JsonObjectRequest sponsorsRequest = new JsonObjectRequest(
                Request.Method.POST,
                Helper.buildSimpleUrl(Values.SPONSORS),
                jsonObject,
                responseListener,
                errorListener);
        requestQueue.add(sponsorsRequest);
    }
}
