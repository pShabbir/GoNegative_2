package com.va.shabbirhussain.gonegative_2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StoryPage extends Fragment {

    public static StoryPage newInstance() {
        StoryPage fragment = new StoryPage();
        return fragment;
    }
    private Button signOut;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private List<Post> arr;
    private RecyclerView recyclerView;
    private PostAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_story_page, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("posts");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        collectPhoneNumbers((Map<String,Object>) dataSnapshot.getValue());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_story_page);


        mAuth = FirebaseAuth.getInstance();

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               if(firebaseAuth.getCurrentUser()==null){
                   Intent i=new Intent(getContext(),MainActivity.class);
                   startActivity(i);
                   //finish();
               }
            }
        };



    }

    private void collectPhoneNumbers(Map<String,Object> users) {

        //ArrayList<String> phoneNumbers = new ArrayList<>();
         arr=new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
             String postId = (String) singleUser.get("postId");
             String author = (String) singleUser.get("author");
             String likes = (String) singleUser.get("likes");
             String postDate = (String) singleUser.get("postDate");
             String postImageUrl = (String) singleUser.get("postImageUrl");
             String storyText = (String) singleUser.get("storyText");
             String title = (String) singleUser.get("title");
             String userID = (String) singleUser.get("userID");

             arr.add(new Post(postId,author,likes,postDate,postImageUrl,storyText,title,userID));

        }



        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new PostAdapter(arr);
        recyclerView.setAdapter(mAdapter);



//        recyclerView.addOnItemTouchListener(new PostTouchListner(getContext(), recyclerView, new PostTouchListner.ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//               // Post movie = arr.get(position);
//                Toast.makeText(getContext(), " is selected!", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//                Toast.makeText(getContext(), "This is long press", Toast.LENGTH_SHORT).show();
//            }
//        }));




//        System.out.println(phoneNumbers.toString());
       // Toast.makeText(getContext(),phoneNumbers.toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }


}

