package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.sync.receiver.SyncReceiver;
import com.example.myapplication.sync.service.SyncService;
import com.example.myapplication.util.NavBarUtil;
import com.example.myapplication.util.UserSession;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private PendingIntent pendingIntent;

    private SyncReceiver sync;
    public static String SYNC_DATA = "SYNC_DATA";

    SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "login_preferences";

    String userEmail = "";
    UserSession userSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.all_tasks);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        sync = new SyncReceiver();
        userSession = new UserSession(getApplicationContext());
//        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);


        boolean isUserLogedIn = userSession.checkLogin();

        Menu menu =navigationView.getMenu();

        if (!isUserLogedIn) {                   //TODO proveriti

            userEmail = userSession.getUserEmail();

            MenuItem menuItem = menu.findItem(R.id.nav_log_in);

            menuItem.setVisible(false);


            Intent i = new Intent(this, SyncService.class);
            i.putExtra("Email", userEmail);
            i.putExtra("activityName", "MainActivity");
            startService(i);


            // Retrieve a PendingIntent that will perform a broadcast
            Intent intent = new Intent(this, SyncService.class);
            pendingIntent = PendingIntent.getService(this, 0, intent, 0);

            System.out.println("main activity ON CREATE");

        }

    }


    public void onClickReport(View view) {
        Intent intent = new Intent(MainActivity.this, ReportActivity.class);
        intent.putExtra("activityName", "MainActivity");

        startActivity(intent);
    }


    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("main activity ON STOP");
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("main activity ON START");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("main activity ON DESTROY");

    }

    @Override
    protected void onPause() {
        unregisterReceiver(sync);
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(SYNC_DATA);

        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        registerReceiver(sync, filter);

        System.out.println("main activity ON RESUME");


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("main activity ON RESTART");

    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Intent intent = NavBarUtil.setNavBarActions(MainActivity.this, menuItem);
        if (intent != null) {
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }


}
