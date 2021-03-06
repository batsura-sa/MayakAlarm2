package com.gray.mayakalarm2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AutoStart extends BroadcastReceiver
{   
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i(null, "In AutoStart.onReceive");

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            Intent service = new Intent(context, MayakService.class);
            context.startService(service);
        }
    }
}