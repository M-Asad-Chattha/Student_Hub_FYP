package com.achba.studenthub.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.achba.studenthub.MessageActivity;
import com.achba.studenthub.Model.Chat;
import com.achba.studenthub.Model.Roommate;
import com.achba.studenthub.Model.User;
import com.achba.studenthub.R;
import com.achba.studenthub.RoommateFinder.FindRoomActivity;
import com.achba.studenthub.RoommateFinder.FindRoomProfileActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class RoommateAdapter extends RecyclerView.Adapter<RoommateAdapter.ViewHolder> {

    private Context mContext;
    private List<Roommate> mUsers;

    public RoommateAdapter(Context mContext, List<Roommate> mUsers) {
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_roommate_item, parent, false);
        return new RoommateAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Roommate user = mUsers.get(position);
        holder.name.setText(user.getName());
        holder.employer.setText(user.getEmployer());

            Glide.with(mContext).load(user.getImageURL()).into(holder.imageView);

        // Show complete Roommate Info
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FindRoomProfileActivity.class);
                intent.putExtra("userID", user.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView employer;
        public CircularImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            employer = itemView.findViewById(R.id.employer);
            imageView = itemView.findViewById(R.id.profile_image);
        }
    }
}

