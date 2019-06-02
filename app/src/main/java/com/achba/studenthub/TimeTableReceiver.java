package com.achba.studenthub;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class TimeTableReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, TimeTableIntentService.class);
        intent1.setData((Uri.parse("custom://"+System.currentTimeMillis())));
        context.startService(intent1);
    }
}
