package com.achba.studenthub;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.achba.studenthub.Adapter.UserAdapter;
import com.achba.studenthub.Decoration.SimpleDividerItemDecoration;
import com.achba.studenthub.Model.Chat;
import com.achba.studenthub.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;
    private List<String> usersList;
    FirebaseUser fUser;
    DatabaseReference reference;

    private FloatingActionButton fab;
    private LinearLayout layoutPlaceHolder;

    public ChatFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        fab = view.findViewById(R.id.fab);
        layoutPlaceHolder = view.findViewById(R.id.layout_placeHolder);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));

        /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(getContext()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);*/
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat=snapshot.getValue(Chat.class);

                    if (chat.getSender().equals(fUser.getUid())) {
                        /*recyclerView.setVisibility(View.VISIBLE);
                        layoutPlaceHolder.setVisibility(View.GONE);*/
                        usersList.add(chat.getReceiver());
                    }
                    if(chat.getReceiver().equals(fUser.getUid())){
                        usersList.add(chat.getSender());
                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UsersActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void readChats() {
        mUsers = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    //Display 1 user from Chat
                    for(String id: usersList){
                        if(user.getId().equals(id)){
                            if(mUsers.size() !=0){
                                for(User user1: mUsers){
                                    if(!user.getId().equals(user1.getId())){
                                        mUsers.add(user);
                                    }
                                }
                            }else {
                                mUsers.add(user);
                            }
                        }
                    }
                    userAdapter = new UserAdapter(getContext(), mUsers, true);
                    recyclerView.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*private void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                *//*if (search_users.getText().toString().equals("")) {*//*
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user=snapshot.getValue(User.class);

                        assert user !=null;
                        assert firebaseUser !=null;
                        if (!user.getId().equals(firebaseUser.getUid())) {
                            recyclerView.setVisibility(View.VISIBLE);
                            layoutPlaceHolder.setVisibility(View.GONE);
                            mUsers.add(user);
                        }

                    }

                    userAdapter = new UserAdapter(getContext(), mUsers ,true);
                    recyclerView.setAdapter(userAdapter);
//              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/
}
