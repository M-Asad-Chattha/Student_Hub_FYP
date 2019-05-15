package com.achba.studenthub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    RelativeLayout layout;
    TextView mEmailView;
    View focusView = null;
    ProgressDialog progress;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        mEmailView = findViewById(R.id.tv_forgetEmail);
        layout = findViewById(R.id.layout);
        progress = new ProgressDialog(this);

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

    public void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void hideKeyboard(View view){
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onClickForgetPsw(View view) {
        hideKeyboard();

        boolean cancel = false;
        email = mEmailView.getText().toString();

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
        } else {
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
            firebaseAuth();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic ex."uog.edu.pk"
        return email.contains("@");
    }

    public void firebaseAuth(){
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progress.dismiss();
                        if (task.isSuccessful()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);
                            builder.setMessage("Password send to your email.")
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
                            Toast.makeText(ForgetPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
