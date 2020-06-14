package com.example.myapplication.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

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

import com.example.myapplication.DTO.ChangeTaskStateDto;

import com.example.myapplication.R;
import com.example.myapplication.database.DBContentProvider;
import com.example.myapplication.database.SqlHelper;
import com.example.myapplication.util.AppConfig;
import com.example.myapplication.util.NavBarUtil;
import com.google.android.material.navigation.NavigationView;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_tasks);

        taskIds = new ArrayList<>();
        apartmentTitle = new ArrayList<>();
        apartmentAddress = new ArrayList<>();
        checkApartmentDate = new ArrayList<>();
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
    protected void onResume() {

        super.onResume();
        listView();

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


            if(taskWaitingList.get(position) == 1) {

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

                    taskId = taskIds.get(position);
                    waiting.setVisibility(View.VISIBLE);
                    Cursor taskData = db.getTaskById(taskId);
                    while (taskData.moveToNext()) {
                        String mysqlId = taskData.getString(1);
                        taskMysqlId = mysqlId;
                    }


//                    listView.getChildAt(position).setEnabled(false);
                    isEnabled(position);

                    title1.setTextColor(getResources().getColor(R.color.silver));
                    description1.setTextColor(getResources().getColor(R.color.silver));
                    date1.setTextColor(getResources().getColor(R.color.silver));
                    assignButton.setTextColor(getResources().getColor(R.color.silver));
                    assignButton.setEnabled(false);


//                    apartmentAddress.remove(position);
//                    apartmentTitle.remove(position);
//                    checkApartmentDate.remove(position);
//                    notifyDataSetChanged();
                    changeTaskState();


                }
            });






            return item;
        }
    }




    public void changeTaskState() {
        final String uri = AppConfig.apiURI + "task/change/state";
        new AllTasksActivity.RESTChangeStateTask().execute(uri);
    }

    class RESTChangeStateTask extends AsyncTask<String, Void, ResponseEntity<Boolean>> {

        @Override
        protected ResponseEntity<Boolean> doInBackground(String... uri) {
            final String url = uri[0];
            RestTemplate restTemplate = new RestTemplate();
            try {
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

                ChangeTaskStateDto changeTaskStateDto = new ChangeTaskStateDto();
                changeTaskStateDto.setEmail("user@yahoo.com");
                changeTaskStateDto.setState("IN_PROCESS");
                changeTaskStateDto.setTaskId(taskMysqlId);

                HttpEntity entity = new HttpEntity(changeTaskStateDto, headers);   //TODO ispraviti posle odradjenog logovanja

                ResponseEntity<Boolean> response = restTemplate.postForEntity(url, entity, Boolean.class);


                return response;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }

        }

        protected void onPostExecute(ResponseEntity<Boolean> responseEntity) {

            Boolean success = responseEntity.getBody();
            SqlHelper dbHelper = new SqlHelper(AllTasksActivity.this);


            ContentValues entryTask = new ContentValues();

            Cursor taskData = db.getTaskById(taskId);
            while (taskData.moveToNext()) {
                String mysqlId = taskData.getString(1);
                String typeOfApartment = taskData.getString(2);
                String state = "IN_PROCESS";
                String urgent = taskData.getString(4);
                String deadline = taskData.getString(5);
                String apartmentId = taskData.getString(6);
                String userId = "1";                       //TODO

//                        entryTask.put(SqlHelper.COLUMN_TASK_MYSQLID, mysqlId);
                entryTask.put(SqlHelper.COLUMN_TASK_STATE, state);
//                        entryTask.put(SqlHelper.COLUMN_TASK_DEADLINE, deadline);
//                        entryTask.put(SqlHelper.COLUMN_TASK_TYPE_OF_APARTMENT, typeOfApartment);
//                        entryTask.put(SqlHelper.COLUMN_TASK_URGENT, urgent);
//                        entryTask.put(SqlHelper.COLUMN_TASK_APARTMENT_ID, apartmentId);
                entryTask.put(SqlHelper.COLUMN_TASK_USER_ID, userId);
                entryTask.put(SqlHelper.COLUMN_TASK_REQESTED, 1);



                AllTasksActivity.this.getContentResolver().update(DBContentProvider.CONTENT_URI_TASK, entryTask, "id=" + taskId, null);
            }

        }
    }


}
