package com.achba.studenthub.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.achba.studenthub.Conveyance.FindConveyanceProfileActivity;
import com.achba.studenthub.Model.Conveyance;
import com.achba.studenthub.Model.Roommate;
import com.achba.studenthub.R;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class ConveyanceAdapter extends RecyclerView.Adapter<ConveyanceAdapter.ViewHolder> {

    private Context mContext;
    private List<Conveyance> mUsers;

    public ConveyanceAdapter(Context mContext, List<Conveyance> mUsers) {
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_roommate_item, parent, false);
        return new ConveyanceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Conveyance user = mUsers.get(position);
        holder.name.setText(user.getName());
        holder.employer.setText(user.getCost());
        holder.address.setText(user.getLocation());

            Glide.with(mContext).load(user.getProfileImageUrl()).into(holder.imageView);

        // Show complete Roommate Info
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FindConveyanceProfileActivity.class);
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
        public TextView address;
        public CircularImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            employer = itemView.findViewById(R.id.employer);
            imageView = itemView.findViewById(R.id.profile_image);
            address = itemView.findViewById(R.id.address);
        }
    }
}

