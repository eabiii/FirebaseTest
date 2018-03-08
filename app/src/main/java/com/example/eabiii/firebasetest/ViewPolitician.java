package com.example.eabiii.firebasetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by eabiii on 08/03/2018.
 */

public class ViewPolitician extends AppCompatActivity {

    private TextView txtpName,txtpPosi,txtpParty;
    private Button rateButton;
    private ArrayList<PoliticianModel>politicianModels=new ArrayList<>();
    String username="";
    FirebaseAuth mAuth;
    DatabaseReference dbRef;
    DatabaseReference dbUser;
    String POLI_KEY="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_politician);
        POLI_KEY=getIntent().getExtras().get("Poli Name").toString();
        dbRef= FirebaseDatabase.getInstance().getReference().child("Politician");
        dbRef.child(POLI_KEY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name=(String)dataSnapshot.child("name").getValue();
                String position=(String)dataSnapshot.child("position").getValue();
                String partylist=(String)dataSnapshot.child("partylist").getValue();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        rateButton=findViewById(R.id.btnRate);
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ViewPolitician.this,RatePolitician.class);
                intent.putExtra("Poli Info", POLI_KEY);
                startActivity(intent);
            }
        });
        Log.d("Poliname",POLI_KEY);
        mAuth= FirebaseAuth.getInstance();
        setupFirebase();


    }

    @Override
    protected void onStart(){

        super.onStart();




    }

    private void setupFirebase(){
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
