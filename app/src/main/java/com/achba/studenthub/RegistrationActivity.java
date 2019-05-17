package com.achba.studenthub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
//    private AutoCompleteTextView mEmailView;
    private EditText mName, mUserName, mPasswordView, mPasswordAgainView, mEmailView;
    View focusView = null;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    ProgressDialog progress;
    Spinner spinnerProgram, spinnerSemester, spinnerSection, spinnerCampus;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_registration);

        mName=findViewById(R.id.name);
        mUserName = findViewById(R.id.userName);
        mEmailView=findViewById(R.id.email_registration);
        mPasswordView=findViewById(R.id.password_registration);
        mPasswordAgainView = findViewById(R.id.password_registration_agin);
        spinnerProgram = findViewById(R.id.spinnerProgram);
        spinnerSemester = findViewById(R.id.spinnerSemester);
        spinnerSection = findViewById(R.id.spinnerSection);
        spinnerCampus = findViewById(R.id.spinnerCampus);
        layout = findViewById(R.id.layout);

        layout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });

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

        resetFields();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetFields();
    }

    @Override
    public void onBackPressed() {
        return;
    }

    public void onCancel(View view) {
        super.onBackPressed();
        Toast.makeText(this, "Registration failed.", Toast.LENGTH_SHORT).show();

    }
    public void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void onRegister(View view) {
        hideKeyboard();
        attemptLogin();
    }

    public void hideKeyboard(View view){
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void attemptLogin(){
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
        }else if (TextUtils.isEmpty(password)) {
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
    }

    private void firebaseAuth(){
        final String userName = mUserName.getText().toString();

        //User Info save in firebase Database
        Query userNameQuery = FirebaseDatabase.getInstance().getReference().child("Users")
                .orderByChild("userName").equalTo(userName);
        userNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    progress.dismiss();
                    mUserName.setError("Username already exist, try another.");
                    Toast.makeText(RegistrationActivity.this, "User already exist.", Toast.LENGTH_SHORT).show();
                } else {
                    createFirebaseUser();
                    saveUserInfo();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private void resetFields(){
        mName.setText(null);
        mUserName.setText(null);
        mEmailView.setText(null);
        mPasswordView.setText(null);
        mPasswordAgainView.setText(null);
        focusView=mName;
        focusView.requestFocus();
    }

    public void createFirebaseUser(){
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progress.dismiss();

                        if (task.isSuccessful()) {
                            //User info saved in Database
//                            saveUserInfo();

                            firebaseUser.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                resetFields();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                                                builder.setMessage("Register successfully, Please check you email for verification.")
                                                        .setCancelable(false)
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        });
                                                AlertDialog alert = builder.create();
                                                alert.show();
                                            }else{
                                                Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(e instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(getApplicationContext(), "User Already exist", Toast.LENGTH_SHORT).show();
//                                updateStatus("User Already exist");
                        }
                        else{
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                                updateStatus(e.getLocalizedMessage());
                        }
                    }
                });
    }

    public void saveUserInfo(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        final String email = mEmailView.getText().toString();
        final String name = mName.getText().toString();
        final String userName = mUserName.getText().toString();
        final String spinnerProgramValue = spinnerProgram.getSelectedItem().toString();
        final String spinnerSemesterValue = spinnerSemester.getSelectedItem().toString();
        final String spinnerSectionValue = spinnerSection.getSelectedItem().toString();
        final String spinnerCampusValue = spinnerCampus.getSelectedItem().toString();

        String userID = firebaseUser.getUid();
        DatabaseReference userDB= FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        Map dataMap=new HashMap();
        dataMap.put("name", name);
        dataMap.put("bio", "Add bio from Edit Info");
        dataMap.put("email", email);
        dataMap.put("userName", userName);
        dataMap.put("program", spinnerProgramValue);
        dataMap.put("semester", spinnerSemesterValue);
        dataMap.put("section", spinnerSectionValue);
        dataMap.put("campus", spinnerCampusValue);
        userDB.setValue(dataMap);
/*
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        firebaseUser.updateProfile(profileUpdates);*/
    }

}
