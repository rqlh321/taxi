package com.example.taxidriverapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

    public static final String DATABASE_TABLE = "activity";
    public static final String CONTENT_START_TIME = "startTime";
    public static final String CONTENT_END_TIME = "endTime";
    public static final String CONTENT_PAYMENT = "payment";
    public static final String CONTENT_LAT_FROM = "latFrom";
    public static final String CONTENT_LON_FROM = "lonFrom";
    public static final String CONTENT_LAT_TO = "latTo";
    public static final String CONTENT_LON_TO = "lonTo";
    public static final String CONTENT_IS_IN_LIST = "isInGlobalDb";
    public static final String CONTENT_IS_ADDRESS_FOUND = "isAddressFound";
    public static final String CONTENT_ADDRESS = "address";
    private static final String DATABASE_NAME = "localTaxi.db";

    private static final String DATABASE_CREATE_SCRIPT = "create table "
            + DATABASE_TABLE + "(" + BaseColumns._ID + " integer primary key autoincrement, "
            + CONTENT_IS_IN_LIST + " integer default 0, "
            + CONTENT_IS_ADDRESS_FOUND + " integer default 0, "
            + CONTENT_ADDRESS + " text not null,"
            + CONTENT_START_TIME + " text not null,"
            + CONTENT_PAYMENT + " text not null,"
            + CONTENT_LAT_FROM + " double not null,"
            + CONTENT_LON_FROM + " double not null,"
            + CONTENT_LAT_TO + " double not null,"
            + CONTENT_LON_TO + " double not null,"
            + CONTENT_END_TIME + " text not null"
            +");";

    // версия базы данных
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
        onCreate(sqLiteDatabase);
    }

}