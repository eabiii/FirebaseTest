package com.example.eabiii.firebasetest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by eabiii on 06/03/2018.
 */

public class FragmentPost extends Fragment {


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

        View view=inflater.inflate(R.layout.fragment_post,container,false);
        recyclerview=view.findViewById(R.id.postView);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(),LinearLayoutManager.VERTICAL));
        //pAdapter=new PostAdapter(pModel);
        mTextMessage = (TextView) view.findViewById(R.id.message);
        txtName=view.findViewById(R.id.name_Text);
        empty=view.findViewById(R.id.internet_connect);

        dbRef= FirebaseDatabase.getInstance().getReference().child("Post");
        String key=FirebaseDatabase.getInstance().getReference().child("Post").getKey();
        mAuth=FirebaseAuth.getInstance();
        txt=view.findViewById(R.id.txtName);
        post=view.findViewById(R.id.btnPost);
        mCurrentUser=mAuth.getCurrentUser();
        logout=view.findViewById(R.id.btnLogOut);
        fab=view.findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected()) {

                    startActivity(new Intent(getActivity(),PostActivity.class));                }
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

        loadInfo();
        if (!isConnected()) {
            empty.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager lm=new LinearLayoutManager(getActivity());
        lm.setStackFromEnd(true);
        lm.setReverseLayout(true);
        recyclerview.setLayoutManager(lm);

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        if(mAuth.getCurrentUser()==null){

                startActivity(new Intent(getActivity(), MainActivity.class));


        }

        //mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerOptions<PostModel> options=new FirebaseRecyclerOptions.Builder<PostModel>()
                .setQuery(dbRef,PostModel.class).build();
        FirebaseRecyclerAdapter fAdapter=new FirebaseRecyclerAdapter<PostModel,PostHolder>(options) {

            @Override
            public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post,parent,false);

                return new PostHolder(view);
            }

            @Override
            protected void onBindViewHolder( PostHolder holder, int position,  PostModel model) {
                // model=pModel.get(position);
                if (!isConnected()) {
                    empty.setVisibility(View.VISIBLE);
                    return;
                }
                else {
                    final String POST_KEY = getRef(position).getKey().toString();
                    Log.d("Post Key", POST_KEY);
                    holder.getTxtTitle().setText(model.getTitle());
                    //holder.getTxtDesc().setText(model.getDesc());
                    holder.getTxtUser().setText(model.getUsername());
                    holder.setTxtTime(model.getTime());

                    //holder.getImgView(Picasso.with(holder.imgView.getContext()).load(model.getImage()).into(holder.imgView));
                    Picasso.with(holder.imgView.getContext()).load(model.getImage()).fit().into(holder.imgView);
                    holder.v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("Post Key", POST_KEY);
                            Intent intent = new Intent(getActivity(), ViewSinglePost.class);
                            intent.putExtra("Post ID", POST_KEY);
                            startActivity(intent);
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

    private String encodeString(String s){

        return s.replace(".", ",");
    }

    public static class PostHolder extends RecyclerView.ViewHolder{

        View v;
        TextView txtTitle,txtDesc,txtUser,txtTime;
        ImageView imgView;
        public PostHolder(View itemView) {
            super(itemView);
            v=itemView;
            txtTitle=itemView.findViewById(R.id.post_title_txtview);
          //  txtDesc=itemView.findViewById(R.id.post_desc_txtview);
            txtUser=itemView.findViewById(R.id.post_user);
            txtTime=itemView.findViewById(R.id.txtTime);
            imgView=itemView.findViewById(R.id.post_image);
            Log.d("UID",txtUser.getText().toString());

        }

        public TextView getTxtTime() {
            return txtTime;
        }

        public void setTxtTime(Long txttime) {
            SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            try{
                txtTime.setText(format.format(new Date(txttime)));
            } catch(Exception e){
                e.printStackTrace();
            }
        }

        public void setTitle(String title) {
            txtTitle.setText(title);

        }
        public void setDesc(String desc){
            txtDesc.setText(desc);

        }
        public void setUserName(String user){
            txtUser.setText(user);

        }
        public void setImgUrl(Context context, String img){

        }

        public ImageView getImgView(){return imgView;}

        public TextView getTxtTitle() {
            return txtTitle;
        }

        public TextView getTxtDesc() {
            return txtDesc;
        }

        public TextView getTxtUser() {
            return txtUser;
        }
    }


}




