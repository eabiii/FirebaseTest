package com.example.eabiii.firebasetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewPartylistMember extends AppCompatActivity {
    private TextView txtpParty,txtEmpty;
    private Button rateButton;
    private ImageView imageView;
    private ArrayList<PoliticianModel> politicianModels=new ArrayList<>();
    String username="";
    FirebaseAuth mAuth;
    DatabaseReference dbRef;
    DatabaseReference dbUser;
    RecyclerView rvPartyList;
    FirebaseRecyclerAdapter fAdapter;
    String POLI_KEY="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_partylist_member);
        txtpParty=findViewById(R.id.memberName);
        txtEmpty=findViewById(R.id.emptytext);
        POLI_KEY=getIntent().getExtras().get("Party List").toString();
        txtpParty.setText(POLI_KEY);
        Log.d("key",POLI_KEY);
        rvPartyList=findViewById(R.id.memberView);
        rvPartyList.setLayoutManager(new LinearLayoutManager(this));
        rvPartyList.setItemAnimator(new DefaultItemAnimator());
        imageView=findViewById(R.id.imageParty);
        dbRef=FirebaseDatabase.getInstance().getReference().child("PartyList").child(POLI_KEY);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imgUrl=(String)dataSnapshot.child("image").getValue();
                Log.d("IMGURL",imgUrl);
                Picasso.with(ViewPartylistMember.this).load(imgUrl).fit().into(imageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //getImage();
        checkChildren();

    }


    @Override
    protected void onStart(){
        super.onStart();
        Log.d("TEST!","TEST123");
        final DatabaseReference dbPostComment=FirebaseDatabase.getInstance().getReference().child("PartyList").child(POLI_KEY).child("members");

        FirebaseRecyclerOptions<PartyListMemberModel> options=new FirebaseRecyclerOptions.Builder<PartyListMemberModel>().setQuery(dbPostComment,PartyListMemberModel.class).build();
        Log.d("TEST2","TEST123");

        fAdapter=new FirebaseRecyclerAdapter<PartyListMemberModel,PartyListMemberViewHolder>(options) {
            @Override
            public PartyListMemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                Log.d("TEST3","TEST123");

                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_partylist_politician,parent,false);
                Log.d("TEST4","TEST123");


                return new PartyListMemberViewHolder(view);

            }

            @Override
            protected void onBindViewHolder( PartyListMemberViewHolder holder, int position,  PartyListMemberModel model) {
                Log.d("TEST!","TEST123");
                // holder.getTxtUser().setText(model.getUsername());

                 holder.getTxtPosition().setText(model.getPosition().toString());
                holder.getTxtParty().setText(model.getName().toString());
                Picasso.with(holder.imgView.getContext()).load(model.getImage()).into(holder.imgView);

                // holder.setComment(model.getComment().toString());

            }
        };
        fAdapter.startListening();
        rvPartyList.setAdapter(fAdapter);


    }

    private void getImage(){
        dbRef=FirebaseDatabase.getInstance().getReference().child("PartyList").child("image");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imgUrl=String.valueOf(dataSnapshot.getValue());
                Log.d("IMGURL",imgUrl);
                Picasso.with(ViewPartylistMember.this).load(imgUrl).fit().into(imageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent intent=new Intent(ViewPartylistMember.this,UserHomepage.class);
        startActivity(intent);
        finish();

    }


    private void checkChildren(){
        dbRef=FirebaseDatabase.getInstance().getReference().child("PartyList").child(POLI_KEY).child("members");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    rvPartyList.setVisibility(View.VISIBLE);
                    txtEmpty.setVisibility(View.INVISIBLE);
                }
                else{
                    rvPartyList.setVisibility(View.INVISIBLE);
                    txtEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void setupFirebase(){
        final FirebaseUser firebaseUser=mAuth.getCurrentUser();
        String uName=firebaseUser.getEmail();
        dbUser= FirebaseDatabase.getInstance().getReference().child("Users").child(encodeString(uName)).child("username");
        dbUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username=String.valueOf(dataSnapshot.getValue());
                //fAdapter.notifyDataSetChanged();
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
