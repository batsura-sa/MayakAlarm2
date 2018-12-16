package com.gray.mayakalarm2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements ServiceCallbacks {
    private MayakService mayakService;
    private boolean bound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //log("start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();
        // bind to Service
        Intent intent = new Intent(this, MayakService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);


    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from service
        if (bound) {
            mayakService.setCallbacks(null); // unregister
            unbindService(serviceConnection);
            bound = false;
        }
    }

    /**
     * Callbacks for service binding, passed to bindService()
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // cast the IBinder and get MayakService instance
            MayakService.LocalBinder binder = (MayakService.LocalBinder) service;
            mayakService = binder.getService();
            bound = true;
            mayakService.setCallbacks(MainActivity.this); // register
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

    @Override
    public void log(String msg) {
        EditText editText = findViewById(R.id.edit_text);
        try {
            editText.getText().append(new Date() + msg);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "MayakAlarm2 exception " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}
