package com.va.shabbirhussain.gonegative_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;


public class ProfilePage extends Fragment {


    TextView nameV,emailV;
    CircularImageView imageV;
    private String uid;
    private FirebaseStorage mStorage;
//    private ImageView imageViewForProfilePic;
    private ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    View v;
    public static ProfilePage newInstance() {
        ProfilePage fragment = new ProfilePage();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_page, container, false);
        nameV= (TextView)view.findViewById(R.id.name);
        emailV = (TextView)view.findViewById(R.id.email);
        imageV = (CircularImageView) view.findViewById(R.id.image);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String name=sharedPreferences.getString("name","Error");
        name = name.toUpperCase();
        String email=sharedPreferences.getString("email","Error");
        uid = sharedPreferences.getString("uid","Error");
        String image = sharedPreferences.getString("pic","Error");

        Picasso.with(getContext())
                .load(sharedPreferences.getString("pic",""))
                .placeholder(R.drawable.logo1)
                .error(R.drawable.logo1)
                .into(imageV);


//        imageViewForProfilePic = (ImageView)view.findViewById(R.id.imageViewForProfilePic);

        progressBar=(ProgressBar)view.findViewById(R.id.profileProgressBar);
        FloatingActionButton profilepic = (FloatingActionButton)view.findViewById(R.id.profilePic);
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CropImage.activity()
                        .getIntent(getContext());
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);




            }
        });

        nameV.setText(name);
        emailV.setText(email);



        return view;
    }

    public void updateProfilePic(){
        progressBar.setVisibility(View.VISIBLE);
        imageV.setDrawingCacheEnabled(true);
        imageV.buildDrawingCache();
        Bitmap bitmap = imageV.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        Log.d("Shabbir",bitmap.getHeight()+"");
        byte[] data = baos.toByteArray();

        mStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = mStorage.getReference();
        StorageReference riversRef = storageRef.child("images").child(uid).child("myprofilepic"+".jpg");

        UploadTask uploadTask = riversRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                String postImageUrl =""+ taskSnapshot.getDownloadUrl();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("pic",postImageUrl);
                editor.apply();

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users").child(uid).child("urlToProfileImage");
                mDatabase.setValue(postImageUrl);

                progressBar.setVisibility(View.INVISIBLE);


            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri uri = result.getUri();
                imageV.setImageURI(uri);
                updateProfilePic();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
