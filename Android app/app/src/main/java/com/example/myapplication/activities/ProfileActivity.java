package com.example.myapplication.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.net.Uri;
import android.opengl.GLDebugHelper;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.DTO.PictureDto;
import com.example.myapplication.DTO.UserDto;
import com.example.myapplication.R;
import com.example.myapplication.database.DBContentProvider;
import com.example.myapplication.database.SqlHelper;
import com.example.myapplication.util.AppConfig;
import com.example.myapplication.util.MiscUtil;
import com.example.myapplication.util.NavBarUtil;
import com.example.myapplication.util.SavePictureUtil;
import com.google.android.material.navigation.NavigationView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SqlHelper db;
    ImageView profilePicture;
    PictureDto updatedPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.profile, R.string.profile);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_profile);

        getUserProfile("3");
        showUserProfile();

        profilePicture = findViewById(R.id.profile_picture);
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, AppConfig.CAMERA_PERMISSION_CODE);

                }
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, AppConfig.CAMERA_REQUEST);
                }
            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppConfig.CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, getString(R.string.camera_access_allowed), Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, AppConfig.CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, getString(R.string.camera_access_denied), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConfig.CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            profilePicture.setImageBitmap(photo);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            String picName = "profilePicture-" + MiscUtil.generateRandom(7) + ".png";

            SavePictureUtil.writeToFile(stream.toByteArray(), picName, getApplicationContext(), getFilesDir());
            SqlHelper dbHelper = new SqlHelper(ProfileActivity.this);
            dbHelper.setUserPicture("3", picName);
            setUserProfilePicture("3", new PictureDto(picName, stream.toByteArray()));
        }
    }

    public void showUserProfile() {
        db = new SqlHelper(this);
        Cursor data = db.getUserByMySqlId("3");
        if (data.getCount() != 0) {
            data.moveToNext();
//            File picture = SavePictureUtil.readFromFile(data.getString(7), getApplicationContext(), getFilesDir());

            TextView name1 = findViewById(R.id.profile_name_surname);
            ImageView profile_picture = findViewById(R.id.profile_picture);
            TextView phone_number = findViewById(R.id.profile_phone_number);
            TextView email = findViewById(R.id.profile_email);

            name1.setText(data.getString(2) + " " + data.getString(3));
            ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
            File directory = contextWrapper.getDir(getFilesDir().getName(), Context.MODE_PRIVATE);
            File file =  new File(directory, data.getString(7));
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            profile_picture.setImageBitmap(bitmap);
            phone_number.setText(data.getString(4));
            email.setText(data.getString(5));
        }

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Intent intent = NavBarUtil.setNavBarActions(ProfileActivity.this, menuItem);
        if (intent != null) {
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getUserProfile(String id) {
        final String uri = AppConfig.apiURI + "user/" + id;    // TODO read cached user id value
        new RESTGetUserTask().execute(uri);
    }

    public void setUserProfilePicture(String id, PictureDto pictureDto) {
        final String uri = AppConfig.apiURI + "user/" + id + "/picture";    // TODO read cached user id value
        updatedPicture = pictureDto;
        new RESTSetUserProfilePictureTask().execute(uri);
    }

    class RESTGetUserTask extends AsyncTask<String, Void, ResponseEntity<UserDto>> {

        @Override
        protected ResponseEntity<UserDto> doInBackground(String... uri) {
            final String url = uri[0];
            RestTemplate restTemplate = new RestTemplate();
            try {
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                HttpEntity entity = new HttpEntity(headers);

                ResponseEntity<UserDto> response = restTemplate.getForEntity(url, UserDto.class, entity);

                return response;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }

        }

        protected void onPostExecute(ResponseEntity<UserDto> responseEntity) {

            UserDto userDto = responseEntity.getBody();

            SqlHelper dbHelper = new SqlHelper(ProfileActivity.this);
            dbHelper.dropUserTable();

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues entryUser = new ContentValues();

            entryUser.put(SqlHelper.COLUMN_USER_MYSQLID, userDto.getId());
            entryUser.put(SqlHelper.COLUMN_USER_NAME, userDto.getName());
            entryUser.put(SqlHelper.COLUMN_USER_SURNAME, userDto.getSurname());
            entryUser.put(SqlHelper.COLUMN_USER_PHONE_NUMBER, userDto.getPhoneNumber());
            entryUser.put(SqlHelper.COLUMN_USER_EMAIL, userDto.getEmail());
            entryUser.put(SqlHelper.COLUMN_USER_PICTURE, userDto.getPictureName());
            SavePictureUtil.writeToFile(userDto.getPicture(), userDto.getPictureName(), getApplicationContext(), getFilesDir());
            Uri userUri = ProfileActivity.this.getContentResolver().insert(DBContentProvider.CONTENT_URI_USER, entryUser);
            db.close();
            showUserProfile();

        }
    }

    class RESTSetUserProfilePictureTask extends AsyncTask<String, Void, ResponseEntity<UserDto>> {

        @Override
        protected ResponseEntity<UserDto> doInBackground(String... uri) {
            final String url = uri[0];
            RestTemplate restTemplate = new RestTemplate();
            try {
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                HttpEntity entity = new HttpEntity(headers);

                ResponseEntity<UserDto> response = restTemplate.postForEntity(url, updatedPicture, UserDto.class);

                return response;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }

        }

        protected void onPostExecute(ResponseEntity<UserDto> responseEntity) {

            UserDto userDto = responseEntity.getBody();

            SqlHelper dbHelper = new SqlHelper(ProfileActivity.this);
            dbHelper.dropUserTable();

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues entryUser = new ContentValues();

            entryUser.put(SqlHelper.COLUMN_USER_MYSQLID, userDto.getId());
            entryUser.put(SqlHelper.COLUMN_USER_NAME, userDto.getName());
            entryUser.put(SqlHelper.COLUMN_USER_SURNAME, userDto.getSurname());
            entryUser.put(SqlHelper.COLUMN_USER_PHONE_NUMBER, userDto.getPhoneNumber());
            entryUser.put(SqlHelper.COLUMN_USER_EMAIL, userDto.getEmail());
            entryUser.put(SqlHelper.COLUMN_USER_PICTURE, userDto.getPictureName());
            SavePictureUtil.writeToFile(userDto.getPicture(), userDto.getPictureName(), getApplicationContext(), getFilesDir());
            Uri userUri = ProfileActivity.this.getContentResolver().insert(DBContentProvider.CONTENT_URI_USER, entryUser);
            db.close();
            showUserProfile();

        }
    }
}
