package com.example.myapplication.sync.restTask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.myapplication.DTO.ChangeTaskStateDto;
import com.example.myapplication.database.DBContentProvider;
import com.example.myapplication.database.SqlHelper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestTaskTask extends AsyncTask<String, Void, ResponseEntity<ChangeTaskStateDto>> { //ulazni parametri, vrednost za racunanje procenta zavrsenosti posla, povratna

    private Context context;
    private String email;
    private SqlHelper db;
    private String userId = "";


    public RequestTaskTask(Context applicationContext) {
        this.context = applicationContext;
    }

    @Override
    protected ResponseEntity<ChangeTaskStateDto> doInBackground(String... params) {

        //       UserSession userSession = new UserSession(context);

        db = new SqlHelper(context);

        final String url = params[0];
        email = params[1];

        RestTemplate restTemplate = new RestTemplate();

        Cursor userData = db.getUserByEmail(email);
        userData.moveToFirst();
        userId = Integer.toString(userData.getInt(0));


        try {
            //        String email = userSession.getUserEmail();
            if (!email.equals("")) {
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

                Cursor requestedData = db.getTasksBySynchronizedAndUserId(0, userId);

                ChangeTaskStateDto changeTaskStateDto = new ChangeTaskStateDto();
                changeTaskStateDto.setEmail(email);
                changeTaskStateDto.setState("IN_PROCESS");
                List<String> tasksIds = new ArrayList<>();

                while (requestedData.moveToNext()){
                    String id = Integer.toString(requestedData.getInt(1));
                    tasksIds.add(id);
                }

                changeTaskStateDto.setTaskIds(tasksIds);

                HttpEntity entity = new HttpEntity(changeTaskStateDto, headers);
                ResponseEntity<ChangeTaskStateDto> response = restTemplate.postForEntity(url, entity, ChangeTaskStateDto.class);

                return response;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    protected void onPostExecute(ResponseEntity<ChangeTaskStateDto> responseEntity) {

        ChangeTaskStateDto changeTaskStateDto = responseEntity.getBody();
        SqlHelper db = new SqlHelper(context);

        if (changeTaskStateDto != null) {

            for (String taskId : changeTaskStateDto.getTaskIds()) {

                ContentValues entryTask = new ContentValues();

                Cursor taskData = db.getTaskByMySqlId(taskId);
                while (taskData.moveToNext()) {
                    String state = "IN_PROCESS";
                    entryTask.put(SqlHelper.COLUMN_TASK_STATE, state);
                    entryTask.put(SqlHelper.COLUMN_TASK_IS_SYNCHRONIZED, 1);

                    context.getContentResolver().update(DBContentProvider.CONTENT_URI_TASK, entryTask, "id=" + taskId, null);

                }
            }
            Cursor forDelete = db.getTasksBySynchronizedAndUserId(0, userId);

            while (forDelete.moveToNext()){
                int taskId = forDelete.getInt(0);
                context.getContentResolver().delete(DBContentProvider.CONTENT_URI_TASK, "id=" + taskId, null);

            }


        }

    }

}
