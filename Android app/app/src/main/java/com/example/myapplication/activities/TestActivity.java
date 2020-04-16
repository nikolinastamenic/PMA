package com.example.myapplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;

public class TestActivity extends Activity {


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linearlayout);

        Intent intent = getIntent();
        int brojj = intent.getIntExtra("broj", 12);

        System.out.println("usao u test activity ON CREATE" + brojj);
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("usao u test activity ON START");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("usao u test activity ON RESTART");

    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("usao u test activity ON RESUME");

    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("usao u test activity ON PAUSE");

    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("usao u test activity ON STOP");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("usao u test activity ON DESTROY");

    }
}
