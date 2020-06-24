package com.example.myapplication.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SqlHelper extends SQLiteOpenHelper {


    public static final String TABLE_ADDRESS = "address";
    public static final String TABLE_BUILDING = "building";
    public static final String TABLE_APARTMENT = "apartment";
    public static final String TABLE_REPORT_ITEM = "report_item";
    public static final String TABLE_REPORT = "report";
    public static final String JOIN_TABLE = "report_report_item";
    public static final String TABLE_TASK = "task";
    public static final String TABLE_USER = "user";


    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_MYSQLID = "mysql_id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_SURNAME = "surname";
    public static final String COLUMN_USER_PHONE_NUMBER = "phoneNumber";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_PICTURE = "picture";


    public static final String COLUMN_ADDRESS_ID = "id";
    public static final String COLUMN_ADDRESS_MYSQLID = "mysql_id";
    public static final String COLUMN_ADDRESS_COUNTRY = "country";
    public static final String COLUMN_ADDRESS_CITY = "city";
    public static final String COLUMN_ADDRESS_STREET = "street";
    public static final String COLUMN_ADDRESS_NUMBER = "number";
    public static final String COLUMN_ADDRESS_LONGITUDE = "longitude";
    public static final String COLUMN_ADDRESS_LATITUDE = "latitude";
    public static final String COLUMN_ADDRESS_BUILDING_ID = "building_id";


    public static final String COLUMN_BUILDING_ID = "id";
    public static final String COLUMN_BUILDING_MYSQLID = "mysql_id";
    public static final String COLUMN_BUILDING_ADDRESS_ID = "address_id";

    public static final String COLUMN_APARTMENT_ID = "id";
    public static final String COLUMN_APARTMENT_MYSQLID = "mysql_id";
    public static final String COLUMN_APARTMENT_NUMBER = "number";
    public static final String COLUMN_APARTMENT_BUILDING_ID = "building_id";


    public static final String COLUMN_REPORT_ITEM_ID = "id";
    public static final String COLUMN_REPORT_ITEM_MYSQLID = "mysql_id";
    public static final String COLUMN_REPORT_ITEM_FAULT_NAME = "faultName";
    public static final String COLUMN_REPORT_ITEM_DETAILS = "details";
    public static final String COLUMN_REPORT_ITEM_FAULT_PICTURE = "picture";
    public static final String COLUMN_REPORT_ITEM_IS_SYNCHRONIZED = "is_synchronized";


    public static final String COLUMN_REPORT_ID = "id";
    public static final String COLUMN_REPORT_MYSQLID = "mysql_id";
    public static final String COLUMN_REPORT_DATE = "date";
    public static final String COLUMN_REPORT_TASK_ID = "task_id";
    public static final String COLUMN_REPORT_IS_SYNCHRONIZED = "is_synchronized";


    public static final String COLUMN_REPORT_REPORT_ITEM_REPORT_ID = "report_id";
    public static final String COLUMN_REPORT_REPORT_ITEM_REPORT_MYSQLIDID = "mysql_report_id";
    public static final String COLUMN_REPORT_REPORT_ITEM_REPOR_ITEM_ID = "report_item_id";
    public static final String COLUMN_REPORT_REPORT_ITEM_REPORT_ITEM_MYSQLID = "mysql_report_item_id";


    public static final String COLUMN_TASK_ID = "id";
    public static final String COLUMN_TASK_MYSQLID= "mysql_id";
    public static final String COLUMN_TASK_TYPE_OF_APARTMENT = "type_of_apartment";
    public static final String COLUMN_TASK_STATE = "state";
    public static final String COLUMN_TASK_URGENT = "urgent";
    public static final String COLUMN_TASK_DEADLINE = "deadline";
    public static final String COLUMN_TASK_IS_SYNCHRONIZED = "is_synchronized";
    public static final String COLUMN_TASK_APARTMENT_ID = "apartment_id";
    public static final String COLUMN_TASK_USER_ID = "user_id";
    public static final String COLUMN_TASK_REPORT_ID = "report_id";



    private static final String DATABASE_NAME = "pma.db";
    private static final int DATABASE_VERSION = 1;


    private static final String TABLE_USER_CREATE = "create table "
            + TABLE_USER + "("
            + COLUMN_USER_ID + " integer primary key autoincrement , "
            + COLUMN_USER_MYSQLID + " integer, "
            + COLUMN_USER_NAME + " text, "
            + COLUMN_USER_SURNAME + " text, "
            + COLUMN_USER_PHONE_NUMBER + " text, "
            + COLUMN_USER_EMAIL + " text, "
            + COLUMN_USER_PASSWORD + " text, "
            + COLUMN_USER_PICTURE + " text"
            + ")";

    private static final String TABLE_ADDRESS_CREATE = "create table "
            + TABLE_ADDRESS + "("
            + COLUMN_ADDRESS_ID + " integer primary key autoincrement , "
            + COLUMN_ADDRESS_MYSQLID + " integer, "
            + COLUMN_ADDRESS_COUNTRY + " text, "
            + COLUMN_ADDRESS_CITY + " text, "
            + COLUMN_ADDRESS_STREET + " text, "
            + COLUMN_ADDRESS_NUMBER + " text, "
            + COLUMN_ADDRESS_LONGITUDE + " double, "
            + COLUMN_ADDRESS_LATITUDE + " double, "
            + COLUMN_ADDRESS_BUILDING_ID + " integer, "
            + "FOREIGN KEY(building_id) REFERENCES building(id)"
            + ")";

    private static final String TABLE_BUILDING_CREATE = "create table "
            + TABLE_BUILDING + "("
            + COLUMN_BUILDING_ID + " integer primary key autoincrement , "
            + COLUMN_BUILDING_MYSQLID + " integer, "
            + COLUMN_BUILDING_ADDRESS_ID + " integer, "
            + "FOREIGN KEY(address_id) REFERENCES address(id)"
            + ")";

    private static final String TABLE_APARTMENT_CREATE = "create table "
            + TABLE_APARTMENT + "("
            + COLUMN_APARTMENT_ID + " integer primary key autoincrement , "
            + COLUMN_APARTMENT_MYSQLID + " integer, "
            + COLUMN_APARTMENT_NUMBER + " integer, "
            + COLUMN_APARTMENT_BUILDING_ID + " integer, "
            + "FOREIGN KEY(building_id) REFERENCES building(id)"
            + ")";

    private static final String TABLE_REPORT_ITEM_CREATE = "create table "
            + TABLE_REPORT_ITEM + "("
            + COLUMN_REPORT_ITEM_ID + " integer primary key autoincrement , "
            + COLUMN_REPORT_ITEM_MYSQLID + " integer, "
            + COLUMN_REPORT_ITEM_FAULT_NAME + " text, "
            + COLUMN_REPORT_ITEM_DETAILS + " text, "
            + COLUMN_REPORT_ITEM_FAULT_PICTURE + " text, "
            + COLUMN_REPORT_ITEM_IS_SYNCHRONIZED + " integer"
            + ")";

    private static final String TABLE_REPORT_CREATE = "create table "
            + TABLE_REPORT + "("
            + COLUMN_REPORT_ID + " integer primary key autoincrement , "
            + COLUMN_REPORT_MYSQLID + " integer, "
            + COLUMN_REPORT_DATE + " date, "
            + COLUMN_REPORT_TASK_ID + " integer, "
            + COLUMN_REPORT_IS_SYNCHRONIZED + " integer, "
            + "FOREIGN KEY(task_id) REFERENCES task(id)"
            + ")";

    private static final String TABLE_REPORT_REPORT_ITEM_CREATE = "create table "
            + JOIN_TABLE + "("
            + COLUMN_REPORT_REPORT_ITEM_REPORT_ID + " integer, "
            + COLUMN_REPORT_REPORT_ITEM_REPORT_MYSQLIDID + " integer, "
            + COLUMN_REPORT_REPORT_ITEM_REPOR_ITEM_ID + " integer, "
            + COLUMN_REPORT_REPORT_ITEM_REPORT_ITEM_MYSQLID + " integer, "
            + "FOREIGN KEY(report_id) REFERENCES report(id),"
            + "FOREIGN KEY(report_item_id) REFERENCES report_item(id)"
            + ")";

    private static final String TABLE_TASK_CREATE = "create table "
            + TABLE_TASK + "("
            + COLUMN_TASK_ID + " integer primary key autoincrement , "  //0
            + COLUMN_TASK_MYSQLID + " integer, "                        //1 ...
            + COLUMN_TASK_TYPE_OF_APARTMENT + " text, "
            + COLUMN_TASK_STATE + " text, "
            + COLUMN_TASK_URGENT + " boolean, "
            + COLUMN_TASK_DEADLINE + " date, "
            +  COLUMN_TASK_APARTMENT_ID+ " integer, "
            + COLUMN_TASK_REPORT_ID + " integer, "
            + COLUMN_TASK_USER_ID + " integer, "
            + COLUMN_TASK_IS_SYNCHRONIZED + " integer, "
            + "FOREIGN KEY(apartment_id) REFERENCES apartment(id), "
            + "FOREIGN KEY(report_id) REFERENCES report(id), "
            + "FOREIGN KEY(user_id) REFERENCES user(id)"
            + ")";


    public SqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_ADDRESS_CREATE);
        db.execSQL(TABLE_USER_CREATE);
        db.execSQL(TABLE_BUILDING_CREATE);
        db.execSQL(TABLE_APARTMENT_CREATE);
        db.execSQL(TABLE_REPORT_ITEM_CREATE);
        db.execSQL(TABLE_REPORT_CREATE);
        db.execSQL(TABLE_REPORT_REPORT_ITEM_CREATE);
        db.execSQL(TABLE_TASK_CREATE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUILDING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APARTMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORT_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORT);
        db.execSQL("DROP TABLE IF EXISTS " + JOIN_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);

        onCreate(db);
    }

    public void dropBuildingTable(){
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_BUILDING);
        sqlDB.execSQL(TABLE_BUILDING_CREATE);
    }

    public void dropApartmentTable(){
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_APARTMENT);
        sqlDB.execSQL(TABLE_APARTMENT_CREATE);
    }

    public void dropAddressTable(){
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS);
        sqlDB.execSQL(TABLE_ADDRESS_CREATE);
    }

    public void dropTaskTable(){
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORT);
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORT_ITEM);
        sqlDB.execSQL("DROP TABLE IF EXISTS " + JOIN_TABLE);
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_BUILDING);
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_APARTMENT);

        sqlDB.execSQL(TABLE_TASK_CREATE);
        sqlDB.execSQL(TABLE_REPORT_CREATE);
        sqlDB.execSQL(TABLE_REPORT_ITEM_CREATE);
        sqlDB.execSQL(TABLE_REPORT_REPORT_ITEM_CREATE);
        sqlDB.execSQL(TABLE_BUILDING_CREATE);
        sqlDB.execSQL(TABLE_APARTMENT_CREATE);


//        this.onUpgrade(sqlDB, sqlDB.getVersion(), sqlDB.getVersion() + 1);

    }

    public void dropReportTable(){
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORT);
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORT_ITEM);
        sqlDB.execSQL("DROP TABLE IF EXISTS " + JOIN_TABLE);

        sqlDB.execSQL(TABLE_REPORT_CREATE);
        sqlDB.execSQL(TABLE_REPORT_ITEM_CREATE);
        sqlDB.execSQL(TABLE_REPORT_REPORT_ITEM_CREATE);

    }

    public void dropUserTable(){
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        sqlDB.execSQL(TABLE_USER_CREATE);

    }

    public Cursor getAllTasks() {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        Cursor data = sqlDB.rawQuery("SELECT * FROM " + TABLE_TASK + " WHERE state = 'NEW' ", null);

        return data;

    }

    public Cursor getTaskById (String id) {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        Cursor data = sqlDB.rawQuery("SELECT * FROM " + TABLE_TASK + " WHERE id " + "= " + id, null);

        return data;
    }

    public Cursor getApartmentByMySqlId (String id) {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        Cursor data = sqlDB.rawQuery("SELECT * FROM " + TABLE_APARTMENT + " WHERE mysql_id " + "= " + id, null);

        return data;
    }

    public Cursor getApartmentById (String id) {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        Cursor data = sqlDB.rawQuery("SELECT * FROM " + TABLE_APARTMENT + " WHERE id " + "= " + id, null);

        return data;
    }


    public Cursor getBuildingById (String id) {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        Cursor data = sqlDB.rawQuery("SELECT * FROM " + TABLE_BUILDING + " WHERE id " + "= " + id, null);

        return data;
    }

    public Cursor getBuildingByMySqlId (String id) {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        Cursor data = sqlDB.rawQuery("SELECT * FROM " + TABLE_BUILDING + " WHERE mysql_id " + "= " + id, null);

        return data;
    }

    public Cursor getAddressById (String id) {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        Cursor data = sqlDB.rawQuery("SELECT * FROM " + TABLE_ADDRESS + " WHERE id " + "= " + id, null);

        return data;
    }

    public Cursor getAddressByMySqlId (String id) {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        Cursor data = sqlDB.rawQuery("SELECT * FROM " + TABLE_ADDRESS + " WHERE mysql_id " + "= " + id, null);

        return data;
    }

    public Cursor getTasksInProcess() {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        Cursor data = sqlDB.rawQuery("SELECT * FROM " + TABLE_TASK + " WHERE state = 'IN_PROCESS' ", null);

        return data;

    }

    public Cursor getFinishedTasks() {
        SQLiteDatabase sqlDB = this.getWritableDatabase();

        Cursor data = sqlDB.rawQuery("SELECT * FROM " + TABLE_TASK + " WHERE state = 'FINISHED' ", null);

        return data;
    }

    public Cursor getUserById (String id) {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        Cursor data = sqlDB.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE id " + "= " + id, null);

        return data;
    }

    public Cursor getUserByEmail (String email) {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        Cursor data = sqlDB.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE email " + "=? ", new String[]{email});

        return data;
    }

    public Cursor getUserByMySqlId (String id) {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        Cursor data = sqlDB.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE mysql_id " + "= " + id, null);

        return data;
    }

    public Cursor getTaskByMySqlId (String id) {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        Cursor data = sqlDB.rawQuery("SELECT * FROM " + TABLE_TASK + " WHERE mysql_id " + "= " + id, null);

        return data;
    }

    public Cursor getTasksBySynchronizedAndUserId (int isSynchronized, String id) {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        Cursor data = sqlDB.rawQuery("SELECT * FROM " + TABLE_TASK + " WHERE user_id" + "= " + id + " and is_synchronized" + "= " + isSynchronized, null);

        return data;
    }

    public Cursor setUserPicture (String mySqlId, String pictureName) {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        Cursor data = sqlDB.rawQuery("UPDATE " + TABLE_USER + " SET picture = '" + pictureName + "' WHERE mysql_id = " + mySqlId, null);

        return data;
    }

    public Cursor getReportById (String id) {

        SQLiteDatabase sqlDB = this.getWritableDatabase();

        Cursor data = sqlDB.rawQuery("SELECT * FROM " + TABLE_REPORT + " WHERE id " + "= " + id, null);
        return data;
    }

    public Cursor getReportItemsByReportId(String id){
        SQLiteDatabase sqlDB = this.getWritableDatabase();

        Cursor data = sqlDB.rawQuery("SELECT * FROM " + JOIN_TABLE + " WHERE report_id" + "= " + id,null);

        return data;
    }

    public Cursor getReportItemById (String id) {

        SQLiteDatabase sqlDB = this.getWritableDatabase();

        Cursor data = sqlDB.rawQuery("SELECT * FROM " + TABLE_REPORT_ITEM + " WHERE id " + "= " + id, null);
        return data;
    }


}
