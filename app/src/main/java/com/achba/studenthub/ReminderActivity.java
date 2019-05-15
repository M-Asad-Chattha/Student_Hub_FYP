package com.achba.studenthub;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class ReminderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FloatingActionButton fab = findViewById(R.id.fab_reminder);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("beginTime", cal.getTimeInMillis());
                intent.putExtra("allDay", false);
                intent.putExtra("rrule", "FREQ=DAILY");
                intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
                intent.putExtra("title", "A Test Event from android app");
                startActivity(intent);
            }
        });

        ContentResolver cr = this.getContentResolver();
        Cursor cursor = cr.query(Uri.parse("content://calendar/events"), new String[]{ "calendar_id", "title", "description", "dtstart", "dtend", "eventLocation" }, null, null, null);
        //Cursor cursor = cr.query(Uri.parse("content://calendar/calendars"), new String[]{ "_id", "name" }, null, null, null);
        String add = null;
        cursor.moveToFirst();
        String[] CalNames = new String[cursor.getCount()];
        int[] CalIds = new int[cursor.getCount()];
        for (int i = 0; i < CalNames.length; i++) {
            CalIds[i] = cursor.getInt(0);
            CalNames[i] = "Event"+cursor.getInt(0)+": \nTitle: "+ cursor.getString(1)+"\nDescription: "+cursor.getString(2)+"\nStart Date: "+new Date(cursor.getLong(3))+"\nEnd Date : "+new Date(cursor.getLong(4))+"\nLocation : "+cursor.getString(5);
            if(add == null)
                add = CalNames[i];
            else{
                add += CalNames[i];
            }
            ((TextView)findViewById(R.id.calendars)).setText(add);

            cursor.moveToNext();
        }
        cursor.close();
    }
}
