package com.gray.mayakalarm2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;

public class Alarm extends WakefulBroadcastReceiver {

    public static void log(Context context, String str) {

        try {
            File logFile = new File( context.getFilesDir(),"mayakalarm.log");

            FileOutputStream outputStream = new FileOutputStream(logFile);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);

            writer.write(new Date() + ":" + str + "\n");

            writer.close();

        } catch (Exception e) {
            Toast.makeText(context, "MayakAlarm2 exception " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {


        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        log(context, "onReceive! day=" + day + " hour=" + hour);

        Toast.makeText(context, "MayakAlarm2 onReceive! day " + day + " hour " + hour, Toast.LENGTH_LONG).show();

/*
        if (day > 5) {
            Toast.makeText(context, "MayakAlarm2! day_of_week>5" + day, Toast.LENGTH_LONG).show();
            return;
        }
*/


        if (hour < 6 || hour > 24) {
            Toast.makeText(context, "MayakAlarm2! hour " + hour, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            while (Calendar.getInstance().get(Calendar.SECOND) != 38) {
                Thread.sleep(100);
            }
        } catch (Exception e) {
            Toast.makeText(context, "MayakAlarm2Exception! " + e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();


        MediaPlayer mp = MediaPlayer.create(context, R.raw.mayak);
        mp.setLooping(false);
        mp.start();

        wl.release();



        setAlarm(context);
    }

    public void setAlarm(Context context) {
        int min = 59;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 1);

        long delta = calendar.getTimeInMillis()-Calendar.getInstance().getTimeInMillis();
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HOUR, pendingIntent);
        //alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, delta, pendingIntent);
        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HOUR, pendingIntent);
        Toast.makeText(context, "MayakAlarm27 setTimerOk! delta="+delta+" min=" +delta/60000, Toast.LENGTH_LONG).show();

        Intent intent2 = new Intent(context, MainActivity.class);
        PendingIntent pi2 = PendingIntent.getActivity(context, 0, intent2, 0);

        AlarmManager.AlarmClockInfo info =  new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), pi2);

        alarmManager.setAlarmClock(info, pendingIntent);

    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}