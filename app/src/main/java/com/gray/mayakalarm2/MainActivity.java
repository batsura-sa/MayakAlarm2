package com.gray.mayakalarm2;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        File logFile = new File( getBaseContext().getFilesDir(), "mayakalarm.log");



        Alarm.log(getBaseContext(),"start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(getBaseContext(), "MayakAlarm2 file=" +logFile.getAbsolutePath()
                , Toast.LENGTH_LONG).show();
                startService(new Intent(getBaseContext(), MayakService.class));
    }
}
