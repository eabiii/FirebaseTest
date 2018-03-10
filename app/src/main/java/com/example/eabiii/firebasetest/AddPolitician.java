package com.example.eabiii.firebasetest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPolitician extends AppCompatActivity {


    private EditText name, position,partylist;
    private Button add;
    List<String> dbPartylist=new ArrayList<String>();
    Spinner spinner;
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
      //  partylist=findViewById(R.id.editPartylist);
        mAuth=FirebaseAuth.getInstance();
        spinner=findViewById(R.id.editPartylist);
        add=findViewById(R.id.btnAddParty);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CLICK ME","CLICK");
                addPolitician();
            }
        });
        DatabaseReference dbGetParty= FirebaseDatabase.getInstance().getReference().child("PartyList");
        dbGetParty.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                    String dbValue= String.valueOf(dataSnapshot1.getValue());
                    Log.d("Party",dbValue);
                    dbPartylist.add(dbValue);

                }

                ArrayAdapter<String>partylistAdapter=new ArrayAdapter<String>(AddPolitician.this,android.R.layout.simple_spinner_item,dbPartylist);
                partylistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(partylistAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //dbRef=FirebaseDatabase.getInstance().getReference().child(pName);




    }


    private void addPolitician(){
        Log.d("ENTER","ENTER");
        final String pName=name.getText().toString().trim();
        final String pPosition=position.getText().toString().trim();
        final String pPartylist=spinner.getSelectedItem().toString();

        if(checkIfPoliticianExist(pName)){
            dbRef=FirebaseDatabase.getInstance().getReference().child("Politician").child(pName);
            final DatabaseReference addPol=dbRef.push();
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Map newPost=new HashMap();
                    newPost.put("name",pName);
                    newPost.put("position",pPosition);
                    newPost.put("partylist",pPartylist);
                    dbRef.setValue(newPost);
                    /*.addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"Sucessfully Added!",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddPolitician.this,UserHomepage.class));
                        }
                    });
*/

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
                dbRef=FirebaseDatabase.getInstance().getReference().child("PartyList").child(pPartylist);
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map newPost=new HashMap();
                        newPost.put(pName,pName);
                        dbRef.setValue(newPost).addOnCompleteListener(new OnCompleteListener<Void>() {
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
