package com.va.shabbirhussain.gonegative_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class CreatePost extends Fragment  {

    private static final int GALLERY_INTENT = 201;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    private FirebaseStorage mStorage;
    private Uri uri,downloadUrl;
    private ImageView imageView;
    private String userID,postId,postImageUrl,postDate,likes,storyText,title,name;
    private SharedPreferences shared;
    private TextView txt,titletxt;
    private boolean myFlag = false;
    private RatingBar ratingBar;
    private EditText price,recommendation;
    private int myprice;
    private Spinner spinner;
    String locality;
    String food_type="Veg";
    RadioButton radioButton,radioButton2;

    float myrating = 0;

    public static CreatePost newInstance() {
        CreatePost fragment = new CreatePost();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_create_post, container, false);

        imageView = (ImageView)view.findViewById(R.id.image);

        txt = (TextView)view.findViewById(R.id.story);
        titletxt = (TextView)view.findViewById(R.id.title);
        ratingBar = (RatingBar)view.findViewById(R.id.rating);
        price = (EditText)view.findViewById(R.id.price);
        recommendation = (EditText)view.findViewById(R.id.recommendation);
        radioButton = (RadioButton)view.findViewById(R.id.veg);
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    food_type="Veg";
            }
        });
        radioButton2 = (RadioButton)view.findViewById(R.id.nonveg);
        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    food_type="Non-Veg";
            }
        });
        postDate = DateFormat.getDateTimeInstance().format(new Date());
        name=shared.getString("name","");
        likes="0";

        //Addind Spinner
        spinner=(Spinner)view.findViewById(R.id.locality_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.location_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        //End Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locality = ""+parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                locality = "Null";
            }
        });

        ImageView pickImage = (ImageView)view.findViewById(R.id.image);
        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

        Button button= (Button) view.findViewById(R.id.Submit);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new mySubmit().execute();
                if(myFlag){
                    Toast.makeText(getContext(),"This is post",Toast.LENGTH_LONG).show();
                    mDatabase = database.getReference("posts").child(postId);
                    storyText = txt.getText().toString();
                    title = titletxt.getText().toString();
                    myprice = Integer.parseInt(price.getText().toString());
                    String recom = recommendation.getText().toString();
                    MyPost post = new MyPost(postId,name,myprice,postImageUrl,storyText,title,userID,myrating,locality,food_type,recom);

                    mDatabase.setValue(post);
                }

            }
        });


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                myrating =  rating;
            }
        });

        return view;

    }


    class Demo extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {



            return null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_create_post);
        shared = PreferenceManager.getDefaultSharedPreferences(getContext());
        database = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance();
        userID = shared.getString("uid","");
        postId= database.getReference("users").push().getKey();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
             uri = data.getData();
             imageView.setImageURI(uri);
            StorageReference storageRef = mStorage.getReference();

            StorageReference riversRef = storageRef.child("images").child(userID).child(postId+".jpg");

            riversRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadUrl = taskSnapshot.getDownloadUrl();
                    postImageUrl = downloadUrl.toString();
                    myFlag = true;
                    Toast.makeText(getContext(),"This is done"+downloadUrl,Toast.LENGTH_LONG).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),"This has failed",Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        RadioButton radioButton=(RadioButton)view.findViewById(R.id.veg);;
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.veg:
                if (checked)
                    // Pirates are the best
                    food_type = "Veg";
                    break;
            case R.id.nonveg:
                if (checked)
                    // Ninjas rule
                    food_type = "Non-Veg";
                    break;
        }
    }
}

class MyPost {
    public String postId;
    public String author;
    public int price;
    public String postImageUrl;
    public String description;
    public String title;
    public String userID;
    public Object rating;
    public String locality;
    public String food_type;
    public String recommendation;

    public String getRecommendation() {
        return recommendation;
    }

    public String getFood_type() {
        return food_type;
    }

    public String getLocality() {
        return locality;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public String getAuthor() {

        return author;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public Object getRating() {
        return rating;
    }


    public MyPost(String postId, String author, int price, String postImageUrl, String description, String title, String userID, Object rating,String locality
        ,String food_type,String recommendation) {
        this.postId = postId;
        this.author = author;
        this.price = price;
        this.postImageUrl = postImageUrl;
        this.description = description;
        this.title = title;
        this.userID = userID;
        this.rating = rating;
        this.locality=locality;
        this.food_type = food_type;
        this.recommendation = recommendation;
    }
}
