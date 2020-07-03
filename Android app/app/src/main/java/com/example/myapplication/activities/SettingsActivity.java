package com.example.myapplication.activities;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;

import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.R;

import com.example.myapplication.util.AppConfig;
import com.example.myapplication.util.NavBarUtil;
import com.example.myapplication.util.SavePictureUtil;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;


public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    RadioButton serbianRadio;
    RadioButton englishRadio;
    Spinner spinnerctrl;
    int selectedLang;
    SharedPreferences languagepref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.settings, R.string.settings);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_settings);

        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_log_in);

        menuItem.setVisible(false);

        serbianRadio = (RadioButton) findViewById(R.id.radioBtn1);
        englishRadio = (RadioButton) findViewById(R.id.radioBtn2);


    }


    @Override
    protected void onResume() {
        super.onResume();

        String currentLang = Locale.getDefault().getLanguage();
        System.out.println(currentLang);
        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        if (conf.locale.getLanguage().equals("sr")) {
            serbianRadio.setChecked(true);
        } else {
            englishRadio.setChecked(true);
        }

        serbianRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("sr");
            }
        });
        englishRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("en-rGB");
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent intent = NavBarUtil.setNavBarActions(SettingsActivity.this, item);
        if (intent != null) {
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);

        Locale.setDefault(myLocale);
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        finish();
        startActivity(refresh);
    }

}
