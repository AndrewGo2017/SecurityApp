package ru.sbrf.android.securityapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, SecurityService.class);
        context.startService(serviceIntent);
    }
}
