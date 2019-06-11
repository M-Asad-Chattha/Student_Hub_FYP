package com.achba.studenthub.RoommateFinder;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.achba.studenthub.MainActivity;
import com.achba.studenthub.Model.User;
import com.achba.studenthub.ProfileActivity;
import com.achba.studenthub.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

public class RoommateStartActivity extends AppCompatActivity {
    User user;
    CircularImageView imageView;
    DatabaseReference reference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roommate_start);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = findViewById(R.id.profile_image);

        firebaseAuth = FirebaseAuth.getInstance();
        String userID=firebaseAuth.getCurrentUser().getUid();

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.profileimg_placeholder);
                requestOptions.error(R.drawable.profileimg_placeholder);
                Glide.with(getApplicationContext())
                        .setDefaultRequestOptions(requestOptions)
                        .load(user.getProfileImageUrl())
                        .into(imageView);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void postRoom(View view) {
        Intent intent = new Intent(this, PostRoomActivity_1.class);
        startActivity(intent);
    }

    public void findRoom(View view) {
        Intent intent = new Intent(this, FindRoomActivity.class);
        startActivity(intent);
    }
}
