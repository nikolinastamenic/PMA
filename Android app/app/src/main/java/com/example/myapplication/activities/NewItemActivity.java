package com.example.myapplication.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.DTO.PictureDto;
import com.example.myapplication.DTO.ReportItemDto;
import com.example.myapplication.R;
import com.example.myapplication.database.DBContentProvider;
import com.example.myapplication.database.NewEntry;
import com.example.myapplication.database.SqlHelper;
import com.example.myapplication.sync.receiver.SyncReceiver;
import com.example.myapplication.sync.service.SyncService;
import com.example.myapplication.util.AppConfig;
import com.example.myapplication.util.MiscUtil;
import com.example.myapplication.util.NavBarUtil;
import com.example.myapplication.util.SavePictureUtil;
import com.example.myapplication.util.UserSession;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.File;
import java.io.IOException;

import lombok.SneakyThrows;

public class NewItemActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView reportItemImage;
    EditText faultNameET;
    EditText faultDescriptionET;
    String picName;
    String reportId;
    String taskId;
    ImageView cameraButton;
    ImageView galleryButton;
    Button createReportItem;
    private SyncReceiver sync;
    public static String SYNC_DATA = "SYNC_DATA";
    String reportItemId;
    String reportItemIdForUpdate = "";
    UserSession userSession;
    SqlHelper db;
    String reportItemMysqlId = "";
    String activityNameForReadonly = "";
    String readonly = "";
    boolean update;
    SQLiteDatabase sqlDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_item);

        Intent intent = getIntent();
        reportId = intent.getStringExtra("reportId");
        taskId = intent.getStringExtra("taskId");
        reportItemIdForUpdate = intent.getStringExtra("reportItemIdForUpdate");
        activityNameForReadonly = intent.getStringExtra("activityNameForReadonly");
        readonly = intent.getStringExtra("readonly");
        update = false;

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
        createReportItem = findViewById(R.id.finishButtonanewItem);
        cameraButton = findViewById(R.id.cameraButtonItem);
        galleryButton = findViewById(R.id.galleryButtonItem);

        faultNameET = (EditText) findViewById(R.id.nameOfFaultEditText);
        faultDescriptionET = (EditText) findViewById(R.id.descriptionOfFaultEditText);
        reportItemImage = (ImageView) findViewById(R.id.reportItemImage);


        if(readonly.equals("true")){
            faultNameET.setFocusable(false);
            faultDescriptionET.setFocusable(false);
            cameraButton.setVisibility(View.GONE);
            galleryButton.setVisibility(View.GONE);
            TextView textView = (TextView) findViewById(R.id.pictureOfFaultTextView);
            textView.setVisibility(View.GONE);
            createReportItem.setText("");
            createReportItem.setEnabled(false);
        }

        userSession = new UserSession(getApplicationContext());

        sync = new SyncReceiver();


        if (!reportItemIdForUpdate.equals("")) {
            getReportItemForUpdate(reportItemIdForUpdate);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void getReportItemForUpdate(String id) {

        Cursor reportItemData = db.getReportItemById(id);
        if (reportItemData.moveToFirst()) {

            faultNameET.setText(reportItemData.getString(2));
            faultDescriptionET.setText(reportItemData.getString(3));


            if(reportItemData.getString(1) != null && !reportItemData.getString(1).equals("")) {
                reportItemMysqlId = reportItemData.getString(1);
            }

            ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
            File directory = contextWrapper.getDir(getFilesDir().getName(), Context.MODE_PRIVATE);
            if (reportItemData.getString(4) != null) {
                File file = new File(directory, reportItemData.getString(4));
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                reportItemImage.setImageBitmap(bitmap);

            }
        }

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

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, AppConfig.CAMERA_PERMISSION_CODE);

                }
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, AppConfig.CAMERA_REQUEST);
                }
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //request permission
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, AppConfig.GALLERY_PERMISSION_CODE);
                    } else {
                        //permission already granted
                        pickImageFromGallery();
                    }
                } else {
                    pickImageFromGallery();

                }
            }


        });

        createReportItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!reportItemIdForUpdate.equals("")) {

                    Cursor reportItemUpdateData = db.getReportItemById(reportItemIdForUpdate);
                    if (reportItemUpdateData.moveToFirst()) {

                        ContentValues entryTask = new ContentValues();

                        entryTask.put(SqlHelper.COLUMN_REPORT_ITEM_FAULT_NAME, String.valueOf(faultNameET.getText()));
                        entryTask.put(SqlHelper.COLUMN_REPORT_ITEM_DETAILS, String.valueOf(faultDescriptionET.getText()));
                        if(update == true) {
                            entryTask.put(SqlHelper.COLUMN_REPORT_ITEM_FAULT_PICTURE, picName);
                        }
                        entryTask.put(SqlHelper.COLUMN_REPORT_ITEM_IS_SYNCHRONIZED, 0);

                        NewItemActivity.this.getContentResolver().update(DBContentProvider.CONTENT_URI_REPORT_ITEM, entryTask, "id=" + reportItemIdForUpdate, null);


                    }

                } else {

                    ReportItemDto reportItemDto = new ReportItemDto();
                    reportItemDto.setFaultName(faultNameET.getText().toString());
                    reportItemDto.setDetails(faultDescriptionET.getText().toString());
                    PictureDto pictureDto = new PictureDto();
                    pictureDto.setPictureName(picName);
                    reportItemDto.setPicture(pictureDto);
                    String reportItemUri = NewEntry.newReportItemEntry(NewItemActivity.this, reportItemDto, true);

                    reportItemId = reportItemUri.split("/")[1];
                    String reportReporetItemUri = NewEntry.newReportReportItemEntryWithoutMysqlIds(NewItemActivity.this, reportId, reportItemId);

                }

                Intent i = new Intent(NewItemActivity.this, SyncService.class);
                i.putExtra("activityName", "NewItemActivity");
                i.putExtra("TaskId", taskId );
                i.putExtra("ReportItemId", reportItemId);
                i.putExtra("Email", userSession.getUserEmail());
                i.putExtra("reportItemMysqlId", reportItemMysqlId);

                startService(i);

                Intent intent = new Intent(NewItemActivity.this, ReportActivity.class);
                intent.putExtra("taskId", taskId);
                intent.putExtra("activityName", "NewItemActivity");


                startActivity(intent);

            }
        });


        sqlDB.close();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppConfig.CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getString(R.string.camera_access_allowed), Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, AppConfig.CAMERA_REQUEST);
            } else {
                Toast.makeText(this, getString(R.string.camera_access_denied), Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == AppConfig.GALLERY_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getString(R.string.gallery_access_allowed), Toast.LENGTH_LONG).show();
                pickImageFromGallery();
            } else {
                Toast.makeText(this, getString(R.string.camera_access_denied), Toast.LENGTH_LONG).show();

            }
        }
    }

    private void pickImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, AppConfig.IMAGE_PICK_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (requestCode == AppConfig.CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            update = true;


            Bitmap photo = (Bitmap) data.getExtras().get("data");


            reportItemImage.setImageBitmap(photo);

            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            picName = "reportItemPicture-" + MiscUtil.generateRandom(7) + ".png";
            SavePictureUtil.writeToFile(stream.toByteArray(), picName, getApplicationContext(), getFilesDir());
            //TODO treba da se sacuva i u galeriju ako je dozvoljeno


        }
        if (resultCode == Activity.RESULT_OK && requestCode == AppConfig.IMAGE_PICK_CODE) {

            update = true;

            Bitmap photo = null;
            try {
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photo != null) {
                reportItemImage.setImageBitmap(photo);
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                picName = "reportItemPicture-" + MiscUtil.generateRandom(7) + ".png";
                SavePictureUtil.writeToFile(stream.toByteArray(), picName, getApplicationContext(), getFilesDir());
            }

        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = NavBarUtil.setNavBarActions(NewItemActivity.this, item);
        if (intent != null) {
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        unregisterReceiver(sync);
        super.onPause();
    }

//    public void deletePhoto(View view) {
//
//        reportItemImage.setImageBitmap(null);
//
//    }
}
