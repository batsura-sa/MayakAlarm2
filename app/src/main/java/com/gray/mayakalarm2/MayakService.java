package com.gray.mayakalarm2;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class MayakService extends Service
{
    private Messenger toActivityMessenger;
    IncomingHandler inHandler;

    Messenger messanger;



    private Alarm alarm = new Alarm();
    public void onCreate()
    {
        super.onCreate();
        HandlerThread thread = new HandlerThread("ServiceStartArguments");
        thread.start();
        inHandler = new IncomingHandler(thread.getLooper());
        messanger = new Messenger(inHandler);
        log("MayakService.onCreate done");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return messanger.getBinder();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) 
    {
        log( "MayakAlarm2 Service Started");
        alarm.setAlarm(this );

        return START_STICKY;
    }

    private void log(String msg) {
        Message outMsg = Message.obtain();
        Bundle data = Bundle.EMPTY;
        data.putString("msg", msg);
        outMsg.setData(data);

        try {
            toActivityMessenger.send(outMsg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private class IncomingHandler extends Handler {
        public IncomingHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg){
            toActivityMessenger = msg.replyTo;
        }
    }
}