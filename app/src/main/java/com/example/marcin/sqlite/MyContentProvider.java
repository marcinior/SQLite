package com.example.marcin.sqlite;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by marcin on 24.05.18.
 */

public class MyContentProvider extends ContentProvider {
    private DatabaseHelper databaseHelper;
    private static final String AUTHORITY = "com.example.marcin.sqlite.MyContentProvider";
    public static final Uri URI_SMARTPHONE_TABLE = Uri.parse("content://" + AUTHORITY + "/" + DatabaseHelper.SMARTPHONE_TABLE);
    private static final int WHOLE_TABLE = 1;
    private static final int CHOSEN_ROW = 2;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY,DatabaseHelper.SMARTPHONE_TABLE,WHOLE_TABLE);
        uriMatcher.addURI(AUTHORITY,DatabaseHelper.SMARTPHONE_TABLE + "/#", CHOSEN_ROW);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase smartphoneDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor = null;

        switch (uriType){
            case WHOLE_TABLE:
                cursor = smartphoneDatabase.query(false,DatabaseHelper.SMARTPHONE_TABLE,projection,selection,selectionArgs,null,null,sortOrder,null,null);
                break;
            case CHOSEN_ROW:
                cursor = smartphoneDatabase.query(false,DatabaseHelper.SMARTPHONE_TABLE,projection,getSelectionWithId(uri,selection),selectionArgs,null,null,sortOrder,null,null);
                break;
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    private String getSelectionWithId(Uri uri,String selection){
        if(selection != null && !selection.equals("")){
            selection += " and " + DatabaseHelper.ID + "=" + uri.getLastPathSegment();
        }else{
            selection = DatabaseHelper.ID + "=" + uri.getLastPathSegment();
        }

        return selection;
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
                smartphoneDatabase.insert(DatabaseHelper.SMARTPHONE_TABLE,null,contentValues);
                smartphoneDatabase.close();
                break;
                default:
                    throw new IllegalArgumentException("Nieprawidłowe Uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return Uri.parse(DatabaseHelper.SMARTPHONE_TABLE + "/" + recordId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase smartphoneDatabase = databaseHelper.getWritableDatabase();
        int numOfDeletedRecords = 0;

        switch (uriType){
            case WHOLE_TABLE:
                numOfDeletedRecords = smartphoneDatabase.delete(DatabaseHelper.SMARTPHONE_TABLE,selection, selectionArgs);
                break;
            case CHOSEN_ROW:
                numOfDeletedRecords = smartphoneDatabase.delete(DatabaseHelper.SMARTPHONE_TABLE,getSelectionWithId(uri,selection),selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Nieprawidłowe uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return numOfDeletedRecords;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase smartphoneDatabase = databaseHelper.getWritableDatabase();
        int numOfUpdatedRecords = 0;

        switch (uriType){
            case WHOLE_TABLE:
                numOfUpdatedRecords = smartphoneDatabase.update(DatabaseHelper.SMARTPHONE_TABLE,contentValues,selection,selectionArgs);
                break;
            case CHOSEN_ROW:
                numOfUpdatedRecords = smartphoneDatabase.update(DatabaseHelper.SMARTPHONE_TABLE,contentValues,getSelectionWithId(uri,selection),selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Nieprawidłowe uri : " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return numOfUpdatedRecords;
    }
}
