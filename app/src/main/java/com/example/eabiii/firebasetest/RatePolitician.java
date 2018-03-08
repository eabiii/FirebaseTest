package com.example.eabiii.firebasetest;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by eabiii on 08/03/2018.
 */

public class RatePolitician extends AppCompatActivity {

    private RatingBar ratingBar;
    private TextView poliName,position,partylist;
    private Button rate;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    DatabaseReference dbUser;
    FirebaseAuth mAuth;
    String POLI_INFO_KEY="";
    String username="";
    float ratingNumber=0;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        poliName=findViewById(R.id.polName_Text);
        position=findViewById(R.id.polPosition_Text);
        partylist=findViewById(R.id.partyList_Text);
        rate=findViewById(R.id.btnRate);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view,"Rate "+ratingNumber,Snackbar.LENGTH_LONG).setAction("Action",null).show();
            }
        });
        ratingBar=findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingNumber=ratingBar.getRating();
                //get rating
            }
        });
        POLI_INFO_KEY=getIntent().getExtras().getString("Poli Info");


        setupFirebase();

    }


    @Override
    protected void onStart(){
        super.onStart();
    }

    private void setupFirebase(){
        mAuth=FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser=mAuth.getCurrentUser();
        String uName=firebaseUser.getEmail();
        dbUser=FirebaseDatabase.getInstance().getReference().child("Users").child(encodeString(uName)).child("username");
        dbUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username=String.valueOf(dataSnapshot.getValue());
                //fAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private String encodeString(String s){

        return s.replace(".", ",");
    }



}
