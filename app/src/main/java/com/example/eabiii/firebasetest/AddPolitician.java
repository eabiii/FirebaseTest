package com.example.eabiii.firebasetest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddPolitician extends AppCompatActivity {


    private EditText name, position,partylist;
    private Button add;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    FirebaseAuth mAuth;
    Boolean b=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_politician);
        name=findViewById(R.id.editName);
        position=findViewById(R.id.editPosition);
        partylist=findViewById(R.id.editPartylist);
        mAuth=FirebaseAuth.getInstance();
        add=findViewById(R.id.btnAddPoli);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CLICK ME","CLICK");
                addPolitician();
            }
        });
        //dbRef=FirebaseDatabase.getInstance().getReference().child(pName);




    }


    private void addPolitician(){
        Log.d("ENTER","ENTER");
        final String pName=name.getText().toString().trim();
        final String pPosition=position.getText().toString().trim();
        final String pPartylist=partylist.getText().toString().trim();

        if(checkIfPoliticianExist(pName)){
            dbRef=FirebaseDatabase.getInstance().getReference().child("Politician").child(pName);
            final DatabaseReference addPol=dbRef.push();
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    addPol.child("name").setValue(pName);
                    addPol.child("position").setValue(pPosition);
                    addPol.child("partylist").setValue(pPartylist).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"Sucessfully Added!",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddPolitician.this,UserHomepage.class));
                        }
                    });




                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }





    }

    private boolean checkIfPoliticianExist(String pName){

        dbRef=FirebaseDatabase.getInstance().getReference().child("Politician").child(pName);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){

                    b=true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.d("TESTING",b.toString());
        return b;

    }
}
