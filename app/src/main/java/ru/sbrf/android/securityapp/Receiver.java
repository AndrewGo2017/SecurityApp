package ru.sbrf.android.securityapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class Receiver extends BroadcastReceiver {
    private static List<LocalTime> counter = new ArrayList<LocalTime>() {{add(LocalTime.now());}};

    @Override
    public void onReceive(Context context, Intent intent) {
        check();
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF) || intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            counter.add(LocalTime.now());
            if (counter.size() > 4){
                Intent i = new Intent(context, SecurityService.class);
                i.putExtra("screen_state", 1);
                context.startService(i);

                counter.clear();
            }
        }
    }



    public synchronized void check(){
        LocalTime current;
        if (counter.size() > 0){
            current = counter.get(0);
        } else {
            current = LocalTime.now();
            counter.add(current);
        }

        for (int i = 1; i< counter.size(); i++){
            int diff = current.toSecondOfDay() - counter.get(i).toSecondOfDay();
            if (diff > 2){
                counter.clear();
                counter.add(counter.get(i));
                break;
            }
            current = counter.get(i);
        }
    }
}
