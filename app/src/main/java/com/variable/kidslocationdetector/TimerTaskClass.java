package com.variable.kidslocationdetector;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;

public class TimerTaskClass {
    Timer timer = new Timer();

    public void startTimer() {

        Log.d("Constants", "Timer Started");
        timer.scheduleAtFixedRate(new java.util.TimerTask() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                Log.e("con","started");
            }
        }, 60000, 60000);

    }
    public void stopTimer() {
        timer.cancel();
        Log.e("con","stopped");

    }}