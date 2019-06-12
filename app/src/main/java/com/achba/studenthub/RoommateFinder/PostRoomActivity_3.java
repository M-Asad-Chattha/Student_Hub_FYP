package com.achba.studenthub.RoommateFinder;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.achba.studenthub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import info.hoang8f.android.segmented.SegmentedGroup;

public class PostRoomActivity_3 extends AppCompatActivity {
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    EditText etRent, etAvailableDate;
    private Spinner spinnerTermLength;
    View focusView = null;
    TextView tvRoommates;
    List<String> checkboxes;
    RelativeLayout layoutAmenities;
    int roommatesValue;
    SegmentedGroup segmentRoomType;
    String roomType = "Shared";
    String rent, availableDate, spinnerTermLengthValue;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReferenceRoommate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_room_3);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        segmentRoomType = findViewById(R.id.segmentRoomType);
        spinnerTermLength = findViewById(R.id.spinnerTermLength);
        layoutAmenities = findViewById(R.id.layoutAmenities);

        ArrayAdapter<CharSequence> adapterTermLength = ArrayAdapter.createFromResource(this,
                R.array.spinnerTermLength, android.R.layout.simple_spinner_item);
        adapterTermLength.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTermLength.setAdapter(adapterTermLength);

        tvRoommates = findViewById(R.id.tvRoommatesNumber);
        roommatesValue = Integer.parseInt(tvRoommates.getText().toString());
        firebaseAuth = FirebaseAuth.getInstance();

        myCalendar = Calendar.getInstance();
        etAvailableDate = findViewById(R.id.etAvailableDate);
        etRent = findViewById(R.id.etRent);

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        segmentRoomType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btnRoomType_flexible) {
                    roomType = "Flexible";
                } else if (checkedId == R.id.btnRoomType_private) {
                    roomType = "Private";
                } else if (checkedId == R.id.btnRoomType_Shared) {
                    roomType = "Shared";
                }
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etAvailableDate.setText(sdf.format(myCalendar.getTime()));
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

    public void onClickMinusRoommates(View view) {
        if (roommatesValue > 2) {
            roommatesValue--;
            tvRoommates.setText(Integer.toString(roommatesValue));
        } else {
            Toast.makeText(this, "Roommates must be more or equal to 2.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickPlusRoommates(View view) {
        roommatesValue++;
        tvRoommates.setText(Integer.toString(roommatesValue));
    }

    public void onClickAvailableDate(View view) {
        new DatePickerDialog(PostRoomActivity_3.this, date,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void onClickNext(View view) {
        attemptNext();
    }

    private void attemptNext() {
        boolean cancel = false;
        rent = etRent.getText().toString();
        availableDate = etAvailableDate.getText().toString();
        spinnerTermLengthValue = spinnerTermLength.getSelectedItem().toString();
        checkBoxManager();

        if (TextUtils.isEmpty(rent)) {
            etRent.setError(getString(R.string.error_field_required));
            focusView = etRent;
            cancel = true;
        }
        if (TextUtils.isEmpty(availableDate)) {
            etAvailableDate.setError(getString(R.string.error_field_required));
            focusView = etAvailableDate;
            cancel = true;
        }
        if (spinnerTermLengthValue.equals("e.g. 12 months")) {
            ((TextView) spinnerTermLength.getSelectedView()).setError("Select one from drop down");
            focusView = spinnerTermLength;
            cancel = true;
        }
        if (checkboxes.isEmpty()) {
            Toast.makeText(this, "At least One Amenity must be Select.", Toast.LENGTH_SHORT).show();
            focusView = layoutAmenities;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            Toast.makeText(this, "Incorrect Input.", Toast.LENGTH_SHORT).show();
        } else {
            uploadData();
        }
    }

    private void checkBoxManager() {
        checkboxes = new ArrayList<String>();
        for (int i = 1; i <= 7; i++) {
            int resID = getResources().getIdentifier("checkbox_" + i,
                    "id", getPackageName());
            CheckBox checkBox = findViewById(resID);
            if (checkBox.isChecked()) {
                String text = checkBox.getText().toString();
                checkboxes.add(text);
            }
        }

        for (String ch : checkboxes) {
            System.out.println(ch);
        }
    }

    private void uploadData() {
        String userID = firebaseAuth.getCurrentUser().getUid();
        databaseReferenceRoommate = FirebaseDatabase.getInstance().getReference("Roommate").child(userID);

        HashMap dataMap = new HashMap();
        dataMap.put("roomType", roomType);
        dataMap.put("roommates", Integer.toString(roommatesValue));
        dataMap.put("rent", rent); //Update in _1 from here
        dataMap.put("availableDate", availableDate);
        dataMap.put("termLength", spinnerTermLengthValue);
        dataMap.put("amenities", checkboxes);
        databaseReferenceRoommate.updateChildren(dataMap);

        Intent intent = new Intent(this, PostRoomActivity_4.class);
        startActivity(intent);
    }
}
