package com.va.shabbirhussain.gonegative_2;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;



public class ProfilePage extends Fragment {


    TextView nameV,emailV;
    CircularImageView imageV;
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
        String uid = sharedPreferences.getString("uid","Error");
        String image = sharedPreferences.getString("pic","Error");

        nameV.setText(name);
        emailV.setText(email);

        Picasso.with(getContext())
                .load(sharedPreferences.getString("pic",""))
                .placeholder(R.drawable.logo1)
                .error(R.drawable.logo1)
                .into(imageV);


        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
