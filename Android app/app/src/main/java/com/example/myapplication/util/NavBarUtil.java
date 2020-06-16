package com.example.myapplication.util;

import android.content.Context;
import android.content.Intent;
import android.service.autofill.AutofillService;
import android.view.MenuItem;

import com.example.myapplication.activities.FinishedTasksActivity;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.activities.AllTasksActivity;
import com.example.myapplication.activities.LoginActivity;
import com.example.myapplication.activities.ProfileActivity;
import com.example.myapplication.activities.SettingsActivity;
import com.example.myapplication.activities.TasksInProgressActivity;

public class NavBarUtil {
    private  static UserSession userSession;
    public static Intent setNavBarActions(Context parent, MenuItem menuItem) {
        userSession = new UserSession(parent);
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                return new Intent(parent, MainActivity.class);
            case R.id.nav_settings:
                return new Intent(parent, SettingsActivity.class);
            case R.id.nav_profile:
                return new Intent(parent, ProfileActivity.class);
            case R.id.nav_log_in:
                return new Intent(parent, LoginActivity.class);
            case R.id.nav_log_out:
                userSession.logoutUser();
                return new Intent(parent, LoginActivity.class);
            case R.id.nav_all_tasks:
                return new Intent(parent, AllTasksActivity.class);
            case R.id.nav_finished_tasks:
                return new Intent(parent, FinishedTasksActivity.class);
            case R.id.nav_tasks_in_process:
                return new Intent(parent, TasksInProgressActivity.class);
        }
        return null;
    }
}
