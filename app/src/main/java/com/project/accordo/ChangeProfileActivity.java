package com.project.accordo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.project.accordo.Service.Helper;
import com.project.accordo.Service.RequestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ChangeProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int PICK_IMAGE_REQUEST = 0;
    public static final int MAX_DIMENSION = 137000;
    private String TAG = ChangeProfileActivity.class.getCanonicalName();
    private RequestController requestController;
    private ExtendedFloatingActionButton btnUpload;
    private ExtendedFloatingActionButton btnSave;
    private ExtendedFloatingActionButton btnDrop;
    private EditText etUsername;
    private ImageView ivPicture;
    private String picture = null;
    private String username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        requestController = new RequestController(this);

        ivPicture = findViewById(R.id.setImagePost);
        etUsername = findViewById(R.id.setUsername);

        //Buttons
        btnUpload = findViewById(R.id.upload);
        btnSave = findViewById(R.id.post);
        btnDrop = findViewById(R.id.drop);
        btnUpload.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnDrop.setOnClickListener(this);

        if (Helper.getName(this)!=null) {
            etUsername.setText(Helper.getName(this));
        }
        if (Helper.getPicture(this)!=null){
            Log.d(TAG, "PICTURE in: "+Helper.getPicture(this));
            ivPicture.setImageBitmap(Helper.convertFromBase64toBitmap(Helper.getPicture(this)));
        }
        Log.d(TAG, "PICTURE out: "+Helper.getPicture(this));
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==Helper.MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission GRANTED",Toast.LENGTH_SHORT).show();
                chooseImage();
            }else{
                Toast.makeText(this,"Permission DENIED",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            Uri imageUri = data.getData();
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Log.d(TAG, imageUri.getPath()+"");
            Bitmap bm = BitmapFactory.decodeStream(inputStream);

            picture = Helper.convertToBase64(bm);
            if (picture!=null && picture.length() <= MAX_DIMENSION && isSquare(bm)){
                ivPicture.setImageURI(imageUri);
                Helper.savePicture(this, picture);
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Problem!")
                        .setMessage("Image must be square and at most 100 KB.")
                        .setPositiveButton("ok", (dialog, which) ->
                                dialog.dismiss())
                        .create().show();
            }
        }
    }

    private boolean isSquare(Bitmap bitmap){
        return bitmap.getWidth()==bitmap.getHeight();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.upload:
                if (Helper.checkStoragePermission(this))
                    chooseImage();
                break;
            case R.id.post:
                username = etUsername.getText().toString().trim();
                if (username.isEmpty()){
                    Toast.makeText(this,"Username must be set",Toast.LENGTH_LONG).show();
                }else if(username.length()>20){
                    Toast.makeText(this,"Username too long",Toast.LENGTH_LONG).show();
                }else {
                    Helper.saveName(this, username);
                    Log.d(TAG,"ELSE "+Helper.getName(this)+ picture);
                    requestController.setProfile(
                            username,
                            picture,
                            response -> Helper.changeActivity(this, PublishProfileActivity.class),
                            error -> Helper.requestError(this,TAG, error, null));
                }
                break;
            case R.id.drop:
                if (Helper.getName(this)==null && Helper.getPicture(this)==null) {
                    Toast.makeText(this, "Username or Profile picture must be set.", Toast.LENGTH_LONG).show();
                }else {
                    Helper.changeActivity(this, PublishProfileActivity.class);
                }
                break;
            default:
                Log.d(TAG,"Unknown Button ID");
                throw new RuntimeException("Unknown Button ID");
        }

    }
}