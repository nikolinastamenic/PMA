package com.example.myapplication.sync.restTask;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.myapplication.DTO.AllTaskDto;
import com.example.myapplication.DTO.LoginDto;
import com.example.myapplication.DTO.ReportItemDto;
import com.example.myapplication.DTO.UserAndTaskDto;
import com.example.myapplication.DTO.UserDto;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.database.NewEntry;
import com.example.myapplication.database.SqlHelper;
import com.example.myapplication.util.UserSession;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class LoginTask extends AsyncTask<String, Void, ResponseEntity<UserAndTaskDto>> {  //ulazni parametri, vrednost za racunanje procenta zavrsenosti posla, povrtna

    private Context context;
    private UserSession session;
    private String email;
    private String password;
    private SqlHelper db;

    public static String RESULT_CODE = "RESULT_CODE";

    public LoginTask(Context context) {
        this.context = context;
    }


    @Override
    protected ResponseEntity<UserAndTaskDto> doInBackground(String... uri) {

        session = new UserSession(context);

        final String url = uri[0];
        email = uri[1];
        password = uri[2];
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

            ResponseEntity<UserAndTaskDto> response = restTemplate.postForEntity(url, entity, UserAndTaskDto.class);


            return response;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    protected void onPostExecute(ResponseEntity<UserAndTaskDto> responseEntity) {
        db = new SqlHelper(context);

        UserAndTaskDto userAndTaskDto = responseEntity.getBody();

//        db.dropTaskTable();
//        db.dropReportTable();
//        db.dropUserTable();
//        db.dropReportTable();
//        db.dropAddressTable();
//        db.dropApartmentTable();
//        db.dropBuildingTable();


        if (userAndTaskDto != null) {
            session.createUserLoginSession(email, password);
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);

            String userEmail = userAndTaskDto.getUser().getEmail();
            Cursor userData = db.getUserByEmail(userEmail);

            userData.moveToFirst();

            if (!(userData.moveToFirst()) || userData.getCount() ==0) {
                String userUri = NewEntry.newUserEntry(context, userAndTaskDto.getUser());

                if(!userAndTaskDto.getTasks().isEmpty()){

                    for (AllTaskDto taskDto : userAndTaskDto.getTasks()) {

                        String userId = "";
                        String reportId = "";
                        String addressId = "";
                        String buildingId = "";
                        String apartmentId = "";

                        int mySqlId = (int) taskDto.getId();

                        userId = userUri.split("/")[1];

                        Cursor addressData = db.getAddressByMySqlId(String.valueOf(taskDto.getApartmentDto().getBuildingDto().getAddress().getId()));
                        Cursor buildingData = db.getBuildingByMySqlId(String.valueOf(taskDto.getApartmentDto().getBuildingDto().getId()));
                        Cursor taskData = db.getTaskByMySqlId(String.valueOf(mySqlId));
                        Cursor apartmentData = db.getApartmentByMySqlId(String.valueOf(taskDto.getApartmentDto().getId()));

                        System.out.println(addressData.getCount() + " COUNT");
                        if (!(addressData.moveToFirst()) || addressData.getCount() == 0 ) {
                            addressId = (NewEntry.newAddressEntry(context, taskDto.getApartmentDto().getBuildingDto())).split("/")[1];
                        } else {
                            addressData.moveToFirst();
                            addressId = Integer.toString(addressData.getInt(0));
                        }

                        if (!(buildingData.moveToFirst()) || buildingData.getCount() ==0) {
                            buildingId = (NewEntry.newBuildingEntry(context, taskDto.getApartmentDto().getBuildingDto(), addressId)).split("/")[1];
                        } else {
                            buildingData.moveToFirst();
                            buildingId = Integer.toString(buildingData.getInt(0));
                        }

                        if (!(apartmentData.moveToFirst()) || apartmentData.getCount() ==0) {
                            apartmentId = (NewEntry.newApartmentEntry(context, taskDto.getApartmentDto(), buildingId)).split("/")[1];
                        } else {
                            apartmentData.moveToFirst();
                            apartmentId = Integer.toString(apartmentData.getInt(0));
                        }


                        if (taskDto.getReportDto() != null) {
                            String reportUri = NewEntry.newReportEntry(context, taskDto.getReportDto());
                            reportId = reportUri.split("/")[1];

                            if (!taskDto.getReportDto().getItemList().isEmpty()) {

                                for (ReportItemDto reportItemDto : taskDto.getReportDto().getItemList()) {

                                    String reportItemUri = NewEntry.newReportItemEntry(context, reportItemDto);

                                    String reportItemId = reportItemUri.split("/")[1];
                                    String reportReporetItemUri = NewEntry.newReportReportItemEntry(context, reportItemDto, taskDto.getReportDto(), reportId, reportItemId);

                                }
                            }
                        }

                        if (!(taskData.moveToFirst()) || taskData.getCount() ==0) {
                            String taskUri = NewEntry.newTaskEntry(context, taskDto, apartmentId, userId, reportId);
                        }

                        addressData.close();
                        apartmentData.close();
                        buildingData.close();
                        taskData.close();

                    }


                }

            }

        } else {
            Toast.makeText(context,
                    "Email/Password is incorrect",
                    Toast.LENGTH_LONG).show();
        }


    }
}