package com.example.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.example.myapplication.DTO.AllTaskDto;
import com.example.myapplication.DTO.ApartmentDto;
import com.example.myapplication.DTO.BuildingDto;
import com.example.myapplication.DTO.ReportDto;
import com.example.myapplication.DTO.ReportItemDto;
import com.example.myapplication.DTO.UserDto;


public class NewEntry {

    public static String newAddressEntry(Context context, BuildingDto buildingDto) {
        ContentValues entryAddress = new ContentValues();


        entryAddress.put(SqlHelper.COLUMN_ADDRESS_MYSQLID, buildingDto.getAddress().getId());
        entryAddress.put(SqlHelper.COLUMN_ADDRESS_COUNTRY, buildingDto.getAddress().getCountry());
        entryAddress.put(SqlHelper.COLUMN_ADDRESS_CITY, buildingDto.getAddress().getCity());
        entryAddress.put(SqlHelper.COLUMN_ADDRESS_STREET, buildingDto.getAddress().getStreet());
        entryAddress.put(SqlHelper.COLUMN_ADDRESS_NUMBER, buildingDto.getAddress().getNumber());
        entryAddress.put(SqlHelper.COLUMN_ADDRESS_LONGITUDE, buildingDto.getAddress().getLongitude());
        entryAddress.put(SqlHelper.COLUMN_ADDRESS_LATITUDE, buildingDto.getAddress().getLatitude());
        Uri addressUri = context.getContentResolver().insert(DBContentProvider.CONTENT_URI_ADDRESS, entryAddress);
        return addressUri.toString();
    }

    public static String newBuildingEntry(Context context, BuildingDto buildingDto, String addressId) {

        ContentValues entryBuilding = new ContentValues();
        entryBuilding.put(SqlHelper.COLUMN_BUILDING_MYSQLID, buildingDto.getId());
        entryBuilding.put(SqlHelper.COLUMN_BUILDING_ADDRESS_ID, addressId);
        Uri buildingUri = context.getContentResolver().insert(DBContentProvider.CONTENT_URI_BUILDING, entryBuilding);
        return buildingUri.toString();
    }

    public static String newApartmentEntry(Context context, ApartmentDto apartmentDto, String buildingId) {

        ContentValues entryApartment = new ContentValues();

        entryApartment.put(SqlHelper.COLUMN_APARTMENT_MYSQLID, apartmentDto.getId());
        entryApartment.put(SqlHelper.COLUMN_APARTMENT_NUMBER, apartmentDto.getNumber());
        entryApartment.put(SqlHelper.COLUMN_APARTMENT_BUILDING_ID, buildingId);
        Uri apartmentUri = context.getContentResolver().insert(DBContentProvider.CONTENT_URI_APARTMENT, entryApartment);
        return apartmentUri.toString();
    }

    public static String newTaskEntry(Context context, AllTaskDto allTaskDto, String apartmentId, String userId, String reportId) {
        ContentValues entryTask = new ContentValues();

        entryTask.put(SqlHelper.COLUMN_TASK_MYSQLID, allTaskDto.getId());
        entryTask.put(SqlHelper.COLUMN_TASK_STATE, allTaskDto.getState());
        entryTask.put(SqlHelper.COLUMN_TASK_DEADLINE, allTaskDto.getDeadline().toString());
        entryTask.put(SqlHelper.COLUMN_TASK_TYPE_OF_APARTMENT, allTaskDto.getTypeOfApartment());
        entryTask.put(SqlHelper.COLUMN_TASK_URGENT, allTaskDto.isUrgent());
        entryTask.put(SqlHelper.COLUMN_TASK_APARTMENT_ID, apartmentId);
        entryTask.put(SqlHelper.COLUMN_TASK_IS_SYNCHRONIZED, 2);     //default vrednost, tek je stigao task, 0 nije sinh, 1 jeste sinh
        if (!userId.equals("")) {
            entryTask.put(SqlHelper.COLUMN_TASK_USER_ID, userId);

        }
        if (!reportId.equals("")) {
            entryTask.put(SqlHelper.COLUMN_TASK_REPORT_ID, reportId);
        }
        Uri taskUri = context.getContentResolver().insert(DBContentProvider.CONTENT_URI_TASK, entryTask);

        return taskUri.toString();


    }


    public static String newUserEntry(Context context, UserDto userDto) {

        ContentValues entryUser = new ContentValues();
        entryUser.put(SqlHelper.COLUMN_USER_MYSQLID, userDto.getId());
        entryUser.put(SqlHelper.COLUMN_USER_EMAIL, userDto.getEmail());
//        entryUser.put(SqlHelper.COLUMN_USER_PASSWORD, userDto.getPassword());
        entryUser.put(SqlHelper.COLUMN_USER_NAME, userDto.getName());
        entryUser.put(SqlHelper.COLUMN_USER_SURNAME, userDto.getSurname());
        entryUser.put(SqlHelper.COLUMN_USER_PICTURE, userDto.getPictureName());

        Uri userUri = context.getContentResolver().insert(DBContentProvider.CONTENT_URI_USER, entryUser);

        return userUri.toString();


    }

    public static String newReportEntry(Context context, ReportDto reportDto) {
        ContentValues entryReport = new ContentValues();
        entryReport.put(SqlHelper.COLUMN_REPORT_MYSQLID, reportDto.getId());
        entryReport.put(SqlHelper.COLUMN_REPORT_DATE, reportDto.getDate().toString());

        Uri reportUri = context.getContentResolver().insert(DBContentProvider.CONTENT_URI_REPORT, entryReport);

        return reportUri.toString();
    }

    public static String newReportItemEntry(Context context, ReportItemDto reportItemDto) {

        ContentValues entryReportItem = new ContentValues();
        entryReportItem.put(SqlHelper.COLUMN_REPORT_ITEM_MYSQLID, reportItemDto.getId());
        entryReportItem.put(SqlHelper.COLUMN_REPORT_ITEM_FAULT_NAME, reportItemDto.getFaultName());
        entryReportItem.put(SqlHelper.COLUMN_REPORT_ITEM_DETAILS, reportItemDto.getDetails()); //TODO fali slika
        entryReportItem.put(SqlHelper.COLUMN_REPORT_ITEM_FAULT_PICTURE, reportItemDto.getPicture());

        Uri reportItemUri = context.getContentResolver().insert(DBContentProvider.CONTENT_URI_REPORT_ITEM, entryReportItem);
        return reportItemUri.toString();

    }

    public static String newReportReportItemEntry(Context context, ReportItemDto reportItemDto, ReportDto reportDto, String reportId, String reportItemId) {

        ContentValues entryReportReportItem = new ContentValues();
        entryReportReportItem.put(SqlHelper.COLUMN_REPORT_REPORT_ITEM_REPORT_ID, reportId);
        entryReportReportItem.put(SqlHelper.COLUMN_REPORT_REPORT_ITEM_REPORT_MYSQLIDID, reportDto.getId());
        entryReportReportItem.put(SqlHelper.COLUMN_REPORT_REPORT_ITEM_REPOR_ITEM_ID, reportItemId);
        entryReportReportItem.put(SqlHelper.COLUMN_REPORT_REPORT_ITEM_REPORT_ITEM_MYSQLID, reportItemDto.getId());

        Uri reportReportItemUri = context.getContentResolver().insert(DBContentProvider.CONTENT_URI_JOIN_TABLE, entryReportReportItem);
        return reportReportItemUri.toString();

    }


}
