package com.example.myapplication.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.DTO.AllTaskDto;
import com.example.myapplication.DTO.EmailDto;
import com.example.myapplication.R;
import com.example.myapplication.database.DBContentProvider;
import com.example.myapplication.database.SqlHelper;
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
import java.util.HashMap;
import java.util.List;

public class TasksInProgressActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ListView listView;
    List<String> apartmentTitle;
    List<String> apartmentAddress;
    List<String> checkApartmentDate;
    SqlHelper db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_tasks);

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
        getTasksInProcess();            //premestiti gde treba!
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

                TasksInProgressActivity.MyAdapter myAdapter = new TasksInProgressActivity.MyAdapter(this, apartmentTitle, apartmentAddress, checkApartmentDate);
                listView.setAdapter(myAdapter);
            }
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TasksInProgressActivity.this, ApartmentActivity.class);
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
        Intent intent = NavBarUtil.setNavBarActions(TasksInProgressActivity.this, item);
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

            title1.setText(title.get(position));
            description1.setText(address.get(position));
            date1.setText(date.get(position));

            return item;
        }
    }


    public void getTasksInProcess() {
        final String uri = "http://10.0.2.2:8080/api/task/inprocess";
        new TasksInProgressActivity.RESTTask().execute(uri);
    }

    class RESTTask extends AsyncTask<String, Void, ResponseEntity<AllTaskDto[]>> {

        @Override
        protected ResponseEntity<AllTaskDto[]> doInBackground(String... uri) {
            final String url = uri[0];
            RestTemplate restTemplate = new RestTemplate();
            try {
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                EmailDto emailDto = new EmailDto("user@yahoo.com");
                HttpEntity entity = new HttpEntity(emailDto, headers);   //TODO ispraviti posle odradjenog logovanja

                ResponseEntity<AllTaskDto[]> response = restTemplate.postForEntity(url, entity, AllTaskDto[].class);


                return response;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }

        }

        protected void onPostExecute(ResponseEntity<AllTaskDto[]> responseEntity) {
//            HttpStatus status = responseEntity.getStatusCode();

            AllTaskDto[] taskDtos = responseEntity.getBody();

            SqlHelper dbHelper = new SqlHelper(TasksInProgressActivity.this);
            dbHelper.dropTable();
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            for (AllTaskDto taskDto : taskDtos) {

                ContentValues entryUser = new ContentValues();  //TODO promeniti kad se odradi logovanje!
                ContentValues entryAddress = new ContentValues();
                ContentValues entryBuilding = new ContentValues();
                ContentValues entryApartment = new ContentValues();
                ContentValues entryTask = new ContentValues();


                entryUser.put(SqlHelper.COLUMN_USER_MYSQLID, taskDto.getUserDto().getId());
                entryUser.put(SqlHelper.COLUMN_USER_EMAIL, taskDto.getUserDto().getEmail());
                entryUser.put(SqlHelper.COLUMN_USER_PASSWORD, taskDto.getUserDto().getPassword());
                entryUser.put(SqlHelper.COLUMN_USER_NAME, taskDto.getUserDto().getName());
                entryUser.put(SqlHelper.COLUMN_USER_SURNAME, taskDto.getUserDto().getSurname());


                Uri userUri = TasksInProgressActivity.this.getContentResolver().insert(DBContentProvider.CONTENT_URI_USER, entryUser);


                entryAddress.put(SqlHelper.COLUMN_ADDRESS_MYSQLID, taskDto.getApartmentDto().getBuildingDto().getAddress().getId());
                entryAddress.put(SqlHelper.COLUMN_ADDRESS_COUNTRY, taskDto.getApartmentDto().getBuildingDto().getAddress().getCountry());
                entryAddress.put(SqlHelper.COLUMN_ADDRESS_CITY, taskDto.getApartmentDto().getBuildingDto().getAddress().getCity());
                entryAddress.put(SqlHelper.COLUMN_ADDRESS_STREET, taskDto.getApartmentDto().getBuildingDto().getAddress().getStreet());
                entryAddress.put(SqlHelper.COLUMN_ADDRESS_NUMBER, taskDto.getApartmentDto().getBuildingDto().getAddress().getNumber());
                entryAddress.put(SqlHelper.COLUMN_ADDRESS_LONGITUDE, taskDto.getApartmentDto().getBuildingDto().getAddress().getLongitude());
                entryAddress.put(SqlHelper.COLUMN_ADDRESS_LATITUDE, taskDto.getApartmentDto().getBuildingDto().getAddress().getLatitude());
                Uri addressUri = TasksInProgressActivity.this.getContentResolver().insert(DBContentProvider.CONTENT_URI_ADDRESS, entryAddress);

                entryBuilding.put(SqlHelper.COLUMN_BUILDING_MYSQLID, taskDto.getApartmentDto().getBuildingDto().getId());
                String addressId = addressUri.toString().split("/")[1];
                entryBuilding.put(SqlHelper.COLUMN_BUILDING_ADDRESS_ID, addressId);
                Uri buildingUri = TasksInProgressActivity.this.getContentResolver().insert(DBContentProvider.CONTENT_URI_BUILDING, entryBuilding);


                entryApartment.put(SqlHelper.COLUMN_APARTMENT_MYSQLID, taskDto.getApartmentDto().getId());
                entryApartment.put(SqlHelper.COLUMN_APARTMENT_NUMBER, taskDto.getApartmentDto().getNumber());
                String buildingId = buildingUri.toString().split("/")[1];
                entryApartment.put(SqlHelper.COLUMN_APARTMENT_BUILDING_ID, buildingId);
                Uri apartmentUri = TasksInProgressActivity.this.getContentResolver().insert(DBContentProvider.CONTENT_URI_APARTMENT, entryApartment);

                entryTask.put(SqlHelper.COLUMN_TASK_MYSQLID, taskDto.getId());
                entryTask.put(SqlHelper.COLUMN_TASK_STATE, taskDto.getState());
                entryTask.put(SqlHelper.COLUMN_TASK_DEADLINE, taskDto.getDeadline().toString());
                entryTask.put(SqlHelper.COLUMN_TASK_TYPE_OF_APARTMENT, taskDto.getTypeOfApartment());
                entryTask.put(SqlHelper.COLUMN_TASK_URGENT, taskDto.isUrgent());
                String apartmentId = apartmentUri.toString().split("/")[1];
                String userId = userUri.toString().split("/")[1];

                entryTask.put(SqlHelper.COLUMN_TASK_APARTMENT_ID, apartmentId);
                entryTask.put(SqlHelper.COLUMN_TASK_USER_ID, userId);

                Uri taskUri = TasksInProgressActivity.this.getContentResolver().insert(DBContentProvider.CONTENT_URI_TASK, entryTask);


            }

            db.close();

        }
    }
}
