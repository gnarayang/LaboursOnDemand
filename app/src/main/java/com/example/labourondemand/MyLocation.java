package com.example.labourondemand;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;

public class MyLocation extends BroadcastReceiver {

    public static final String ACTION_PROCESS_UPDATE = "com.example.labourondemand";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null){

            final  String action = intent.getAction();
            if(ACTION_PROCESS_UPDATE.equals(action))
            {
                LocationResult result = LocationResult.extractResult(intent);
                if(result != null){

                    Location location = result.getLastLocation();
                    String location_string = new StringBuilder(""+location.getLatitude())
                            .append("/")
                            .append(location.getLongitude())
                            .toString();
                    try{
                        LabourerHomeActivity.getInstance().update(location_string);
                    }catch (Exception ex)
                    {
                        Toast.makeText(context,location_string+"!",Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
}
