package com.example.eabiii.firebasetest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    ProgressDialog progressDialog;
    private ImageButton img;
    private static final int GALLERY_CODE=2;
    Uri uri=null;
    StorageReference path;




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
                if(partylist.getText().toString().isEmpty()){
                    partylist.setError("Name of Partylist is required");
                    partylist.requestFocus();
                    return;

                }
                addPartylist();
            }
        });
        cancel=findViewById(R.id.btnBack);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddPartyList.this,UserHomepage.class));
            }
        });
        img=findViewById(R.id.imageParty);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_CODE);
            }
        });
        path= FirebaseStorage.getInstance().getReference();
        progressDialog=new ProgressDialog(AddPartyList.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setTitle("Adding Partylist");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==GALLERY_CODE && resultCode==RESULT_OK){
            uri=data.getData();
            img.setImageURI(uri);
        }

    }


    private void addPartylist(){
        Log.d("ENTER","ENTER");
        progressDialog.show();
        StorageReference filepath=path.child("partyImage").child(uri.getLastPathSegment());
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Uri finalUri=taskSnapshot.getDownloadUrl();
                String get="";
                if(!finalUri.toString().isEmpty()){
                    get=finalUri.toString();
                }
                final String image=get;
                final String pPartylist=partylist.getText().toString().trim();
                dbRef=FirebaseDatabase.getInstance().getReference().child("PartyList").child(pPartylist);
                Map newPost=new HashMap();
                //  newPost.put("name",pPartylist);
                newPost.put("partylist",pPartylist);
                newPost.put("image",image);
                dbRef.setValue(newPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Partylist Added!",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddPartyList.this,UserHomepage.class));
                        AddPartyList.this.finish();
                    }
                });
                /*
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()){
                            dbRef=FirebaseDatabase.getInstance().getReference().child("PartyList").child(pPartylist);
                            final DatabaseReference addPol=dbRef.push();
                            dbRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Map newPost=new HashMap();
                                    //  newPost.put("name",pPartylist);
                                    newPost.put("partylist",pPartylist);
                                    newPost.put("image",image);
                                    dbRef.setValue(newPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(),"Partylist Added!",Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(AddPartyList.this,UserHomepage.class));
                                            AddPartyList.this.finish();
                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                */
            }
        });





    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onBackPressed(){
        Intent intent=new Intent(AddPartyList.this,UserHomepage.class);
        startActivity(intent);
        finish();

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
