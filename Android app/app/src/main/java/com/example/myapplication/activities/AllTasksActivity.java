package com.example.myapplication.activities;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;

import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.example.myapplication.R;
import com.example.myapplication.database.DBContentProvider;
import com.example.myapplication.database.SqlHelper;
import com.example.myapplication.sync.receiver.SyncReceiver;
import com.example.myapplication.sync.service.SyncService;
import com.example.myapplication.util.NavBarUtil;
import com.example.myapplication.util.NetworkStateTools;
import com.example.myapplication.util.UserSession;
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
    List<Integer> taskWaitingList;
    SqlHelper db;
    List<String> taskIds;
    String taskMysqlId;
    String taskId;
    private SyncReceiver sync;
    public static String SYNC_DATA = "SYNC_DATA";
    private PendingIntent pendingIntent;
    private Intent intentService;
    UserSession userSession;
    public MyAdapter myAdapter;
    private MyReceiver myReceiver;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_tasks);

        System.out.println(" on create usao..................");


        taskIds = new ArrayList<>();
        taskWaitingList = new ArrayList<>();
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

        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_log_in);

        menuItem.setVisible(false);

        sync = new SyncReceiver();
        userSession = new UserSession(getApplicationContext());


    }

    public void listView() {

        apartmentTitle = new ArrayList<>();
        apartmentAddress = new ArrayList<>();
        checkApartmentDate = new ArrayList<>();

        listView = (ListView) findViewById(R.id.listViewAllTasks);
        db = new SqlHelper(this);
//        Cursor data = AllTasksActivity.this.getContentResolver().query(DBContentProvider.CONTENT_URI_TASK, SqlHelper.ALLCOLUMNSTASK, null, null);
        Cursor data = db.getAllTasks();
        String apartmentId = "";
        String buildingId = "";
        if (data.getCount() == 0) {
            System.out.println("prazna lista");
        } else {
            while (data.moveToNext()) {
                taskIds.add(data.getString(0));
                checkApartmentDate.add(data.getString(5).substring(0, 16));
                taskWaitingList.add(data.getInt(9));
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
                        addressData.close();
                    }
                    buildungData.close();

                }
                apartmentData.close();

                myAdapter = new MyAdapter(this, apartmentTitle, apartmentAddress, checkApartmentDate);
                listView.setAdapter(myAdapter);
            }
            data.close();
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
    protected void onResume() {


        super.onResume();


        IntentFilter filter = new IntentFilter();
        filter.addAction(SYNC_DATA);

        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(sync, filter);

        listView();

        Intent intent = new Intent(AllTasksActivity.this, SyncService.class);
        intent.putExtra("activityName", "AllTasksActivity");
        intent.putExtra("Email", userSession.getUserEmail());

//        startService(intent);

    }

    @Override
    protected void onPause() {
        unregisterReceiver(sync);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        System.out.println(" on destroy usao..................");

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


        @Override
        public boolean isEnabled(int position) {
            return false;
        }

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
            final TextView description1 = item.findViewById(R.id.address);
            final TextView date1 = item.findViewById(R.id.apartmentDate);
            final TextView waiting = item.findViewById(R.id.textViewWaiting);

            title1.setText(title.get(position));
            description1.setText(address.get(position));
            date1.setText(date.get(position));
            final Button assignButton = item.findViewById(R.id.buttonAssing);


            if (taskWaitingList.get(position) == 0) {

                isEnabled(position);

                title1.setTextColor(getResources().getColor(R.color.silver));
                description1.setTextColor(getResources().getColor(R.color.silver));
                date1.setTextColor(getResources().getColor(R.color.silver));
                assignButton.setTextColor(getResources().getColor(R.color.silver));
                assignButton.setEnabled(false);
                waiting.setVisibility(View.VISIBLE);
            } else {
                waiting.setVisibility(View.GONE);

            }

            assignButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    IntentFilter intentFilter = new IntentFilter();
                    myReceiver = new AllTasksActivity.MyReceiver(new Handler());
                    intentFilter.addAction("com.example.myapplication.ACTION");
                    registerReceiver(myReceiver, intentFilter);

                    int status = NetworkStateTools.getConnectivityStatus(context);

                    if (status == 0) {
                        Intent intent = new Intent("com.example.myapplication.ACTION");
                        intent.putExtra("connectivity", "false");
                        sendBroadcast(intent);
                    } else {


                        ContentValues entryTask = new ContentValues();

                        String userId = "";
                        Cursor userData = db.getUserByEmail(userSession.getUserEmail());
                        while (userData.moveToNext()) {
                            userId = Integer.toString(userData.getInt(0));
                        }
                        userData.close();
                        taskId = taskIds.get(position);


                        waiting.setVisibility(View.VISIBLE);
                        Cursor taskData = db.getTaskById(taskId);
                        while (taskData.moveToNext()) {
                            String mysqlId = taskData.getString(1);
                            taskMysqlId = mysqlId;


                            entryTask.put(SqlHelper.COLUMN_TASK_USER_ID, userId);
                            entryTask.put(SqlHelper.COLUMN_TASK_IS_SYNCHRONIZED, 0);

                            context.getContentResolver().update(DBContentProvider.CONTENT_URI_TASK, entryTask, "id=" + taskId, null);
                        }
                        taskData.close();

                        Intent i = new Intent(AllTasksActivity.this, SyncService.class);
                        i.putExtra("activityName", "AllTasksActivity");
                        i.putExtra("Email", userSession.getUserEmail());
//                        i.putExtra("position", position);
                        i.putExtra("MySqlId", taskMysqlId);
                        startService(i);

                        // Retrieve a PendingIntent that will perform a broadcast
//                    intentService = new Intent(AllTasksActivity.this, SyncService.class);
//                    intentService.putExtra("activityName", "AllTasksActivity");
//                    intentService.putExtra("mySqlTaskId", taskMysqlId);
//
//                    pendingIntent = PendingIntent.getService(AllTasksActivity.this, 0, intentService, 0);

//                    listView.getChildAt(position).setEnabled(false);

                        isEnabled(position);

                        title1.setTextColor(getResources().getColor(R.color.silver));
                        description1.setTextColor(getResources().getColor(R.color.silver));
                        date1.setTextColor(getResources().getColor(R.color.silver));
                        assignButton.setTextColor(getResources().getColor(R.color.silver));
                        assignButton.setEnabled(false);

                        taskIds.remove(position);
                        apartmentTitle.remove(position);
                        apartmentAddress.remove(position);
                        checkApartmentDate.remove(position);

                    }

                }
            });


            return item;
        }
    }


    public void updateUI(Intent intent) {

//        Intent intent1 = new Intent(AllTasksActivity.this, AllTasksActivity.class);
//        startActivity(intent1);

        myAdapter.notifyDataSetChanged();

    }

    public class MyReceiver extends BroadcastReceiver {

        private final Handler handler; // Handler used to execute code on the UI thread

        public MyReceiver(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void onReceive(final Context context, final Intent intent) {
            // Post the UI updating code to our Handler
            handler.post(new Runnable() {
                @Override
                public void run() {

                    if (intent.getStringExtra("connectivity") != null && intent.getStringExtra("connectivity").equals("false")) {
                        Toast.makeText(context, "Nema konekcije sa internetom", Toast.LENGTH_SHORT).show();
                        if (null != myReceiver) {
                            unregisterReceiver(myReceiver);
                            myReceiver = null;
                        }

                    } else {

                        if (intent.getStringExtra("success").equals("false")) {

                            Toast.makeText(context, "Task koji ste zatrazili je zauzet!", Toast.LENGTH_SHORT).show();
                            if (null != myReceiver) {
                                unregisterReceiver(myReceiver);
                                myReceiver = null;
                            }
                            AllTasksActivity.this.updateUI(intent);
                        }

                        AllTasksActivity.this.updateUI(intent);

                    }
                }
            });


        }
    }

}
