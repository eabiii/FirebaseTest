package com.example.eabiii.firebasetest;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements View.OnClickListener{

    EditText emailAdd,password,confirmPassword,username,firstName,lastName;
    Button birthDate;
    DatePickerDialog.OnDateSetListener dateSetListener;
    ProgressDialog progressDialog;

   // String user_id;
    int b=0;


    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog=new ProgressDialog(Register.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setTitle("Registering User");
        emailAdd=(EditText)findViewById(R.id.email_Edit);
        password=(EditText)findViewById(R.id.pw_Edit);
        confirmPassword=(EditText)findViewById(R.id.pw2_Edit);
        firstName=(EditText)findViewById(R.id.fName_Edit);
        lastName=(EditText)findViewById(R.id.lName_Edit);
        username=(EditText)findViewById(R.id.uName_Edit);
        birthDate= findViewById(R.id.birthday);
        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(Register.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,dateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month=month+1;
                String date=month + "/" +day +"/" +year;
                birthDate.setText(date);
            }
        };

        Log.d("BDAY",birthDate.getText().toString());
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.register_Btn).setOnClickListener(this);






    }


    private void registerUser(){

       final String email=emailAdd.getText().toString().trim();
        final String pass=password.getText().toString().trim();
        String confirm=confirmPassword.getText().toString().trim();
        String user=username.getText().toString().trim();



        if(email.isEmpty()){

            emailAdd.setError("Email is required");
            emailAdd.requestFocus();
            return;
        }
         if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailAdd.setError("Enter a valid Email");
            emailAdd.requestFocus();
            return;

        }

        if(pass.isEmpty()){
            password.setError("Password is required");
            password.requestFocus();
            return;

        }
        if(pass.length()<6){
            password.setError("Minumum length is 6");
            password.requestFocus();
            return;

        }
        if(!pass.equals(confirm)){
            confirmPassword.setError("Passwords does not match");
            confirmPassword.requestFocus();
            Log.d("password",pass);
            Log.d("confirm",confirm);
            return;


        }
       // user_id=mAuth.getCurrentUser().getUid();
        dbRef=FirebaseDatabase.getInstance().getReference().child("Users").child(username.getText().toString().trim());
        final String u=username.getText().toString().trim();
//        DatabaseReference reference= (DatabaseReference) dbRef.child("Users").orderByChild("username").equalTo(u);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.show();
                if(!dataSnapshot.exists()){
                    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                String user_id=mAuth.getCurrentUser().getUid();
                                String user_name = username.getText().toString();
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(encodeString(email));
                                //String user_name = username.getText().toString();
                                String fName=firstName.getText().toString().trim();
                                String lName=lastName.getText().toString().trim();
                                String fullName=fName+" "+lName;
                                  String birth=birthDate.getText().toString().trim();
                                Map newPost = new HashMap();
                                newPost.put("username", user_name);
                                newPost.put("email",email);
                                newPost.put("pass",pass);
                                newPost.put("first_name",fName);
                                newPost.put("last_name",lName);
                                newPost.put("full_name",fullName);
                                newPost.put("birthday",birth);
                                current_user_db.setValue(newPost);
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "User Registered!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Register.this,MainActivity.class));
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Error Registering User!", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                }

                else{
                    progressDialog.dismiss();
                    username.setError("Username already Exists!");
                    username.requestFocus();
                    return;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private String encodeString(String s){

        return s.replace(".", ",");
    }


    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case R.id.register_Btn:
                registerUser();

                break;

        }


    }
}
