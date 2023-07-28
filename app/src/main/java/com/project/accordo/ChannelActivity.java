package com.project.accordo;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.project.accordo.Entity.Picture;
import com.project.accordo.Entity.PicturePost;
import com.project.accordo.Entity.LocationPost;
import com.project.accordo.Entity.Post;
import com.project.accordo.Entity.ProfilePicture;
import com.project.accordo.Entity.TextPost;
import com.project.accordo.Model.MyModel;
import com.project.accordo.Service.AdapterPost;
import com.project.accordo.Service.AppDatabase;
import com.project.accordo.Service.Helper;
import com.project.accordo.Service.OnRecyclerViewClickListener;
import com.project.accordo.Service.PostException;
import com.project.accordo.Service.RequestController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ChannelActivity extends BaseActivity implements OnRecyclerViewClickListener{
    private RecyclerView recyclerView;
    private AdapterPost adapterPost;
    private final Activity thisActivity = this;
    private RequestController requestController;
    private Looper secondaryThreadLooper;
    private Handler secondaryHandler;
    private Handler mainHandler;
    private Bundle btitle;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyModel.getInstance().setPostsNull();
        LinearLayout dynamicContent = findViewById(R.id.dynamicContent);
        View view = getLayoutInflater().inflate(R.layout.activity_channel,null);
        dynamicContent.addView(view);

        requestController = new RequestController(this);
        recyclerView = findViewById(R.id.post_list);

        //handler per letture e scritture su DB
        HandlerThread handlerThread = new HandlerThread("MyHandlerThread");
        handlerThread.start();

        secondaryThreadLooper = handlerThread.getLooper();
        secondaryHandler = new Handler(secondaryThreadLooper);
        mainHandler = new Handler(this.getMainLooper());

        btitle = getIntent().getExtras();
        String ctitle = btitle != null ? btitle.getString("ctitle") : null;
        setTitle(ctitle);
        Log.d(TAG, "Title: "+ctitle);

        //request posts
        requestController.getChannel(
                ctitle,
                this::savePosts,
                error -> Helper.requestError(this,TAG ,error, null));

    }

    @Override
    int getBottomNavigationMenuItemId() {
        return R.id.home;
    }


    private String verifyNullName(String name){
        return name.equals("null") ? null : name;
    }

    private void savePosts(JSONObject response) {
        try {
            JSONArray jsonArray = response.getJSONArray("posts");
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject objectPost = jsonArray.getJSONObject(i);
                    String uid = objectPost.getString("uid");
                    String name = objectPost.getString("name");
                    String pversion = objectPost.getString("pversion");
                    String pid= objectPost.getString("pid");
                    String type = objectPost.getString("type");
                    Log.d(TAG,type+" "+pid);
                    switch (type) {
                        case "t":
                            TextPost post = new TextPost(uid,verifyNullName(name),pversion,pid,type);
                            post.setContent(objectPost.getString("content"));
                            MyModel.getInstance().addPost(post);
                            break;
                        case "i":
                            Post picturePost = new PicturePost(uid,verifyNullName(name),pversion,pid, type);
                            MyModel.getInstance().addPost(picturePost);
                            break;
                        case "l":
                            LocationPost locationPost = new LocationPost(uid, verifyNullName(name), pversion, pid, type);
                            locationPost.setLat(objectPost.getString("lat"));
                            locationPost.setLon(objectPost.getString("lon"));
                            MyModel.getInstance().addPost(locationPost);
                            break;
                        default:
                            throw new PostException("The post type is not a valid type.");
                    }
                    Log.d(TAG, "UTENTE: "+ MyModel.getInstance().getPostByIndex(i).toString());
                }
            } catch (JSONException | PostException jsonException) {
                jsonException.printStackTrace();
            }
            //cerco su model e DB la profile picture
            checkMissingProfilePictures();

            //cerco su model e DB la picture del post
            checkMissingPostPicture();

            showRecyclerViewPosts(recyclerView);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }

    private void checkMissingProfilePictures() {
        Set<Post> profilePicturesToAsk = new HashSet<>();
        Log.d(TAG, "numero post: "+MyModel.getInstance().getPosts().size());
        for (Post p : MyModel.getInstance().getPosts()) {
            if (p.isToUpdateProfilePicture()) {
                profilePicturesToAsk.add(p);
            }
        }
        if (profilePicturesToAsk.size()>0)
            checkMissingProfilePictureOnDB(profilePicturesToAsk);

    }

    private void checkMissingProfilePictureOnDB(Set<Post> profilePicturesToAsk) {
        secondaryHandler.post(() -> {
            Set<Post> ppToAskServer = new HashSet<>();
            boolean flag = false;
            for (Post post: profilePicturesToAsk){
                ProfilePicture pp = AppDatabase.getInstance(thisActivity).profilePictureDao().getProfilePictureByUid(post.getUid());
                if (pp!=null && pp.getPicture()!=null && pp.getPversion().equals(post.getPversion())) {
                    //Log.d(TAG, "Profile Pic check DB "+pp.toString());
                    flag = true;
                    mainHandler.post(() -> {
                        MyModel.getInstance().updateProfilePic(pp);
                        MyModel.getInstance().updateProfilePicPostModel(post.getUid(), post.getPid());
                    });
                    //Log.d(TAG,"DB: has the right pp on DB "+pp.toString()+" "+pp.getPicture().length());
                }else {
                    ppToAskServer.add(post);
                    //Log.d(TAG, "DB: has to ask SERVER " + post.toString());
                }
            }
            if (flag) mainHandler.post(() -> adapterPost.notifyDataSetChanged());
            if (ppToAskServer.size()>0){
                Log.d(TAG, "immagini da chiedere al server: "+ppToAskServer.size()+"");
                checkMissingProfilePictureOnServer(ppToAskServer);
            }
        });
    }

    private void checkMissingProfilePictureOnServer(Set<Post> ppToAskServer) {
            for (Post post: ppToAskServer){
                if (post.isToUpdateProfilePicture()) {
                    requestController.getUserPicture(
                            post.getUid(),
                            response -> mainHandler.post(() -> profilePicturesFromServer(post, response)),
                            error -> mainHandler.post(() -> Helper.requestError(this, TAG, error, null) )
                    );
                }
            }
    }

    private void profilePicturesFromServer(Post post, JSONObject response) {
        try {
            String uid = response.getString("uid");
            String pversion = response.getString("pversion");
            String picture = response.getString("picture");
            Log.d(TAG, post.getPid() + " " + picture);
            if (picture!=null && Helper.convertFromBase64toBitmap(picture) != null && isSquare(Helper.convertFromBase64toBitmap(picture))) {
                MyModel.getInstance().updateProfilePic(new ProfilePicture(uid, picture, pversion));
                Log.d(TAG, "PP: "+ MyModel.getInstance().getProfilePic(uid).getUid()+"||"+ picture+"\n");

                MyModel.getInstance().updateProfilePicPostModel(post.getUid(), post.getPid());
                Log.d(TAG, "SERVER: pp from server " + picture.length());
                adapterPost.notifyItemChanged(MyModel.getInstance().getIndexOfPost(post));
                //adapterPost.notifyDataSetChanged();
                //aggiorno il DB
                secondaryHandler.post(() -> AppDatabase.getInstance((Context) thisActivity)
                        .profilePictureDao().insertProfilePic(new ProfilePicture(post.getUid(), picture, pversion)));
            } else {
                Log.d(TAG, "SERVER: null pp from server " + picture.length());
                //aggiorno il DB
                secondaryHandler.post(() -> AppDatabase.getInstance((Context) thisActivity)
                        .profilePictureDao().insertProfilePic(new ProfilePicture(post.getUid(), null, pversion)));
            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }


    private void checkMissingPostPicture() {
        Set<Post> postPicturesToAsk = new HashSet<>();
        for (Post p : MyModel.getInstance().getPosts()) {
            if (p instanceof PicturePost && MyModel.getInstance().getPicturePost(p.getPid()).getPicture()==null) {
                Log.d(TAG, "IMMAGINI POST DA CHIEDERE");
                postPicturesToAsk.add(p);
            }
        }
        if (postPicturesToAsk.size()>0)
            checkMissingPostPictureOnDB(postPicturesToAsk);
    }

    private void checkMissingPostPictureOnDB(Set<Post> postPicturesToAsk) {
        secondaryHandler.post(() -> {
            Set<Post> postPictureToAskServer = new HashSet<>();
            boolean flag = false;
            for (Post p : postPicturesToAsk) {
                Picture picture = AppDatabase.getInstance(thisActivity).pictureDao().getPictureByPid(p.getPid());
                if (picture != null && picture.getPicture()!=null) {
                    //Log.d(TAG,"PICTURE: "+picture.toString());
                    flag = true;
                    mainHandler.post(() -> { MyModel.getInstance().updatePicture(picture);
                    MyModel.getInstance().updatePicturePost(picture.getPid()); });

                    //TESTARE
                    Log.d(TAG, "PICTURE from DB " + p.getPid());
                }else{
                    postPictureToAskServer.add(p);
                }
            }
            if (flag) mainHandler.post(() -> adapterPost.notifyDataSetChanged());
            if (postPictureToAskServer.size()>0)
                checkMissingPostPictureOnServer(postPictureToAskServer);
        });
    }

    private void checkMissingPostPictureOnServer(Set<Post> postPictureToAskServer){
        for (Post p: postPictureToAskServer){
            if (MyModel.getInstance().getPicturePost(p.getPid()).getPicture()==null){
                requestController.getPostImage(
                        p.getPid(),
                        response -> mainHandler.post(() -> picturesFromServer(p, response)),
                        error -> Helper.requestError(thisActivity, TAG, error, null)
                );
            }
        }
    }

    private void picturesFromServer(Post post,JSONObject response){
        try {
            String content = response.getString("content");
            Log.d(TAG, "picturesFromServer: "+post.getPid()+" "+content);
            if (content!=null && Helper.convertFromBase64toBitmap(content)!=null ){
                MyModel.getInstance().updatePicture(new Picture(post.getPid(),content));
                MyModel.getInstance().updatePicturePost(post.getPid());
                adapterPost.notifyItemChanged(MyModel.getInstance().getIndexOfPost(post));
                //adapterPost.notifyDataSetChanged();

                secondaryHandler.post(() -> AppDatabase.getInstance(thisActivity)
                        .pictureDao().insertImagePost(new Picture(post.getPid(), content)));
            }else{
                secondaryHandler.post(() -> AppDatabase.getInstance(thisActivity)
                        .pictureDao().insertImagePost(new Picture(post.getPid(), null)));
            }
        }catch (JSONException | IllegalArgumentException exception){
            exception.printStackTrace();
        }
    }

    private void showRecyclerViewPosts(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterPost = new AdapterPost(this,this);
        recyclerView.setAdapter(adapterPost);
    }

    @Override
    public void onRecyclerViewClick(View view, int position) {
        Post p = MyModel.getInstance().getPostByIndex(position);
        Log.d(TAG,p.toString());
        btitle.putInt("postIndex",position);
        if (p instanceof PicturePost){
            Helper.changeActivityWithArguments(this, FullScreenImageActivity.class, btitle);
        }
        if (p instanceof LocationPost){


                Helper.changeActivityWithArguments(this, FullScreenMapActivity.class, btitle);

        }
    }

    private boolean isSquare(Bitmap bm){
        return bm.getHeight()==bm.getWidth();
    }
}