package com.gray.mayakalarm2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private final Messenger mainActivityMessenger = new Messenger(new ActivityHandler());
    private Boolean isBound = false;
    private  Messenger mServiceMessenger;


    SimpleDateFormat df = new SimpleDateFormat("dd.mm.YYYY hh:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //log("start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindService(new Intent(this, MayakService.class),
                mServiceConnection,
                Context.BIND_AUTO_CREATE);

    }
    @Override
    public void onDestroy(){
        super.onDestroy();

        unbindService(mServiceConnection);
    }
    @Override
    protected void onStart() {
        super.onStart();
        // bind to Service
        log("before intent");
        //Intent intent = new Intent(this, MayakService.class);
        //log("before startservice");
        //startService(intent);
        //log("beforeBindService");
        //bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        //log("onStart done");

    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBound = true;

            mServiceMessenger = new Messenger(binder);

            if (mServiceMessenger != null) {
                Message msg = Message.obtain();
                msg.replyTo = mainActivityMessenger;
                try {
                    mServiceMessenger.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    public class ActivityHandler extends Handler {

        @Override
        public void handleMessage(Message msg){
            log((String) msg.getData().get("msg"));
        }

    }

    public void log(String msg) {
        EditText editText = findViewById(R.id.edit_text);
        try {
            editText.getText().append(df.format(new Date())+": "+msg+"\n");
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "MayakAlarm2 exception " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}
