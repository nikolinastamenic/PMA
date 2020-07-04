package com.example.myapplication.sync.restTask;

import android.content.Context;
import android.os.AsyncTask;

import com.example.myapplication.DTO.ChangePasswordDto;
import com.example.myapplication.database.SqlHelper;
import com.example.myapplication.util.UserSession;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class ChangePasswordTask extends AsyncTask<String, Void, ResponseEntity> {

    private Context context;
    private UserSession session;
    private SqlHelper db;

    public static String RESULT_CODE = "RESULT_CODE";

    public ChangePasswordTask(Context context) { this.context = context; }

    @Override
    protected ResponseEntity<?> doInBackground(String... strings) {

        session = new UserSession(context);

        final String url = strings[0];
        final String password = strings[1];
        ChangePasswordDto userDto = new ChangePasswordDto(session.getUserEmail(), password);
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));


            HttpEntity entity = new HttpEntity(userDto, headers);


            return restTemplate.postForEntity(url, entity, Object.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}