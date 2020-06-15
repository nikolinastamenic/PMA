package com.example.myapplication.sync;

import android.content.Context;
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

import java.util.Arrays;

public class SyncTask extends AsyncTask<String, Void, ResponseEntity<AllTaskDto[]>> {

    private Context context;


    public static String RESULT_CODE = "RESULT_CODE";

    public SyncTask(Context context)
    {
        this.context = context;
    }
    @Override
    protected ResponseEntity<AllTaskDto[]> doInBackground(String... uri) {

        final String url = uri[0];
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            EmailDto emailDto = new EmailDto("user@yahoo.com");

            HttpEntity entity = new HttpEntity(emailDto, headers);   //TODO ispraviti posle odradjenog logovanja

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
        dbHelper.dropTaskTable();

        for (AllTaskDto taskDto : taskDtos) {
            String userId = null;
            if (taskDto.getUserDto() != null) {
                String userUri = NewEntry.newUserEntry(context, taskDto.getUserDto()); //TODO promeniti kad se odradi logovanje!
                userId = userUri.split("/")[1];
            }
            String addressUri = NewEntry.newAddressEntry(context, taskDto.getApartmentDto().getBuildingDto());
            String buildingUri = NewEntry.newBuildingEntry(context, taskDto.getApartmentDto().getBuildingDto(), addressUri);

            String apartmentUri = NewEntry.newApartmentEntry(context, taskDto.getApartmentDto(), buildingUri);
            String reportId = null;
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


            String taskUri = NewEntry.newTaskEntry(context, taskDto, apartmentUri, userId, reportId);


        }

    }
}
