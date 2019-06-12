package com.achba.studenthub.RoommateFinder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.achba.studenthub.MessageActivity;
import com.achba.studenthub.Model.Roommate;
import com.achba.studenthub.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FindRoomProfileActivity extends AppCompatActivity {
    ImageView imageView;
    TextView aboutMe,roommate,rent, duration, availableDate, roomType, amenities, relationship, pets, smoking, clean,
            nightOwl, student, employer;
    Intent intent;
    String userID;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_room_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        imageView = findViewById(R.id.imageView);
        aboutMe = findViewById(R.id.tvAboutMeValue);
        roommate = findViewById(R.id.tvRoommate);
        rent = findViewById(R.id.tvRent);
        duration = findViewById(R.id.tvDuration);
        availableDate = findViewById(R.id.tvAvailableDate);
        roomType = findViewById(R.id.tvRoomType);
        amenities = findViewById(R.id.tvAmenities);
        relationship = findViewById(R.id.tvRelationship);
        pets = findViewById(R.id.tvPets);
        smoking = findViewById(R.id.tvSmoking);
        clean = findViewById(R.id.tvClean);
        nightOwl = findViewById(R.id.tvNight);
        student = findViewById(R.id.tvStudent);
        employer = findViewById(R.id.tvEmployer);

        intent = getIntent();
        userID = intent.getStringExtra("userID");

        reference = FirebaseDatabase.getInstance().getReference("Roommate").child(userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String relationShipValue, petsValue, smokingValue, nightOwlValue, cleanValue;
                Roommate user = dataSnapshot.getValue(Roommate.class);
                getSupportActionBar().setTitle(user.getName());
                Glide.with(getApplicationContext()).load(user.getImageURL()).into(imageView);
                aboutMe.setText(user.getAbout());
                roommate.setText(user.getRoommates()+" roommates");
                rent.setText("Rent Rs: "+user.getRent());
                availableDate.setText("Move in: "+ user.getAvailableDate());
                roomType.setText(user.getRoomType());
                // Amenities ll goes here
                relationShipValue = user.getRelationShip();
                if(relationShipValue.equals("true")){
                    relationship.setText("Enjoing Relationship");
                }else{
                    relationship.setText("Not in a Relationship So far");
                }
                petsValue = user.getPets();
                if(petsValue.equals("true")){
                    pets.setText("Pets are okay");
                }else{
                    pets.setText("Pets are not okay");
                }
                smokingValue = user.getSmoking();
                if(smokingValue.equals("true")){
                    smoking.setText("Smoking is okay");
                }else{
                    smoking.setText("Smoking is not okay");
                }
                nightOwlValue = user.getNightOwl();
                if(nightOwlValue.equals("true")){
                    nightOwl.setText("Night Owl");
                }else{
                    nightOwl.setText("Early riser");
                }
                cleanValue = user.getClean();
                if(cleanValue.equals("true")){
                    clean.setText("Clean freak");
                }else{
                    clean.setText("Not a clean freak");
                }
                student.setText(user.getEmployer());

                // Hobbieses ll goes here

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
}
