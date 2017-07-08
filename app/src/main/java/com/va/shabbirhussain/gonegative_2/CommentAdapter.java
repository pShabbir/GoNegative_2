package com.va.shabbirhussain.gonegative_2;

/**
 * Created by Shabbir Hussain on 6/21/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;



import android.content.Context;
import android.renderscript.Sampler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Shabbir Hussain on 4/21/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder>  {

    private List<CommentClass> postList;




    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView comment;

        public MyViewHolder(View view){
            super(view);
            comment = (TextView)view.findViewById(R.id.comment);

        }
    }

    public CommentAdapter(List<CommentClass> post){
        postList = post;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.comment,parent,false);

        final MyViewHolder myViewHolder = new MyViewHolder(view);
        return  myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        CommentClass post = postList.get(position);
        holder.comment.setText(post.getComment());

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }




}

class CommentClass{
    private String name;
    private String comment;

    public CommentClass(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }
}
