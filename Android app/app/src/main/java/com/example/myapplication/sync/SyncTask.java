package com.example.myapplication.sync;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.myapplication.DTO.AllTaskDto;
import com.example.myapplication.DTO.EmailDto;
import com.example.myapplication.DTO.ReportItemDto;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.database.NewEntry;
import com.example.myapplication.database.SqlHelper;
import com.example.myapplication.util.AppConfig;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SyncTask extends AsyncTask<String, Void, ResponseEntity<AllTaskDto[]>> {

    private Context context;
    SqlHelper db;


    public static String RESULT_CODE = "RESULT_CODE";

    public SyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected ResponseEntity<AllTaskDto[]> doInBackground(String... uri) {

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

        AllTaskDto[] taskDtos = responseEntity.getBody();

        SqlHelper dbHelper = new SqlHelper(context);
//        dbHelper.dropTaskTable();
//        dbHelper.dropReportTable();
        int i = 0, j = 0;

        String apartmentId = "";
        String apartmentNumber = "";
        String buildingId = "";

        for (AllTaskDto taskDto : taskDtos) {

            List<Boolean> existList = new ArrayList<>();
            String apartmentUri = "";
            String userId = "";
            String reportId = "";

                int mySqlId = (int) taskDto.getId();

                if (taskDto.getUserDto() != null) {
                    String userUri = NewEntry.newUserEntry(context, taskDto.getUserDto()); //TODO promeniti kad se odradi logovanje!
                    userId = userUri.split("/")[1];
                }

                String addressUri = NewEntry.newAddressEntry(context, taskDto.getApartmentDto().getBuildingDto());
                String buildingUri = NewEntry.newBuildingEntry(context, taskDto.getApartmentDto().getBuildingDto(), addressUri);

                apartmentUri = NewEntry.newApartmentEntry(context, taskDto.getApartmentDto(), buildingUri);

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

            db = new SqlHelper(context);
            Cursor allTasksData = db.getAllTasks();

            while (allTasksData.moveToNext()) {

                System.out.println(mySqlId + " SERVER ID ");
                System.out.println(allTasksData.getInt(1) + " ALLTASKSDATA ID");

                if (mySqlId == allTasksData.getInt(1)) {
                    i++;
                    System.out.println(" IDJEVI SU ISTI..................brojac: " + i);
                    existList.add(true);
                } else {
                    j++;
                    System.out.println(" IDJEVI SU RAZLICITI..................brojac: " + j);
                    existList.add(false);
                }


//            String taskUri = NewEntry.newTaskEntry(context, taskDto, apartmentUri, userId, reportId);

            }

            if (existList.contains(true)) {
                System.out.println("postoji u sqlite bazi");
            } else {
                String taskUri = NewEntry.newTaskEntry(context, taskDto, apartmentUri, userId, reportId);
            }

        }

    }
}
