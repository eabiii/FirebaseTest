package com.example.eabiii.firebasetest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by eabiii on 09/04/2018.
 */

public class FragmentSettings extends Fragment {

    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    private String userName,fullName;
    private Button changePass,changeUser,logout;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        mAuth=FirebaseAuth.getInstance();
        mCurrentUser=mAuth.getCurrentUser();
        progressDialog.setMessage("Please Wait...");
        progressDialog.setTitle("Logging Out");
        loadInfo();
        changePass=view.findViewById(R.id.change_pass);
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),ChangePassword.class));
            }
        });
        changeUser=view.findViewById(R.id.change_username);
        changeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        logout=view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                mAuth.signOut();
                startActivity(new Intent(getActivity(),MainActivity.class));
                progressDialog.dismiss();
                getActivity().finish();
            }
        });
        return view;

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
//               txt.setText(userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dataRef=db.getReference("Users").child(encodeString(user_id)).child("full_name");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                fullName=String.valueOf(dataSnapshot.getValue());
//                txtName.setText(fullName);

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
