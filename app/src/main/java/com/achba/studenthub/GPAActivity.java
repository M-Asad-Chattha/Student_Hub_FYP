package com.achba.studenthub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import java.text.DecimalFormat;

public class GPAActivity extends AppCompatActivity {
    private Spinner spinnerGrades1, spinnerCreditHours1, spinnerGrades2, spinnerCreditHours2, spinnerGrades3, spinnerCreditHours3,
            spinnerGrades4, spinnerCreditHours4, spinnerGrades5, spinnerCreditHours5, spinnerGrades6, spinnerCreditHours6,
            spinnerGrades7, spinnerCreditHours7, spinnerGrades8, spinnerCreditHours8; /*,spinnerGrades9, spinnerCreditHours9*/;
    double points=0;
    int totalCreditHours=0;
    double result=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpa);

        //Initialize variables
        spinnerGrades1 = findViewById(R.id.spinner_gradesGPA1);
        spinnerCreditHours1 = findViewById(R.id.spinner_creditHoursGPA1);
        spinnerGrades2 = findViewById(R.id.spinner_gradesGPA2);
        spinnerCreditHours2 = findViewById(R.id.spinner_creditHoursGPA2);
        spinnerGrades3 = findViewById(R.id.spinner_gradesGPA3);
        spinnerCreditHours3 = findViewById(R.id.spinner_creditHoursGPA3);
        spinnerGrades4 = findViewById(R.id.spinner_gradesGPA4);
        spinnerCreditHours4 = findViewById(R.id.spinner_creditHoursGPA4);
        spinnerGrades5 = findViewById(R.id.spinner_gradesGPA5);
        spinnerCreditHours5 = findViewById(R.id.spinner_creditHoursGPA5);
        spinnerGrades6 = findViewById(R.id.spinner_gradesGPA6);
        spinnerCreditHours6 = findViewById(R.id.spinner_creditHoursGPA6);
        spinnerGrades7 = findViewById(R.id.spinner_gradesGPA7);
        spinnerCreditHours7= findViewById(R.id.spinner_creditHoursGPA7);
        spinnerGrades8 = findViewById(R.id.spinner_gradesGPA8);
        spinnerCreditHours8 = findViewById(R.id.spinner_creditHoursGPA8);
        /*spinnerGrades9 = findViewById(R.id.spinner_gradesGPA9);
        spinnerCreditHours9 = findViewById(R.id.spinner_creditHoursGPA9);*/

        //Set Spinner Adapter
        setSpinnerGradesAdapter(spinnerGrades1);
        setSpinnerCreditHoursAdapter(spinnerCreditHours1);
        setSpinnerGradesAdapter(spinnerGrades2);
        setSpinnerCreditHoursAdapter(spinnerCreditHours2);
        setSpinnerGradesAdapter(spinnerGrades3);
        setSpinnerCreditHoursAdapter(spinnerCreditHours3);
        setSpinnerGradesAdapter(spinnerGrades4);
        setSpinnerCreditHoursAdapter(spinnerCreditHours4);
        setSpinnerGradesAdapter(spinnerGrades5);
        setSpinnerCreditHoursAdapter(spinnerCreditHours5);
        setSpinnerGradesAdapter(spinnerGrades6);
        setSpinnerCreditHoursAdapter(spinnerCreditHours6);
        setSpinnerGradesAdapter(spinnerGrades7);
        setSpinnerCreditHoursAdapter(spinnerCreditHours7);
        setSpinnerGradesAdapter(spinnerGrades8);
        setSpinnerCreditHoursAdapter(spinnerCreditHours8);
        /*setSpinnerGradesAdapter(spinnerGrades9);
        setSpinnerCreditHoursAdapter(spinnerCreditHours9);*/


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setSpinnerGradesAdapter(Spinner spinner){
        ArrayAdapter<CharSequence> adapterGrades = ArrayAdapter.createFromResource(this,
                R.array.grades, R.layout.custom_spinner_item);
        adapterGrades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterGrades);
    }

    private void setSpinnerCreditHoursAdapter(Spinner spinner){
        ArrayAdapter<CharSequence> adapterGrades = ArrayAdapter.createFromResource(this,
                R.array.creditHours, R.layout.custom_spinner_item);
        adapterGrades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterGrades);
    }


    public void onCalculateGPA(View view) {

        String creditHours1, grades1, creditHours2, grades2, creditHours3, grades3, creditHours4, grades4, creditHours5, grades5,
                creditHours6, grades6, creditHours7, grades7, creditHours8, grades8;

        creditHours1 = spinnerCreditHours1.getSelectedItem().toString();
        grades1 = spinnerGrades1.getSelectedItem().toString();
        creditHours2 = spinnerCreditHours2.getSelectedItem().toString();
        grades2 = spinnerGrades2.getSelectedItem().toString();
        creditHours3 = spinnerCreditHours3.getSelectedItem().toString();
        grades3 = spinnerGrades3.getSelectedItem().toString();
        creditHours4 = spinnerCreditHours4.getSelectedItem().toString();
        grades4 = spinnerGrades4.getSelectedItem().toString();
        creditHours5 = spinnerCreditHours5.getSelectedItem().toString();
        grades5 = spinnerGrades5.getSelectedItem().toString();
        creditHours6 = spinnerCreditHours6.getSelectedItem().toString();
        grades6 = spinnerGrades6.getSelectedItem().toString();
        creditHours7 = spinnerCreditHours7.getSelectedItem().toString();
        grades7 = spinnerGrades7.getSelectedItem().toString();
        creditHours8 = spinnerCreditHours8.getSelectedItem().toString();
        grades8 = spinnerGrades8.getSelectedItem().toString();

        if(!creditHours1.equals("Credit Hour") && !grades1.equals("Grade")){
            calculatePoints(grades1, creditHours1);
        }
        if(!creditHours2.equals("Credit Hour") && !grades2.equals("Grade")){
            calculatePoints(grades2, creditHours2);
        }
        if(!creditHours3.equals("Credit Hour") && !grades3.equals("Grade")){
            calculatePoints(grades3, creditHours3);
        }
        if(!creditHours4.equals("Credit Hour") && !grades4.equals("Grade")){
            calculatePoints(grades4, creditHours4);
        }
        if(!creditHours5.equals("Credit Hour") && !grades5.equals("Grade")){
            calculatePoints(grades5, creditHours5);
        }
        if(!creditHours6.equals("Credit Hour") && !grades6.equals("Grade")){
            calculatePoints(grades6, creditHours6);
        }
        if(!creditHours7.equals("Credit Hour") && !grades7.equals("Grade")){
            calculatePoints(grades7, creditHours7);
        }
        if(!creditHours8.equals("Credit Hour") && !grades8.equals("Grade")){
            calculatePoints(grades8, creditHours8);
        }

        new FancyGifDialog.Builder(GPAActivity.this)
                .setTitle("Calculated GPA")
                .setMessage("Total Credit Hours: "+totalCreditHours+"\nTotal grade points: "+points+
                        "\n\nGPA: "+Double.parseDouble(new DecimalFormat("##.##").format(result)))
                .setNegativeBtnText("Exit")
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("Ok")
                .setNegativeBtnBackground("#FFA9A7A8")
                .setGifResource(R.drawable.study)   //Pass your Gif here
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(GPAActivity.this, "Calculate another GPA",Toast.LENGTH_SHORT).show();
                        resetValues();
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        resetValues();
                        onBackPressed();
                    }
                })
                .build();

    }

    private void calculatePoints(String grade, String creditHour){
        double grades=0;
        int creditHours=Integer.parseInt(creditHour);

        switch(grade){
            case "A+":{
                grades=4;
                break;
            }
            case "A":{
                grades=3.70;
                break;
            }
            case "B+":{
                grades=3.40;
                break;
            }
            case "B":{
                grades=3;
                break;
            }
            case "B-":{
                grades=2.50;
                break;
            }
            case "C+":{
                grades=2;
                break;
            }
            case "C":{
                grades=1.5;
                break;
            }
            case "D":{
                grades=1;
                break;
            }
            case "F":{
                grades=0;
                break;
            }
        } //switch ends
    totalCreditHours=totalCreditHours+creditHours;
    points+=creditHours*grades;
    result = points/totalCreditHours;
    }

    private void resetValues(){
        points=0;
        totalCreditHours=0;
        result=0;
    }
}
