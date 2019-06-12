package com.achba.studenthub.RoommateFinder;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.achba.studenthub.MainActivity;
import com.achba.studenthub.Model.User;
import com.achba.studenthub.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import info.hoang8f.android.segmented.SegmentedGroup;

public class PostRoomActivity_1 extends AppCompatActivity {
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    EditText etBirthday, etEmployer;
    View focusView = null;
    SegmentedGroup segmentGender, segmentStudent;
    RelativeLayout layoutEmployer;
    RelativeLayout layoutHobby;
    FirebaseAuth firebaseAuth;
    List<String> checkboxes;
    Boolean employerStatus = true;
    DatabaseReference databaseReferenceRoommate, reference;
    String gender="Male";
    String employer, birthDay;
    String name="Name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_room_1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etBirthday = findViewById(R.id.etBirthday);
        etEmployer = findViewById(R.id.etEmployer);
        segmentGender = findViewById(R.id.segmentGender);
        segmentStudent = findViewById(R.id.segmentStudent);
        layoutEmployer = findViewById(R.id.layoutEmployer);
        layoutHobby = findViewById(R.id.layoutHobby);
        firebaseAuth = FirebaseAuth.getInstance();


        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                updateLabel();
            }

        };

        //Gender Segment
        segmentGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btnMale) {
                    gender = "Male";
                } else if (checkedId == R.id.btnFemale) {
                    gender = "Female";
                }
            }
        });
        //Gender Segment
        segmentStudent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btnStudent_Yes) {
                    layoutEmployer.setVisibility(View.GONE);
                    employerStatus = false;
                    employer = "Student";
                } else if (checkedId == R.id.btnStudent_No) {
                    layoutEmployer.setVisibility(View.VISIBLE);
                    employerStatus = true;
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

    public void onClickBirthday(View view) {
        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        String myFormat = "yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etBirthday.setText(sdf.format(myCalendar.getTime()));
    }

    public void onClickNext(View view) {
        attemptNext();

    }

    private void checkBoxManager() {
        checkboxes = new ArrayList<String>();
        for (int i = 1; i <= 10; i++) {
            int resID = getResources().getIdentifier("checkbox_" + i,
                    "id", getPackageName());
            Log.i("Ch", "ID: " + Integer.toString(resID) + "Original: Ch1" + Integer.toString(R.id.checkbox_1));
            CheckBox checkBox = findViewById(resID);
            if (checkBox.isChecked()) {
                String text = checkBox.getText().toString();
                checkboxes.add(text);
            }
        }
/*
        for (String ch : checkboxes) {
            System.out.println(ch);
        }*/
    }


    private void attemptNext() {
        boolean cancel = false;
        String birthDay = etBirthday.getText().toString();
        String employer = etEmployer.getText().toString();
        checkBoxManager();

        if (TextUtils.isEmpty(birthDay)) {
            etBirthday.setError(getString(R.string.error_field_required));
            focusView = etBirthday;
            cancel = true;
        } else {
            this.birthDay = birthDay;
        }
        if (employerStatus) {
            if (TextUtils.isEmpty(employer)) {
                etEmployer.setError(getString(R.string.error_field_required));
                focusView = etEmployer;
                cancel = true;
            } else {
                this.employer = employer;
            }
        }

        if (checkboxes.isEmpty()) {
            Toast.makeText(this, "At least One Hobby must be Select.", Toast.LENGTH_SHORT).show();
            focusView = layoutHobby;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            Toast.makeText(this, "Incorrect Input.", Toast.LENGTH_SHORT).show();
        } else {
            uploadData();
        }
    }

    public String userInfo(){
        String userID=firebaseAuth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Chat Related Modification
//                User user = dataSnapshot.getValue(User.class);
                if(dataSnapshot.exists()) {
                    String userName=dataSnapshot.child("name").getValue(String.class);
                    name = userName;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return name;
    }

    private void uploadData() {
//        userInfo();
        String userID = firebaseAuth.getCurrentUser().getUid();
        databaseReferenceRoommate = FirebaseDatabase.getInstance().getReference("Roommate").child(userID);

        HashMap dataMap = new HashMap();
        dataMap.put("id", userID);
        dataMap.put("name", userInfo());
        dataMap.put("gender", gender);
        dataMap.put("birthday", birthDay);
        dataMap.put("employer", employer);
        dataMap.put("hobbies", checkboxes);
        dataMap.put("imageURL", "http://diazworld.com/images/avatar-placeholder.png");
        /*dataMap.put("homeType", "default");
        dataMap.put("bedRoom", "default");
        dataMap.put("bathRoom", "default");
        dataMap.put("roomType", "default");
        dataMap.put("roommates", "default");
        dataMap.put("rent", "default");
        dataMap.put("availableDate", "default");
        dataMap.put("termLength", "default");
        dataMap.put("amenities", "default");
        dataMap.put("relationShip","default");
        dataMap.put("pets", "default");
        dataMap.put("smoking", "default");
        dataMap.put("clean", "default");
        dataMap.put("nightOwl", "default");
        dataMap.put("about", "default");*/
        databaseReferenceRoommate.setValue(dataMap);

        Intent intent = new Intent(this, PostRoomActivity_2.class);
        startActivity(intent);
    }

}
