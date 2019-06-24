package com.achba.studenthub.Conveyance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.achba.studenthub.Model.User;
import com.achba.studenthub.R;
import com.achba.studenthub.RoommateFinder.RoommateStartActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import info.hoang8f.android.segmented.SegmentedGroup;

public class OfferConveyanceActivity extends AppCompatActivity {
    SegmentedGroup segmentCost, segmentLocation;
    String cost="free", location="Gujrat,Pakistan", address, phoneNumber, seats;
    View focusView = null;
    EditText etAddress, etPhoneNumber, etSeats;
    String about, id, name, profileImageUrl;
    DatabaseReference databaseReferenceConveyance, databaseReferenceNotification;
    FirebaseAuth firebaseAuth;
    String date, time, timeStamp;
    RelativeLayout layoutAddress;
    Boolean locationStatus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_conveyance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        segmentCost = findViewById(R.id.segmentCost);
        segmentLocation = findViewById(R.id.segmentLocation);
        etAddress = findViewById(R.id.etAddress);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etSeats = findViewById(R.id.etSeats);
        layoutAddress = findViewById(R.id.layoutAddress);

        firebaseAuth = FirebaseAuth.getInstance();

        //Relationship Segment
        segmentCost.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btnCost_Free) {
                    cost = "true";
                } else if (checkedId == R.id.btnCost_Paid) {
                    cost = "paid";
                }
            }
        });

        //Pet Segment
        segmentLocation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btnLocation_Custom) {
                    layoutAddress.setVisibility(View.VISIBLE);
                    locationStatus = true;
                    location = "custom";
                } else if (checkedId == R.id.btnLocation_Current) {
                    layoutAddress.setVisibility(View.GONE);
                    locationStatus = false;
                    location = "current";
                    etAddress.setText("Gujrat,Pakistan");
                }
            }
        });


        userInfo();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    public void onClickDone(View view) {
        attemptDone();
    }

    private void attemptDone() {
        boolean cancel = false;
        address = etAddress.getText().toString();
        phoneNumber = etPhoneNumber.getText().toString();
        seats = etSeats.getText().toString();

        if (TextUtils.isEmpty(phoneNumber)) {
            etPhoneNumber.setError(getString(R.string.error_field_required));
            focusView = etPhoneNumber;
            cancel = true;
        }
        if (TextUtils.isEmpty(seats)) {
            etSeats.setError(getString(R.string.error_field_required));
            focusView = etSeats;
            cancel = true;
        }
        if (locationStatus) {
            if (TextUtils.isEmpty(address)) {
                etAddress.setError(getString(R.string.error_field_required));
                focusView = etAddress;
                cancel = true;
            }else {
                this.address = address;
            }
        }

        if (cancel) {
            focusView.requestFocus();
            Toast.makeText(this, "Incorrect Input.", Toast.LENGTH_SHORT).show();
        } else {
            uploadData();
        }
    }

    public void userInfo(){
        String userID=firebaseAuth.getCurrentUser().getUid();
        id = userID;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                name = user.getName();
                profileImageUrl = user.getProfileImageUrl();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void uploadNotificationData(){
        String userID = firebaseAuth.getCurrentUser().getUid();
        databaseReferenceNotification = FirebaseDatabase.getInstance().getReference("Notifications").push();

        HashMap dataMap = new HashMap();
        dataMap.put("id", userID);
        dataMap.put("timeStamp", timeStamp);
        dataMap.put("description", " is interested to offer conveyance facility.");
        dataMap.put("name", name);
        dataMap.put("status", "not seen");
        dataMap.put("profileImageUrl", profileImageUrl);
        dataMap.put("type", "Conveyance");
        databaseReferenceNotification.setValue(dataMap);
    }

    private void uploadData() {
        uploadNotificationData();
        String userID = firebaseAuth.getCurrentUser().getUid();
        databaseReferenceConveyance = FirebaseDatabase.getInstance().getReference("Conveyance").child(userID);

        HashMap dataMap = new HashMap();
        dataMap.put("id", id);
        dataMap.put("name", name);
        dataMap.put("profileImageUrl", profileImageUrl);
        dataMap.put("address", address);
        dataMap.put("phoneNumber", phoneNumber);
        dataMap.put("seats", seats);
        dataMap.put("location", location);
        dataMap.put("cost", cost);
        databaseReferenceConveyance.updateChildren(dataMap);

        onClickFinish();
    }

    public void onClickFinish() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OfferConveyanceActivity.this);
        builder.setMessage("Room Successfully added.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(OfferConveyanceActivity.this, ConveyanceStartActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
