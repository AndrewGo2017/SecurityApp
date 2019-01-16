package ru.sbrf.android.securityapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static ru.sbrf.android.securityapp.App.CHANNEL_ID;

public class SecurityService extends Service {
    public static List<String> DATA_STORAGE = new ArrayList<>();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        final SecurityService context = this;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i< 100; i++){
//                    DATA_STORAGE.add("message # " + i);
//                    sendPush(context, "message # " + i);
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        }).start();

        connectToWebSocket();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    WebSocketClient mWebSocketClient = null;

    private void connectToWebSocket() {
        URI uri = null;
        try {
            String s = Base64.encodeToString("Polz1:976849".getBytes("utf-8"), Base64.DEFAULT).replace("\n", "");
            uri = new URI("ws://geosb.mckrona.ru/restapi/socket/alerts?authorization=" + s);
//            uri = new URI("ws://10.0.2.2:8080/socket");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final SecurityService context = this;
        mWebSocketClient = new WebSocketClient(uri) {


            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("serverHandshake", serverHandshake.toString());
                sendPush(context, "serverHandshake");
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                Log.i("onMessage", s);
//                DATA_STORAGE.add(s);
//                sendPush(context, s);
                try {
                    JSONArray jarr = new JSONArray(s);
                    JSONArray jarrAlarm = new JSONArray();
                    for (int i = 0; i < jarr.length(); i++) {
                        JSONObject jsonObject = jarr.getJSONObject(i);
                        boolean isAlarm = jsonObject.getBoolean("isAlarm");
                        if (isAlarm) {
                            jarrAlarm.put(jsonObject);
                        } else{
                            DATA_STORAGE.add(jsonObject.toString());
                            sendPush(context, jsonObject.toString());
                        }
                    }
                    if (jarrAlarm.length() > 0) {
                        openActivity(jarrAlarm.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onClose(int i, String s, boolean b) {
                sendPush(context, "serverOnClose");
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                sendPush(context, "serverOnError");
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
//        mWebSocketClient.send("hello");
    }

    private volatile int counter = 0;

    private void sendPush(final SecurityService context, final String message) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Intent notificationIntent = new Intent(context, FalseAlarmActivity.class);
                notificationIntent.putExtra("data", message);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

                Notification notification = new Notification.Builder(context, CHANNEL_ID)
                        .setContentText("Security Service")
                        .setContentText("Info. " + message)
                        .setSmallIcon(R.drawable.ic_announcement)
                        .setContentIntent(pendingIntent)
                        .build();


                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(++counter, notification);

//                startForeground(++counter, notification);
            }
        });

        thread.start();
    }

    public void openActivity(String data) {
        Intent intent = new Intent(this, TrueAlarmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("data", data);
        startActivity(intent);
    }
}

