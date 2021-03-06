package com.example.eabiii.firebasetest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by eabiii on 07/03/2018.
 */

public class FragmentPolitician extends Fragment {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser mCurrentUser;
    DatabaseReference dbRef;
    private String userName;
    private String fullName;
    private FloatingActionButton fab;
    private ArrayList<PostModel> pModel=new ArrayList<>();

    private TextView txt,mTextMessage,txtName,empty;
    private Button post,logout,addPol;
    private RecyclerView recyclerview;
    private PostAdapter pAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState){

        View view=inflater.inflate(R.layout.fragment_politician,container,false);
        empty=view.findViewById(R.id.internet_connect);
        recyclerview=view.findViewById(R.id.poliView);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(),LinearLayoutManager.VERTICAL));
        //pAdapter=new PostAdapter(pModel);
        mTextMessage = (TextView) view.findViewById(R.id.message);
        txtName=view.findViewById(R.id.name_Text);
        fab=view.findViewById(R.id.fab3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected()) {

                    startActivity(new Intent(getActivity(),AddPolitician.class));
                    getActivity().finish();

                    //
                }
                else{

                    AlertDialog alertDialog=new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setCancelable(false);
                    alertDialog.setTitle("Internet Connection");
                    alertDialog.setMessage("Please connect to the internet to access the content");
                    alertDialog.setButton(getActivity().getApplicationContext().getString(R.string.ok),new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        });
        dbRef= FirebaseDatabase.getInstance().getReference().child("Politician");
        String key=FirebaseDatabase.getInstance().getReference().child("Politician").getKey();
        mAuth=FirebaseAuth.getInstance();
        txt=view.findViewById(R.id.txtName);
        post=view.findViewById(R.id.btnPost);
        mCurrentUser=mAuth.getCurrentUser();
        logout=view.findViewById(R.id.btnLogOut);
        loadInfo();
        if (!isConnected()) {
            empty.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        //  Log.d("USERCURRENT",UserHomepage.userName);
        //mAuth.addAuthStateListener(mAuthListener);
        //Query dbFinalRef=dbRef.orderByChild("username").equalTo(userName);
        FirebaseRecyclerOptions<PoliticianModel> options=new FirebaseRecyclerOptions.Builder<PoliticianModel>()
                .setQuery(dbRef,PoliticianModel.class).build();
        FirebaseRecyclerAdapter fAdapter=new FirebaseRecyclerAdapter<PoliticianModel,PoliticianViewHolder>(options) {

            @Override
            public PoliticianViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_politician,parent,false);

                return new  PoliticianViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(PoliticianViewHolder holder, int position, PoliticianModel model) {
                // model=pModel.get(position);
                if (!isConnected()) {
                    empty.setVisibility(View.VISIBLE);
                    return;
                } else {
                    final String POST_KEY = getRef(position).getKey().toString();

                    Log.d("Poli Name", POST_KEY);
                    holder.getTxtPoli().setText(model.getName());
                    holder.getTxtParty().setText(model.getPosition());
                    holder.getTxtPos().setText(model.getPartylist());
                    Picasso.with(holder.imgView.getContext()).load(model.getImage()).into(holder.imgView);
                    holder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("Post Key", POST_KEY);
                            Intent intent = new Intent(getActivity(), ViewPolitician.class);
                            intent.putExtra("Poli Name", POST_KEY);
                            startActivity(intent);
                            getActivity().finish();

                        }
                    });
                }
            }
        };
        if(isConnected()) {
            fAdapter.startListening();
            recyclerview.setAdapter(fAdapter);
        }


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

    private boolean isConnected(){
        if(getActivity()!=null){
            ConnectivityManager connectivityManager=(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();

            if(networkInfo !=null && networkInfo.isConnected()){
                return true;
            }
            else{
                return false;
            }
        }
        return false;

    }




}
