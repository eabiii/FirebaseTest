package com.example.eabiii.firebasetest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
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

public class UserHomepage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
//Firebase stuff
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser mCurrentUser;
    DatabaseReference dbRef;
    public  static String userName;
    private String fullName;

    private ArrayList<PostModel>pModel=new ArrayList<>();

    private TextView txt,txtBirth,txtName;
    private Button settings,logout,addPol;
    private RecyclerView recyclerview;
    private PostAdapter pAdapter;

    private FragmentStatePagerAdapter fragmentStatePagerAdapter;
    private ViewPager viewPager;
    private Button testing;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
      //  fragmentStatePagerAdapter=new FragmentAdapter(getSupportFragmentManager());
       // viewPager=findViewById(R.id.viewPager);
        txtName=findViewById(R.id.name_Text);
        txtBirth=findViewById(R.id.birthday_Text);
        BottomNavigationView btmNav=findViewById(R.id.navigation);
        btmNav.setOnNavigationItemSelectedListener(this);

        /*
        settings=findViewById(R.id.btnSettings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserHomepage.this,Settings.class));
            }
        });
        */
        mAuth=FirebaseAuth.getInstance();
        mCurrentUser=mAuth.getCurrentUser();
        loadInfo();

        //setupViewPager(viewPager);


        /*
        setContentView(R.layout.activity_profile);
        recyclerview=findViewById(R.id.recyclerView);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        //pAdapter=new PostAdapter(pModel);
        mTextMessage = (TextView) findViewById(R.id.message);
        txtName=findViewById(R.id.name_Text);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mAuth=FirebaseAuth.getInstance();
        txt=findViewById(R.id.txtName);
        post=findViewById(R.id.btnPost);
        mCurrentUser=mAuth.getCurrentUser();
        logout=findViewById(R.id.btnLogOut);
/*
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserHomepage.this,PostActivity.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(UserHomepage.this,MainActivity.class));
            }
        });
        addPol=findViewById(R.id.btnPolitician);
        addPol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserHomepage.this,AddPolitician.class));
            }
        });
        */
        dbRef=FirebaseDatabase.getInstance().getReference().child("Post");
        String key=FirebaseDatabase.getInstance().getReference().child("Post").getKey();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_posts);
       // this.setViewPager(2);
        loadFragment(new FragmentPost());

        //loadInfo();


    }
    @Override
    protected void onStart(){
        super.onStart();
        if(mAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this,MainActivity.class));

        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        if(!isConnected()){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Please Connect to the Internet");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    UserHomepage.this.finish();
                }
            });

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
    public void onBackPressed(){
       // Intent intent=new Intent(ViewPolitician.this,UserHomepage.class);
       // startActivity(intent);
        //finish();

    }



    private void setupViewPager(ViewPager viewPager){
        FragmentAdapter adapter=new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentProfile(),"Profile");
        adapter.addFragment(new FragmentPost(),"Post");
        viewPager.setAdapter(adapter);

    }

    public void setViewPager(int fragmentNumber){

        viewPager.setCurrentItem(fragmentNumber);
    }

    private void loadInfo(){

        final FirebaseUser user=mAuth.getCurrentUser();
        final FirebaseDatabase db=FirebaseDatabase.getInstance();
        String user_id=user.getEmail();
         DatabaseReference dataRef=db.getReference("Users").child(encodeString(user_id)).child("full_name");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName=String.valueOf(dataSnapshot.getValue());
                Log.d("Current user",userName);
               txtName.setText(userName);
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
               // txtName.setText(fullName);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dataRef=db.getReference("Users").child(encodeString(user_id)).child("username");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //fullName=String.valueOf(dataSnapshot.getValue());
                txtBirth.setText(String.valueOf(dataSnapshot.getValue()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    private String encodeString(String s){

        return s.replace(".", ",");
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
            return true;

        }
        return false;

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment=null;
       // this.setViewPager(2);
        switch (item.getItemId()){
            case R.id.navigation_dashboard:
                fragment=new FragmentProfile();
                break;
            case R.id.navigation_posts:
                fragment=new FragmentPost();
                break;
            case R.id.navigation_candidates:
                fragment=new FragmentPolitician();
                break;
            case R.id.navigation_partylist:
                fragment=new FragmentPartyList();
                break;
            case R.id.navigation_settings:
                fragment=new FragmentSettings();
                break;


        }
        return loadFragment(fragment);
    }


    public static class PostHolder extends RecyclerView.ViewHolder{

    View v;
          TextView txtTitle,txtDesc,txtUser;
          ImageView imgView;
        public PostHolder(View itemView) {
            super(itemView);
            v=itemView;
            txtTitle=itemView.findViewById(R.id.post_title_txtview);
          //  txtDesc=itemView.findViewById(R.id.post_desc_txtview);
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
        public void setImgUrl(Context context,String img){

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
