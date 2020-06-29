package com.example.myapplication.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.R;
import com.example.myapplication.database.SqlHelper;
import com.example.myapplication.util.NavBarUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

import java.util.Date;

public class ApartmentActivity extends  FragmentActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    String taskId;

    Toolbar toolbar;

    String activityName;
    GoogleMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.apartment);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        taskId = intent.getStringExtra("taskId");
        activityName = intent.getStringExtra("activityName");

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Quickinspect");


        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.all_tasks);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_log_in);

        menuItem.setVisible(false);

        Button button = findViewById(R.id.assignMeButton);

        if (activityName.equals("FinishedTasksActivity")) {
            button.setText(R.string.view_report);
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng ns = new LatLng(45.259333, 19.831826);
        map.addMarker(new MarkerOptions().position(ns).title("Novi Sad"));
        map.moveCamera(CameraUpdateFactory.newLatLng(ns));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTask(taskId);

    }

    public void getTask(String taskId) {

        SqlHelper db = new SqlHelper(this);
        Cursor data = db.getTaskById(taskId);


        String apartmentId = "";
        String apartmentNumber = "";
        String apartmentAddress = "";
        String buildingId = "";
        String addressId = "";
        String stateName = "";
        TextView state = findViewById(R.id.stateTaskId);
        TextView address = findViewById(R.id.addressTaskId);
        TextView description = findViewById(R.id.apartmentNameTaskId);

        if (data.getCount() == 0) {
            System.out.println("prazna lista apartmentActivity");
        } else {
            while (data.moveToNext()) {


                stateName = data.getString(3);
                apartmentId = data.getString(6);

                Cursor dataApartment = db.getApartmentById(apartmentId);

                while (dataApartment.moveToNext()) {

                    apartmentNumber = dataApartment.getString(2);
                    buildingId = dataApartment.getString(3);

                    Cursor buildingData = db.getBuildingById(buildingId);

                    while (buildingData.moveToNext()) {

                        addressId = buildingData.getString(2);
                        Cursor addressData = db.getAddressById(addressId);

                        while (addressData.moveToNext()) {

                            apartmentAddress = addressData.getString(4) + " " + addressData.getString(5)
                                    + ", " + addressData.getString(3);

                        }

                    }


                }
                if (stateName.equals("IN_PROCESS")) {
                    state.setText("In process");
                } else if (stateName.equals("FINISHED")) {
                    state.setText("Finished");
                } else if (stateName.equals("NEW")) {
                    state.setText("New");
                }

                address.setText(apartmentAddress);
                description.setText("Apartment number: " + apartmentNumber);


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
        intent.putExtra("taskId", taskId);
        intent.putExtra("activityName", activityName);


        startActivity(intent);
    }


}
