package com.project.accordo;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.accordo.Model.MyModel;
import com.project.accordo.Service.Helper;
import com.project.accordo.Service.RequestController;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class AddPicturePostFragment extends Fragment implements View.OnClickListener {
    public static final int MAX_DIMENSION = 137000;
    private ExtendedFloatingActionButton btnUpload;
    private ImageView ivPicture;
    private String TAG = this.getClass().getCanonicalName();
    private String picture = null;
    private static final int PICK_IMAGE_REQUEST = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_picture_post, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        ivPicture = getActivity().findViewById(R.id.setImagePost);

        //String text = schannel.getSelectedItem().toString();

        btnUpload = getActivity().findViewById(R.id.upload);
        btnUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (Helper.checkStoragePermission(getActivity()))
            chooseImage();

    }

    public String getPicture(){
        return picture;
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode== Helper.MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(),"Permission GRANTED",Toast.LENGTH_SHORT).show();
                chooseImage();
            }else{
                Toast.makeText(getActivity(),"Permission DENIED",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            Uri imageUri = data.getData();
            InputStream inputStream = null;
            try {
                inputStream = getContext().getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Log.d(TAG, imageUri.getPath()+"");
            Bitmap bm = BitmapFactory.decodeStream(inputStream);
            picture = Helper.convertToBase64(bm);

            Log.d(TAG, "Lunghezza immagine: "+picture.length());
            if (picture.length() > MAX_DIMENSION) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Image too large!")
                        .setMessage("Image must be at most 100 KB.")
                        .setPositiveButton("ok", (dialog, which) ->
                                dialog.dismiss())
                        .create().show();
            }else {
                Log.d(TAG, picture+"");
                ivPicture.setImageURI(imageUri);
            }
        }
    }
}