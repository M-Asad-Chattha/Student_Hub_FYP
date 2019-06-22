package com.achba.studenthub;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.achba.studenthub.Adapter.NotificationAdapter;
import com.achba.studenthub.Model.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> mNotifications;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mNotifications = new ArrayList<>();
        readNotifications();

        return view;
    }

    private void readNotifications() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mNotifications.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Notification user=snapshot.getValue(Notification.class);

                        assert user !=null;
                        assert firebaseUser !=null;
                            mNotifications.add(user);
                    notificationAdapter = new NotificationAdapter(getContext(), mNotifications,false);
                    recyclerView.setAdapter(notificationAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
