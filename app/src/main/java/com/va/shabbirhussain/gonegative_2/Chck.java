package com.va.shabbirhussain.gonegative_2;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Chck extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter mAdapter;
    private List<CommentClass> arr;
    private String postid,author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chck);
        Bundle bundle=getIntent().getExtras();
        postid = bundle.getString("postid");
        author = bundle.getString("name");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setVisibility(View.GONE);
        // progressBar.setVisibility(View.VISIBLE);

        recyclerView.setHasFixedSize(true);



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("posts").child(postid).child("comments");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        collectPhoneNumbers((Map<String,Object>) dataSnapshot.getValue());
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });




    }

    public void submitComment(View view){
        EditText editText=(EditText)findViewById(R.id.commentText);
        String comment = editText.getText().toString();
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
        String name=sharedPreferences.getString("name","unknown");
        String uid = sharedPreferences.getString("uid","qwerrtyuuiop");

        CommentClass commentClass=new CommentClass(name,comment);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference  mDatabase = database.getReference("posts").child(postid).child("comments").child(uid);
        mDatabase.setValue(commentClass);
        finish();

    }
    private void collectPhoneNumbers(Map<String,Object> users) {

        //ArrayList<String> phoneNumbers = new ArrayList<>();
        arr=new ArrayList<>();

        if(users!=null){

            //iterate through each user, ignoring their UID
            for (Map.Entry<String, Object> entry : users.entrySet()){

                //Get user map
//            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//            String name= sharedPreferences.getString("name","unknown");
                Map singleUser = (Map) entry.getValue();
//            String
//            arr.add(comment);

                //Get phone field and append to list
                String name = (String) singleUser.get("name");
                String comment = (String) singleUser.get("comment");
//                Toast.makeText(this,comment,Toast.LENGTH_LONG).show();

//            String likes = (String) singleUser.get("likes");
//            String postDate = (String) singleUser.get("postDate");
//            String postImageUrl = (String) singleUser.get("postImageUrl");
//            String storyText = (String) singleUser.get("storyText");
//            String title = (String) singleUser.get("title");
//            String userID = (String) singleUser.get("userID");


                arr.add(new CommentClass(name,comment));

            }
        }else {
            arr.add(new CommentClass("",""));
        }



        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new CommentAdapter(arr);
        recyclerView.setAdapter(mAdapter);
        //progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);


        //recyclerView.addOnItemTouchListener(new PostTouchListner(getContext(), recyclerView, new PostTouchListner.ClickListener() {







//        System.out.println(phoneNumbers.toString());
        // Toast.makeText(getContext(),phoneNumbers.toString(),Toast.LENGTH_LONG).show();
    }

}
