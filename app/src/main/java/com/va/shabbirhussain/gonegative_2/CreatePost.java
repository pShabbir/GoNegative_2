package com.va.shabbirhussain.gonegative_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Date;

public class CreatePost extends AppCompatActivity {

    private static final int GALLERY_INTENT = 201;
    private DatabaseReference mDatabase;
    FirebaseDatabase database;
    FirebaseStorage mStorage;
    Uri uri,downloadUrl;
    ImageView imageView;
    String userID,postId,postImageUrl,postDate,likes,storyText,name;
    SharedPreferences shared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        shared = PreferenceManager.getDefaultSharedPreferences(this);

        database = FirebaseDatabase.getInstance();

        mStorage = FirebaseStorage.getInstance();

        imageView = (ImageView)findViewById(R.id.image);

        userID = shared.getString("uid","");

        postId= database.getReference("users").push().getKey();

    }

    public void pickImage(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){


             uri = data.getData();
             imageView.setImageURI(uri);
           //uri = Uri.parse("android.resource://com.va.shabbirhussain.gonegative_2/drawable/logo1");



        }
    }

    public void submitPost(View view){
        new mySubmit().execute();
    }

    public class mySubmit extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
             postDate = DateFormat.getDateTimeInstance().format(new Date());
             name=shared.getString("name","");
             likes="0";

            TextView txt = (TextView)findViewById(R.id.story);

             storyText = txt.getText().toString();

        }

        @Override
        protected Void doInBackground(Void... params) {
            StorageReference storageRef = mStorage.getReference();

            StorageReference riversRef = storageRef.child("images").child(userID).child(postId+".jpg");

            riversRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadUrl = taskSnapshot.getDownloadUrl();
                    postImageUrl = downloadUrl.toString();
                    Toast.makeText(CreatePost.this,"This is done"+downloadUrl,Toast.LENGTH_LONG).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreatePost.this,"This has failed",Toast.LENGTH_LONG).show();
                }
            });
            while (postImageUrl==null){

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mDatabase = database.getReference("posts").child(postId);

            Post post = new Post(postId,name,likes,postDate,postImageUrl,storyText,userID);

            mDatabase.setValue(post);

        }
    }
}

class Post{
    public String postId;
    public String author;
    public String likes;
    public String postDate;
    public String postImageUrl;
    public String storyText;
    public String userID;

    public Post() {

    }

    public Post(String postId, String author, String likes, String postDate, String postImageUrl, String storyText, String userID) {
        this.postId = postId;
        this.author = author;
        this.likes = likes;
        this.postDate = postDate;
        this.postImageUrl = postImageUrl;
        this.storyText = storyText;
        this.userID = userID;
    }
}
