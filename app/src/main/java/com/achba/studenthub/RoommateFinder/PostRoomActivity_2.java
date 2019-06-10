package com.achba.studenthub.RoommateFinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.achba.studenthub.R;

public class PostRoomActivity_2 extends AppCompatActivity {
    private Spinner spinnerHome;
    TextView bedRoom, bathRoom;
    int bathRoomValue, bedRoomValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_room_2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bedRoom = findViewById(R.id.tvBedroom);
        bathRoom = findViewById(R.id.tvBathroom);

        bedRoomValue = Integer.parseInt(bedRoom.getText().toString());
        bathRoomValue = Integer.parseInt(bathRoom.getText().toString());

        spinnerHome = findViewById(R.id.spinnerHome);
        ArrayAdapter<CharSequence> adapterHome = ArrayAdapter.createFromResource(this,
                R.array.spinnerHome, android.R.layout.simple_spinner_item);
        adapterHome.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHome.setAdapter(adapterHome);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void onClickMinusBedroom(View view) {
        if(bedRoomValue>1){
            bedRoomValue--;
            bedRoom.setText(Integer.toString(bedRoomValue));
        }else{
            Toast.makeText(this, "Rooms must be more or equal to 1.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickPlusBedroom(View view) {
        bedRoomValue++;
        bedRoom.setText(Integer.toString(bedRoomValue));
    }

    public void onClickMinusBathroom(View view) {
        if(bathRoomValue>1){
            bathRoomValue--;
            bathRoom.setText(Integer.toString(bathRoomValue));
        }else{
            Toast.makeText(this, "Rooms must be more or equal to 1.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickPlusBathroom(View view) {
        bathRoomValue++;
        bathRoom.setText(Integer.toString(bathRoomValue));
    }

    public void onClickNext(View view) {
        Intent intent = new Intent(this, PostRoomActivity_3.class);
        startActivity(intent);
    }
}
