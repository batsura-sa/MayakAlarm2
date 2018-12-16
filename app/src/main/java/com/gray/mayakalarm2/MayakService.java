package com.gray.mayakalarm2;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

public class MayakService extends Service
{
    private final IBinder binder = new LocalBinder();
    private ServiceCallbacks serviceCallbacks;

    private Alarm alarm = new Alarm();
    public void onCreate()
    {
        super.onCreate();       
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) 
    {
        //serviceCallbacks.log( "MayakAlarm2 Service Started");
        alarm.setAlarm(this, serviceCallbacks);
        return START_STICKY;
    }

    public class LocalBinder extends Binder {
        MayakService getService() {
            return MayakService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setCallbacks(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
    }
}