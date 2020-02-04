package com.achba.studenthub.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.achba.studenthub.Conveyance.ConveyanceStartActivity;
import com.achba.studenthub.Conveyance.FindConveyanceProfileActivity;
import com.achba.studenthub.Conveyance.OfferConveyanceActivity;
import com.achba.studenthub.MessageActivity;
import com.achba.studenthub.Model.Chat;
import com.achba.studenthub.Model.Notification;
import com.achba.studenthub.Model.Roommate;
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

import java.util.HashMap;
import java.util.List;

import static android.graphics.Color.parseColor;
import static android.graphics.Color.rgb;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context mContext;
    private List<Notification> mUsers;
    private boolean status;
//    private String type;

    String theLastMessage;

    public NotificationAdapter(Context mContext, List<Notification> mUsers, boolean status){
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.status = status;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Notification notification = mUsers.get(position);

        String description="<font color='DarkGray'>"+ notification.getDescription() +"</font>";
        String name ="<b>" + notification.getName() + "</b> " + description;

        holder.time_stamp.setText(notification.getTimeStamp());
        holder.name.setText(Html.fromHtml(name));
            Glide.with(mContext).load(notification.getProfileImageUrl()).into(holder.profile_image);


            if (notification.getStatus().equals("seen")){
                holder.layout.setBackgroundColor(Color.WHITE);
            } else {
                holder.layout.setBackgroundColor(parseColor("#EEEEEE"));
            }

            /*if (notification.getType().equals("Roommate")){
                Intent intent = new Intent(mContext, FindRoomProfileActivity.class);
                intent.putExtra("userID", notification.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            } else {
                return;
            }*/



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);*/

//                Log.i("Notification", "Function ID: "+notification.getId()+" Firebase ID: "+ FirebaseAuth.getInstance().getCurrentUser().getUid());
                if (notification.getType().equals("Roommate") &&
                        !(notification.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))){
                    Intent intent = new Intent(mContext, FindRoomProfileActivity.class);
                    intent.putExtra("userID", notification.getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
                else if (notification.getType().equals("Conveyance") &&
                        !(notification.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))){
                    Intent intent = new Intent(mContext, FindConveyanceProfileActivity.class);
                    intent.putExtra("userID", notification.getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
               else if (notification.getType().equals("Admin")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage(notification.getDescription())
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    return;
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    return;
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        public RelativeLayout layout;
        public TextView name;
        public ImageView profile_image;
        private TextView time_stamp;

        public ViewHolder(View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.layoutNotification);
            name = itemView.findViewById(R.id.name);
            profile_image = itemView.findViewById(R.id.profile_image);
            time_stamp = itemView.findViewById(R.id.time_stamp);
        }
    }

    //check for last message
    private void lastMessage(final String userid, final TextView last_msg){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = chat.getMessage();
                        }
                    }
                }

                switch (theLastMessage){
                    case  "default":
                        last_msg.setText("No Message");
                        break;

                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void uploadNotificationData(){
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReferenceNotification = FirebaseDatabase.getInstance().getReference("Notifications").push();

        HashMap dataMap = new HashMap();
//        dataMap.put("id", userID);
//        dataMap.put("timeStamp", timeStamp);
//        dataMap.put("description", " is interested to find new roommates.");
//        dataMap.put("name", name);
        dataMap.put("status", "not seen");
//        dataMap.put("profileImageUrl", profileImageUrl);
        databaseReferenceNotification.updateChildren(dataMap);
    }
}

