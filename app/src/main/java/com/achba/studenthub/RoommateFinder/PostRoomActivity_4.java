package com.achba.studenthub.RoommateFinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.achba.studenthub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import info.hoang8f.android.segmented.SegmentedGroup;

public class PostRoomActivity_4 extends AppCompatActivity {
    SegmentedGroup segmentRelationship, segmentPet, segmentSmoking, segmentClean, segmentNight;
    String relationShip="true", pets="true", smoking="true", clean="true", nightOwl="true";
    View focusView = null;
    EditText etAbout;
    String about;
    DatabaseReference databaseReferenceRoommate;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_room_4);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        segmentRelationship = findViewById(R.id.segmentRelationship);
        segmentPet = findViewById(R.id.segmentPet);
        segmentSmoking = findViewById(R.id.segmentSmoking);
        segmentClean = findViewById(R.id.segmentClean);
        segmentNight = findViewById(R.id.segmentNight);
        etAbout = findViewById(R.id.etAbout);
        firebaseAuth = FirebaseAuth.getInstance();

        //Relationship Segment
        segmentRelationship.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btnReationship_Yes) {
                    relationShip = "true";
                } else if (checkedId == R.id.btnReationship_No) {
                    relationShip = "false";
                }
            }
        });

        //Pet Segment
        segmentPet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btnPet_Yes) {
                    pets = "true";
                } else if (checkedId == R.id.btnPet_No) {
                    pets = "false";
                }
            }
        });

        //Smoking Segment
        segmentSmoking.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btnSmoking_Yes) {
                    smoking = "true";
                } else if (checkedId == R.id.btnSmoking_No) {
                    smoking = "false";
                }
            }
        });

        //Clean Segment
        segmentClean.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btnClean_Yes) {
                    clean = "true";
                } else if (checkedId == R.id.btnClean_No) {
                    clean = "false";
                }
            }
        });

        //Night Segment
        segmentNight.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btnNight_Yes) {
                    nightOwl = "true";
                } else if (checkedId == R.id.btnNight_No) {
                    nightOwl = "false";
                }
            }
        });
    }

    /*@Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }*/

    @Override
    public void onBackPressed() {
        return;
    }

    public void onClickNext(View view) {
        attemptNext();
    }

    private void attemptNext() {
        boolean cancel = false;
        about = etAbout.getText().toString();
        int charNumber = Integer.parseInt(String.valueOf(about.length()));

        if (TextUtils.isEmpty(about)) {
            etAbout.setError(getString(R.string.error_field_required));
            focusView = etAbout;
            cancel = true;
        }
        if (charNumber<40) {
            etAbout.setError("About Info must contain 40 or more characters.");
            focusView = etAbout;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
            Toast.makeText(this, "Incorrect Input.", Toast.LENGTH_SHORT).show();
        } else {
            uploadData();
        }
    }

    private void uploadData() {
        String userID = firebaseAuth.getCurrentUser().getUid();
        databaseReferenceRoommate = FirebaseDatabase.getInstance().getReference("Roommate").child(userID);

        HashMap dataMap = new HashMap();
        dataMap.put("relationShip", relationShip);
        dataMap.put("pets", pets);
        dataMap.put("smoking", smoking);
        dataMap.put("clean", clean);
        dataMap.put("nightOwl", nightOwl);
        dataMap.put("about", about);
        databaseReferenceRoommate.updateChildren(dataMap);

        Intent intent = new Intent(this, RoommateStartActivity.class);
        startActivity(intent);
    }
}
