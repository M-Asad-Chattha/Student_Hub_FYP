package com.achba.studenthub.RoommateFinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.achba.studenthub.R;

public class RoommateFinderStartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roommate_finder_start);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void postRoom(View view) {
        Intent intent = new Intent(this, PostRoomActivity_1.class);
        startActivity(intent);
    }

    public void findRoom(View view) {
        Intent intent = new Intent(this, FindRoomActivity.class);
        startActivity(intent);
    }
}
