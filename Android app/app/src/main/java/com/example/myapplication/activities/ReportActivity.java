package com.example.myapplication.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.myapplication.R;
import com.example.myapplication.database.DBContentProvider;
import com.example.myapplication.database.NewEntry;
import com.example.myapplication.database.SqlHelper;
import com.example.myapplication.sync.receiver.SyncReceiver;
import com.example.myapplication.sync.service.SyncService;
import com.example.myapplication.util.NavBarUtil;
import com.example.myapplication.util.UserSession;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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

    List<Bitmap> images;
    List<ImageView> imageViews;
    String reportId;
    String activityName;
    String userEmail;
    List<String> itemIds;


    String taskId;

    private SyncReceiver sync;
    public static String SYNC_DATA = "SYNC_DATA";
    UserSession userSession;
    MyAdapter adapter;
    boolean readonly;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);

        itemTitle = new ArrayList<>();
        itemDescription = new ArrayList<>();
        images = new ArrayList<>();
        itemIds = new ArrayList<>();
        readonly = false;

        Intent intent = getIntent();
        taskId = intent.getStringExtra("taskId");
        activityName = intent.getStringExtra("activityName");

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
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_log_in);

        menuItem.setVisible(false);

        Button newItemButton = findViewById(R.id.newItemButtonReport);
        Button finishReportButton = findViewById(R.id.finishButtonReport);

        if (activityName.equals("FinishedTasksActivity")) {
            newItemButton.setVisibility(View.GONE);
            finishReportButton.setVisibility(View.GONE);
            readonly = true;

        }

        userSession = new UserSession(getApplicationContext());

        sync = new SyncReceiver();


        userEmail = userSession.getUserEmail();


        Intent i = new Intent(this, SyncService.class);
        i.putExtra("Email", userEmail);
        i.putExtra("activityName", "ReportActivity");
        startService(i);

    }

    private void listView() {

        TextView reportDateTextView = findViewById(R.id.textViewReportDate);
        listView = (ListView) findViewById(R.id.listViewReport);
        SqlHelper db = new SqlHelper(this);
        Cursor data = db.getTaskById(taskId);
        reportId = "";

        while (data.moveToNext()) {
            if (data.getString(7) != null) {
                reportId = data.getString(7);
            } else {
                ReportDto reportDto = new ReportDto();

                Date date = new Date();
                reportDto.setDate(date);

                String reportUri = NewEntry.newReportEntry(ReportActivity.this, reportDto, taskId);
                reportId = reportUri.split("/")[1];

                ContentValues entryTask = new ContentValues();

                entryTask.put(SqlHelper.COLUMN_TASK_REPORT_ID, reportId);

                ReportActivity.this.getContentResolver().update(DBContentProvider.CONTENT_URI_TASK, entryTask, "id=" + taskId, null);


            }
            if (!reportId.equals("")) {
                Cursor reportData = db.getReportById(reportId);
                while (reportData.moveToNext()) {
                    if (reportData.getString(1) != null) {
                        reportIdMySQL = reportData.getString(1);
                    }
                    if (reportData.getString(2) != null) {

                        reportDate = reportData.getString(2);
                    }
                }
                reportData.close();
                Cursor reportItemData = db.getReportItemsByReportId(reportId);
                while (reportItemData.moveToNext()) {
                    Cursor reportItems = db.getReportItemById(reportItemData.getString(2));
                    while (reportItems.moveToNext()) {

                        itemIds.add(reportItems.getString(0));

                        itemTitle.add(reportItems.getString(2));
                        itemDescription.add(reportItems.getString(3));


                        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
                        File directory = contextWrapper.getDir(getFilesDir().getName(), Context.MODE_PRIVATE);
                        if (reportItems.getString(4) != null) {
                            File file = new File(directory, reportItems.getString(4));
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                            images.add(bitmap);
                        }


                    }
                    reportItems.close();


                }
                reportItemData.close();
            }
        }
        data.close();

        if (reportDate != "") {
            reportDateTextView.setText(reportDate.substring(0, 16));
        } else {
            TextView textView = findViewById(R.id.textViewReportDateString);
            textView.setVisibility(View.GONE);
        }
        adapter = new ReportActivity.MyAdapter(this, itemTitle, itemDescription, images);
        listView.setAdapter(adapter);

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(ReportActivity.this, NewItemActivity.class);
                intent.putExtra("reportItemIdForUpdate", itemIds.get(position));
                intent.putExtra("reportId", reportId);
                intent.putExtra("taskId", taskId);
                if(readonly == true){
                    intent.putExtra("readonly", "true");
                } else {
                    intent.putExtra("readonly", "false");
                }
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onPause() {
        unregisterReceiver(sync);
        super.onPause();
    }


    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        List<String> title;
        List<String> description;
        List<Bitmap> images;

        MyAdapter(Context c, List<String> title, List<String> description, List<Bitmap> images) {
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

            if(images.size() > 0){
                Bitmap image = images.get(position);
                images1.setImageBitmap(image);
            }

            TextView title1 = item.findViewById(R.id.reportItemTitle);
            TextView description1 = item.findViewById(R.id.reportItemDescription);

            title1.setText(title.get(position));
            description1.setText(description.get(position));

            return item;
        }
    }


    public void onClickNewItem(View view) {
        Intent intent = new Intent(ReportActivity.this, NewItemActivity.class);
        intent.putExtra("reportId", reportId);
        intent.putExtra("taskId", taskId);
        intent.putExtra("reportItemIdForUpdate", "");
        intent.putExtra("readonly", "false");

        startActivity(intent);
    }

    public void onClickFinishReport(View view) {

        ContentValues entryTask = new ContentValues();

        entryTask.put(SqlHelper.COLUMN_TASK_STATE, "FINISHED");
        ReportActivity.this.getContentResolver().update(DBContentProvider.CONTENT_URI_TASK, entryTask, "id=" + taskId, null);

        ContentValues entryReport = new ContentValues();

        Date date = new Date();
        entryReport.put(SqlHelper.COLUMN_REPORT_DATE, date.toString());
        ReportActivity.this.getContentResolver().update(DBContentProvider.CONTENT_URI_REPORT, entryReport, "id=" + reportId, null);



        Intent i = new Intent(this, SyncService.class);
        i.putExtra("Email", "");
        i.putExtra("activityName", "ReportActivity");
        i.putExtra("finishTask", "true");

        startService(i);


        Intent intent = new Intent(ReportActivity.this, TasksInProgressActivity.class);

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

    @Override
    protected void onStart() {
        if (adapter != null) {
            adapter.clear();
        }
        super.onStart();
    }

}
