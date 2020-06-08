package com.example.myapplication.database;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;

import com.example.myapplication.DTO.AllTaskDto;
import com.example.myapplication.DTO.ApartmentDto;
import com.example.myapplication.DTO.BuildingDto;
import com.example.myapplication.DTO.ReportDto;
import com.example.myapplication.DTO.ReportItemDto;
import com.example.myapplication.DTO.UserDto;
import com.example.myapplication.activities.FinishedTasksActivity;
import com.example.myapplication.activities.TasksInProgressActivity;

public class NewEntry {

    public static String newAddressEntry(Activity activity, BuildingDto buildingDto) {
        ContentValues entryAddress = new ContentValues();


        entryAddress.put(SqlHelper.COLUMN_ADDRESS_MYSQLID, buildingDto.getAddress().getId());
        entryAddress.put(SqlHelper.COLUMN_ADDRESS_COUNTRY, buildingDto.getAddress().getCountry());
        entryAddress.put(SqlHelper.COLUMN_ADDRESS_CITY, buildingDto.getAddress().getCity());
        entryAddress.put(SqlHelper.COLUMN_ADDRESS_STREET, buildingDto.getAddress().getStreet());
        entryAddress.put(SqlHelper.COLUMN_ADDRESS_NUMBER, buildingDto.getAddress().getNumber());
        entryAddress.put(SqlHelper.COLUMN_ADDRESS_LONGITUDE, buildingDto.getAddress().getLongitude());
        entryAddress.put(SqlHelper.COLUMN_ADDRESS_LATITUDE, buildingDto.getAddress().getLatitude());
        Uri addressUri = activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_ADDRESS, entryAddress);
        return addressUri.toString();
    }

    public static String newBuildingEntry(Activity activity, BuildingDto buildingDto, String addressUri) {

        ContentValues entryBuilding = new ContentValues();
        entryBuilding.put(SqlHelper.COLUMN_BUILDING_MYSQLID, buildingDto.getId());
        String addressId = addressUri.split("/")[1];
        entryBuilding.put(SqlHelper.COLUMN_BUILDING_ADDRESS_ID, addressId);
        Uri buildingUri = activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_BUILDING, entryBuilding);
        return buildingUri.toString();
    }

    public static String newApartmentEntry(Activity activity, ApartmentDto apartmentDto, String buildingUri) {

        ContentValues entryApartment = new ContentValues();

        entryApartment.put(SqlHelper.COLUMN_APARTMENT_MYSQLID, apartmentDto.getId());
        entryApartment.put(SqlHelper.COLUMN_APARTMENT_NUMBER, apartmentDto.getNumber());
        String buildingId = buildingUri.split("/")[1];
        entryApartment.put(SqlHelper.COLUMN_APARTMENT_BUILDING_ID, buildingId);
        Uri apartmentUri = activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_APARTMENT, entryApartment);
        return apartmentUri.toString();
    }

    public static String newTaskEntry(Activity activity, AllTaskDto allTaskDto, String apartmentUri, String userId, String reportId) {
        ContentValues entryTask = new ContentValues();

        entryTask.put(SqlHelper.COLUMN_TASK_MYSQLID, allTaskDto.getId());
        entryTask.put(SqlHelper.COLUMN_TASK_STATE, allTaskDto.getState());
        entryTask.put(SqlHelper.COLUMN_TASK_DEADLINE, allTaskDto.getDeadline().toString());
        entryTask.put(SqlHelper.COLUMN_TASK_TYPE_OF_APARTMENT, allTaskDto.getTypeOfApartment());
        entryTask.put(SqlHelper.COLUMN_TASK_URGENT, allTaskDto.isUrgent());
        String apartmentId = apartmentUri.split("/")[1];
        entryTask.put(SqlHelper.COLUMN_TASK_APARTMENT_ID, apartmentId);
        if (userId != null) {
            entryTask.put(SqlHelper.COLUMN_TASK_USER_ID, userId);

        }
        if (reportId != null) {
            entryTask.put(SqlHelper.COLUMN_TASK_REPORT_ID, reportId);
        }
        Uri taskUri = activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_TASK, entryTask);

        return taskUri.toString();


    }


    public static String newUserEntry(Activity activity, UserDto userDto) {

        ContentValues entryUser = new ContentValues();
        entryUser.put(SqlHelper.COLUMN_USER_MYSQLID, userDto.getId());
        entryUser.put(SqlHelper.COLUMN_USER_EMAIL, userDto.getEmail());
        entryUser.put(SqlHelper.COLUMN_USER_PASSWORD, userDto.getPassword());
        entryUser.put(SqlHelper.COLUMN_USER_NAME, userDto.getName());
        entryUser.put(SqlHelper.COLUMN_USER_SURNAME, userDto.getSurname());


        Uri userUri = activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_USER, entryUser);

        return userUri.toString();


    }

    public static String newReportEntry(Activity activity, ReportDto reportDto) {
        ContentValues entryReport = new ContentValues();
        entryReport.put(SqlHelper.COLUMN_REPORT_MYSQLID, reportDto.getId());
        entryReport.put(SqlHelper.COLUMN_REPORT_DATE, reportDto.getDate().toString());

        Uri reportUri = activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_REPORT, entryReport);

        return reportUri.toString();
    }

    public static String newReportItemEntry(Activity activity, ReportItemDto reportItemDto) {

        ContentValues entryReportItem = new ContentValues();
        entryReportItem.put(SqlHelper.COLUMN_REPORT_ITEM_MYSQLID, reportItemDto.getId());
        entryReportItem.put(SqlHelper.COLUMN_REPORT_ITEM_FAULT_NAME, reportItemDto.getFaultName());
        entryReportItem.put(SqlHelper.COLUMN_REPORT_ITEM_DETAILS, reportItemDto.getDetails()); //TODO fali slika

        Uri reportItemUri = activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_REPORT_ITEM, entryReportItem);
        return reportItemUri.toString();

    }

    public static String newReportReportItemEntry(Activity activity, ReportItemDto reportItemDto, ReportDto reportDto, String reportId, String reportItemId) {

        ContentValues entryReportReportItem = new ContentValues();
        entryReportReportItem.put(SqlHelper.COLUMN_REPORT_REPORT_ITEM_REPORT_ID, reportId);
        entryReportReportItem.put(SqlHelper.COLUMN_REPORT_REPORT_ITEM_REPORT_MYSQLIDID, reportDto.getId());
        entryReportReportItem.put(SqlHelper.COLUMN_REPORT_REPORT_ITEM_REPOR_ITEM_ID, reportItemId);
        entryReportReportItem.put(SqlHelper.COLUMN_REPORT_REPORT_ITEM_REPORT_ITEM_MYSQLID, reportItemDto.getId());

        Uri reportReportItemUri = activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_JOIN_TABLE, entryReportReportItem);
        return reportReportItemUri.toString();

    }


}
