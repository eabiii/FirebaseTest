package com.example.eabiii.firebasetest;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePassword extends AppCompatActivity {

    private Button confirm,back;
    public static EditText oldPass,newPass,confirmPass;
    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    DatabaseReference dbRef;
    ProgressDialog progressDialog;


    public  static String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        oldPass=findViewById(R.id.editOldPass);
        newPass=findViewById(R.id.editNewPass);
        confirmPass=findViewById(R.id.editNewPassConfirm);
        progressDialog=new ProgressDialog(ChangePassword.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setTitle("Changing Password");

        confirm=findViewById(R.id.btnChange);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(oldPass.getText().toString().isEmpty()){
                    oldPass.setError("Old Password is required!");
                    oldPass.requestFocus();
                    return;
                }
                if(newPass.getText().toString().isEmpty()){
                    newPass.setError("Old Password is required!");
                    newPass.requestFocus();
                    return;
                }
                if(confirmPass.getText().toString().isEmpty()){
                    confirmPass.setError("Old Password is required!");
                    confirmPass.requestFocus();
                    return;
                }
                if(newPass.getText().toString().length()<6){
                    newPass.setError("Minumum length is 6");
                    newPass.requestFocus();
                    return;

                }
                if(confirmPass.getText().toString().length()<6){
                    confirmPass.setError("Minumum length is 6");
                    confirmPass.requestFocus();
                    return;

                }
                changePassword();
            }
        });
        back=findViewById(R.id.btnCancel);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChangePassword.this,UserHomepage.class));
                finish();
            }
        });

        mAuth=FirebaseAuth.getInstance();
        mCurrentUser=mAuth.getCurrentUser();
        loadInfo();


    }

    private void loadInfo(){

        final FirebaseUser user=mAuth.getCurrentUser();
        final FirebaseDatabase db=FirebaseDatabase.getInstance();
        String user_id=user.getEmail();
        DatabaseReference dataRef=db.getReference("Users").child(encodeString(user_id)).child("username");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName=String.valueOf(dataSnapshot.getValue());
                Log.d("Current user",userName);
               // txtName.setText(userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dataRef=db.getReference("Users").child(encodeString(user_id)).child("full_name");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               // fullName=String.valueOf(dataSnapshot.getValue());
                // txtName.setText(fullName);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dataRef=db.getReference("Users").child(encodeString(user_id)).child("birthday");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //fullName=String.valueOf(dataSnapshot.getValue());
              //  txtBirth.setText(String.valueOf(dataSnapshot.getValue()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    private void changePassword(){
        progressDialog.show();

        AuthCredential credential= EmailAuthProvider.getCredential(mCurrentUser.getEmail(),oldPass.getText().toString().trim());
        mCurrentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    if(newPass.getText().toString().trim().equalsIgnoreCase(confirmPass.getText().toString().trim())){
                        mCurrentUser.updatePassword(newPass.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    oldPass.setText("");
                                    newPass.setText("");
                                    confirmPass.setText("");
                                    progressDialog.dismiss();

                                    Toast.makeText(getApplicationContext(), "Password Changed!", Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    oldPass.setText("");
                                    newPass.setText("");
                                    confirmPass.setText("");
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Error password not updated!", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "New Password and Confrim New Password does not match!", Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Old password does not match!", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    private String encodeString(String s){

        return s.replace(".", ",");
    }
}
