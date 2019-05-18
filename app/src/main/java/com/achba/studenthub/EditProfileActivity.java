package com.achba.studenthub;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditProfileActivity extends AppCompatActivity {
    Spinner spinnerProgram, spinnerSemester, spinnerSection, spinnerCampus;
    ProgressDialog progress;
    RelativeLayout layout;
    private EditText mName, mBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinnerProgram = findViewById(R.id.spinnerProgram);
        spinnerSemester = findViewById(R.id.spinnerSemester);
        spinnerSection = findViewById(R.id.spinnerSection);
        spinnerCampus = findViewById(R.id.spinnerCampus);
        mName = findViewById(R.id.name);
        mBio= findViewById(R.id.bio);
        layout = findViewById(R.id.layout);

        ArrayAdapter<CharSequence> adapterProgram = ArrayAdapter.createFromResource(this,
                R.array.spinnerProgram, android.R.layout.simple_spinner_item);
        adapterProgram.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProgram.setAdapter(adapterProgram);

        ArrayAdapter<CharSequence> adapterSemester = ArrayAdapter.createFromResource(this,
                R.array.spinnerSemester, android.R.layout.simple_spinner_item);
        adapterSemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSemester.setAdapter(adapterSemester);

        ArrayAdapter<CharSequence> adapterSection = ArrayAdapter.createFromResource(this,
                R.array.spinnerSection, android.R.layout.simple_spinner_item);
        adapterSection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSection.setAdapter(adapterSection);

        ArrayAdapter<CharSequence> adapterCampus = ArrayAdapter.createFromResource(this,
                R.array.spinnerCampus, android.R.layout.simple_spinner_item);
        adapterCampus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCampus.setAdapter(adapterCampus);

        layout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void attemptSave(){
        boolean cancel = false;
        String spinnerProgramValue = spinnerProgram.getSelectedItem().toString();
        String spinnerSemesterValue = spinnerSemester.getSelectedItem().toString();
        String spinnerSectionValue = spinnerSection.getSelectedItem().toString();
        String spinnerCampusValue = spinnerCampus.getSelectedItem().toString();
        String name = mName.getText().toString();
        String bio = mBio.getText().toString();

        if (spinnerProgramValue.equals("Select Degree Program:")) {
            ((TextView)spinnerProgram.getSelectedView()).setError("Select one from drop down");
            cancel = true;
        }
        if (spinnerSemesterValue.equals("Select Semester:")){
            ((TextView)spinnerSemester.getSelectedView()).setError("Select one from drop down");
            cancel = true;
        }
        if (spinnerSectionValue.equals("Select Section:")){
            ((TextView)spinnerSection.getSelectedView()).setError("Select one from drop down");
            cancel = true;
        }
        if (spinnerCampusValue.equals("Select Campus:")){
            ((TextView)spinnerCampus.getSelectedView()).setError("Select one from drop down");
            cancel = true;
        }

        if (TextUtils.isEmpty(name)) {
            mName.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        if (TextUtils.isEmpty(bio)) {
            mBio.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        if (cancel) {
            Toast.makeText(this, "Incorrect Input.", Toast.LENGTH_SHORT).show();
        } else {
            progress = new ProgressDialog(this);
            progress.setTitle("Please Wait...");
            progress.setMessage("Processing your request...");
            progress.setCancelable(false);
            progress.show();
            //firebaseOperation();
        }
    }

    public void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void onClickSave(View view) {
        hideKeyboard();
        attemptSave();
    }

    public void hideKeyboard(View view){
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
