package com.example.eabiii.firebasetest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPolitician extends AppCompatActivity {


    private EditText name, position,partylist;
    private Button add,back;
    private ImageButton img;
    private static final int GALLERY_CODE=2;
    Uri uri=null;
    StorageReference path;
    List<String> dbPartylist=new ArrayList<String>();
    Spinner spinner;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    FirebaseAuth mAuth;
    Boolean b=false;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_politician);
        name=findViewById(R.id.editName);
        back=findViewById(R.id.btnBack);
        position=findViewById(R.id.editPosition);
        img=findViewById(R.id.imageBtn);
      //  partylist=findViewById(R.id.editPartylist);
        mAuth=FirebaseAuth.getInstance();
        spinner=findViewById(R.id.editPartylist);
        add=findViewById(R.id.btnAddParty);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(name.getText().toString().isEmpty()){
                    name.setError("Name is required");
                    name.requestFocus();
                    return;

                }
                if(position.getText().toString().isEmpty()){
                    position.setError("Name is required");
                    position.requestFocus();
                    return;
                }
                if(uri==null){
                    name.setError("Image is required");
                    name.requestFocus();
                    return;
                }

                Log.d("CLICK ME","CLICK");
                addPolitician();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddPolitician.this,UserHomepage.class));
                finish();
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_CODE);
            }
        });
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DatabaseReference dbGetParty= FirebaseDatabase.getInstance().getReference();//.child("PartyList").child("partylist");
        dbGetParty.child("PartyList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                    String dbValue= String.valueOf(dataSnapshot1.child("partylist").getValue());
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
        progressDialog=new ProgressDialog(AddPolitician.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setTitle("Adding Politician");
        path= FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==GALLERY_CODE && resultCode==RESULT_OK){
            uri=data.getData();
            img.setImageURI(uri);
        }

    }

    @Override
    public void onBackPressed(){
        Intent intent=new Intent(AddPolitician.this,UserHomepage.class);
        startActivity(intent);
        finish();

    }




    private void addPolitician(){
        Log.d("ENTER","ENTER");
        progressDialog.show();
        final String pName=name.getText().toString().trim();
        final String pPosition=position.getText().toString().trim();
        final String pPartylist=spinner.getSelectedItem().toString();
        StorageReference filepath=path.child("pImage").child(uri.getLastPathSegment());
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Uri finalUri=taskSnapshot.getDownloadUrl();
                String get="";
                if(!finalUri.toString().isEmpty()){
                    get=finalUri.toString();
                }
                final String image=get;

                dbRef=FirebaseDatabase.getInstance().getReference().child("Politician").child(pName);
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()){
                            dbRef=FirebaseDatabase.getInstance().getReference().child("Politician").child(pName);
                            final DatabaseReference addPol=dbRef.push();
                            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    FirebaseDatabase dbKey=FirebaseDatabase.getInstance();
                                    final DatabaseReference dbParty=FirebaseDatabase.getInstance().getReference().child("PartyList").child(pPartylist).child("members").child(pName);
                                    Map newPost=new HashMap();
                                    newPost.put("name",pName);
                                    newPost.put("position",pPosition);
                                    newPost.put("partylist",pPartylist);
                                    newPost.put("image",image);
                                    dbRef.setValue(newPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            Map partyMap=new HashMap();
                                            partyMap.put("name",pName);
                                            partyMap.put("position",pPosition);
                                            partyMap.put("image",image);
                                            dbParty.setValue(partyMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Log.d("POLI ADDED",pName);
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(),"Politician Added!",Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(AddPolitician.this,UserHomepage.class));
                                                    AddPolitician.this.finish();
                                                }
                                            });
                                            //addPost.child("name").setValue(pName);
                                            //addPost.child("position").setValue(pPosition).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            //  @Override
                                            //public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });


                                    //}
                                    //     });



                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    progressDialog.dismiss();

                                }
                            });


                        }
                        progressDialog.dismiss();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });









    }


}
