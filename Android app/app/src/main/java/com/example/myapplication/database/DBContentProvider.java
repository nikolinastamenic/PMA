package com.example.myapplication.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.Nullable;


public class DBContentProvider extends ContentProvider {

    private SqlHelper database;

    private static final int TASK = 10;
    private static final int ADDRESS = 27;

    private static final int TASK_ID = 20;
    private static final int ADDRESS_ID = 30;

    private static final int BUILDING = 22;
    private static final int BUILDING_ID = 39;

    private static final int APARTMENT = 25;
    private static final int APARTMENT_ID = 90;

    private static final int USER = 11;
    private static final int USER_ID = 18;

    private static final int REPORT = 55;
    private static final int REPORT_ID = 1;


    private static final int REPORT_ITEM = 2;
    private static final int REPORT_ITEM_ID = 3;

    private static final int REPORT_REPORT_ITEM = 88;
    private static final int REPORT_REPORT_ITEM_ID = 3;

    private static final String AUTHORITY = "com.example.myapplication";

    private static final String TASK_PATH = "task";
    private static final String ADDRESS_PATH = "address";
    private static final String BUILDING_PATH = "building";
    private static final String APARTMENT_PATH = "apartment";
    private static final String USER_PATH = "user";
    private static final String REPORT_PATH = "report";
    private static final String REPORT_ITEM_PATH = "report_item";
    private static final String REPORT_REPORT_ITEM_PATH = "report_report_item";


    public static final Uri CONTENT_URI_TASK = Uri.parse("content://" + AUTHORITY + "/" + TASK_PATH);
    public static final Uri CONTENT_URI_ADDRESS = Uri.parse("content://" + AUTHORITY + "/" + ADDRESS_PATH);
    public static final Uri CONTENT_URI_BUILDING = Uri.parse("content://" + AUTHORITY + "/" + BUILDING_PATH);
    public static final Uri CONTENT_URI_APARTMENT = Uri.parse("content://" + AUTHORITY + "/" + APARTMENT_PATH);
    public static final Uri CONTENT_URI_USER = Uri.parse("content://" + AUTHORITY + "/" + USER_PATH);
    public static final Uri CONTENT_URI_REPORT = Uri.parse("content://" + AUTHORITY + "/" + REPORT_PATH);
    public static final Uri CONTENT_URI_REPORT_ITEM = Uri.parse("content://" + AUTHORITY + "/" + REPORT_ITEM_PATH);
    public static final Uri CONTENT_URI_JOIN_TABLE = Uri.parse("content://" + AUTHORITY + "/" + REPORT_REPORT_ITEM_PATH);


    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, TASK_PATH, TASK);
        sURIMatcher.addURI(AUTHORITY, TASK_PATH + "/#", TASK_ID);

        sURIMatcher.addURI(AUTHORITY, ADDRESS_PATH, ADDRESS);
        sURIMatcher.addURI(AUTHORITY, ADDRESS_PATH + "/#", ADDRESS_ID);

        sURIMatcher.addURI(AUTHORITY, BUILDING_PATH, BUILDING);
        sURIMatcher.addURI(AUTHORITY, BUILDING_PATH + "/#", BUILDING_ID);

        sURIMatcher.addURI(AUTHORITY, APARTMENT_PATH, APARTMENT);
        sURIMatcher.addURI(AUTHORITY, APARTMENT_PATH + "/#", APARTMENT_ID);

        sURIMatcher.addURI(AUTHORITY, USER_PATH, USER);
        sURIMatcher.addURI(AUTHORITY, USER_PATH + "/#", USER_ID);

        sURIMatcher.addURI(AUTHORITY, REPORT_PATH, REPORT);
        sURIMatcher.addURI(AUTHORITY, REPORT_PATH + "/#", REPORT_ID);

        sURIMatcher.addURI(AUTHORITY, REPORT_ITEM_PATH, REPORT_ITEM);
        sURIMatcher.addURI(AUTHORITY, REPORT_ITEM_PATH + "/#", REPORT_ITEM_ID);

        sURIMatcher.addURI(AUTHORITY, REPORT_REPORT_ITEM_PATH, REPORT_REPORT_ITEM);
        sURIMatcher.addURI(AUTHORITY, REPORT_REPORT_ITEM_PATH + "/#", REPORT_REPORT_ITEM_ID);
    }

    @Override
    public boolean onCreate() {
        database = new SqlHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exist
        //checkColumns(projection);
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case TASK_ID:
                // Adding the ID to the original query
                queryBuilder.appendWhere(SqlHelper.COLUMN_TASK_ID + "="
                        + uri.getLastPathSegment());
                //$FALL-THROUGH$
            case TASK:
                // Set the table
                queryBuilder.setTables(SqlHelper.TABLE_TASK);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri retVal = null;
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case TASK:
                id = sqlDB.insert(SqlHelper.TABLE_TASK, null, values);
                retVal = Uri.parse(TASK_PATH + "/" + id);
                break;
            case ADDRESS:
                id = sqlDB.insert(SqlHelper.TABLE_ADDRESS, null, values);
                retVal = Uri.parse(ADDRESS_PATH + "/" + id);
                break;
            case BUILDING:
                id = sqlDB.insert(SqlHelper.TABLE_BUILDING, null, values);
                retVal = Uri.parse(BUILDING_PATH + "/" + id);
                break;
            case APARTMENT:
                id = sqlDB.insert(SqlHelper.TABLE_APARTMENT, null, values);
                retVal = Uri.parse(APARTMENT_PATH + "/" + id);
                break;
            case USER:
                id = sqlDB.insert(SqlHelper.TABLE_USER, null, values);
                retVal = Uri.parse(USER_PATH + "/" + id);
                break;
            case REPORT_REPORT_ITEM:
                id = sqlDB.insert(SqlHelper.JOIN_TABLE, null, values);
                retVal = Uri.parse(REPORT_REPORT_ITEM_PATH + "/" + id);
                break;
            case REPORT:
                id = sqlDB.insert(SqlHelper.TABLE_REPORT, null, values);
                retVal = Uri.parse(REPORT_PATH + "/" + id);
                break;
            case REPORT_ITEM:
                id = sqlDB.insert(SqlHelper.TABLE_REPORT_ITEM, null, values);
                retVal = Uri.parse(REPORT_ITEM_PATH + "/" + id);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retVal;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        int rowsDeleted = 0;
        switch (uriType) {
            case TASK:
                rowsDeleted = sqlDB.delete(SqlHelper.TABLE_TASK,
                        selection,
                        selectionArgs);
                break;
            case TASK_ID:
                String idTask = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(SqlHelper.TABLE_TASK,
                            SqlHelper.COLUMN_TASK_ID + "=" + idTask,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(SqlHelper.TABLE_TASK,
                            SqlHelper.COLUMN_TASK_ID + "=" + idTask
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        int rowsUpdated = 0;
        switch (uriType) {
            case USER:
                rowsUpdated = sqlDB.update(SqlHelper.TABLE_USER,
                        values,
                        selection,
                        selectionArgs);
                break;
            case TASK:
                rowsUpdated = sqlDB.update(SqlHelper.TABLE_TASK,
                        values,
                        selection,
                        selectionArgs);
                break;
            case TASK_ID:
                String idTask = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(SqlHelper.TABLE_TASK,
                            values,
                            SqlHelper.COLUMN_TASK_ID + "=" + idTask,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(SqlHelper.TABLE_TASK,
                            values,
                            SqlHelper.COLUMN_TASK_ID + "=" + idTask
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }


}
