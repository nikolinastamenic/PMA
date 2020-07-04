package com.example.myapplication.sync.restTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.myapplication.DTO.ChangeTaskStateDto;
import com.example.myapplication.database.SqlHelper;
import com.example.myapplication.util.UserSession;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FinishTaskTask extends AsyncTask<String, Void, ResponseEntity<ChangeTaskStateDto>> { //ulazni parametri, vrednost za racunanje procenta zavrsenosti posla, povratna

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private String email;
    private SqlHelper db;
    SQLiteDatabase sqlDB;
    private UserSession userSession;

    public FinishTaskTask(Context applicationContext) {
        this.context = applicationContext;
    }

    @Override
    protected ResponseEntity<ChangeTaskStateDto> doInBackground(String... params) {

        userSession = new UserSession(context);

        db = new SqlHelper(context);
        sqlDB = db.getWritableDatabase();


        final String url = params[0];
        email = userSession.getUserEmail();

        ChangeTaskStateDto changeTaskStateDto = new ChangeTaskStateDto();

        List<String> mySqlIdsList = new ArrayList<>();
        Cursor taskData = db.getFinishedTasks(sqlDB);
        while (taskData.moveToNext()) {
            int mysqlId = taskData.getInt(1);
            mySqlIdsList.add(String.valueOf(mysqlId));
        }
        taskData.close();

        changeTaskStateDto.setMysqlTaskIds(mySqlIdsList);
        changeTaskStateDto.setEmail(email);
        changeTaskStateDto.setState("FINISHED");

        RestTemplate restTemplate = new RestTemplate();


        try {
            if (!email.equals("")) {
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));


                HttpEntity entity = new HttpEntity(changeTaskStateDto, headers);
                ResponseEntity<ChangeTaskStateDto> response = restTemplate.postForEntity(url, entity, ChangeTaskStateDto.class);
                sqlDB.close();
                return response;
            }
            sqlDB.close();
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            sqlDB.close();
            return null;
        }

    }

    protected void onPostExecute(ResponseEntity<ChangeTaskStateDto> responseEntity) {


        if (responseEntity != null) {
            ChangeTaskStateDto changeTaskStateDto = responseEntity.getBody();


        }

    }
}
