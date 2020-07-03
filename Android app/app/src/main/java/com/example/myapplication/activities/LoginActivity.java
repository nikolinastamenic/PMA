package com.example.myapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.R;

import com.example.myapplication.sync.service.SyncService;
import com.example.myapplication.util.NavBarUtil;
import com.example.myapplication.util.UserSession;
import com.google.android.material.navigation.NavigationView;


public class LoginActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    EditText etEmail, etPassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String PREF_NAME = "login_preferences";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    UserSession session;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(null);


        session = new UserSession(getApplicationContext());


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent intent = NavBarUtil.setNavBarActions(LoginActivity.this, item);
        if (intent != null) {
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    public void onBackPressed() {
//
//        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//
//    }

    public void login(View view) {

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        etEmail = (EditText) findViewById(R.id.login_username);
        etPassword = (EditText) findViewById(R.id.login_password);

        email = etEmail.getText().toString();
        password = etPassword.getText().toString();

        Intent intent = new Intent(this, SyncService.class);
        intent.putExtra("Email", email);
        intent.putExtra("Password", password);
        intent.putExtra("activityName", "LoginActivity");
        startService(intent);


    }

    public void forgotPassword(View view) {

        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}
