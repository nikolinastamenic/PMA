package com.example.myapplication.sync.restTask;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.myapplication.DTO.AllTaskDto;
import com.example.myapplication.DTO.EmailDto;
import com.example.myapplication.activities.AllTasksActivity;
import com.example.myapplication.database.DBContentProvider;
import com.example.myapplication.database.NewEntry;
import com.example.myapplication.database.SqlHelper;
import com.example.myapplication.util.AppConfig;
import com.example.myapplication.util.UserSession;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;


public class SyncTask extends AsyncTask<String, Void, ResponseEntity<AllTaskDto[]>> {

    private Context context;


    public static String RESULT_CODE = "RESULT_CODE";

    public SyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected ResponseEntity<AllTaskDto[]> doInBackground(String... uri) {



        UserSession userSession = new UserSession(context);
        final String url = uri[0];
        String email = uri[1];
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            EmailDto emailDto = new EmailDto(email);

            HttpEntity entity = new HttpEntity(emailDto, headers);

            ResponseEntity<AllTaskDto[]> response = restTemplate.postForEntity(url, entity, AllTaskDto[].class);


            return response;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    protected void onPostExecute(ResponseEntity<AllTaskDto[]> responseEntity) {
        final String uri = AppConfig.apiURI + "task/all";

        if (responseEntity != null) {
            AllTaskDto[] taskDtos = responseEntity.getBody();

            SqlHelper db = new SqlHelper(context);


            for (AllTaskDto taskDto : taskDtos) {

                String userId = "";
                String reportId = "";
                String addressId = "";
                String buildingId = "";
                String apartmentId = "";

                int mySqlId = (int) taskDto.getId();

                Cursor addressData = db.getAddressByMySqlId(String.valueOf(taskDto.getApartmentDto().getBuildingDto().getAddress().getId()));
                Cursor buildingData = db.getBuildingByMySqlId(String.valueOf(taskDto.getApartmentDto().getBuildingDto().getId()));
                Cursor taskData = db.getTaskByMySqlId(String.valueOf(mySqlId));
                Cursor apartmentData = db.getApartmentByMySqlId(String.valueOf(taskDto.getApartmentDto().getId()));

                if (!(addressData.moveToFirst()) || addressData.getCount() == 0) {
                    addressId = (NewEntry.newAddressEntry(context, taskDto.getApartmentDto().getBuildingDto())).split("/")[1];
                } else {
                    addressData.moveToFirst();
                    addressId = Integer.toString(addressData.getInt(0));
                }


                if (!(buildingData.moveToFirst()) || buildingData.getCount() == 0) {
                    buildingId = (NewEntry.newBuildingEntry(context, taskDto.getApartmentDto().getBuildingDto(), addressId)).split("/")[1];
                } else {
                    buildingData.moveToFirst();
                    buildingId = Integer.toString(buildingData.getInt(0));
                }

                if (!(apartmentData.moveToFirst()) || apartmentData.getCount() == 0) {
                    apartmentId = (NewEntry.newApartmentEntry(context, taskDto.getApartmentDto(), buildingId)).split("/")[1];
                } else {
                    apartmentData.moveToFirst();
                    apartmentId = Integer.toString(apartmentData.getInt(0));
                }

                if (!(taskData.moveToFirst()) || taskData.getCount() == 0) {

                    String taskUri = NewEntry.newTaskEntry(context, taskDto, apartmentId, userId, reportId);
                } else {

                    ContentValues entryTask = new ContentValues();
                    String state = taskDto.getState();
                    entryTask.put(SqlHelper.COLUMN_TASK_STATE, state);

                    context.getContentResolver().update(DBContentProvider.CONTENT_URI_TASK, entryTask, "id=" + taskData.getInt(0), null);
                }

                addressData.close();
                apartmentData.close();
                buildingData.close();
                taskData.close();

            }
        }
    }
}
