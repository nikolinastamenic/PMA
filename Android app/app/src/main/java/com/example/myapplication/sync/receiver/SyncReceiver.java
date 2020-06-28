package com.example.myapplication.sync.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.util.NetworkStateTools;

public class SyncReceiver extends BroadcastReceiver {





    @Override
    public void onReceive(Context context, Intent intent) {

        int status = NetworkStateTools.getConnectivityStatus(context);

        String success = intent.getExtras().getString("success");


        Intent intent1 = new Intent();
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);


        if (intent.getAction().equals(MainActivity.SYNC_DATA)) {
            if (status != 0) {
                Toast.makeText(context, "Povezani ste na internet", Toast.LENGTH_SHORT).show();
            }


        }


    }

}

