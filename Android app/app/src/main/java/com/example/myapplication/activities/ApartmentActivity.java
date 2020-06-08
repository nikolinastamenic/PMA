package com.example.myapplication.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.R;
import com.example.myapplication.database.SqlHelper;
import com.example.myapplication.util.NavBarUtil;
import com.google.android.material.navigation.NavigationView;

import java.util.Date;

public class ApartmentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    SqlHelper db;

    private String typeOfApartment;

    private String state;

    private boolean urgent;

    private Date deadline;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apartment);

        Intent intent = getIntent();
        String taskId = intent.getStringExtra("taskId");

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.all_tasks);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);


        getTask(taskId);

    }

    public void getTask(String taskId){

        db = new SqlHelper(this);
        Cursor data = db.getTaskById(taskId);
        String apartmentId = "";
        String apartmentNumber = "";
        String apartmentAddress = "";

        if (data.getCount() == 0) {
            System.out.println("prazna lista apartmentActivity");
        } else {
            while (data.moveToNext()) {

                System.out.println(data);
                System.out.println(data.getString(1));
                System.out.println(data.getString(2));
                System.out.println(data.getString(3));
                System.out.println(data.getString(4));
                System.out.println(data.getString(5));
                System.out.println(data.getString(6));
//                checkApartmentDate.add(data.getString(5).substring(0, 13));
//                apartmentId = data.getString(6);
//                Cursor apartmentData = db.getApartmentById(apartmentId);
//
//                taskIds.add(data.getString(0));
//
//                while (apartmentData.moveToNext()) {
//                    apartmentTitle.add("Apartment number: " + apartmentData.getString(2));
//
//                    buildingId = apartmentData.getString(3);
//                    Cursor buildungData = db.getBuildingById(buildingId);
//                    while (buildungData.moveToNext()) {
//                        String addressId = buildungData.getString(2);
//                        Cursor addressData = db.getAddressById(addressId);
//                        while (addressData.moveToNext()) {
//                            apartmentAddress.add(addressData.getString(3) + ", " + addressData.getString(4) + " " + addressData.getString(5));
//                        }
//                    }
//
//                }
//
//                MyAdapter myAdapter = new MyAdapter(this, apartmentTitle, apartmentAddress, checkApartmentDate);
//                listView.setAdapter(myAdapter);
            }

        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = NavBarUtil.setNavBarActions(ApartmentActivity.this, item);
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

    public void onClickReport(View view) {
        Intent intent = new Intent(ApartmentActivity.this, ReportActivity.class);

        startActivity(intent);
    }




}
