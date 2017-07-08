package com.va.shabbirhussain.gonegative_2;


import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StoryPage extends Fragment{

    public static StoryPage newInstance() {
        StoryPage fragment = new StoryPage();
        return fragment;
    }
    private Button signOut;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private List<MyPost> arr;
    private RecyclerView recyclerView;
    private PostAdapter mAdapter;
    private ProgressBar progressBar;
    private BottomNavigation bottomNavigation;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.activity_story_page, container, false);


        progressBar=(ProgressBar)view.findViewById(R.id.progressBar2);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setHasFixedSize(true);

//        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
//            @Override
//            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
////                 do it
//                Intent i = new Intent(getActivity(),Chck.class);
//                Post a = arr.get(position);
//                i.putExtra("image",a.getPostImageUrl());
//                i.putExtra("postid",a.getPostId());
//                getActivity().startActivity(i);
//            }
//        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("posts");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        collectPhoneNumbers((Map<String,Object>) dataSnapshot.getValue());
                        if(!arr.isEmpty())
                            mAdapter.notifyDataSetChanged();
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
      //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);


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



    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    private void collectPhoneNumbers(Map<String,Object> users) {


        //ArrayList<String> phoneNumbers = new ArrayList<>();
        arr=new ArrayList<>();

        if(users == null){
//            arr.add(new MyPost("qwerty","Unkknown",0,"qazxswedcvfrtb","Null","Null","Null",0,"Null","Null",
//                    "Null"));
            return;
        }
        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

//            //Get user map
//            Map singleUser = (Map) entry.getValue();
//            //Get phone field and append to list
//            String postId = (String) singleUser.get("postId");
//            String author = (String) singleUser.get("author");
//            String likes = (String) singleUser.get("likes");
//            String postDate = (String) singleUser.get("postDate");
//            String postImageUrl = (String) singleUser.get("postImageUrl");
//            String storyText = (String) singleUser.get("description");
//            String title = (String) singleUser.get("title");
//            String userID = (String) singleUser.get("userID");
            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            String author = (String) singleUser.get("author");
            String description = (String) singleUser.get("description");
            String postId = (String) singleUser.get("postId");
            String postImageUrl = (String) singleUser.get("postImageUrl");
            int price =  (int)(long) singleUser.get("price");
            Object rating =  singleUser.get("rating");
            String title = (String) singleUser.get("title");
            String userID = (String) singleUser.get("userID");
            String locality = (String)singleUser.get("locality");
            String food_type = (String)singleUser.get("food_type");
            String recommendation = (String)singleUser.get("recommendation");
            arr.add(new MyPost(postId,author,price,postImageUrl,description,title,userID,rating,locality,food_type,recommendation));

        }



        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new PostAdapter(arr);
        recyclerView.setAdapter(mAdapter);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);


        recyclerView.addOnItemTouchListener(new PostTouchListner(getContext(), recyclerView, new PostTouchListner.ClickListener() {


            @Override
            public void onClick(View view, int position) {
                // Post movie = arr.get(position);
//                Post p = arr.get(position);
//                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("posts").child(p.getPostId()).child("likes");
//
////                Toast.makeText(getContext(), " is selected!", Toast.LENGTH_SHORT).show();
//                if(checkLikes)
//                int li = Integer.parseInt(p.getLikes());
//                li++;
//                String s= String.valueOf(li);
//                mDatabase.setValue(s);
//                p.setLikes(s);
//                TextView likes = (TextView)view.findViewById(R.id.likescount);
//                likes.setText(s);

            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getContext(), "This is long press", Toast.LENGTH_SHORT).show();
            }
        }));




//        System.out.println(phoneNumbers.toString());
        // Toast.makeText(getContext(),phoneNumbers.toString(),Toast.LENGTH_LONG).show();
    }
}

