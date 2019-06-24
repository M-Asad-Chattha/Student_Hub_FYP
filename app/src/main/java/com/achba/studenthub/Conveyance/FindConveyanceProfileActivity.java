package com.achba.studenthub.Conveyance;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.achba.studenthub.MessageActivity;
import com.achba.studenthub.Model.Conveyance;
import com.achba.studenthub.Model.Roommate;
import com.achba.studenthub.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

public class FindConveyanceProfileActivity extends AppCompatActivity {
  ImageView imageView;
    TextView seats, cost, address, phoneNumber;
    Intent intent;
    String userID;
    DatabaseReference reference;
    boolean isImageFitToScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_conveyance_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        imageView = findViewById(R.id.imageView);
        seats = findViewById(R.id.tvSeats);
        cost = findViewById(R.id.tvCost);
        address = findViewById(R.id.tvAddress);
        phoneNumber = findViewById(R.id.tvPhoneNumber);

        intent = getIntent();
        userID = intent.getStringExtra("userID");

        reference = FirebaseDatabase.getInstance().getReference("Conveyance").child(userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String relationShipValue, petsValue, smokingValue, nightOwlValue, cleanValue;
                Conveyance user = dataSnapshot.getValue(Conveyance.class);
                getSupportActionBar().setTitle(user.getName());

                Glide.with(getApplicationContext()).load(user.getProfileImageUrl()).into(imageView);
                seats.setText(user.getSeats()+" Available seats");
                cost.setText("Facility will "+user.getCost());
                address.setText(user.getLocation());
                phoneNumber.setText(user.getPhoneNumber());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        /*imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullScreen();
            }
        });*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void onClickMessage(View view) {
        Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
        intent.putExtra("userid", userID);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /*public void onClickPhoneNumber(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phoneNumber.getText().toString()));
        startActivity(intent);
    }*/

    public void onClickPhoneNumber(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);

        intent.setData(Uri.parse("tel:"+phoneNumber.getText().toString()));
        startActivity(intent);

    }

    public void onClickLocation(View view) {
        String map = "http://maps.google.co.in/maps?q=" + address.getText().toString();
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        startActivity(i);
    }
}
