package com.gray.mayakalarm2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    SimpleDateFormat df = new SimpleDateFormat("dd.mm.YYYY hh:mm:ss");
    public static String INTENT_FILTER = "mayakAlarmIntentFilter";
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            log("onReceive start");
            Toast.makeText(getApplicationContext(), "MayakAlarm2 onReceive start intent", Toast.LENGTH_LONG).show();
            // Get extra data included in the Intent
            String message = intent.getStringExtra("key");
            log(message);
            // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter(INTENT_FILTER));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        log("after on start");
        Log.i(null, "In onStart");
        startService(new Intent(this, MayakService.class));


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void log(String msg) {
        EditText editText = findViewById(R.id.edit_text);
        try {
            editText.getText().append(df.format(new Date()) + ": " + msg + "\n");
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "MayakAlarm2 exception " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

}
