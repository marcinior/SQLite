package com.example.marcin.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by marcin on 24.05.18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public final static int DATABASE_VERSION = 1;
    public final static String ID = "id";
    public final static String DATABASE_NAME = "SartphoneDatabase";
    public final static String SMARTPHONE_TABLE = "smartphone";
    public final static String PRODUCER = "producer";
    public final static String MODEL = "model";
    public final static String ANDROID_VERSION = "android";
    public final static String WWW = "www";

    public final static String CREATE_TABLE_SMARTPHONES = "CREATE TABLE " + SMARTPHONE_TABLE +
                                                                "(" + ID + " int not null autoincrement, " +
                                                                PRODUCER + " text not null, " +
                                                                MODEL + " text not null, " +
                                                                ANDROID_VERSION + " text not null" +
                                                                WWW + " text not null);";
    private final static String DROP_TABLE_SMARTPHONE = "DROP TABLE IF EXISTS " + SMARTPHONE_TABLE;

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_SMARTPHONES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(DROP_TABLE_SMARTPHONE);
        onCreate(sqLiteDatabase);
    }
}
