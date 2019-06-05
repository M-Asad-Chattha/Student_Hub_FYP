package com.achba.studenthub;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.achba.studenthub.Model.Chat;
import com.achba.studenthub.Model.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    ProgressDialog progress;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    AudioManager audioManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TabLayout tabLayout = findViewById(R.id.tabLayout);
        final ViewPager viewPager = findViewById(R.id.viewPager);
        final TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(pagerAdapter);
//        tabLayout.setupWithViewPager(viewPager);

        //Cutomized PagerAdapter call
        //below wxperiment for end fragment loading delay
        pagerAdapter.addFragment(new DashboardFragment(), "Dashboard");
        pagerAdapter.addFragment(new NotificationFragment(), "Notification");
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                TabPagerAdapter viewPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
                int unread = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && !chat.isIsseen()){
                        unread++;
                    }
                }
                /*viewPagerAdapter.addFragment(new DashboardFragment(), "Dashboard");
                viewPagerAdapter.addFragment(new NotificationFragment(), "Notification");*/
                if (unread == 0){
                    pagerAdapter.addFragment(new ChatFragment(), "Chats");
                } else {
                    pagerAdapter.addFragment(new ChatFragment(), "("+unread+") Chats");
                }

                viewPager.setAdapter(pagerAdapter);

                tabLayout.setupWithViewPager(viewPager);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        progress = new ProgressDialog(this);
        audioManager= (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);


        userinfoDrawerLayout();
        vibrationMood();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        // Retrieve the SearchView and plug it into SearchManager
        /*final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_logout) {
            signOut();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        firebaseAuth.signOut();
//        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            stopLoading();
            Toast.makeText(getApplicationContext(), "Log Out failed.", Toast.LENGTH_SHORT).show();
        } else {
            stopLoading();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Log Out Successfully.", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_events) {

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else if (id == R.id.nav_settings) {

        }else if (id == R.id.nav_feedback) {

        }else if (id == R.id.nav_help) {

        }else if (id == R.id.nav_logout) {
            startLoading();
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClickNotification(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void startLoading(){
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }

    private void stopLoading(){
        progress.dismiss();
    }

    public void userinfoDrawerLayout(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        final ImageView imageView=headerView.findViewById(R.id.imageView);
        final TextView nameDrawerHeader = headerView.findViewById(R.id.name_drawerHeader);
        //        TextView emailDrawerHeader = (TextView) headerView.findViewById(R.id.email_drawerHeader);

        String userID=firebaseAuth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Chat Related Modification
                User user = dataSnapshot.getValue(User.class);
                Toast.makeText(MainActivity.this, "Chat: "+user.getName(), Toast.LENGTH_SHORT).show();
                if(dataSnapshot.exists()) {
                    String userName=dataSnapshot.child("userName").getValue(String.class);
                    String profileImageUrl=dataSnapshot.child("profileImageUrl").getValue(String.class);
                    Uri imageUrl = Uri.parse(profileImageUrl);

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.profileimg_placeholder);
                    requestOptions.error(R.drawable.profileimg_placeholder);
                    Glide.with(getApplicationContext())
                            .setDefaultRequestOptions(requestOptions)
                            .load(imageUrl)
                            .into(imageView);
                    nameDrawerHeader.setText(userName);
                }/*else{
                    Toast.makeText(MainActivity.this, "Data retrieve Problem", Toast.LENGTH_SHORT).show();
                }*/  // No need to show userSD about data error in dashboard
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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

            notification("Vibration Mode inactivate ",
                    "University time ends, your device is now in normal mode.");
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }

    public void notification(String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);
        builder.setSmallIcon(R.drawable.ic_cap);
        builder.setBadgeIconType(R.drawable.ic_book);
        builder.setContentTitle(title);
        builder.setContentText(content);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

}
