package com.achba.studenthub.RoommateFinder;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.achba.studenthub.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PostRoomActivity_3 extends AppCompatActivity {
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    EditText etAvailableDate;
    private Spinner spinnerTermLength;
    TextView tvRoommates;
    int RoommatesValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_room_3);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinnerTermLength = findViewById(R.id.spinnerTermLength);
        ArrayAdapter<CharSequence> adapterTermLength = ArrayAdapter.createFromResource(this,
                R.array.spinnerTermLength, android.R.layout.simple_spinner_item);
        adapterTermLength.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTermLength.setAdapter(adapterTermLength);

        tvRoommates = findViewById(R.id.tvRoommatesNumber);
        RoommatesValue = Integer.parseInt(tvRoommates.getText().toString());

        myCalendar = Calendar.getInstance();
        etAvailableDate = findViewById(R.id.etAvailableDate);
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
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etAvailableDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void onClickMinusRoommates(View view) {
        if(RoommatesValue >2){
            RoommatesValue--;
            tvRoommates.setText(Integer.toString(RoommatesValue));
        }else{
            Toast.makeText(this, "Roommates must be more or equal to 2.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickPlusRoommates(View view) {
        RoommatesValue++;
        tvRoommates.setText(Integer.toString(RoommatesValue));
    }

    public void onClickAvailableDate(View view) {
        new DatePickerDialog(PostRoomActivity_3.this, date,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void onClickNext(View view) {
        Intent intent = new Intent(this, PostRoomActivity_4.class);
        startActivity(intent);
    }
}
