package com.achba.studenthub.Conveyance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.achba.studenthub.MainActivity;
import com.achba.studenthub.R;
import com.achba.studenthub.RoommateFinder.FindRoomActivity;
import com.achba.studenthub.RoommateFinder.PostRoomActivity_1;

public class ConveyanceStartActivity extends AppCompatActivity {
    /*User user;
    CircularImageView imageView;
    DatabaseReference reference;
    private FirebaseAuth firebaseAuth;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conveyance_start);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*imageView = findViewById(R.id.profile_image);
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
        });*/

    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return true;
    }

    public void offerConveyance(View view) {
        Intent intent = new Intent(this, OfferConveyanceActivity.class);
        startActivity(intent);
    }

    public void findConveyance(View view) {
        Intent intent = new Intent(this, FindConveyanceActivity.class);
        startActivity(intent);
    }
}
