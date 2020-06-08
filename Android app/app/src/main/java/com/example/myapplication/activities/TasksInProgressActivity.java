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
import com.example.myapplication.database.NewEntry;
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
        setContentView(R.layout.tasks_in_process);

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

        listView = (ListView) findViewById(R.id.listViewTasksInProcess);
        db = new SqlHelper(this);
        Cursor data = db.getTasksInProcess();
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

            for (AllTaskDto taskDto : taskDtos) {

                String userUri = NewEntry.newUserEntry(TasksInProgressActivity.this, taskDto.getUserDto()); //TODO promeniti kad se odradi logovanje!
                String userId = userUri.split("/")[1];
                String addressUri = NewEntry.newAddressEntry(TasksInProgressActivity.this, taskDto.getApartmentDto().getBuildingDto());
                String buildingUri = NewEntry.newBuildingEntry(TasksInProgressActivity.this, taskDto.getApartmentDto().getBuildingDto(), addressUri);

                String apartmentUri = NewEntry.newApartmentEntry(TasksInProgressActivity.this, taskDto.getApartmentDto(), buildingUri);
                String taskUri = NewEntry.newTaskEntry(TasksInProgressActivity.this, taskDto, apartmentUri, userId, null);


            }


        }
    }
}
