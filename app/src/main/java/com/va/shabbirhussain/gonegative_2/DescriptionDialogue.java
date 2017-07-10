package com.va.shabbirhussain.gonegative_2;

import android.app.DialogFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Shabbir Hussain on 6/14/2017.
 */

public class DescriptionDialogue extends DialogFragment implements View.OnClickListener {

    View view;
    TextView description,costtxt,locality,recom;
    RatingBar ratingBar;
    ImageView imageView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.description_layout,null);
        String desc = getArguments().getString("desc");
        String uri = getArguments().getString("uri");
        String cost = getArguments().getString("cost");
        String rating = getArguments().getString("rating");
        String loc= getArguments().getString("locality");
        String r= getArguments().getString("recom");

        description = (TextView)view.findViewById(R.id.descText);
        costtxt = (TextView)view.findViewById(R.id.cost);
        locality = (TextView)view.findViewById(R.id.locality);
        recom = (TextView)view.findViewById(R.id.recom);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar2);

        imageView = (ImageView)view.findViewById(R.id.descImage);
        imageView.setOnClickListener(this);

        Picasso.with(view.getContext())
                .load(uri)
                .placeholder(R.mipmap.placeholder)
                .error(R.mipmap.error)
                .fit()
                .into(imageView);

        description.setText(desc);
        costtxt.setText(cost);
        locality.setText(loc);
        recom.setText(r);
        ratingBar.setRating(Float.parseFloat(rating));
        Typeface custom_font = Typeface.createFromAsset(view.getContext().getAssets(),  "fonts/product.ttf");
        description.setTypeface(custom_font);
        costtxt.setTypeface(custom_font);
        recom.setTypeface(custom_font);
        locality.setTypeface(custom_font);


        return view;
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
    }
}
