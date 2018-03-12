package com.example.eabiii.firebasetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
        recyclerview=view.findViewById(R.id.partyView);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(),LinearLayoutManager.VERTICAL));
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

        String key=FirebaseDatabase.getInstance().getReference().child("PartyList").getKey();
        dbRef= FirebaseDatabase.getInstance().getReference().child("PartyList");
        mAuth=FirebaseAuth.getInstance();
        txt=view.findViewById(R.id.txtName);
        post=view.findViewById(R.id.btnPost);
        mCurrentUser=mAuth.getCurrentUser();
        logout=view.findViewById(R.id.btnLogOut);
        loadInfo();

        fab=view.findViewById(R.id.fab4);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),AddPartyList.class));
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<PartylistModel> options=new FirebaseRecyclerOptions.Builder<PartylistModel>()
                .setQuery(dbRef,PartylistModel.class).build();
        FirebaseRecyclerAdapter fAdapter=new FirebaseRecyclerAdapter<PartylistModel,PartylistViewHolder>(options) {

            @Override
            public PartylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_partylist,parent,false);

                return new  PartylistViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(PartylistViewHolder holder, int position, PartylistModel model) {
                // model=pModel.get(position);
                final String PARTY_KEY=getRef(position).getKey().toString();

                Log.d("Party Name",PARTY_KEY);
              //  holder.getTxtPoli().setText(model.getName());
                holder.getTxtParty().setText(model.getPartylist());
              //  holder.getTxtPos().setText(model.getPartylist());
                //  Picasso.with(holder.imgView.getContext()).load(model.getImage()).into(holder.imgView);


                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("Post Key",PARTY_KEY);
                        Intent intent=new Intent(getActivity(),ViewPartylistMember.class);
                        intent.putExtra("Party List",PARTY_KEY);

                        startActivity(intent);
                    }
                });

            }
        };
        fAdapter.startListening();
        recyclerview.setAdapter(fAdapter);

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
