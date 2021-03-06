package com.example.myapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.R;
import com.example.myapplication.database.SqlHelper;
import com.example.myapplication.sync.receiver.SyncReceiver;
import com.example.myapplication.sync.service.SyncService;
import com.example.myapplication.util.NavBarUtil;
import com.example.myapplication.util.UserSession;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class FinishedTasksActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ListView listView;
    List<String> apartmentTitle;
    List<String> apartmentAddress;
    List<String> checkApartmentDate;
    List<String> taskIds;
    private SyncReceiver sync;
    public static String SYNC_DATA = "SYNC_DATA";
    UserSession userSession;
    MyAdapter myAdapter;
    SqlHelper db;
    SQLiteDatabase sqlDB;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.finished_tasks);

        taskIds = new ArrayList<>();
        apartmentTitle = new ArrayList<>();
        apartmentAddress = new ArrayList<>();
        checkApartmentDate = new ArrayList<>();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.all_tasks, R.string.all_tasks);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_finished_tasks);
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_log_in);

        menuItem.setVisible(false);

        sync = new SyncReceiver();

        userSession = new UserSession(getApplicationContext());

        Intent i = new Intent(this, SyncService.class);
        i.putExtra("Email", "");
        i.putExtra("activityName", "FinishedTasksActivity");
        i.putExtra("finishTask", "true");

        startService(i);


    }

    public void listView() {

        Cursor userData = db.getUserByEmail(userSession.getUserEmail());
        String userId = "";
        if(userData.moveToFirst()){
            userId = userData.getString(0);
        }

        listView = (ListView) findViewById(R.id.listViewFinishedTasks);
        Cursor data = db.getFinishedTasks(sqlDB, userId);
        String apartmentId = "";
        String apartmentNumber = "";
        String buildingId = "";
        if (data.getCount() == 0) {
            System.out.println("prazna lista");
        } else {
            while (data.moveToNext()) {

                taskIds.add(data.getString(0));

                checkApartmentDate.add(data.getString(5).substring(0, 16));
                apartmentId = data.getString(6);
                Cursor apartmentData = db.getApartmentById(apartmentId, sqlDB);
                while (apartmentData.moveToNext()) {
                    apartmentTitle.add("Apartment number: " + apartmentData.getString(2));

                    buildingId = apartmentData.getString(3);
                    Cursor buildungData = db.getBuildingById(buildingId, sqlDB);
                    while (buildungData.moveToNext()) {
                        String addressId = buildungData.getString(2);
                        Cursor addressData = db.getAddressById(addressId, sqlDB);
                        while (addressData.moveToNext()) {
                            apartmentAddress.add(addressData.getString(4) + " " + addressData.getString(5)
                                    + ", " + addressData.getString(3));
                        }
                    }

                }

                myAdapter = new FinishedTasksActivity.MyAdapter(this, apartmentTitle, apartmentAddress, checkApartmentDate);
                listView.setAdapter(myAdapter);
            }
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String taskId = taskIds.get(position);


                Intent intent = new Intent(FinishedTasksActivity.this, ApartmentActivity.class);
                intent.putExtra("taskId", taskId);
                intent.putExtra("activityName", "FinishedTasksActivity");

                startActivity(intent);
            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        if (myAdapter != null) {
            myAdapter.clear();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = new SqlHelper(this);

        sqlDB = db.getWritableDatabase();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SYNC_DATA);

        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(sync, filter);

        listView();
        sqlDB.close();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(sync);
        super.onPause();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = NavBarUtil.setNavBarActions(FinishedTasksActivity.this, item);
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

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        List<String> title;
        List<String> address;
        List<String> date;

        MyAdapter(Context c, List<String> title, List<String> address, List<String> date) {
            super(c, android.R.layout.simple_list_item_1, R.id.apartmentTitleTextView, title);
            this.context = c;
            this.title = title;
            this.address = address;
            this.date = date;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View item = layoutInflater.inflate(R.layout.apartment_item, parent, false);

            TextView title1 = item.findViewById(R.id.apartmentTitleTextView);
            TextView description1 = item.findViewById(R.id.address);
            TextView date1 = item.findViewById(R.id.apartmentDate);
            Button assignButton = item.findViewById(R.id.buttonAssing);

            assignButton.setVisibility(View.GONE);
            TextView waiting = item.findViewById(R.id.textViewWaiting);
            waiting.setVisibility(View.GONE);

            title1.setText(title.get(position));
            description1.setText(address.get(position));
            date1.setText(date.get(position));

            return item;
        }
    }

}
