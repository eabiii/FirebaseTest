package com.example.eabiii.firebasetest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Settings extends AppCompatActivity {



    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser mCurrentUser;
    ProgressDialog progressDialog;
    private Button changePass,logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth=FirebaseAuth.getInstance();
        mCurrentUser=mAuth.getCurrentUser();
        progressDialog=new ProgressDialog(Settings.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setTitle("Logging Out");
        logout=findViewById(R.id.btnLogOut);
        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                progressDialog.show();
                mAuth.signOut();
                startActivity(new Intent(Settings.this,MainActivity.class));
                progressDialog.dismiss();
                finish();
            }
        });
        changePass=findViewById(R.id.btnChangePassword);
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this,ChangePassword.class));
            }
        });
    }


    @Override
    protected void onStart(){

        super.onStart();

    }


}
