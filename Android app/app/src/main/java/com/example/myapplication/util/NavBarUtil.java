package com.example.myapplication.util;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.activities.LoginActivity;
import com.example.myapplication.activities.ProfileActivity;

public class NavBarUtil {

    public static Intent setNavBarActions(Context parent, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Intent homeIntent = new Intent(parent, MainActivity.class);
                return homeIntent;
            case R.id.nav_profile:
                Intent profileIntent = new Intent(parent, ProfileActivity.class);
                return profileIntent;
            case R.id.nav_log_in:
                Intent loginIntent = new Intent(parent, LoginActivity.class);
                return loginIntent;
        }
        return null;
    }
}
