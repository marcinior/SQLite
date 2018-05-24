package com.example.marcin.sqlite;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.net.URI;

/**
 * Created by marcin on 24.05.18.
 */

public class MyContentProvider extends ContentProvider {
    private DatabaseHelper databaseHelper;
    private static final String AUTHORITY = "com.example.marcin.sqlite.MyContentProvider";
    public static final Uri URI_SMARTPHONE_TABLE = Uri.parse("content://" + AUTHORITY + "/" + DatabaseHelper.SMARTPHONE_TABLE);
    private static final int WHOLE_TABLE = 1;
    private static final int CHOSEN_RAW = 2;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY,DatabaseHelper.SMARTPHONE_TABLE,WHOLE_TABLE);
        uriMatcher.addURI(AUTHORITY,DatabaseHelper.SMARTPHONE_TABLE + "/#", CHOSEN_RAW);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase smartphoneDatabase = databaseHelper.getReadableDatabase();
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase smartphoneDatabase = databaseHelper.getWritableDatabase();
        long recordId = 0;
        switch (uriType){
            case WHOLE_TABLE:
                smartphoneDatabase.insert(DatabaseHelper.DATABASE_NAME,null,contentValues);
                smartphoneDatabase.close();
                break;
                default:
                    throw new IllegalArgumentException("Nieprawidłowe Uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return Uri.parse(DatabaseHelper.SMARTPHONE_TABLE + "/" + recordId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
