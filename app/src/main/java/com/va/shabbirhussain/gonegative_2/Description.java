package com.va.shabbirhussain.gonegative_2;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class Description extends AppCompatActivity {


    View view;
    TextView description,costtxt,locality,recom;
    RatingBar ratingBar;
    ImageView imageView;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        Bundle b=getIntent().getExtras();
        String desc = b.getString("desc");
        String uri = b.getString("uri");

        String cost = b.getString("cost");

        String rating =b.getString("rating");
        String loc= b.getString("locality");

        String r= b.getString("recom");

        String sloc=b.getString("sloc");

        address =b.getString("address");

        if(address==null)
            address=loc+" Greater Noida";


        description = (TextView)findViewById(R.id.descText);
        costtxt = (TextView)findViewById(R.id.cost);
        locality = (TextView)findViewById(R.id.locality);
        recom = (TextView)findViewById(R.id.recom);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar2);

        imageView = (ImageView)findViewById(R.id.descImage);


        Picasso.with(this)
                .load(uri)
                .placeholder(R.mipmap.placeholder)
                .error(R.mipmap.error)
                .fit()
                .into(imageView);


        description.setText(desc);
        costtxt.setText(cost);
        locality.setText(address);
        recom.setText(r);

        ratingBar.setRating(Float.parseFloat(rating));
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/product.ttf");
        description.setTypeface(custom_font);
        costtxt.setTypeface(custom_font);
        recom.setTypeface(custom_font);
        locality.setTypeface(custom_font);
    }

    public void takeMe(View view){
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q="+address));//Great India palace,Noida"));
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
