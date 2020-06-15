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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.myapplication.DTO.AllTaskDto;
import com.example.myapplication.DTO.EmailDto;
import com.example.myapplication.DTO.ReportItemDto;
import com.example.myapplication.R;
import com.example.myapplication.database.NewEntry;
import com.example.myapplication.database.SqlHelper;
import com.example.myapplication.sync.SyncReceiver;
import com.example.myapplication.sync.SyncService;
import com.example.myapplication.util.AppConfig;
import com.example.myapplication.util.NavBarUtil;
import com.google.android.material.navigation.NavigationView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private PendingIntent pendingIntent;

    private SyncReceiver sync;
    public static String SYNC_DATA = "SYNC_DATA";


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
        startService(new Intent(this, SyncService.class));


        // Retrieve a PendingIntent that will perform a broadcast
        Intent intent = new Intent(this, SyncService.class);
        pendingIntent = PendingIntent.getService(this, 0, intent, 0);

        System.out.println("main activity ON CREATE");

    }


    public void onClickReport(View view) {
        Intent intent = new Intent(MainActivity.this, ReportActivity.class);
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

        super.onPause();
        System.out.println("main activity ON PAUSE");

    }


    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(SYNC_DATA);

        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
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
