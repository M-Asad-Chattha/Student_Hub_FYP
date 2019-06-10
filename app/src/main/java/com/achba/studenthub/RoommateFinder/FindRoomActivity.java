package com.achba.studenthub.RoommateFinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.achba.studenthub.R;

public class FindRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_room);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
