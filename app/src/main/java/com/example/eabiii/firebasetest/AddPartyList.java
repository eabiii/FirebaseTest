package com.example.eabiii.firebasetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by eabiii on 10/03/2018.
 */

public class AddPartyList extends AppCompatActivity {

    private EditText partylist;
    private Button add,cancel;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    FirebaseAuth mAuth;
    Boolean b=false;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_partylist);
        partylist=findViewById(R.id.editPartyList);
        mAuth=FirebaseAuth.getInstance();
        add=findViewById(R.id.btnAddParty);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CLICK ME","CLICK");
                addPartylist();
            }
        });
        cancel=findViewById(R.id.btnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddPartyList.this,UserHomepage.class));
            }
        });


    }


    private void addPartylist(){
        Log.d("ENTER","ENTER");
        final String pPartylist=partylist.getText().toString().trim();

        if(checkIfPartylistExist(pPartylist)){
          //  String key=FirebaseDatabase.getInstance().getReference().child("PartyList").child(pPartylist);
            dbRef=FirebaseDatabase.getInstance().getReference().child("PartyList").child(pPartylist);

            final DatabaseReference addPol=dbRef.push();
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                   Map newPost=new HashMap();
                  //  newPost.put("name",pPartylist);
                    newPost.put("partylist",pPartylist);
                    dbRef.setValue(newPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"Sucessfully Added!",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddPartyList.this,UserHomepage.class));
                        }
                    });




                    //addPol.child("name").setValue(pName);
                    //addPol.child("position").setValue(pPosition);
                    /*
                    addPol.child("partylist").setValue(pPartylist).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"Sucessfully Added!",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddPolitician.this,UserHomepage.class));
                        }
                    });
*/



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }





    }

    private boolean checkIfPartylistExist(String pPartylist){

        dbRef=FirebaseDatabase.getInstance().getReference().child("Partylist").child(pPartylist);
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
