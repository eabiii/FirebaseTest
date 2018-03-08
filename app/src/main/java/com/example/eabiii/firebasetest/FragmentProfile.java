package com.example.eabiii.firebasetest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

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
 * Created by eabiii on 06/03/2018.
 */

public class FragmentProfile extends Fragment {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser mCurrentUser;
    DatabaseReference dbRef;
    private String userName;
    private String fullName;

    private ArrayList<PostModel> pModel=new ArrayList<>();

    private TextView txt,mTextMessage,txtName;
    private FloatingActionButton fab;
    private Button post,logout,addPol;
    private RecyclerView recyclerview;
    private PostAdapter pAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState){

        View view=inflater.inflate(R.layout.fragment_profile,container,false);
        recyclerview=view.findViewById(R.id.profileView);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        //pAdapter=new PostAdapter(pModel);
        mTextMessage = (TextView) view.findViewById(R.id.message);
        txtName=view.findViewById(R.id.name_Text);
        fab=view.findViewById(R.id.fab);
        dbRef= FirebaseDatabase.getInstance().getReference().child("Post");
        String key=FirebaseDatabase.getInstance().getReference().child("Post").getKey();
        mAuth=FirebaseAuth.getInstance();
        txt=view.findViewById(R.id.txtName);
        post=view.findViewById(R.id.btnPost);
        mCurrentUser=mAuth.getCurrentUser();
        logout=view.findViewById(R.id.btnLogOut);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),PostActivity.class));
                Snackbar.make(view,"Add Post",Snackbar.LENGTH_LONG).setAction("Action",null).show();
            }
        });
        loadInfo();


        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

      //  Log.d("USERCURRENT",UserHomepage.userName);
        //mAuth.addAuthStateListener(mAuthListener);
        Query dbFinalRef=dbRef.orderByChild("username").equalTo(userName);
        FirebaseRecyclerOptions<PostModel> options=new FirebaseRecyclerOptions.Builder<PostModel>()
                .setQuery(dbFinalRef,PostModel.class).build();
        FirebaseRecyclerAdapter fAdapter=new FirebaseRecyclerAdapter<PostModel,FragmentPost.PostHolder>(options) {

            @Override
            public FragmentPost.PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_profile,parent,false);

                return new FragmentPost.PostHolder(view);
            }

            @Override
            protected void onBindViewHolder(FragmentPost.PostHolder holder, int position, PostModel model) {
                // model=pModel.get(position);
                final String POST_KEY=getRef(position).getKey().toString();
                Log.d("Post Key",POST_KEY);
                holder.getTxtTitle().setText(model.getTitle());
//                holder.getTxtDesc().setText(model.getDesc());
                holder.getTxtUser().setText(model.getUsername());
                //holder.getImgView(Picasso.with(holder.imgView.getContext()).load(model.getImage()).into(holder.imgView));
                Picasso.with(holder.imgView.getContext()).load(model.getImage()).into(holder.imgView);
                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("Post Key",POST_KEY);
                        Intent intent=new Intent(getActivity(),ViewSinglePost.class);
                        intent.putExtra("Post ID",POST_KEY);
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

    public static class PostHolder extends RecyclerView.ViewHolder{

        View v;
        TextView txtTitle,txtDesc,txtUser;
        ImageView imgView;
        public PostHolder(View itemView) {
            super(itemView);
            v=itemView;
            txtTitle=itemView.findViewById(R.id.post_title_txtview);
            txtDesc=itemView.findViewById(R.id.post_desc_txtview);
            txtUser=itemView.findViewById(R.id.post_user);
            imgView=itemView.findViewById(R.id.post_image);
            Log.d("UID",txtUser.getText().toString());

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
