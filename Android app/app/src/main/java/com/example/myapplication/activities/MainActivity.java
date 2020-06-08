package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.myapplication.DTO.AllTaskDto;
import com.example.myapplication.DTO.EmailDto;
import com.example.myapplication.DTO.ReportItemDto;
import com.example.myapplication.R;
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

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.all_tasks);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        System.out.println("main activity ON CREATE");
//        getFinishedTasks();


    }


    public void onClickButton(View view) {

        Intent intent = new Intent(MainActivity.this, TestActivity.class);
        //da li ove dve linije znace isto?
//        intent.setClassName("activities", "TestActivity");
        intent.putExtra("broj", 12);
        startActivity(intent);

        System.out.println("main activity ON CLICK BUTTON");

    }

    public void onClickLogin(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);

        startActivity(intent);
    }

    public void onClickApartmentView(View view) {
        Intent intent = new Intent(MainActivity.this, ApartmentActivity.class);

        startActivity(intent);
    }

    public void onClickReport(View view) {
        Intent intent = new Intent(MainActivity.this, ReportActivity.class);

        startActivity(intent);
    }

    public void onClickSettings(View view) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);

        startActivity(intent);
    }

    public void onClickAllTasks(View view) {
        Intent intent = new Intent(MainActivity.this, AllTasksActivity.class);

        startActivity(intent);
    }


    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("main activity ON STOP");
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Util.initDB(MainActivity.this);

        System.out.println("main activity ON START");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("main activity ON DESTROY");

    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("main activity ON PAUSE");

    }


    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("main activity ON RESUME");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("main activity ON RESTART");

    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Intent intent = NavBarUtil.setNavBarActions(MainActivity.this, menuItem);
        if (intent != null) {
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    public void getFinishedTasks(View view) {
        final String uri = "http://10.0.2.2:8080/api/task/all";
        new MainActivity.RESTTask().execute(uri);
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

            AllTaskDto[] taskDtos = responseEntity.getBody();

            SqlHelper dbHelper = new SqlHelper(MainActivity.this);
            dbHelper.dropTable();

            for (AllTaskDto taskDto : taskDtos) {
                String userId = null;
                if (taskDto.getUserDto() != null) {
                    String userUri = NewEntry.newUserEntry(MainActivity.this, taskDto.getUserDto()); //TODO promeniti kad se odradi logovanje!
                    userId = userUri.split("/")[1];
                }
                String addressUri = NewEntry.newAddressEntry(MainActivity.this, taskDto.getApartmentDto().getBuildingDto());
                String buildingUri = NewEntry.newBuildingEntry(MainActivity.this, taskDto.getApartmentDto().getBuildingDto(), addressUri);

                String apartmentUri = NewEntry.newApartmentEntry(MainActivity.this, taskDto.getApartmentDto(), buildingUri);
                String reportId = null;
                if (taskDto.getReportDto() != null) {
                    String reportUri = NewEntry.newReportEntry(MainActivity.this, taskDto.getReportDto());
                    reportId = reportUri.split("/")[1];

                    if (!taskDto.getReportDto().getItemList().isEmpty()) {

                        for (ReportItemDto reportItemDto : taskDto.getReportDto().getItemList()) {

                            String reportItemUri = NewEntry.newReportItemEntry(MainActivity.this, reportItemDto);

                            String reportItemId = reportItemUri.split("/")[1];
                            String reportReporetItemUri = NewEntry.newReportReportItemEntry(MainActivity.this, reportItemDto, taskDto.getReportDto(), reportId, reportItemId);

                        }
                    }
                }

                String taskUri = NewEntry.newTaskEntry(MainActivity.this, taskDto, apartmentUri, userId, reportId);



            }

        }
    }

}
