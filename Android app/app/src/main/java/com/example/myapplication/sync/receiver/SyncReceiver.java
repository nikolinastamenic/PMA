package com.example.myapplication.sync.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.util.NetworkStateTools;

public class SyncReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int status = NetworkStateTools.getConnectivityStatus(context);

//        int resultCode = intent.getExtras().getInt(SyncService.RESULT_CODE);


        if (intent.getAction().equals(MainActivity.SYNC_DATA)) {
            if(status != 0 ) {
                Toast.makeText(context, "Povezani ste na internet", Toast.LENGTH_SHORT).show();
            }

        }


    }
}

