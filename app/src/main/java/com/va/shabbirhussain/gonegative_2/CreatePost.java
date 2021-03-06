package com.va.shabbirhussain.gonegative_2;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
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
    private EditText price,recommendation,address;
    private int myprice;
    private Spinner spinner,subLocality;
    String locality,recom,myAddress,sloc;
    String food_type="Veg";
    RadioButton radioButton,radioButton2;
    ProgressBar progressBar;
    float myrating = 0;
    private FragmentActivity myContext;
    int mCount;

    //Testting
    private boolean imageCheck=false,ratingCheck=false;


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
        address =(EditText)view.findViewById(R.id.address);
        progressBar = (ProgressBar)view.findViewById(R.id.mProgress);
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
        subLocality=(Spinner)view.findViewById(R.id.locality_spinner2);
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
                String s = ""+parent.getItemAtPosition(position);
                locality = s;
                populateSubSpinner(s);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                locality = "Other";
            }
        });

        subLocality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sloc=""+parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sloc="Other";
            }
        });

        ImageView pickImage = (ImageView)view.findViewById(R.id.image);
        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent,GALLERY_INTENT);
                Intent intent = CropImage.activity()
                        .getIntent(getContext());
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        Button button= (Button) view.findViewById(R.id.Submit);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new mySubmit().execute()

                if((txt.getText().toString().matches(""))){
                    Toast.makeText(getContext(),"Description is not filled",Toast.LENGTH_SHORT).show();
                }else if( (titletxt.getText().toString().matches(""))){
                    Toast.makeText(getContext(),"Title is not filled",Toast.LENGTH_SHORT).show();
                }else if((price.getText().toString().matches(""))){
                    Toast.makeText(getContext(),"Price is not filled",Toast.LENGTH_SHORT).show();
                }else if((recommendation.getText().toString().matches(""))){
                    Toast.makeText(getContext(),"Recommendation is not filled",Toast.LENGTH_SHORT).show();
                }else if((address.getText().toString().matches(""))){
                    Toast.makeText(getContext(),"Address is not filled",Toast.LENGTH_SHORT).show();
                }else if(!ratingCheck){
                    Toast.makeText(getContext(),"Raring is not filled",Toast.LENGTH_SHORT).show();
                }else{
                    final DatabaseReference upvotesRef = database.getReference("mvalue");
                    upvotesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mCount = dataSnapshot.getValue(Integer.class);
                            upvotesRef.setValue(mCount+1);
                            mCount++;
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    new Demo().execute();
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                myrating =  rating;
                ratingCheck=true;

            }


        });

        return view;

    }

    void populateSubSpinner(String s){
        ArrayAdapter<CharSequence> adapter ;
        switch (s){
            case "Delhi":
                adapter = ArrayAdapter.createFromResource(getContext(),
                        R.array.Delhi_L, android.R.layout.simple_spinner_item);
                break;
            case "Noida":
                adapter = ArrayAdapter.createFromResource(getContext(),
                        R.array.Noida_L, android.R.layout.simple_spinner_item);
                break;
            case "Greater Noida":
                adapter = ArrayAdapter.createFromResource(getContext(),
                        R.array.GNoida_L, android.R.layout.simple_spinner_item);
                break;
            default:
                adapter = ArrayAdapter.createFromResource(getContext(),
                        R.array.Other, android.R.layout.simple_spinner_item);
                break;
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subLocality.setAdapter(adapter);
    }


    class Demo extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            storyText = txt.getText().toString();
            title = titletxt.getText().toString();
            myprice = Integer.parseInt(price.getText().toString());
            recom = recommendation.getText().toString();
            myAddress = address.getText().toString();

        }

        @Override
        protected Void doInBackground(Void... voids) {


//
            //Posting image
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmap = imageView.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            Log.d("Shabbir",bitmap.getHeight()+"");
            byte[] data = baos.toByteArray();

            StorageReference storageRef = mStorage.getReference();
            StorageReference riversRef = storageRef.child("images").child(userID).child(postId+".jpg");

            UploadTask uploadTask = riversRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    postImageUrl =""+ taskSnapshot.getDownloadUrl();

                    mDatabase = database.getReference("posts").child(postId);
//                    storyText = txt.getText().toString();
//                    title = titletxt.getText().toString();
//                    myprice = Integer.parseInt(price.getText().toString());
//                    recom = recommendation.getText().toString();
                    MyPost post = new MyPost(postId,name,myprice,postImageUrl,storyText,title,userID,myrating,locality,
                            food_type,recom,myAddress,sloc,mCount);

                    mDatabase.setValue(post);






//                    imageView.setImageResource(R.mipmap.picholder);
//
//
//                    titletxt.setHint("Write your story here");
//                    txt.setHint("Give it a nice title");
//                    price.setHint("Enter the price");
//                    recommendation.setHint("Write your Recommendation here");
//                    ratingBar.setRating(0);
//
//                    onBackPressed();

                }
            });





            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.INVISIBLE);
            Fragment selectedFragment = CreatePost.newInstance();
            FragmentTransaction transaction = myContext.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout,selectedFragment);
            transaction.commit();
            Toast.makeText(getContext(),"Posted",Toast.LENGTH_LONG).show();


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



//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
//             uri = data.getData();
//             imageView.setImageURI(uri);
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                 uri = result.getUri();
                imageView.setImageURI(uri);
                imageCheck=true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
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
    public String sloc;
    public int mCount;

    public String getSloc() {
        return sloc;
    }

    public String getAddress() {
        return address;
    }

    public String address;

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

    public String getPostId() {
        return postId;
    }

    public MyPost(String postId, String author, int price, String postImageUrl, String description, String title, String userID, Object rating, String locality
        , String food_type, String recommendation,String address,String sloc,int mCount) {
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
        this.address=address;
        this.sloc=sloc;
        this.mCount=mCount;
    }
}
