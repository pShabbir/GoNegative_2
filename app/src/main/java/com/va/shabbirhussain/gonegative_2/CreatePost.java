package com.va.shabbirhussain.gonegative_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class CreatePost extends Fragment  {

    private static final int GALLERY_INTENT = 201;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    private FirebaseStorage mStorage;
    private Uri uri,downloadUrl;
    private ImageView imageView;
    private String userID,postId,postImageUrl,postDate,likes,storyText,title,name;
    private SharedPreferences shared;
    private TextView txt,titletxt;
    private boolean myFlag = false;

    public static CreatePost newInstance() {
        CreatePost fragment = new CreatePost();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_create_post, container, false);

        imageView = (ImageView)view.findViewById(R.id.image);

        txt = (TextView)view.findViewById(R.id.story);
        titletxt = (TextView)view.findViewById(R.id.title);

        postDate = DateFormat.getDateTimeInstance().format(new Date());
        name=shared.getString("name","");
        likes="0";


        ImageView pickImage = (ImageView)view.findViewById(R.id.image);
        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

        Button button= (Button) view.findViewById(R.id.Submit);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new mySubmit().execute();
                if(myFlag){
                    Toast.makeText(getContext(),"This is post",Toast.LENGTH_LONG).show();
                    mDatabase = database.getReference("posts").child(postId);
                    storyText = txt.getText().toString();
                    title = titletxt.getText().toString();
                    Post post = new Post(postId,name,likes,postDate,postImageUrl,storyText,title,userID);

                    mDatabase.setValue(post);
                }

            }
        });



        return view;

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_create_post);

        shared = PreferenceManager.getDefaultSharedPreferences(getContext());

        database = FirebaseDatabase.getInstance();

        mStorage = FirebaseStorage.getInstance();


        userID = shared.getString("uid","");

        postId= database.getReference("users").push().getKey();

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
             uri = data.getData();
             imageView.setImageURI(uri);
            StorageReference storageRef = mStorage.getReference();

            StorageReference riversRef = storageRef.child("images").child(userID).child(postId+".jpg");

            riversRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadUrl = taskSnapshot.getDownloadUrl();
                    postImageUrl = downloadUrl.toString();
                    myFlag = true;
                    Toast.makeText(getContext(),"This is done"+downloadUrl,Toast.LENGTH_LONG).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),"This has failed",Toast.LENGTH_LONG).show();
                }
            });
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
    public String title;
    public String userID;


    public Post() {

    }

    public Post(String postId, String author, String likes, String postDate, String postImageUrl, String storyText,String title ,String userID) {
        this.postId = postId;
        this.author = author;
        this.likes = likes;
        this.postDate = postDate;
        this.postImageUrl = postImageUrl;
        this.storyText = storyText;
        this.title=title;
        this.userID = userID;

    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public void setStoryText(String storyText) {
        this.storyText = storyText;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPostId() {
        return postId;
    }

    public String getAuthor() {
        return author;
    }

    public String getLikes() {
        return likes;
    }

    public String getPostDate() {
        return postDate;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public String getStoryText() {
        return storyText;
    }

    public String getTitle() {
        return title;
    }

    public String getUserID() {
        return userID;
    }
}
