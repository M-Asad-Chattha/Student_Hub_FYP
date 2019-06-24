package com.achba.studenthub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;

public class ProfileActivity extends AppCompatActivity {
    TextView mName, mBio, mProgram, mSemester, mSection, mCampus;
    ImageView profileImg, coverImg;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String userID;
    ProgressDialog progress;
    ProgressBar progressBar;
    RelativeLayout layoutContainer;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        mName=findViewById(R.id.name);
        profileImg = findViewById(R.id.profileImg);
        mBio=findViewById(R.id.description);
        mProgram=findViewById(R.id.tv_firstProgram);
        mSemester =findViewById(R.id.tv_secondSemester);
        mSection=findViewById(R.id.tv_secondSection);
        mCampus = findViewById(R.id.tv_secondCampus);
        swipeRefreshLayout=findViewById(R.id.swipeRefresh);
        progressBar=findViewById(R.id.progressBar);
        layoutContainer=findViewById(R.id.layoutContainer);

        swipeRefreshLayout.setColorScheme(R.color.blue,
                R.color.colorBrand, R.color.colorSplash_large, R.color.colorDarkGray);
        swipeRefreshLayout.setProgressViewOffset (true, 10, 70);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        finish();
                        startActivity(getIntent());
                    }
                }, 1500);
            }
        });

        /*progress = new ProgressDialog(this);
        progress.setTitle("Please Wait...");
        progress.setMessage("Fetching UserSD Data...");
        progress.setCancelable(false);
        progress.show();*/

        firebaseAuth=FirebaseAuth.getInstance();
        userID=firebaseAuth.getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    retrieveData();
                }else{
                    progress.dismiss();
                    Toast.makeText(ProfileActivity.this, "Data retrieve Problem", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

//        retrieveData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        return true;
    }

    public void onClickEditProfile(View view) {
        Intent intent=new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    public void onClickDeleteAccount(View view) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Please Wait...");
        progress.setMessage("Processing your request...");
        progress.setCancelable(false);
        progress.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progress.dismiss();
                            Toast.makeText(ProfileActivity.this, "Delete account", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }else{
                            progress.dismiss();
                            Toast.makeText(ProfileActivity.this, "Something went wrong, Try Later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onClickSeeMore(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setMessage("UserSD hasn't added more About Info.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void retrieveData(){
        firebaseAuth=FirebaseAuth.getInstance();
        userID=firebaseAuth.getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String profileImageUrl=dataSnapshot.child("profileImageUrl").getValue(String.class);
                    Uri imageUrl = Uri.parse(profileImageUrl);
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String bio = dataSnapshot.child("bio").getValue(String.class);
                    String program = dataSnapshot.child("program").getValue(String.class);
                    String semester = dataSnapshot.child("semester").getValue(String.class);
                    String section = dataSnapshot.child("section").getValue(String.class);
                    String campus = dataSnapshot.child("campus").getValue(String.class);

                    mName.setText(name);
                    mBio.setText(bio);
                    mProgram.setText("Studies "+program+" at ");
                    mSemester.setText(semester);
                    mSection.setText(section);
                    mCampus.setText(campus);

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.profileimg_placeholder);
                    requestOptions.error(R.drawable.profileimg_placeholder);
                    Glide.with(getApplicationContext())
                            .setDefaultRequestOptions(requestOptions)
                            .load(imageUrl)
                            .into(profileImg);

                    /*progress.dismiss();*/
                    progressBar.setVisibility(View.GONE);
                    layoutContainer.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(ProfileActivity.this, "Data retrieve Problem", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }




}
