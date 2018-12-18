package com.gray.mayakalarm2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class MayakService extends Service {
    final String LOG_TAG = "mayakLogs";


    private Alarm alarm = new Alarm();

    public void onCreate() {
        super.onCreate();
        //log("MayakService.onCreate done");
        //Toast.makeText(getBaseContext(), "MayakAlarm2 onCreate()", Toast.LENGTH_LONG).show();
        Log.i(LOG_TAG, "In MayakService.onCreate");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        //log("MayakAlarm2 Service Started");
        alarm.setAlarm(this);
        Log.i(LOG_TAG, "In MayakService.onStartCommand");
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "In MayakService.onDestroy");
    }

    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "In MayakService.onBind");
        return null;
    }

    private void log(String msg) {
        Intent intent = new Intent("intentKey");
// You can also include some extra data.
        intent.putExtra("key", msg);
        sendBroadcast(intent);
        LocalBroadcastManager.getInstance(this.getApplicationContext()).sendBroadcast(intent);
        Log.d(LOG_TAG, "In MayakService.log msg " + msg);
    }
}
