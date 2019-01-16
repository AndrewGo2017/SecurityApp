package ru.sbrf.android.securityapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class FalseAlarmActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_false_alarm);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <= SecurityService.DATA_STORAGE.size(); i++) {
            stringBuilder
                    .append(i)
                    .append("\n")
                    .append(SecurityService.DATA_STORAGE.get(i - 1)).
                    append("\n\n");
        }

//        String data = getIntent().getStringExtra("data");
        textView = findViewById(R.id.textViewFalseAlarm);


        textView.setText(stringBuilder.toString());
    }
}
