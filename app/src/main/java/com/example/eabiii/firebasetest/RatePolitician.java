package com.example.eabiii.firebasetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by eabiii on 08/03/2018.
 */

public class RatePolitician extends AppCompatActivity {

    private RatingBar ratingBar;
    private TextView poliName,position,partylist;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    FirebaseAuth mAuth;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        ratingBar=findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                //get rating
            }
        });


    }



}
