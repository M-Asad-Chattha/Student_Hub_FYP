package com.achba.studenthub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

public class TimetableActivity extends AppCompatActivity {
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        radioGroup = findViewById(R.id.radioGroup_days);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                if (checkedId==R.id.monday){
                    Toast.makeText(TimetableActivity.this, "Monday", Toast.LENGTH_SHORT).show();
                }
                else if(checkedId==R.id.tuesday){
                    Toast.makeText(TimetableActivity.this, "Tuesday selected", Toast.LENGTH_SHORT).show();
                }
                else if(checkedId==R.id.wednesday){
                    Toast.makeText(TimetableActivity.this, "Wednesday ", Toast.LENGTH_SHORT).show();
                }
                else if(checkedId==R.id.thursday){
                    Toast.makeText(TimetableActivity.this, "Thursday ", Toast.LENGTH_SHORT).show();
                }
                else if(checkedId==R.id.friday){
                    Toast.makeText(TimetableActivity.this, "Friday ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
