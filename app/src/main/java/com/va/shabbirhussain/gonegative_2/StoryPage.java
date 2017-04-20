package com.va.shabbirhussain.gonegative_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koushikdutta.ion.Ion;

public class StoryPage extends AppCompatActivity {

    private Button signOut;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    TextView nameV,emailV;
    ImageView imageV;
    SharedPreferences sharedPreferences;


    //DB
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_page);

//        FirebaseApp.initializeApp(this);
        signOut = (Button)findViewById(R.id.signOut);
        nameV= (TextView)findViewById(R.id.name);
        emailV = (TextView)findViewById(R.id.email);
        imageV = (ImageView)findViewById(R.id.image);

        //DB Reference


         sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name=sharedPreferences.getString("name","Error");
        String email=sharedPreferences.getString("email","Error");
        String uid = sharedPreferences.getString("uid","Error");
        String image = sharedPreferences.getString("pic","Error");

        nameV.setText(name);
        emailV.setText(email);

        Ion.with(imageV)
                .error(R.color.colorPrimaryDark)
                .load(sharedPreferences.getString("pic",""));


        mAuth = FirebaseAuth.getInstance();

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               if(firebaseAuth.getCurrentUser()==null){
                   Intent i=new Intent(StoryPage.this,MainActivity.class);
                   startActivity(i);
                   finish();
               }
            }
        };
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });
        //DataBase Works here
        writeNewUser(name,email,uid,image);


    }

    private void writeNewUser(String name, String email,String uid,String urlToImage) {
        User user = new User(name, email,uid,urlToImage);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        mDatabase.child("users").child(uid).setValue(user);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    public void createPost(View view){
        Intent i = new Intent(this,CreatePost.class);
        startActivity(i);
    }
}
 class User {

    public String username;
    public String email;
    public String udi;
    public String urlToProfileImage;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email,String uid,String urlToProfileImage) {
        this.username = username;
        this.email = email;
        this.udi = uid;
        this.urlToProfileImage = urlToProfileImage;
    }

}
