package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.sync.service.SyncService;
import com.example.myapplication.util.NavBarUtil;
import com.google.android.material.navigation.NavigationView;

public class ChangePasswordActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

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

        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_log_in);
        menuItem.setVisible(false);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = NavBarUtil.setNavBarActions(ChangePasswordActivity.this, item);
        if (intent != null) {
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changePassword(View view) {
        EditText pass = findViewById(R.id.change_password_pass);
        EditText repeatPass = findViewById(R.id.change_password_repeat_pass);

        if (pass.getText().toString().equals("") || repeatPass.getText().toString().equals("")) {
            Toast.makeText(this, R.string.password_not_empty, Toast.LENGTH_SHORT).show();
        } else if (!pass.getText().toString().equals(repeatPass.getText().toString())) {
            Toast.makeText(this, R.string.password_not_same, Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, SyncService.class);
            intent.putExtra("activityName", "ChangePasswordActivity");
            intent.putExtra("newPassword", pass.getText().toString());
            startService(intent);

            Toast.makeText(this, R.string.password_changed, Toast.LENGTH_LONG).show();

            Intent profileIntent = new Intent(this, ProfileActivity.class);
            startActivity(profileIntent);
        }
    }
}