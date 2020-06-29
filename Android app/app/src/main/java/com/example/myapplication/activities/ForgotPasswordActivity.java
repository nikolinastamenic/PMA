package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.sync.service.SyncService;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
    }

    public void submit(View view) {

        Intent intent = new Intent(this, SyncService.class);
        intent.putExtra("Email", ((EditText) findViewById(R.id.forgot_pass_email)).getText().toString());
        intent.putExtra("activityName", "ForgotPasswordActivity");
        startService(intent);

        Toast.makeText(getApplicationContext(), "Email has been sent", Toast.LENGTH_SHORT).show();

        Intent loginIntent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(loginIntent);

    }
}