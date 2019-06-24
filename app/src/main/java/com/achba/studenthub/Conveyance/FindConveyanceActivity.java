package com.achba.studenthub.Conveyance;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.achba.studenthub.Adapter.ConveyanceAdapter;
import com.achba.studenthub.Adapter.RoommateAdapter;
import com.achba.studenthub.Model.Conveyance;
import com.achba.studenthub.Model.Roommate;
import com.achba.studenthub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FindConveyanceActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ConveyanceAdapter userAdapter;
    private List<Conveyance> mUsers;
    private LinearLayout layoutPlaceHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_conveyance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
        layoutPlaceHolder = findViewById(R.id.layout_placeHolder);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));


        mUsers = new ArrayList<>();

        readUsers();
    }

    private void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Conveyance");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Conveyance user=snapshot.getValue(Conveyance.class);

                        assert user !=null;
                        assert firebaseUser !=null;
                        if (!user.getId().equals(firebaseUser.getUid())) {
                            mUsers.add(user);
                        }

                    }

                    if(!mUsers.isEmpty()){
                    layoutPlaceHolder.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    userAdapter = new ConveyanceAdapter(FindConveyanceActivity.this, mUsers);
                    recyclerView.setAdapter(userAdapter);
                    }
                    else {
                        recyclerView.setVisibility(View.GONE);
                        layoutPlaceHolder.setVisibility(View.VISIBLE);
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
