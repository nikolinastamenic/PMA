package com.example.myapplication.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.util.AppConfig;
import com.example.myapplication.util.NetworkStateTools;

public class SyncService  extends Service {

    public static String RESULT_CODE = "RESULT_CODE";


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent ints = new Intent(MainActivity.SYNC_DATA);
        int status = NetworkStateTools.getConnectivityStatus(getApplicationContext());
        String email = intent.getStringExtra("Email");

        ints.putExtra(RESULT_CODE, status);

        //ima konekcije ka netu skini sta je potrebno i sinhronizuj bazu
        String uri = AppConfig.apiURI + "task/all";

        if(status == NetworkStateTools.TYPE_WIFI || status == NetworkStateTools.TYPE_MOBILE){
            if(!email.equals("") ) {
                new SyncTask(getApplicationContext()).execute(uri, email);
            }
        }

//        sendBroadcast(ints);

        stopSelf();

        return START_REDELIVER_INTENT;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
