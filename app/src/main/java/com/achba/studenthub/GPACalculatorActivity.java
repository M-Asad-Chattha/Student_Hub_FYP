package com.achba.studenthub;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GPACalculatorActivity extends AppCompatActivity {
    private Button btnGPA, btnCGPA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpacalculator);

        btnGPA = findViewById(R.id.btn_gpa);
        btnCGPA = findViewById(R.id.btn_cgpa);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onClickBtnGPA(View view) {
        Intent intent = new Intent(this, GPAActivity.class);
        startActivity(intent);
    }

    public void onClickBtnCGPA(View view) {
        Intent intent = new Intent(this, CGPAActivity.class);
        startActivity(intent);
    }

}
