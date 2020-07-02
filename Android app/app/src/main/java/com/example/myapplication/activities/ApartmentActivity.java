package com.example.myapplication.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.R;
import com.example.myapplication.database.SqlHelper;
import com.example.myapplication.sync.service.SyncService;
import com.example.myapplication.util.AppConfig;
import com.example.myapplication.util.NavBarUtil;
import com.example.myapplication.util.UserSession;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

import java.util.Date;

import static com.example.myapplication.util.AppConfig.LOCATION_PERMISSION_REQUEST_CODE;

public class ApartmentActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    String taskId;

    Toolbar toolbar;
    private UserSession userSession;
    String activityName;
    GoogleMap map;
    private boolean permissionDenied = false;
    double taskAddressLongitude = 0.0;
    double taskAddressLatitude = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.apartment);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        userSession = new UserSession(getApplicationContext());

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
        if (activityName.equals("AllTasksActivity")) {
            button.setText("");
            button.setEnabled(false);
        }


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

                            taskAddressLatitude = addressData.getDouble(7);
                            taskAddressLongitude = addressData.getDouble(6);

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


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public boolean onMyLocationButtonClick() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        assert locationManager != null;
        if (!locationManager.isLocationEnabled()){
            Toast.makeText(this, getString(R.string.turn_on_location), Toast.LENGTH_LONG).show();

        }



        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
//        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng ns = new LatLng(taskAddressLatitude, taskAddressLongitude);
        map.addMarker(new MarkerOptions().position(ns).title("Novi Sad"));
        map.moveCamera(CameraUpdateFactory.newLatLng(ns));

        map.setOnMyLocationButtonClickListener(this);
        map.setOnMyLocationClickListener(this);
        enableMyLocation();
    }


    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                map.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE);


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
        }
    }

}
