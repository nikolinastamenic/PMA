package com.example.myapplication.sync.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.sync.restTask.ChangePasswordTask;
import com.example.myapplication.sync.restTask.FinishTaskTask;
import com.example.myapplication.sync.restTask.ForgotPasswordTask;
import com.example.myapplication.sync.restTask.LoginTask;
import com.example.myapplication.sync.restTask.NewReportItemTask;
import com.example.myapplication.sync.restTask.RequestTaskTask;
import com.example.myapplication.sync.restTask.SyncTask;
import com.example.myapplication.sync.restTask.UserTask;
import com.example.myapplication.util.AppConfig;
import com.example.myapplication.util.NetworkStateTools;

public class SyncService extends Service {

    public static String RESULT_CODE = "RESULT_CODE";


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent ints = new Intent(MainActivity.SYNC_DATA);
        int status = NetworkStateTools.getConnectivityStatus(getApplicationContext());
        String email = "";
        if (intent.getStringExtra("Email") != null) {

            email = intent.getStringExtra("Email");
        }
        String activity = intent.getStringExtra("activityName");
        String finish = "";
        if (intent.getStringExtra("finishTask") != null) {
            finish = intent.getStringExtra("finishTask");
        }

        ints.putExtra(RESULT_CODE, status);

        //ima konekcije ka netu skini sta je potrebno i sinhronizuj bazu
        if (status == NetworkStateTools.TYPE_WIFI || status == NetworkStateTools.TYPE_MOBILE) {

            if (!email.equals("") && activity.equals("MainActivity")) {
                new SyncTask(getApplicationContext()).execute(AppConfig.apiURI + "task/all", email);
            } else if (!email.equals("") && activity.equals("LoginActivity")) {
                new LoginTask(getApplicationContext()).execute(AppConfig.apiURI + "user/login", email, intent.getStringExtra("Password"));
            } else if (!email.equals("") && activity.equals("AllTasksActivity")) {
                String mysqlId = intent.getStringExtra("MySqlId");
                new RequestTaskTask(getApplicationContext()).execute(AppConfig.apiURI + "task/change/state", email, mysqlId);
            } else if (!email.equals("") && activity.equals("ProfileActivity")) {
                new UserTask(getApplicationContext()).execute(AppConfig.apiURI + "user/email/" + email, email);
            } else if (!email.equals("") && (activity.equals("NewItemActivity") || activity.equals("ReportActivity"))) {
                new NewReportItemTask(getApplicationContext()).execute(AppConfig.apiURI + "report/item/new");
            } else if (email.equals("") && finish.equals("true") && (activity.equals("ReportActivity") || activity.equals("FinishedTasksActivity") )) {
                new FinishTaskTask(getApplicationContext()).execute(AppConfig.apiURI + "task/change/state");
            } else if (!email.equals("") && activity.equals("ForgotPasswordActivity")) {
                new ForgotPasswordTask(getApplicationContext()).execute(AppConfig.apiURI + "user/forgot-password/" + email, email);
            } else if (activity.equals("ChangePasswordActivity")) {
                String password = intent.getStringExtra("newPassword");
                new ChangePasswordTask(getApplicationContext()).execute(AppConfig.apiURI + "user/change-password", password);
            }

        }

//        sendBroadcast(ints);

        stopSelf();

        return START_REDELIVER_INTENT;      //ako nismo povezani na internet necemo da pokrenemo sinhronizaciju,
        //hocu da vidim da li trenutno ima interneta, ako ne onda nemoj pokretati sinhronizaciju
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
