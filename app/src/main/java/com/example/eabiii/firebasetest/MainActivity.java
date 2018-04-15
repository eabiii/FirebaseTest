package com.example.eabiii.firebasetest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    EditText email, pass;
    TextView register;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        email=findViewById(R.id.email_Edit);
        pass=findViewById(R.id.pw_Edit);
        findViewById(R.id.login_Btn).setOnClickListener(this);
//        findViewById(R.id.register_Btn).setOnClickListener(this);
        register=findViewById(R.id.txtRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Register.class));
            }
        });
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setTitle("Logging In");
    }

    private String encodeString(String s){

        return s.replace(".", ",");
    }

    private void login() {

        if (isConnected()) {
            final String logEmail = email.getText().toString().trim();
            final String logPass = pass.getText().toString().trim();
            if (logEmail.isEmpty()) {

                email.setError("Email is required");
                email.requestFocus();
                return;
            }
            if (logPass.isEmpty()) {

                pass.setError("Password is required");
                pass.requestFocus();
                return;
            }
            progressDialog.show();
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users");


            dbRef.child(encodeString(logEmail)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        //  String username= (String) dataSnapshot.child("username").getValue();
                        //  String email=(String)dataSnapshot.child("email").getValue();
                        //  String pass=(String)dataSnapshot.child("pass").getValue();
                        // String USER_KEY
                        mAuth.signInWithEmailAndPassword(logEmail, logPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();

                                    Intent intent = new Intent(MainActivity.this, UserHomepage.class);
                                    startActivity(intent);
                                    MainActivity.this.finish();
                                } else {
                                    progressDialog.dismiss();
                                    email.setError("Invalid Email or Password!");
                                    email.requestFocus();


                                }

                            }
                        });

                    } else {
                        progressDialog.dismiss();
                        email.setError("Invalid Email or Password!");
                        email.requestFocus();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        else{
            email.setError("Please make sure you are connected to the internet.");
            email.requestFocus();


        }
    }

    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        else
            return false;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
           // case R.id.register_Btn:
              //  startActivity(new Intent(this,Register.class));
               // break;
            case R.id.login_Btn:
                login();


        }

    }
}
