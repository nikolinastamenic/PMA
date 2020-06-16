package com.example.myapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.DTO.LoginDto;
import com.example.myapplication.R;

import com.example.myapplication.util.AppConfig;
import com.example.myapplication.util.NavBarUtil;
import com.example.myapplication.util.UserSession;
import com.google.android.material.navigation.NavigationView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

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
    boolean success;


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

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    public void login(View view) {

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        etEmail = (EditText) findViewById(R.id.login_username);
        etPassword = (EditText) findViewById(R.id.login_password);

        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        checkLogin();


    }


    public void checkLogin() {
        final String uri = AppConfig.apiURI + "user/login";
        new LoginActivity.RESTCheckLogin().execute(uri);
    }

    class RESTCheckLogin extends AsyncTask<String, Void, ResponseEntity<Boolean>> {   //ulazni parametri, vrednost za racunanje procenta zavrsenosti posla, povrtna

        @Override
        protected ResponseEntity<Boolean> doInBackground(String... uri) {
            final String url = uri[0];
            RestTemplate restTemplate = new RestTemplate();
            try {
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

                LoginDto loginDto = new LoginDto();
                loginDto.setEmail(email);
                loginDto.setPassword(password);

                HttpEntity entity = new HttpEntity(loginDto, headers);

                ResponseEntity<Boolean> response = restTemplate.postForEntity(url, entity, Boolean.class);


                return response;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }

        }

        protected void onPostExecute(ResponseEntity<Boolean> responseEntity) {
            success = responseEntity.getBody();

            if (success) {
                session.createUserLoginSession(email, password);
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Email/Password is incorrect",
                        Toast.LENGTH_LONG).show();
            }


        }
    }

}
