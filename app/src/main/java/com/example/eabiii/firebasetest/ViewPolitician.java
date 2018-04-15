package com.example.eabiii.firebasetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import java.util.List;

/**
 * Created by eabiii on 08/03/2018.
 */

public class ViewPolitician extends AppCompatActivity {

    private TextView txtpName,txtpPosi,txtpParty,txtEmpty;
    private Button rateButton;
    private ImageView img;
    RatingBar rateBar;
    List<Float>dbRatings=new ArrayList<>();
    RecyclerView rvPolitician;
    String username="";
    float totalRating=0;
    FirebaseAuth mAuth;
    DatabaseReference dbRef;
    DatabaseReference dbUser;
    String POLI_KEY="";
    FirebaseRecyclerAdapter fAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_politician);
        POLI_KEY=getIntent().getExtras().get("Poli Name").toString();
        dbRef= FirebaseDatabase.getInstance().getReference().child("Politician");
        txtpName=findViewById(R.id.polName_Text);
        txtpParty=findViewById(R.id.polPosition_Text);
        txtpPosi=findViewById(R.id.partyList_Text);
        rvPolitician=findViewById(R.id.politicianView);
        rvPolitician.setLayoutManager(new LinearLayoutManager(this));
        rvPolitician.setItemAnimator(new DefaultItemAnimator());
        rvPolitician.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        LinearLayoutManager lm=new LinearLayoutManager(this);
        lm.setStackFromEnd(true);
        lm.setReverseLayout(true);
        rvPolitician.setLayoutManager(lm);
        rateBar=findViewById(R.id.ratingBar);
        img=findViewById(R.id.imagePoli);
        txtEmpty=findViewById(R.id.emptytext);

        dbRef.child(POLI_KEY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name=(String)dataSnapshot.child("name").getValue();
                String position=(String)dataSnapshot.child("position").getValue();
                String partylist=(String)dataSnapshot.child("partylist").getValue();
                String imgUrl=(String)dataSnapshot.child("image").getValue();
                txtpName.setText(name);
                txtpPosi.setText(position);
                txtpParty.setText(partylist);
                Picasso.with(ViewPolitician.this).load(imgUrl).fit().into(img);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rateButton=findViewById(R.id.btnRate);
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ViewPolitician.this,RatePolitician.class);
                intent.putExtra("Poli Info", POLI_KEY);
                startActivity(intent);
            }
        });
        Log.d("Poliname",POLI_KEY);
        mAuth= FirebaseAuth.getInstance();
        setupFirebase();
        checkChildren();
        computeRating();

    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d("TEST!","TEST123");
        DatabaseReference dbPostComment=FirebaseDatabase.getInstance().getReference().child("Politician").child(POLI_KEY).child("ratings");

        FirebaseRecyclerOptions<RateModel> options=new FirebaseRecyclerOptions.Builder<RateModel>().setQuery(dbPostComment,RateModel.class).build();
        Log.d("TEST2","TEST123");

        fAdapter=new FirebaseRecyclerAdapter<RateModel,RateViewHolder>(options) {
            @Override
            public RateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                Log.d("TEST3","TEST123");

                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_rate,parent,false);
                Log.d("TEST4","TEST123");

                return new RateViewHolder(view);

            }

            @Override
            protected void onBindViewHolder( RateViewHolder holder, int position,  RateModel model) {
                Log.d("TEST!","TEST123");
                holder.getTxtUsername().setText(model.getUsername());
                holder.getTxtComment().setText(model.getComment());
                holder.getTxtRating().setText("User Rating: "+Float.toString(model.getRating()));
                holder.setTxtTime(model.getTime());

            }
        };
        fAdapter.startListening();
        rvPolitician.setAdapter(fAdapter);
    }

    private void checkChildren(){
        dbRef=FirebaseDatabase.getInstance().getReference().child("Politician").child(POLI_KEY).child("ratings");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    rvPolitician.setVisibility(View.VISIBLE);
                    txtEmpty.setVisibility(View.INVISIBLE);
                }
                else{
                    rvPolitician.setVisibility(View.INVISIBLE);
                    txtEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void computeRating(){

        dbRef=FirebaseDatabase.getInstance().getReference().child("Politician").child(POLI_KEY);
        dbRef.child("ratings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                    String s=String.valueOf(dataSnapshot1.child("rating").getValue());
                    Log.d("String",s);
                    float rate=Float.parseFloat(s);
                    Log.d("Float",Float.toString(rate));
                    dbRatings.add(rate);


                }

                float test=0;
                for(int i=0;i<dbRatings.size();i++){
                    Log.d("dbvalue",Float.toString(dbRatings.get(i)));
                    test+=dbRatings.get(i);

                }
                totalRating=test/dbRatings.size();
                rateBar.setRating(totalRating);
                Log.d("VALUE",Float.toString(totalRating));
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setupFirebase(){
        final FirebaseUser firebaseUser=mAuth.getCurrentUser();
        String uName=firebaseUser.getEmail();
        dbUser=FirebaseDatabase.getInstance().getReference().child("Users").child(encodeString(uName)).child("username");
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
