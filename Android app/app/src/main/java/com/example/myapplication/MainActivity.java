package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.myapplication.activities.LoginActivity;
import com.example.myapplication.activities.TestActivity;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

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
        System.out.println("main activity ON CREATE");


    }

    public void onClickButton(View view){

        Intent intent = new Intent(MainActivity.this, TestActivity.class);
        //da li ove dve linije znace isto?
//        intent.setClassName("activities", "TestActivity");
        intent.putExtra("broj", 12);
        startActivity(intent);


        System.out.println("main activity ON CLICK BUTTON");

    }

    public void onClickLogin(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);

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

        switch (menuItem.getItemId()) {
            case R.id.nav_log_in:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}