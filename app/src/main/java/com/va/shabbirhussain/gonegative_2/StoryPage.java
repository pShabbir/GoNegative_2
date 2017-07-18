package com.va.shabbirhussain.gonegative_2;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
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
    private List<MyPost> arr,shadow;
    private RecyclerView recyclerView;
    private PostAdapter mAdapter;
    private ProgressBar progressBar;
    private BottomNavigation bottomNavigation;

    private FragmentActivity myContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view =  inflater.inflate(R.layout.activity_story_page, container, false);


        progressBar=(ProgressBar)view.findViewById(R.id.progressBar2);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setHasFixedSize(true);

        setHasOptionsMenu(true);

//        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
//            @Override
//            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
////                 do it
//                Intent i = new Intent(getActivity(),Chck.class);
//                MyPost a = arr.get(position);
//                i.putExtra("image",a.getPostImageUrl());
//                i.putExtra("postid",a.getPostId());
//                getActivity().startActivity(i);
//            }
//        });

        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
              //  Toast.makeText(getContext(),"This is long click",Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(),Chck.class);
                MyPost a = shadow.get(position);
                i.putExtra("image",a.getPostImageUrl());
                i.putExtra("postid",a.getPostId());
                i.putExtra("name",a.getAuthor());
                getActivity().startActivity(i);
                return true;
            }
        });

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                MyPost myPost = shadow.get(position);
//                Toast.makeText(getContext(),myPost.getDescription(),Toast.LENGTH_LONG).show();


                Intent i = new Intent(getActivity(),Description.class);
                i.putExtra("desc",myPost.getDescription());
                i.putExtra("uri",myPost.getPostImageUrl());
                i.putExtra("cost",myPost.getPrice()+"");
                i.putExtra("rating",myPost.getRating()+"");
                i.putExtra("locality",myPost.getLocality()+"");
                i.putExtra("recom",myPost.getRecommendation()+"");
                i.putExtra("address",myPost.getAddress());
                getActivity().startActivity(i);
//
//                FragmentManager fragmentManager = myContext.getFragmentManager();
//                DescriptionDialogue descriptionDialogue = new DescriptionDialogue();
//                Bundle f=new Bundle();
//                f.putString("desc",myPost.getDescription());
//                f.putString("uri",myPost.getPostImageUrl());
//                f.putString("cost",myPost.getPrice()+"");
//                f.putString("rating",myPost.getRating()+"");
//                f.putString("locality",myPost.getLocality());
//                f.putString("recom",myPost.getRecommendation());
//                descriptionDialogue.setArguments(f);
//                descriptionDialogue.show(fragmentManager,"This is PopTime");
            }
        });


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("posts");
        ref.limitToLast(20).addListenerForSingleValueEvent(
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
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        mAuth = FirebaseAuth.getInstance();

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               if(firebaseAuth.getCurrentUser()==null){
//                   Intent i=new Intent(getContext(),MainActivity.class);
//                   startActivity(i);
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
        shadow = new ArrayList<>();
        if(users == null){
//            arr.add(new MyPost("qwerty","Unkknown",0,"qazxswedcvfrtb","Null","Null","Null",0,"Null","Null",
//                    "Null"));
            return;
        }
        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

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
            String loc = (String)singleUser.get("address");
            arr.add(new MyPost(postId,author,price,postImageUrl,description,title,userID,rating,locality,food_type,recommendation,loc));
            shadow.add(new MyPost(postId,author,price,postImageUrl,description,title,userID,rating,locality,food_type,recommendation,loc));

        }



        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new PostAdapter(arr);
        recyclerView.setAdapter(mAdapter);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
//        System.out.println(phoneNumbers.toString());
        // Toast.makeText(getContext(),phoneNumbers.toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.filter_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.p1:
                filter(item.getTitle().toString());
//                Toast.makeText(getContext(),item.getTitle(),Toast.LENGTH_LONG).show();
                break;
            case R.id.p2:
                filter(item.getTitle().toString());
//                Toast.makeText(getContext(),item.getTitle(),Toast.LENGTH_LONG).show();
                break;
            case R.id.p3:
                filter(item.getTitle().toString());
//                Toast.makeText(getContext(),item.getTitle(),Toast.LENGTH_LONG).show();
                break;
            case R.id.veg:
                filterFoodType(item.getTitle().toString());
//                Toast.makeText(getContext(),item.getTitle(),Toast.LENGTH_LONG).show();
                break;
            case R.id.nonveg:
                filterFoodType(item.getTitle().toString());
//                Toast.makeText(getContext(),item.getTitle(),Toast.LENGTH_LONG).show();
                break;
            case R.id.q1:
                filterPrice(0,100);
//                Toast.makeText(getContext(),item.getTitle(),Toast.LENGTH_LONG).show();
                break;
            case R.id.q2:
                filterPrice(100,200);
//                Toast.makeText(getContext(),item.getTitle(),Toast.LENGTH_LONG).show();
                break;
            case R.id.q3:
                filterPrice(200,1000);
//                Toast.makeText(getContext(),item.getTitle(),Toast.LENGTH_LONG).show();
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(getContext(),MainActivity.class);
                startActivity(i);
                getActivity().finish();
                break;


        }



        return super.onOptionsItemSelected(item);
    }

    public void filterPrice(int low,int high){
        ArrayList<MyPost> arr2 = new ArrayList<>();
        for(int i=0;i<arr.size();i++){
            MyPost myPost=arr.get(i);
            int price = myPost.getPrice();
            if(price>low && price<=high )
                arr2.add(myPost);
        }

        if(!arr2.isEmpty()){
            shadow = arr2;
            mAdapter = new PostAdapter(shadow);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }else {
            Toast.makeText(getContext(),"No such item exist",Toast.LENGTH_LONG).show();
        }
    }

    public void filterFoodType(String filter){
        ArrayList<MyPost> arr2 = new ArrayList<>();
        for(int i=0;i<arr.size();i++){
            MyPost myPost=arr.get(i);
            if(myPost.getFood_type().equalsIgnoreCase(filter))
                arr2.add(myPost);
        }

        if(!arr2.isEmpty()){
            shadow = arr2;
            mAdapter = new PostAdapter(shadow);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }else {
            Toast.makeText(getContext(),"No such item exist",Toast.LENGTH_LONG).show();
        }

    }

    public void filter(String filter){
        ArrayList<MyPost> arr2 = new ArrayList<>();
        for(int i=0;i<arr.size();i++){
            MyPost myPost=arr.get(i);
            if(myPost.getLocality().equalsIgnoreCase(filter))
                arr2.add(myPost);
        }

        if(!arr2.isEmpty()){
            shadow = arr2;
            mAdapter = new PostAdapter(shadow);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }else {
            Toast.makeText(getContext(),"No such item exist",Toast.LENGTH_LONG).show();
        }

    }
}

