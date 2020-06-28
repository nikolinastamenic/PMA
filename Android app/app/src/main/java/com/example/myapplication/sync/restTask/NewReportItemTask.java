package com.example.myapplication.sync.restTask;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.myapplication.DTO.ChangeTaskStateDto;
import com.example.myapplication.DTO.NewReportItemDto;
import com.example.myapplication.DTO.PictureDto;
import com.example.myapplication.DTO.ReportItemDto;
import com.example.myapplication.DTO.ReportMysqlIdsDto;
import com.example.myapplication.DTO.UserAndTaskDto;
import com.example.myapplication.activities.AllTasksActivity;
import com.example.myapplication.activities.ReportActivity;
import com.example.myapplication.database.DBContentProvider;
import com.example.myapplication.database.SqlHelper;
import com.example.myapplication.util.SavePictureUtil;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lombok.SneakyThrows;

public class NewReportItemTask extends AsyncTask<String, Void, ResponseEntity<ReportMysqlIdsDto>> {

    private Context context;
    private SqlHelper db;
    String taskId;
    String reportItemId;
    int reportId;



    public NewReportItemTask(Context applicationContext) {
        this.context = applicationContext;
    }

    @SneakyThrows
    @Override
    protected ResponseEntity<ReportMysqlIdsDto> doInBackground(String... params) {


        db = new SqlHelper(context);

        final String url = params[0];
        taskId = params[1];
        reportItemId = params[2];

        RestTemplate restTemplate = new RestTemplate();


        Cursor taskData = db.getTaskById(taskId);
        taskData.moveToFirst();
        int taskMySqlId = taskData.getInt(1);
        reportId = taskData.getInt(7);


        Cursor reportItemData = db.getReportItemById(reportItemId);
        reportItemData.moveToFirst();
        int mySqlReportItemId = 0;
        String faultName = reportItemData.getString(2);
        String description = reportItemData.getString(3);
        String pictureName = reportItemData.getString(4);
        if (reportItemData.getInt(1) != 0) {
            mySqlReportItemId = reportItemData.getInt(1);
        }

        int mySqlReportId = 0;
        Cursor reportData = db.getReportById(Integer.toString(reportId));
        reportData.moveToFirst();
        if (reportData.getInt(1) != 0) {
            mySqlReportId = reportData.getInt(1);
        }
        String reportDate = reportData.getString(2);
//        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(reportDate);

        NewReportItemDto newReportItemDto = new NewReportItemDto();
        PictureDto pictureDto = new PictureDto();
        pictureDto.setPictureName(pictureName);

        newReportItemDto.setMySqlTaskId(taskMySqlId);
        newReportItemDto.setMySqlReportId(mySqlReportId);
        newReportItemDto.setMySqlReportItemId(mySqlReportItemId);
        newReportItemDto.setReportCreatedDate(reportDate);
        newReportItemDto.setFaultName(faultName);
        newReportItemDto.setDetails(description);

        File file = SavePictureUtil.readFromFile(pictureName, context, context.getFilesDir());
        try {
            pictureDto.setPicture(IOUtils.toByteArray(new FileInputStream(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }


        newReportItemDto.setPicture(pictureDto);


        try {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            HttpEntity entity = new HttpEntity(newReportItemDto, headers);
            ResponseEntity<ReportMysqlIdsDto> response = restTemplate.postForEntity(url, entity, ReportMysqlIdsDto.class);

            return response;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;


        }


    }


    protected void onPostExecute(ResponseEntity<ReportMysqlIdsDto> responseEntity) {
//
//        ReportMysqlIdsDto reportMysqlIdsDto = responseEntity.getBody();
//
//
//        ContentValues entryTask = new ContentValues();
//
//        entryTask.put(SqlHelper.COLUMN_REPORT_MYSQLID, reportMysqlIdsDto.getReportMysqlId());
//
//        context.getContentResolver().update(DBContentProvider.CONTENT_URI_REPORT, entryTask, "id=" + reportId, null);
//
//        ContentValues entryTask1 = new ContentValues();
//
//        entryTask1.put(SqlHelper.COLUMN_REPORT_ITEM_MYSQLID, reportMysqlIdsDto.getReportItemMysqlId());
//
//        context.getContentResolver().update(DBContentProvider.CONTENT_URI_REPORT_ITEM, entryTask1, "id=" + reportItemId, null);
//
//
//        Cursor reportReportItemData = db.getRRIByRIdAndRIId(reportItemId);
//        reportReportItemData.moveToFirst();
//
//        int reportReportItemId = reportReportItemData.getInt(0);
//
//        ContentValues entryTask2 = new ContentValues();
//
//        entryTask2.put(SqlHelper.COLUMN_REPORT_REPORT_ITEM_REPORT_MYSQLIDID, reportMysqlIdsDto.getReportMysqlId());
//        entryTask2.put(SqlHelper.COLUMN_REPORT_REPORT_ITEM_REPORT_ITEM_MYSQLID, reportMysqlIdsDto.getReportItemMysqlId());
//
//        context.getContentResolver().update(DBContentProvider.CONTENT_URI_JOIN_TABLE, entryTask2, "id=" + reportReportItemId, null);







    }
}