package com.example.eabiii.firebasetest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewSinglePost extends AppCompatActivity {



    private ImageView img;
    private TextView postTitle,postDesc,txtEmpty;
    private ArrayList<CommentModel> commentModels=new ArrayList<>();
   // private Button addComment;
    private EditText editComment;
    private FloatingActionButton fabcomment;

    String username="";
    String userComment="";
    RecyclerView rvComment;
    String POST_KEY="";
    DatabaseReference dbRef;
    DatabaseReference dbComment;
    DatabaseReference dbUser;
    DatabaseReference dbSave;
    FirebaseAuth mAuth;
    FirebaseRecyclerAdapter fAdapter;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_post);
      //  final TextInputLayout til=findViewById(R.id.textInputLayout);
        img=findViewById(R.id.singleImageview);
        postTitle=findViewById(R.id.singleTitle);
        postDesc=findViewById(R.id.singleDesc);
        rvComment=findViewById(R.id.politicianView);
        rvComment.setLayoutManager(new LinearLayoutManager(this));
        rvComment.setItemAnimator(new DefaultItemAnimator());
        rvComment.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        dbRef= FirebaseDatabase.getInstance().getReference().child("Post");
        POST_KEY=getIntent().getExtras().get("Post ID").toString();
        mAuth=FirebaseAuth.getInstance();
        dbRef.child(POST_KEY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title= (String) dataSnapshot.child("title").getValue();
                String desc= (String) dataSnapshot.child("desc").getValue();

                String user= (String) dataSnapshot.child("username").getValue();
                String imgUrl=(String)dataSnapshot.child("image").getValue();
                postTitle.setText(title);
                postDesc.setText(desc);
                Picasso.with(ViewSinglePost.this).load(imgUrl).fit().into(img);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        setupFirebase();
        editComment=findViewById(R.id.txtComment);
        fabcomment=findViewById(R.id.fabcomment);
        txtEmpty=findViewById(R.id.emptytext);

        editComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(view instanceof EditText)){

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hideKeyboard();
                        }
                    });
                }
                editComment.setBackgroundResource(android.R.drawable.editbox_background_normal);
                editComment.requestFocus();
                InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editComment,0);
            }
        });

        fabcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userComment=editComment.getText().toString();
                if(validate()){
                    FirebaseDatabase dbKey=FirebaseDatabase.getInstance();
                    String uid=dbKey.getReference("Post").push().getKey();
                    dbSave=FirebaseDatabase.getInstance().getReference().child("Post").child(POST_KEY);
                    DatabaseReference dbfinal=dbSave.child("comments").child(uid);
//                    String saveComment=editComment.getText().toString().trim();
                    Map save=new HashMap();
                    save.put("username",username);
                    save.put("comment",userComment);
                    save.put("time", ServerValue.TIMESTAMP);
                    dbfinal.setValue(save);
                    fAdapter.notifyDataSetChanged();
                    editComment.setText("");


                }
            }
        });

        dbComment=FirebaseDatabase.getInstance().getReference().child("Comments");
        checkChildren();

        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //ActionBar actionBar=getActionBar();



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

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TEST!","TEST123");
        DatabaseReference dbPostComment=FirebaseDatabase.getInstance().getReference().child("Post").child(POST_KEY).child("comments");

        FirebaseRecyclerOptions<CommentModel>options=new FirebaseRecyclerOptions.Builder<CommentModel>().setQuery(dbPostComment,CommentModel.class).build();
        Log.d("TEST2","TEST123");

        fAdapter=new FirebaseRecyclerAdapter<CommentModel,CommentViewHolder>(options) {
            @Override
            public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                Log.d("TEST3","TEST123");

                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment,parent,false);
                Log.d("TEST4","TEST123");

                return new CommentViewHolder(view);

            }

            @Override
            protected void onBindViewHolder( CommentViewHolder holder, int position,  CommentModel model) {
                Log.d("TEST!","TEST123");
               // holder.getTxtUser().setText(model.getUsername());
               // holder.getTxtComment().setText(model.getComment());
                holder.setUsername(model.getUsername().toString());
                holder.setComment(model.getComment().toString());
                holder.setTxtTime(model.getTime());


            }
        };
        fAdapter.startListening();
        rvComment.setAdapter(fAdapter);

    }

@Override
    public void onBackPressed(){
        Intent intent=new Intent(ViewSinglePost.this,UserHomepage.class);
        startActivity(intent);
        finish();
    }

    private void checkChildren(){
        dbRef=FirebaseDatabase.getInstance().getReference().child("Post").child(POST_KEY).child("comments");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    rvComment.setVisibility(View.VISIBLE);
                    txtEmpty.setVisibility(View.INVISIBLE);
                }
                else{
                    rvComment.setVisibility(View.INVISIBLE);
                    txtEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean validate(){

        if(userComment.length()==0){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editComment.getWindowToken(), 0);
            displaySnackbar("Please Enter Something");
            return false;
        }
        return true;


    }

    private void hideKeyboard(){

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editComment.getWindowToken(), 0);    }

    private void displaySnackbar(String msg){
        Snackbar sb=Snackbar.make(findViewById(R.id.singlePost),msg, BaseTransientBottomBar.LENGTH_LONG);
        sb.show();

    }




}
