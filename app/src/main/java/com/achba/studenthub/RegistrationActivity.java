package com.achba.studenthub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegistrationActivity extends AppCompatActivity {
//    private AutoCompleteTextView mEmailView;
    private EditText mName, mPasswordView, mPasswordAgainView, mEmailView;
    View focusView = null;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    ProgressDialog progress;
    Spinner spinnerProgram, spinnerSemester, spinnerSection, spinnerCampus;

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
        mEmailView=findViewById(R.id.email_registration);
        mPasswordView=findViewById(R.id.password_registration);
        mPasswordAgainView = findViewById(R.id.password_registration_agin);
        spinnerProgram = findViewById(R.id.spinnerProgram);
        spinnerSemester = findViewById(R.id.spinnerSemester);
        spinnerSection = findViewById(R.id.spinnerSection);
        spinnerCampus = findViewById(R.id.spinnerCampus);

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
//        String spinnerValue = spinnerProgram.getSelectedItem().toString();

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

    public void onRegister(View view) {
        attemptLogin();
    }

    private void attemptLogin(){
        boolean cancel = false;
        String name = mName.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordAgain = mPasswordAgainView.getText().toString();

        // Check for a valid name.
        if (TextUtils.isEmpty(name)) {
            mName.setError(getString(R.string.error_field_required));
            focusView = mName;
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


        /*if (!TextUtils.isEmpty(passwordAgain) && !isPasswordValid(passwordAgain)) {
            mPasswordAgainView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordAgainView;
            cancel = true;
        }else if (TextUtils.isEmpty(passwordAgain)) {
            mPasswordAgainView.setError(getString(R.string.error_field_required));
            focusView = mPasswordAgainView;
            cancel = true;
        }*/
        /*if( (!TextUtils.isEmpty(password) && isPasswordValid(password)) &&
                (!TextUtils.isEmpty(passwordAgain) && isPasswordValid(passwordAgain))){
            if (password.matches(passwordAgain)) {
                mPasswordAgainView.setError("Password does not match.");
                focusView = mPasswordAgainView;
                cancel = true;
            }
        }*/
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
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // TODO: 5/13/2019  Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            progress = new ProgressDialog(this);
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
            firebaseAuth();
        }
    }

    private void firebaseAuth(){
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        /*if ((email.isEmpty() || password.isEmpty())) {
            Toast.makeText(this, "error: Fields are Empty", Toast.LENGTH_SHORT).show();
        } else {*/
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // TODO: 5/13/2019 Hide Progress loading dialog
                            progress.dismiss();
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "User register successfully.", Toast.LENGTH_SHORT).show();
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
        mEmailView.setText(null);
        mPasswordView.setText(null);
        mPasswordAgainView.setText(null);
        focusView=mName;
        focusView.requestFocus();
    }
}
