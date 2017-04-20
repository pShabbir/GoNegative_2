package com.va.shabbirhussain.gonegative_2;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProfilePage extends Fragment {

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
        imageV = (ImageView)view.findViewById(R.id.image);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String name=sharedPreferences.getString("name","Error");
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

    TextView nameV,emailV;
    ImageView imageV;
    SharedPreferences sharedPreferences;
    View v;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_profile_page);
        //v = getView();
//        nameV= (TextView)findViewById(R.id.name);
//        emailV = (TextView)getView().findViewById(R.id.email);
//        imageV = (ImageView)getView().findViewById(R.id.image);
//
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
//        String name=sharedPreferences.getString("name","Error");
//        String email=sharedPreferences.getString("email","Error");
//        String uid = sharedPreferences.getString("uid","Error");
//        String image = sharedPreferences.getString("pic","Error");
//
//        nameV.setText(name);
//        emailV.setText(email);
//
//        Picasso.with(getContext())
//                .load(sharedPreferences.getString("pic",""))
//                .placeholder(R.drawable.logo1)
//                .error(R.drawable.logo1)
//                .into(imageV);


    }
}
