package com.gray.mayakalarm2;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.widget.Toast;

public class MayakService extends Service
{
    Alarm alarm = new Alarm();
    public void onCreate()
    {
        super.onCreate();       
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) 
    {
        Toast.makeText(this, "MayakAlarm2 Service Started", Toast.LENGTH_LONG).show();
        alarm.setAlarm(this);
/* don't work
        ComponentName receiver = new ComponentName(this, Alarm.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
  */
        return START_STICKY;
    }

   @Override        
   public void onStart(Intent intent, int startId)
    {
        onStartCommand(intent, 0, startId);
    }

    @Override
    public IBinder onBind(Intent intent) 
    {
        return null;
    }
}