package com.example.myapplication.sync.restTask;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.os.AsyncTask;

import com.example.myapplication.DTO.NewReportItemDto;
import com.example.myapplication.DTO.NewReportItemItemDto;
import com.example.myapplication.DTO.PictureDto;
import com.example.myapplication.DTO.ReportMysqlIdsDto;
import com.example.myapplication.DTO.ReportMysqlIdsItemDto;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lombok.SneakyThrows;

public class NewReportItemTask extends AsyncTask<String, Void, ResponseEntity<ReportMysqlIdsDto>> {

    private Context context;
    private SqlHelper db;


    public NewReportItemTask(Context applicationContext) {
        this.context = applicationContext;
    }

    @SneakyThrows
    @Override
    protected ResponseEntity<ReportMysqlIdsDto> doInBackground(String... params) {


        NewReportItemDto reportItemsDto = new NewReportItemDto();
        List<NewReportItemItemDto> newReportItemItemDtos = new ArrayList<>();

        db = new SqlHelper(context);

        final String url = params[0];

        RestTemplate restTemplate = new RestTemplate();

        Cursor reportItemsData = db.getReportItemBySynchronized(0);

        while (reportItemsData.moveToNext()) {

            int reportItemId = 0;
            int reportId = 0;
            int taskId = 0;
            int taskMySqlId = 0;
            int mySqlReportId = 0;
            int mySqlReportItemId = 0;

            reportItemId = reportItemsData.getInt(0);

            Cursor joinTableData = db.getRRIByRIdAndRIId(String.valueOf(reportItemId));
            if (joinTableData.moveToFirst()) {
                reportId = joinTableData.getInt(0);
            }
            joinTableData.close();


            ContentValues entryReport = new ContentValues();

            entryReport.put(SqlHelper.COLUMN_REPORT_DATE, new Date().toString());

            context.getContentResolver().update(DBContentProvider.CONTENT_URI_REPORT, entryReport, "id=" + reportId, null);


            Cursor reportData = db.getReportById(String.valueOf(reportId));
            if (reportData.moveToFirst()) {
                mySqlReportId = reportData.getInt(1);
                taskId = reportData.getInt(3);

            }
            String reportDate = reportData.getString(2);
            reportData.close();

            Cursor taskData = db.getTaskById(String.valueOf(taskId));

            if (taskData.moveToFirst()) {
                taskMySqlId = taskData.getInt(1);

            }

            taskData.close();

            String faultName = reportItemsData.getString(2);
            String description = reportItemsData.getString(3);
            String pictureName = reportItemsData.getString(4);
            if (reportItemsData.getInt(1) != 0) {
                mySqlReportItemId = reportItemsData.getInt(1);
            }


            NewReportItemItemDto newReportItemItemDto = new NewReportItemItemDto();
            PictureDto pictureDto = new PictureDto();
            pictureDto.setPictureName(pictureName);

            newReportItemItemDto.setMySqlTaskId(taskMySqlId);
            newReportItemItemDto.setMySqlReportId(mySqlReportId);
            newReportItemItemDto.setMySqlReportItemId(mySqlReportItemId);
            newReportItemItemDto.setReportCreatedDate(reportDate);
            newReportItemItemDto.setFaultName(faultName);
            newReportItemItemDto.setDetails(description);

            try {
                File file = SavePictureUtil.readFromFile(pictureName, context, context.getFilesDir());
                pictureDto.setPicture(IOUtils.toByteArray(new FileInputStream(file)));
            } catch (Exception e) {

            }


            newReportItemItemDto.setPicture(pictureDto);
            newReportItemItemDto.setReportId(reportId);
            newReportItemItemDto.setReportItemId(reportItemId);

            newReportItemItemDtos.add(newReportItemItemDto);
            reportItemsDto.setNewReportItemItemDtoList(newReportItemItemDtos);

        }



        if (reportItemsData.getCount() > 0) {
            reportItemsData.close();

            try {
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

                HttpEntity entity = new HttpEntity(reportItemsDto, headers);
                ResponseEntity<ReportMysqlIdsDto> response = restTemplate.postForEntity(url, entity, ReportMysqlIdsDto.class);

                return response;

            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;

            }

        }
        reportItemsData.close();

        return null;


    }


    protected void onPostExecute(ResponseEntity<ReportMysqlIdsDto> responseEntity) {

        if (responseEntity != null) {

            ReportMysqlIdsDto reportMysqlIdsDto = responseEntity.getBody();

            for (ReportMysqlIdsItemDto reportMysqlIdsItemDto : reportMysqlIdsDto.getReportMysqlIdsItemDtos()) {


                ContentValues entryTask = new ContentValues();

                entryTask.put(SqlHelper.COLUMN_REPORT_MYSQLID, reportMysqlIdsItemDto.getReportMysqlId());

                context.getContentResolver().update(DBContentProvider.CONTENT_URI_REPORT, entryTask, "id=" + reportMysqlIdsItemDto.getReportId(), null);

                ContentValues entryReportItem = new ContentValues();

                entryReportItem.put(SqlHelper.COLUMN_REPORT_ITEM_MYSQLID, reportMysqlIdsItemDto.getReportItemMysqlId());
                entryReportItem.put(SqlHelper.COLUMN_REPORT_ITEM_IS_SYNCHRONIZED, 1);


                context.getContentResolver().update(DBContentProvider.CONTENT_URI_REPORT_ITEM, entryReportItem, "id=" + reportMysqlIdsItemDto.getReportItemId(), null);

                ContentValues entryTask2 = new ContentValues();

                entryTask2.put(SqlHelper.COLUMN_REPORT_REPORT_ITEM_REPORT_MYSQLIDID, reportMysqlIdsItemDto.getReportMysqlId());
                entryTask2.put(SqlHelper.COLUMN_REPORT_REPORT_ITEM_REPORT_ITEM_MYSQLID, reportMysqlIdsItemDto.getReportItemMysqlId());

                context.getContentResolver().update(DBContentProvider.CONTENT_URI_JOIN_TABLE, entryTask2, "report_item_id=" + reportMysqlIdsItemDto.getReportItemId(), null);


            }


        }
    }

}
