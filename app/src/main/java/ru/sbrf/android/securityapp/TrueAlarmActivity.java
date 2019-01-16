package ru.sbrf.android.securityapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TrueAlarmActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_alarm);

        String data = getIntent().getStringExtra("data");
        textView = findViewById(R.id.textViewTrueAlarm);
        textView.setText(data);
    }
}
