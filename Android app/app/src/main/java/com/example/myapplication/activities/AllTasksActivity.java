package com.example.myapplication.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.net.Uri;
import android.os.AsyncTask;

import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.example.myapplication.database.DBContentProvider;
import com.example.myapplication.database.SqlHelper;
import com.example.myapplication.util.NavBarUtil;
import com.google.android.material.navigation.NavigationView;


import java.util.ArrayList;
import java.util.List;

public class AllTasksActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ListView listView;
    List<String> apartmentTitle;
    List<String> apartmentAddress;
    List<String> checkApartmentDate;
    SqlHelper db;
    List<String> taskIds;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_tasks);

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
        navigationView.setCheckedItem(R.id.nav_all_tasks);
        listView();


    }

    public void listView() {

        listView = (ListView) findViewById(R.id.listViewAllTasks);
        db = new SqlHelper(this);
        Cursor data = db.getAllTasks();
        String apartmentId = "";
        String apartmentNumber = "";
        String buildingId = "";
        if (data.getCount() == 0) {
            System.out.println("prazna lista");
        } else {
            while (data.moveToNext()) {
                taskIds.add(data.getString(0));
                checkApartmentDate.add(data.getString(5).substring(0, 13));
                apartmentId = data.getString(6);
                Cursor apartmentData = db.getApartmentById(apartmentId);

                while (apartmentData.moveToNext()) {
                    apartmentTitle.add("Apartment number: " + apartmentData.getString(2));

                    buildingId = apartmentData.getString(3);
                    Cursor buildungData = db.getBuildingById(buildingId);
                    while (buildungData.moveToNext()) {
                        String addressId = buildungData.getString(2);
                        Cursor addressData = db.getAddressById(addressId);
                        while (addressData.moveToNext()) {
                            apartmentAddress.add(addressData.getString(3) + ", " + addressData.getString(4) + " " + addressData.getString(5));
                        }
                    }

                }

                MyAdapter myAdapter = new MyAdapter(this, apartmentTitle, apartmentAddress, checkApartmentDate);
                listView.setAdapter(myAdapter);
            }
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String taskId = taskIds.get(position);

                Intent intent = new Intent(AllTasksActivity.this, ApartmentActivity.class);
                intent.putExtra("taskId", taskId);
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
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = NavBarUtil.setNavBarActions(AllTasksActivity.this, item);
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
        public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View item = layoutInflater.inflate(R.layout.apartment_item, parent, false);

            final TextView title1 = item.findViewById(R.id.apartmentTitleTextView);
            TextView description1 = item.findViewById(R.id.address);
            TextView date1 = item.findViewById(R.id.apartmentDate);
            title1.setText(title.get(position));
            description1.setText(address.get(position));
            date1.setText(date.get(position));

            Button assignButton = item.findViewById(R.id.buttonAssing);
            assignButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    ContentValues entryTask = new ContentValues();

                    String taskId = taskIds.get(position);
                    Cursor taskData = db.getTaskById(taskId);
                    while (taskData.moveToNext()) {
                        String mysqlId = taskData.getString(1);
                        String typeOfApartment = taskData.getString(2);
                        String state = "IN_PROCESS";
                        String urgent = taskData.getString(4);
                        String deadline = taskData.getString(5);
                        String apartmentId = taskData.getString(6);
                        String userId = "1";                       //TODO

                        entryTask.put(SqlHelper.COLUMN_TASK_MYSQLID, mysqlId);
                        entryTask.put(SqlHelper.COLUMN_TASK_STATE, state);
                        entryTask.put(SqlHelper.COLUMN_TASK_DEADLINE, deadline);
                        entryTask.put(SqlHelper.COLUMN_TASK_TYPE_OF_APARTMENT, typeOfApartment);
                        entryTask.put(SqlHelper.COLUMN_TASK_URGENT, urgent);
                        entryTask.put(SqlHelper.COLUMN_TASK_APARTMENT_ID, apartmentId);

                        entryTask.put(SqlHelper.COLUMN_TASK_USER_ID, userId);

                        AllTasksActivity.this.getContentResolver().update(DBContentProvider.CONTENT_URI_TASK, entryTask, "id=" + taskId, null);
                    }

                    apartmentAddress.remove(position);
                    apartmentTitle.remove(position);
                    checkApartmentDate.remove(position);
                    notifyDataSetChanged();


                }
            });

            return item;
        }
    }


}
