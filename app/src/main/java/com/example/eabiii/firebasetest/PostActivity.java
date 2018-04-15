package com.example.eabiii.firebasetest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity {

    private EditText title,desc;
    private Button post;
    private ImageButton img;
    private static final int GALLERY_CODE=2;
    Uri uri=null;
    StorageReference path;
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    DatabaseReference dbUser;
    FirebaseUser mCurrentUser;
    private String userName;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        post=findViewById(R.id.btnPost);
        img=findViewById(R.id.imageBtn);
        title=findViewById(R.id.txtTitle);
        desc=findViewById(R.id.txtDesc);
        mAuth=FirebaseAuth.getInstance();
        path= FirebaseStorage.getInstance().getReference();
        dbRef=FirebaseDatabase.getInstance().getReference().child("Post");
        progressDialog=new ProgressDialog(PostActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setTitle("Creating Post");
        mCurrentUser=mAuth.getCurrentUser();
        dbUser=FirebaseDatabase.getInstance().getReference().child("Users").child(encodeString(mCurrentUser.getEmail()));
        loadInfo();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_CODE);
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String dbTitle=title.getText().toString().trim();
                final String dbDesc=desc.getText().toString().trim();
                final String currUser=FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid()).child("username").toString();
                if(dbTitle.isEmpty()){
                    title.setError("Title is required");
                    title.requestFocus();
                    return;

                }
                if(dbDesc.isEmpty()){
                    desc.setError("Description is required");
                    desc.requestFocus();
                    return;

                }
                if(uri==null){
                    title.setError("Image is required");
                    title.requestFocus();
                    return;

                }
                else{
                    progressDialog.show();

                    try{

                    }catch(Exception e){}
                    StorageReference filepath=path.child("image").child(uri.getLastPathSegment());
                    filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            final Uri finalUri=taskSnapshot.getDownloadUrl();
                            String get="";
                            if(!finalUri.toString().isEmpty()){
                                get=finalUri.toString();
                            }

                            final String image=get;

                          //  dbRef=db.getInstance().getReference().child("Post").child(userName);
                            final DatabaseReference addPost=dbRef.push();
                            dbUser.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
/*
                                    Map newPost=new HashMap();
                                    newPost.put("title",dbTitle);
                                    newPost.put("desc",dbDesc);
                                    newPost.put("image",image);
                                    newPost.put("username",userName);
*/
                                   // Toast.makeText(getApplicationContext(),"Successfull Posted",Toast.LENGTH_SHORT).show();


                                    dbRef=db.getInstance().getReference().child("Filter Post").child(mCurrentUser.getUid());
                                    final DatabaseReference filterPost=dbRef.push();

                                    addPost.child("title").setValue(dbTitle);
                                    addPost.child("desc").setValue(dbDesc);
                                    addPost.child("image").setValue(image);
                                    addPost.child("time").setValue(ServerValue.TIMESTAMP);
                                    addPost.child("username").setValue(userName).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            filterPost.child("title").setValue(dbTitle);
                                            filterPost.child("desc").setValue(dbDesc);
                                            filterPost.child("image").setValue(image);
                                            filterPost.child("time").setValue(ServerValue.TIMESTAMP);
                                            filterPost.child("username").setValue(userName).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressDialog.dismiss();

                                                    Toast.makeText(getApplicationContext(),"Sucessfully Posted!",Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(PostActivity.this,UserHomepage.class));
                                                    PostActivity.this.finish();                                                }
                                            });

                                        }
                                    });
                                    progressDialog.dismiss();


                                    // addPost.child("username").setValue(dataSnapshot.child("username"))

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    progressDialog.dismiss();

                                }
                            });


                        }


                    });

                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==GALLERY_CODE && resultCode==RESULT_OK){
            uri=data.getData();
            img.setImageURI(uri);

        }

    }

    @Override
    public void onBackPressed(){
        Intent intent=new Intent(PostActivity.this,UserHomepage.class);
        startActivity(intent);
        finish();

    }



    private void loadInfo() {

        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        String user_id = user.getEmail();
        DatabaseReference dataRef = db.getReference("Users").child(encodeString(user_id)).child("username");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName = String.valueOf(dataSnapshot.getValue());
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
