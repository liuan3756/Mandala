package com.example.circle;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimeActivity extends Activity {
    TextView textView1;
    TextView textView2;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        textView1 = (TextView) findViewById(R.id.tv1);
        textView2 = (TextView) findViewById(R.id.tv2);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        long sss = System.currentTimeMillis();
                        String ccc = (sss + "").substring(0, 11);

                        textView2.setText(ccc + "");
                        textView1.setText(simpleDateFormat.format(new Date(sss)));

                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 100);
    }
}
