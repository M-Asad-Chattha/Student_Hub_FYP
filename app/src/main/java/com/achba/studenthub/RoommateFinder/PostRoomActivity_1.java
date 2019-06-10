package com.achba.studenthub.RoommateFinder;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.achba.studenthub.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PostRoomActivity_1 extends AppCompatActivity {
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    EditText etBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_room_1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myCalendar = Calendar.getInstance();
        etBirthday = findViewById(R.id.etBirthday);
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
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
        Intent intent = new Intent(this, PostRoomActivity_2.class);
        startActivity(intent);
    }

    /*private void attemptLogin() {
        boolean cancel = false;
        String name = mName.getText().toString();
        String userName = mUserName.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordAgain = mPasswordAgainView.getText().toString();
        String spinnerProgramValue = spinnerProgram.getSelectedItem().toString();
        String spinnerSemesterValue = spinnerSemester.getSelectedItem().toString();
        String spinnerSectionValue = spinnerSection.getSelectedItem().toString();
        String spinnerCampusValue = spinnerCampus.getSelectedItem().toString();

        // Check for a valid name.
        if (TextUtils.isEmpty(name)) {
            mName.setError(getString(R.string.error_field_required));
            focusView = mName;
            cancel = true;
        }

        if (TextUtils.isEmpty(userName)) {
            mUserName.setError(getString(R.string.error_field_required));
            focusView = mUserName;
            cancel = true;
        }

        // Check for a valid password.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(passwordAgain)) {
            mPasswordAgainView.setError(getString(R.string.error_field_required));
            focusView = mPasswordAgainView;
            cancel = true;
        }

        //Spinners validation
        if (spinnerProgramValue.equals("Select Degree Program:")) {
            ((TextView) spinnerProgram.getSelectedView()).setError("Select one from drop down");
            cancel = true;
        }
        if (spinnerSemesterValue.equals("Select Semester:")) {
            ((TextView) spinnerSemester.getSelectedView()).setError("Select one from drop down");
            cancel = true;
        }
        if (spinnerSectionValue.equals("Select Section:")) {
            ((TextView) spinnerSection.getSelectedView()).setError("Select one from drop down");
            cancel = true;
        }
        if (spinnerCampusValue.equals("Select Campus:")) {
            ((TextView) spinnerCampus.getSelectedView()).setError("Select one from drop down");
            cancel = true;
        }

        //Password match validation
        if (!password.matches(passwordAgain)) {
            mPasswordAgainView.setError("Password does not match.");
            focusView = mPasswordAgainView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            Toast.makeText(this, "Incorrect Input.", Toast.LENGTH_SHORT).show();
        } else {
            progress = new ProgressDialog(this);
            progress.setTitle("Please Wait...");
            progress.setMessage("Processing your request...");
            progress.setCancelable(false);
            progress.show();
            firebaseAuth();
        }
    }*/
}
