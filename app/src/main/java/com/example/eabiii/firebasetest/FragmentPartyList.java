package com.example.eabiii.firebasetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by eabiii on 07/03/2018.
 */

public class FragmentPartyList extends Fragment {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser mCurrentUser;
    DatabaseReference dbRef;
    private String userName;
    private String fullName;
    private FloatingActionButton fab;
    private ArrayList<PostModel> pModel=new ArrayList<>();

    private TextView txt,mTextMessage,txtName;
    private Button post,logout,addPol;
    private RecyclerView recyclerview;
    private PostAdapter pAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState){

        View view=inflater.inflate(R.layout.fragment_partylist,container,false);
        recyclerview=view.findViewById(R.id.poliView);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        //pAdapter=new PostAdapter(pModel);
        mTextMessage = (TextView) view.findViewById(R.id.message);
        txtName=view.findViewById(R.id.name_Text);
      /*  fab=view.findViewById(R.id.fab3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),AddPolitician.class));
            }
        });
        */
        dbRef= FirebaseDatabase.getInstance().getReference().child("Politician");
        String key=FirebaseDatabase.getInstance().getReference().child("Politician").getKey();
        mAuth=FirebaseAuth.getInstance();
        txt=view.findViewById(R.id.txtName);
        post=view.findViewById(R.id.btnPost);
        mCurrentUser=mAuth.getCurrentUser();
        logout=view.findViewById(R.id.btnLogOut);
        loadInfo();
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
