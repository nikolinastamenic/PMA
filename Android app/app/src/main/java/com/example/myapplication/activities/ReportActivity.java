package com.example.myapplication.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.DTO.ReportDto;
import com.example.myapplication.DTO.ReportItemDto;
import com.example.myapplication.DTO.UserDto;
import com.example.myapplication.R;
import com.example.myapplication.database.DBContentProvider;
import com.example.myapplication.database.NewEntry;
import com.example.myapplication.database.SqlHelper;
import com.example.myapplication.util.AppConfig;
import com.example.myapplication.util.NavBarUtil;
import com.example.myapplication.util.SavePictureUtil;
import com.google.android.material.navigation.NavigationView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    ListView listView;
    List<String> itemTitle;
    List<String> itemDescription;
    String reportDate = "";
    String reportIdMySQL = "1";

    List<Integer> images;
    List<ImageView> imageViews;


    String taskId;
    SqlHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);

        itemTitle = new ArrayList<>();
        itemDescription = new ArrayList<>();
        images = new ArrayList<>();

        Intent intent = getIntent();
        taskId = intent.getStringExtra("taskId");
        listView = findViewById(R.id.listViewReport);
        imageViews = new ArrayList<>();


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.all_tasks);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        Menu menu =navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_log_in);

        menuItem.setVisible(false);

        listView();


//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
    }

    private void listView() {

        TextView reportDateTextView = findViewById(R.id.textViewReportDate);
        listView = (ListView) findViewById(R.id.listViewReport);
        db = new SqlHelper(this);
        Cursor data = db.getTaskById(taskId);
        String reportId = "";

        while (data.moveToNext()) {
            reportId = data.getString(7);
            System.out.println(reportId + " report idddddddddddddddddddddddd");
            if (!reportId.equals("")) {
                Cursor reportData = db.getReportById(reportId);
                while (reportData.moveToNext()) {
                    reportIdMySQL = reportData.getString(1);
                    reportDate = reportData.getString(2);
                }
                Cursor reportItemData = db.getReportItemsByReportId(reportId);
                while (reportItemData.moveToNext()) {
                    Cursor reportItems = db.getReportItemById(reportItemData.getString(2));
                    String imageName = "";
                    while (reportItems.moveToNext()) {

                        itemTitle.add(reportItems.getString(2));
                        itemDescription.add(reportItems.getString(3));
                        imageName = reportItems.getString(4);

                        String imgname = imageName.split("\\.")[0];
                        String uri = "drawable/" + imgname;

                        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                        images.add(imageResource);


                    }
                }
            }
        }

        if (reportDate != "") {
            reportDateTextView.setText(reportDate.substring(0, 13));
        } else {
            TextView textView = findViewById(R.id.textViewReportDateString);
            textView.setVisibility(View.GONE);
        }
        ReportActivity.MyAdapter adapter = new ReportActivity.MyAdapter(this, itemTitle, itemDescription, images);
        listView.setAdapter(adapter);
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        List<String> title;
        List<String> description;
        List<Integer> images;

        MyAdapter(Context c, List<String> title, List<String> description, List<Integer> images) {
            super(c, R.layout.item, R.id.reportItemTitle, title);
            this.context = c;
            this.title = title;
            this.description = description;
            this.images = images;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View item = layoutInflater.inflate(R.layout.item, parent, false);
            ImageView images1 = item.findViewById(R.id.image);

            Drawable image = getResources().getDrawable(images.get(position));
            images1.setImageDrawable(image);

            TextView title1 = item.findViewById(R.id.reportItemTitle);
            TextView description1 = item.findViewById(R.id.reportItemDescription);

            title1.setText(title.get(position));
            description1.setText(description.get(position));

            return item;
        }
    }

    public void onClickNewItem(View view) {
        Intent intent = new Intent(ReportActivity.this, NewItemActivity.class);

        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = NavBarUtil.setNavBarActions(ReportActivity.this, item);
        if (intent != null) {
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
