package com.gray.mayakalarm2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Alarm extends BroadcastReceiver {
    final String LOG_TAG = "mayakLogs";

    public Alarm() {
        Log.i(LOG_TAG, "in Alarm.Alarm() start!");
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(LOG_TAG, "in Alarm.onReceive start!");

        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        //serviceCallbacks.log("onReceive! day=" + day + " hour=" + hour);


        Log.i(LOG_TAG, "in Alarm.onReceive day=" + day + " hour=" + hour);

/*
        if (day > 5) {
            Toast.makeText(context, "MayakAlarm2! day_of_week>5" + day, Toast.LENGTH_LONG).show();
            return;
        }
*/


        if (hour < 6 || hour > 23) {
            Log.i(LOG_TAG, "in Alarm.onReceive bad time! return");
            return;
        }

        try {
            while (Calendar.getInstance().get(Calendar.SECOND) != 38) {
                Thread.sleep(100);
            }
        } catch (Exception e) {
            Log.i(LOG_TAG, "in Alarm.onReceive Exception! " + e.getMessage());
            return;
        }

        Log.i(LOG_TAG, "in Alarm.onReceive preparing to play music!");

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"mayakAlarm:wakelock");
            //111    32/*PowerManager.PARTIAL_WAKE_LOCK*/, "mayakalarmwl");
        wl.acquire();


        MediaPlayer mp = MediaPlayer.create(context, R.raw.mayak);
        mp.setLooping(false);
        mp.start();

        wl.release();

        Log.i(LOG_TAG, "in Alarm.onReceive done!");
    }

    public void setAlarm(Context context) {
        Log.i(LOG_TAG, "in Alarm.setAlarm start!");

        int min = 29;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, 1);

        log(context,"in Alarm.setAlarm start! " + c.toString());


        long delta = c.getTimeInMillis()-Calendar.getInstance().getTimeInMillis();
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);

//https://github.com/trikita/talalarmo/blob/master/src/main/java/trikita/talalarmo/alarm/AlarmController.java


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {        // KITKAT and later
                am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            } else {
                am.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            }
            intent = new Intent("android.intent.action.ALARM_CHANGED");
            intent.putExtra("alarmSet", true);
            context.sendBroadcast(intent);
            SimpleDateFormat fmt = new SimpleDateFormat("E HH:mm");
            Settings.System.putString(context.getContentResolver(),
                    Settings.System.NEXT_ALARM_FORMATTED,
                    fmt.format(c.getTime()));
        } else {
            Intent showIntent = new Intent(context, MainActivity.class);
            PendingIntent showOperation = PendingIntent.getActivity(context, 0, showIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(c.getTimeInMillis(), showOperation);
            am.setAlarmClock(alarmClockInfo, pendingIntent);
        }
        log(context,"in Alarm.setAlarm done!");
        Log.i(LOG_TAG, "in Alarm.setAlarm done!");

    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    private void log(Context context, String msg) {
        Intent intent = new Intent(MainActivity.INTENT_FILTER);
        intent.putExtra("key", msg);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

    }

}