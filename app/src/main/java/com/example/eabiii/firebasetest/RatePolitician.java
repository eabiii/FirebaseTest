package com.example.eabiii.firebasetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by eabiii on 08/03/2018.
 */

public class RatePolitician extends AppCompatActivity {

    private RatingBar ratingBar;
    private TextView poliName,poliposition,polipartylist;
    private EditText comments;
    private Button rate,back;
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
        poliposition=findViewById(R.id.polPosition_Text);
        polipartylist=findViewById(R.id.partyList_Text);
        rate=findViewById(R.id.btnRate);
        comments=findViewById(R.id.editComment);

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase dbKey=FirebaseDatabase.getInstance();
                String uid=dbKey.getReference("Politician").push().getKey();
                dbRef=FirebaseDatabase.getInstance().getReference().child("Politician").child(poliName.getText().toString()).child("ratings").child(uid);

                String saveComment=comments.getText().toString().trim();
                Map newPost=new HashMap();
                newPost.put("username",username);
                newPost.put("comment",saveComment);
                newPost.put("rating",ratingNumber);
                newPost.put("time", ServerValue.TIMESTAMP);
                dbRef.setValue(newPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Politician Rated!",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(RatePolitician.this,ViewPolitician.class);
                        intent.putExtra("Poli Name",poliName.getText().toString().trim());
                        startActivity(intent);

                    }
                });
               // Snackbar.make(view,"Rate "+ratingNumber,Snackbar.LENGTH_LONG).setAction("Action",null).show();
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
        mAuth=FirebaseAuth.getInstance();
        dbRef= FirebaseDatabase.getInstance().getReference().child("Politician");
        dbRef.child(POLI_INFO_KEY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name=(String)dataSnapshot.child("name").getValue();
                String position=(String)dataSnapshot.child("position").getValue();
                String partylist=(String)dataSnapshot.child("partylist").getValue();
                poliName.setText(name);
                poliposition.setText(position);
                polipartylist.setText(partylist);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        back=findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RatePolitician.this,ViewPolitician.class);
                intent.putExtra("Poli Name", POLI_INFO_KEY);
                startActivity(intent);
            }
        });


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
