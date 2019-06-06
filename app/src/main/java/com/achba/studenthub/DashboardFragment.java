package com.achba.studenthub;


import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import java.util.Calendar;

public class DashboardFragment extends Fragment  {

    private View view;
    private CardView cardGPA, cardTODO, cardRoommate, cardConveyance, cardTimeTable, cardBooks;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_dashboard, container, false);

        cardGPA = view.findViewById(R.id.card_gpa);
        cardGPA.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), GPACalculatorActivity.class);
                startActivity(intent);
            }
        });

        cardTODO = view.findViewById(R.id.card_reminder);
        cardTODO.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ReminderActivity.class);
                startActivity(intent);
            }
        });

        cardRoommate = view.findViewById(R.id.card_roommate);
        cardRoommate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RoommateActivity.class);
                startActivity(intent);
            }
        });

        cardConveyance = view.findViewById(R.id.card_conveyance);
        cardConveyance.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

        cardTimeTable = view.findViewById(R.id.card_timetable);
        cardTimeTable.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TimetableActivity.class);
                startActivity(intent);
            }
        });

        cardBooks = view.findViewById(R.id.card_books);
        cardBooks.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BookActivity.class);
                startActivity(intent);

                /*String url = "http://digitallibrary.edu.pk";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);*/
            }
        });

        return view;
    }


}
