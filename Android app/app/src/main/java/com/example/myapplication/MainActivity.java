package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.activities.LoginActivity;
import com.example.myapplication.activities.ReportActivity;
import com.example.myapplication.activities.SettingsActivity;
import com.example.myapplication.activities.TestActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("main activity ON CREATE");


    }

    public void onClickButton(View view){

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

    public void onClickReport(View view) {
        Intent intent = new Intent(MainActivity.this, ReportActivity.class);

        startActivity(intent);
    }

    public void onClickSettings(View view) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);

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
}
