package com.achba.studenthub;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class TimetableActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    DatabaseReference databaseReferenceUsers, databaseReferenceTimeTable1, databaseReferenceTimeTable2,
            databaseReferenceTimeTable3;
    FirebaseAuth firebaseAuth;
    UserSD userSD;

    TextView subjectName1, subjectName2, subjectName3, classTime1, classTime2, classTime3, roomNumber1, roomNumber2, roomNumber3,
            teacherName1, teacherName2, teacherName3;
    String mProgram, mSemester, mSection;
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        radioGroup = findViewById(R.id.radioGroup_days);
        firebaseAuth = FirebaseAuth.getInstance();
        audioManager= (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);


        String userID = firebaseAuth.getCurrentUser().getUid();
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        databaseReferenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
//                String program = dataSnapshot.child("program").getValue(String.class);
                String program = "Software Engineering";
//                String semester = dataSnapshot.child("semester").getValue(String.class);
                String semester = "1";
//                String section = dataSnapshot.child("section").getValue(String.class);
                String section = "A";
                String campus = dataSnapshot.child("campus").getValue(String.class);

                mProgram = program;
                mSemester = semester;
                mSection = section;

                mondayData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        subjectName1 = findViewById(R.id.subjectName1);
        subjectName2 = findViewById(R.id.subjectName2);
        subjectName3 = findViewById(R.id.subjectName3);
        classTime1 = findViewById(R.id.classTime1);
        classTime2 = findViewById(R.id.classTime2);
        classTime3 = findViewById(R.id.classTime3);
        roomNumber1 = findViewById(R.id.roomNumber1);
        roomNumber2 = findViewById(R.id.roomNumber2);
        roomNumber3 = findViewById(R.id.roomNumber3);
        teacherName1 = findViewById(R.id.teacherName1);
        teacherName2 = findViewById(R.id.teacherName2);
        teacherName3 = findViewById(R.id.teacherName3);

        //Radio Database functionality starts here
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.monday) {
                    mondayData();
                } else if (checkedId == R.id.tuesday) {
                    tuesdayData();
                } else if (checkedId == R.id.wednesday) {
                    wednesdayData();
                } else if (checkedId == R.id.thursday) {
                    thursdayData();
                } else if (checkedId == R.id.friday) {
                    fridayData();
                }
            }
        });


        //BroadCast Receiver
        /*Intent notifyIntent = new Intent(this,TimeTableReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (getApplicationContext(), 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  System.currentTimeMillis(),
                1000 * 60 * 60 * 24, pendingIntent);*/

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, TimeTableReceiver.class); // AlarmReceiver1 = broadcast receiver

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        alarmManager.cancel(pendingIntent);

        Calendar alarmStartTime = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        alarmStartTime.set(Calendar.HOUR_OF_DAY, 14);
        alarmStartTime.set(Calendar.MINUTE, 45);
        alarmStartTime.set(Calendar.SECOND, 0);
        if (now.after(alarmStartTime)) {
            Log.d("Hey", "Added a day");
            alarmStartTime.add(Calendar.DATE, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
        Log.d("Alarm", "Alarms set for everyday 2:45 pm.");


        vibrationMood();
    }

    private void vibrationMood() {
        Date startTime, endTime, currentTime;

        Calendar calStartTime = Calendar.getInstance();
        calStartTime.set(Calendar.HOUR_OF_DAY, 15);
        calStartTime.set(Calendar.MINUTE, 0);
        calStartTime.set(Calendar.SECOND, 0);
        calStartTime.set(Calendar.MILLISECOND, 0);

        Calendar calEndTime = Calendar.getInstance();
        calEndTime.set(Calendar.HOUR_OF_DAY, 20);
        calEndTime.set(Calendar.MINUTE, 0);
        calEndTime.set(Calendar.SECOND, 0);
        calEndTime.set(Calendar.MILLISECOND, 0);

        startTime = calStartTime.getTime();
        endTime = calEndTime.getTime();
        currentTime= Calendar.getInstance().getTime();

    if( (currentTime.after(startTime) && currentTime.before(endTime)) &&
            (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) ){
        notification("Vibration Mode activated",
                "Your device is going to vibrate mood until University time end.");
        //For Vibrate mode
        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

    }else if(!(currentTime.after(startTime) && currentTime.before(endTime)) &&
            (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT
            || audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE)){

        notification("Vibration Mode inactivate ", "University time ends, your device is now in normal mode.");
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }
    
    }

    public void mondayData() {
        Log.i("Data", " Program:" + mProgram + "\tSemester:" + mSemester + "\tSection:" + mSection);

        databaseReferenceTimeTable1 = FirebaseDatabase.getInstance().getReference("Program")
                .child(mProgram).child("Semester").child(mSemester).child("Section").child(mSection).child("Monday")
                .child("First Slot");
        databaseReferenceTimeTable2 = FirebaseDatabase.getInstance().getReference("Program")
                .child(mProgram).child("Semester").child(mSemester).child("Section").child(mSection).child("Monday")
                .child("Second Slot");
        databaseReferenceTimeTable3 = FirebaseDatabase.getInstance().getReference("Program")
                .child(mProgram).child("Semester").child(mSemester).child("Section").child(mSection).child("Monday")
                .child("Third Slot");

        //First Slot Monday
        databaseReferenceTimeTable1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String roomNo1 = dataSnapshot.child("Room No").getValue(String.class);
                    String subjectName1 = dataSnapshot.child("Subject Name").getValue(String.class);
                    String teacherName1 = dataSnapshot.child("Teacher Name").getValue(String.class);
                    String startTime1 = dataSnapshot.child("Start Time").getValue(String.class);
                    String endTime1 = dataSnapshot.child("End Time").getValue(String.class);

                    TimetableActivity.this.subjectName1.setText(subjectName1);
                    classTime1.setText(startTime1 + " - " + endTime1);
                    roomNumber1.setText(roomNo1);
                    TimetableActivity.this.teacherName1.setText(teacherName1);
                } else {
                    Toast.makeText(TimetableActivity.this, "Data is'nt available relating your info!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Second Slot Monday
        databaseReferenceTimeTable2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String roomNo2 = dataSnapshot.child("Room No").getValue(String.class);
                String subjectName2 = dataSnapshot.child("Subject Name").getValue(String.class);
                String teacherName2 = dataSnapshot.child("Teacher Name").getValue(String.class);
                String startTime2 = dataSnapshot.child("Start Time").getValue(String.class);
                String endTime2 = dataSnapshot.child("End Time").getValue(String.class);

                TimetableActivity.this.subjectName2.setText(subjectName2);
                classTime2.setText(startTime2 + " - " + endTime2);
                roomNumber2.setText(roomNo2);
                TimetableActivity.this.teacherName2.setText(teacherName2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Third Slot Monday
        databaseReferenceTimeTable3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String roomNo3 = dataSnapshot.child("Room No").getValue(String.class);
                String subjectName3 = dataSnapshot.child("Subject Name").getValue(String.class);
                String teacherName3 = dataSnapshot.child("Teacher Name").getValue(String.class);
                String startTime3 = dataSnapshot.child("Start Time").getValue(String.class);
                String endTime3 = dataSnapshot.child("End Time").getValue(String.class);

                TimetableActivity.this.subjectName3.setText(subjectName3);
                classTime3.setText(startTime3 + " - " + endTime3);
                roomNumber3.setText(roomNo3);
                TimetableActivity.this.teacherName3.setText(teacherName3);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void tuesdayData() {
        databaseReferenceTimeTable1 = FirebaseDatabase.getInstance().getReference("Program")
                .child(mProgram).child("Semester").child(mSemester).child("Section").child(mSection).child("Tuesday")
                .child("First Slot");
        databaseReferenceTimeTable2 = FirebaseDatabase.getInstance().getReference("Program")
                .child(mProgram).child("Semester").child(mSemester).child("Section").child(mSection).child("Tuesday")
                .child("Second Slot");
        databaseReferenceTimeTable3 = FirebaseDatabase.getInstance().getReference("Program")
                .child(mProgram).child("Semester").child(mSemester).child("Section").child(mSection).child("Tuesday")
                .child("Third Slot");

        //First Slot Tuesday
        databaseReferenceTimeTable1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String roomNo1 = dataSnapshot.child("Room No").getValue(String.class);
                String subjectName1 = dataSnapshot.child("Subject Name").getValue(String.class);
                String teacherName1 = dataSnapshot.child("Teacher Name").getValue(String.class);
                String startTime1 = dataSnapshot.child("Start Time").getValue(String.class);
                String endTime1 = dataSnapshot.child("End Time").getValue(String.class);

                TimetableActivity.this.subjectName1.setText(subjectName1);
                classTime1.setText(startTime1 + " - " + endTime1);
                roomNumber1.setText(roomNo1);
                TimetableActivity.this.teacherName1.setText(teacherName1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Second Slot Tuesday
        databaseReferenceTimeTable2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String roomNo2 = dataSnapshot.child("Room No").getValue(String.class);
                String subjectName2 = dataSnapshot.child("Subject Name").getValue(String.class);
                String teacherName2 = dataSnapshot.child("Teacher Name").getValue(String.class);
                String startTime2 = dataSnapshot.child("Start Time").getValue(String.class);
                String endTime2 = dataSnapshot.child("End Time").getValue(String.class);

                TimetableActivity.this.subjectName2.setText(subjectName2);
                classTime2.setText(startTime2 + " - " + endTime2);
                roomNumber2.setText(roomNo2);
                TimetableActivity.this.teacherName2.setText(teacherName2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Third Slot Tuesday
        databaseReferenceTimeTable3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String roomNo3 = dataSnapshot.child("Room No").getValue(String.class);
                String subjectName3 = dataSnapshot.child("Subject Name").getValue(String.class);
                String teacherName3 = dataSnapshot.child("Teacher Name").getValue(String.class);
                String startTime3 = dataSnapshot.child("Start Time").getValue(String.class);
                String endTime3 = dataSnapshot.child("End Time").getValue(String.class);

                TimetableActivity.this.subjectName3.setText(subjectName3);
                classTime3.setText(startTime3 + " - " + endTime3);
                roomNumber3.setText(roomNo3);
                TimetableActivity.this.teacherName3.setText(teacherName3);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void wednesdayData() {
        databaseReferenceTimeTable1 = FirebaseDatabase.getInstance().getReference("Program")
                .child(mProgram).child("Semester").child(mSemester).child("Section").child(mSection).child("Wednesday")
                .child("First Slot");
        databaseReferenceTimeTable2 = FirebaseDatabase.getInstance().getReference("Program")
                .child(mProgram).child("Semester").child(mSemester).child("Section").child(mSection).child("Wednesday")
                .child("Second Slot");
        databaseReferenceTimeTable3 = FirebaseDatabase.getInstance().getReference("Program")
                .child(mProgram).child("Semester").child(mSemester).child("Section").child(mSection).child("Wednesday")
                .child("Third Slot");

        //First Slot Wednesday
        databaseReferenceTimeTable1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String roomNo1 = dataSnapshot.child("Room No").getValue(String.class);
                String subjectName1 = dataSnapshot.child("Subject Name").getValue(String.class);
                String teacherName1 = dataSnapshot.child("Teacher Name").getValue(String.class);
                String startTime1 = dataSnapshot.child("Start Time").getValue(String.class);
                String endTime1 = dataSnapshot.child("End Time").getValue(String.class);

                TimetableActivity.this.subjectName1.setText(subjectName1);
                classTime1.setText(startTime1 + " - " + endTime1);
                roomNumber1.setText(roomNo1);
                TimetableActivity.this.teacherName1.setText(teacherName1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Second Slot Wednesday
        databaseReferenceTimeTable2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String roomNo2 = dataSnapshot.child("Room No").getValue(String.class);
                String subjectName2 = dataSnapshot.child("Subject Name").getValue(String.class);
                String teacherName2 = dataSnapshot.child("Teacher Name").getValue(String.class);
                String startTime2 = dataSnapshot.child("Start Time").getValue(String.class);
                String endTime2 = dataSnapshot.child("End Time").getValue(String.class);

                TimetableActivity.this.subjectName2.setText(subjectName2);
                classTime2.setText(startTime2 + " - " + endTime2);
                roomNumber2.setText(roomNo2);
                TimetableActivity.this.teacherName2.setText(teacherName2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Third Slot Wednesday
        databaseReferenceTimeTable3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String roomNo3 = dataSnapshot.child("Room No").getValue(String.class);
                String subjectName3 = dataSnapshot.child("Subject Name").getValue(String.class);
                String teacherName3 = dataSnapshot.child("Teacher Name").getValue(String.class);
                String startTime3 = dataSnapshot.child("Start Time").getValue(String.class);
                String endTime3 = dataSnapshot.child("End Time").getValue(String.class);

                TimetableActivity.this.subjectName3.setText(subjectName3);
                classTime3.setText(startTime3 + " - " + endTime3);
                roomNumber3.setText(roomNo3);
                TimetableActivity.this.teacherName3.setText(teacherName3);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void fridayData() {
        databaseReferenceTimeTable1 = FirebaseDatabase.getInstance().getReference("Program")
                .child(mProgram).child("Semester").child(mSemester).child("Section").child(mSection).child("Friday")
                .child("First Slot");
        databaseReferenceTimeTable2 = FirebaseDatabase.getInstance().getReference("Program")
                .child(mProgram).child("Semester").child(mSemester).child("Section").child(mSection).child("Friday")
                .child("Second Slot");
        databaseReferenceTimeTable3 = FirebaseDatabase.getInstance().getReference("Program")
                .child(mProgram).child("Semester").child(mSemester).child("Section").child(mSection).child("Friday")
                .child("Third Slot");

        //First Slot Thursday
        databaseReferenceTimeTable1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String roomNo1 = dataSnapshot.child("Room No").getValue(String.class);
                String subjectName1 = dataSnapshot.child("Subject Name").getValue(String.class);
                String teacherName1 = dataSnapshot.child("Teacher Name").getValue(String.class);
                String startTime1 = dataSnapshot.child("Start Time").getValue(String.class);
                String endTime1 = dataSnapshot.child("End Time").getValue(String.class);

                TimetableActivity.this.subjectName1.setText(subjectName1);
                classTime1.setText(startTime1 + " - " + endTime1);
                roomNumber1.setText(roomNo1);
                TimetableActivity.this.teacherName1.setText(teacherName1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Second Slot Thursday
        databaseReferenceTimeTable2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String roomNo2 = dataSnapshot.child("Room No").getValue(String.class);
                String subjectName2 = dataSnapshot.child("Subject Name").getValue(String.class);
                String teacherName2 = dataSnapshot.child("Teacher Name").getValue(String.class);
                String startTime2 = dataSnapshot.child("Start Time").getValue(String.class);
                String endTime2 = dataSnapshot.child("End Time").getValue(String.class);

                TimetableActivity.this.subjectName2.setText(subjectName2);
                classTime2.setText(startTime2 + " - " + endTime2);
                roomNumber2.setText(roomNo2);
                TimetableActivity.this.teacherName2.setText(teacherName2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Third Slot Thursday
        databaseReferenceTimeTable3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String roomNo3 = dataSnapshot.child("Room No").getValue(String.class);
                String subjectName3 = dataSnapshot.child("Subject Name").getValue(String.class);
                String teacherName3 = dataSnapshot.child("Teacher Name").getValue(String.class);
                String startTime3 = dataSnapshot.child("Start Time").getValue(String.class);
                String endTime3 = dataSnapshot.child("End Time").getValue(String.class);

                TimetableActivity.this.subjectName3.setText(subjectName3);
                classTime3.setText(startTime3 + " - " + endTime3);
                roomNumber3.setText(roomNo3);
                TimetableActivity.this.teacherName3.setText(teacherName3);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void thursdayData() {
        databaseReferenceTimeTable1 = FirebaseDatabase.getInstance().getReference("Program")
                .child(mProgram).child("Semester").child(mSemester).child("Section").child(mSection).child("Thursday")
                .child("First Slot");
        databaseReferenceTimeTable2 = FirebaseDatabase.getInstance().getReference("Program")
                .child(mProgram).child("Semester").child(mSemester).child("Section").child(mSection).child("Thursday")
                .child("Second Slot");
        databaseReferenceTimeTable3 = FirebaseDatabase.getInstance().getReference("Program")
                .child(mProgram).child("Semester").child(mSemester).child("Section").child(mSection).child("Thursday")
                .child("Third Slot");

        //First Slot Thursday
        databaseReferenceTimeTable1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String roomNo1 = dataSnapshot.child("Room No").getValue(String.class);
                String subjectName1 = dataSnapshot.child("Subject Name").getValue(String.class);
                String teacherName1 = dataSnapshot.child("Teacher Name").getValue(String.class);
                String startTime1 = dataSnapshot.child("Start Time").getValue(String.class);
                String endTime1 = dataSnapshot.child("End Time").getValue(String.class);

                TimetableActivity.this.subjectName1.setText(subjectName1);
                classTime1.setText(startTime1 + " - " + endTime1);
                roomNumber1.setText(roomNo1);
                TimetableActivity.this.teacherName1.setText(teacherName1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Second Slot Thursday
        databaseReferenceTimeTable2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String roomNo2 = dataSnapshot.child("Room No").getValue(String.class);
                String subjectName2 = dataSnapshot.child("Subject Name").getValue(String.class);
                String teacherName2 = dataSnapshot.child("Teacher Name").getValue(String.class);
                String startTime2 = dataSnapshot.child("Start Time").getValue(String.class);
                String endTime2 = dataSnapshot.child("End Time").getValue(String.class);

                TimetableActivity.this.subjectName2.setText(subjectName2);
                classTime2.setText(startTime2 + " - " + endTime2);
                roomNumber2.setText(roomNo2);
                TimetableActivity.this.teacherName2.setText(teacherName2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Third Slot Thursday
        databaseReferenceTimeTable3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String roomNo3 = dataSnapshot.child("Room No").getValue(String.class);
                String subjectName3 = dataSnapshot.child("Subject Name").getValue(String.class);
                String teacherName3 = dataSnapshot.child("Teacher Name").getValue(String.class);
                String startTime3 = dataSnapshot.child("Start Time").getValue(String.class);
                String endTime3 = dataSnapshot.child("End Time").getValue(String.class);

                TimetableActivity.this.subjectName3.setText(subjectName3);
                classTime3.setText(startTime3 + " - " + endTime3);
                roomNumber3.setText(roomNo3);
                TimetableActivity.this.teacherName3.setText(teacherName3);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void retrieveFbDatabase() {
        /*String userID = firebaseAuth.getCurrentUser().getUid();
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        databaseReferenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String program = dataSnapshot.child("program").getValue(String.class);
                String semester = dataSnapshot.child("semester").getValue(String.class);
                String section = dataSnapshot.child("section").getValue(String.class);
                String campus = dataSnapshot.child("campus").getValue(String.class);

                *//*userSD.setName(name);
                userSD.setProgram(program);
                userSD.setSemester(semester);
                userSD.setSection(section);
                userSD.setCampus(campus);*//*

                mProgram=program;
                mSemester=semester;
                mSection=section;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void notification(String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(TimetableActivity.this);
        builder.setSmallIcon(R.drawable.ic_cap);
        builder.setBadgeIconType(R.drawable.ic_book);
        builder.setContentTitle(title);
        builder.setContentText(content);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}
