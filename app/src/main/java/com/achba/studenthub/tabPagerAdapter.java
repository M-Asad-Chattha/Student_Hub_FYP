package com.achba.studenthub;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class tabPagerAdapter extends FragmentStatePagerAdapter {
    String[] tabArray={"TimeLine", "Notification", "Message"};

    public tabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabArray[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                TimelineFragment timelineFragment = new TimelineFragment();
                return timelineFragment;
            case 1:
                NotificationFragment notificationFragment = new NotificationFragment();
                return notificationFragment;
            case 2:
                MessageFragment messageFragment = new MessageFragment();
                return messageFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabArray.length;
    }
}
