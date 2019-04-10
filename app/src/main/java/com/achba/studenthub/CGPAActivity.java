package com.achba.studenthub;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

public class CGPAActivity extends AppCompatActivity {
    private EditText creditHours, currentCGPA;
    View focusView=null;
    String credit="0", current_cgpa="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cgpa);

        creditHours = findViewById(R.id.et_totalCreditHours);
        currentCGPA = findViewById(R.id.et_currentCGPA);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onCalculateCGPA(View view) {
        double cgpa=0.0;
        boolean cancel = false;

        /*if( (!creditHours.getText().toString().equals(null)) && (!currentCGPA.getText().toString().equals(null))){
            credit = creditHours.getText().toString();
            current_cgpa = currentCGPA.getText().toString();
        }*/
        credit = creditHours.getText().toString();
        current_cgpa = currentCGPA.getText().toString();

        if(TextUtils.isEmpty(currentCGPA.getText().toString())){
            currentCGPA.setError(getString(R.string.error_field_required));
            focusView=currentCGPA;
            cancel = true;
        }
        if(TextUtils.isEmpty(creditHours.getText().toString())){
            creditHours.setError(getString(R.string.error_field_required));
            focusView=creditHours;
            cancel = true;
        }

        if(cancel) {
            focusView.requestFocus();
        } else {
                cgpa = Double.valueOf(currentCGPA.getText().toString());
                if(cgpa > 4.0){
                    currentCGPA.setError("Invalid CGPA");
                }else {
                    new FancyGifDialog.Builder(CGPAActivity.this)
                            .setTitle("Calculated CGPA")
                            .setMessage("Total Credit Hours: " + credit +
                                    "\n\nGPA: " + current_cgpa)
                            .setNegativeBtnText("Exit")
                            .setPositiveBtnBackground("#FF4081")
                            .setPositiveBtnText("Ok")
                            .setNegativeBtnBackground("#FFA9A7A8")
                            .setGifResource(R.drawable.study)   //Pass your Gif here
                            .isCancellable(true)
                            .OnPositiveClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    Toast.makeText(CGPAActivity.this, "Calculate another CGPA", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .OnNegativeClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    onBackPressed();
                                }
                            })
                            .build();
                    }
                }
        }
    }
