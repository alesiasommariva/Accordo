package com.project.accordo.Model;

import android.graphics.Bitmap;

import com.project.accordo.Entity.Channel;
import com.project.accordo.Entity.Picture;
import com.project.accordo.Entity.PicturePost;
import com.project.accordo.Entity.Post;
import com.project.accordo.Entity.ProfilePicture;
import com.project.accordo.Entity.Sponsor;
import com.project.accordo.Service.Helper;

import java.util.ArrayList;
import java.util.HashMap;


public class MyModel {

    private static final String TAG = MyModel.class.getCanonicalName();
    private static MyModel instance = null;

    private ArrayList<Channel> channels;
    private ArrayList<Post> posts;
    private ArrayList<Sponsor> sponsors;
    private HashMap<String, ProfilePicture> profilePictureHashMap;
    private HashMap<String, Picture> picturePostHashMap;

    private MyModel(){
        channels = new ArrayList<>();
        posts = new ArrayList<>();
        sponsors = new ArrayList<>();
        profilePictureHashMap = new HashMap<>();
        picturePostHashMap = new HashMap<>();
    }

    public static synchronized MyModel getInstance(){
        if (instance==null)
            instance = new MyModel();
        return instance;
    }

    public void addSponsor(Sponsor sponsor){
        sponsors.add(sponsor);
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public void addChannel(Channel channel){
        channels.add(channel);
    }

    public int getWallSize(){
        return channels.size();
    }

    public Channel getChannel(int index){
        return channels.get(index);
    }

    public Sponsor getSponsorByIndex(int index) {return sponsors.get(index);}

    public int getSponsorsSize(){ return sponsors.size();};

    public void addPost(Post post){
        checkProfilePicture(post);
        if (post instanceof PicturePost){
            checkPicturePost(post);
        }
        posts.add(post);
    }

    public Picture getPicturePost(String pid) {
        return picturePostHashMap.get(pid);
    }

    private void checkPicturePost(Post post) {
        Picture picture = getPicturePost(post.getPid());
        if (picture!=null && picture.getPicture()!=null)
            ((PicturePost)post).setPicture(Helper.convertFromBase64toBitmap(picture.getPicture()));
        else
            picturePostHashMap.put(post.getPid(), new Picture(post.getPid(), null));
    }

    //se già conosco la pp la aggiungo, altrimenti null
    private void checkProfilePicture(Post post) {
        ProfilePicture pp = getProfilePic(post.getUid());
        if (pp==null){
            profilePictureHashMap.put(post.getUid(), new ProfilePicture(post.getUid(), null, post.getPversion()));
        }
        if (pp!=null && pp.getPicture()!=null){
            post.setProfile_pic(Helper.convertFromBase64toBitmap(pp.getPicture()));
        }
    }

    public Post getPostByIndex(int index){
        return posts.get(index);
    }

    public ProfilePicture getProfilePic(String uid){
        return profilePictureHashMap.get(uid);
    }

    public HashMap<String, ProfilePicture> getProfilePictureHashMap(){
        return profilePictureHashMap;
    }

    public void updateProfilePic(ProfilePicture profilePicture){
        profilePictureHashMap.put(profilePicture.getUid(), profilePicture);
    }

    public void updatePicture(Picture picture) {
        picturePostHashMap.put(picture.getPid(), picture);
    }

    public void updateProfilePicPostModel(String uid, String pid){
        String pic = getProfilePic(uid).getPicture();
        if (pic!=null) {
            Bitmap pp = Helper.convertFromBase64toBitmap(pic);
            getPostByPid(pid).setProfile_pic(pp);
        }else{
            getPostByPid(pid).setProfile_pic(null);
        }
    }

    public void updatePicturePost(String pid) {
        String picModel = getPicturePost(pid).getPicture();
        Bitmap picConverted = Helper.convertFromBase64toBitmap(picModel);
        ((PicturePost)getPostByPid(pid)).setPicture(picConverted);
    }

    public int getChannelSize(){
        return posts.size();
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public Post getPostByPid(String pid){
        for (Post p : posts) {
            if (p.getPid().equals(pid)){
                return p;
            }
        }
        return null;
    }

    public void setChannelsNull() {
        this.channels = new ArrayList<>();
    }

    public void setPostsNull(){
        this.posts = new ArrayList<>();
    }

    public void setSponsorsNull(){this.sponsors = new ArrayList<>();}
    public int getIndexOfChannel(Channel channel){
        return channels.indexOf(channel);
    }

    public int getIndexOfPost(Post post) {
        return posts.indexOf(post);
    }

}
