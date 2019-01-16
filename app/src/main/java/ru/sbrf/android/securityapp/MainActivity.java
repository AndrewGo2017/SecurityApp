package ru.sbrf.android.securityapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onStartService(View view) {
        Intent serviceIntent = new Intent(this, SecurityService.class);
        startService(serviceIntent);
    }

    public void onStopService(View view) {
        stopService(new Intent(this, SecurityService.class));
    }
}
