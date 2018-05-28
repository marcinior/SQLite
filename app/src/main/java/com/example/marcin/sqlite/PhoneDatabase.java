package com.example.marcin.sqlite;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class PhoneDatabase extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter cursorAdapter;
    private ListView smartphonesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_database);
        smartphonesList = (ListView) findViewById(R.id.smartphonesList);
        launchLoader();
    }

    private void launchLoader(){
        getLoaderManager().initLoader(0,null,this);
        String[] mapFrom = new String[] {DatabaseHelper.ID,DatabaseHelper.PRODUCER,DatabaseHelper.MODEL};
        int[] mapTo = new int[] {R.id.producerTextView,R.id.modelTextView};
        cursorAdapter = new SimpleCursorAdapter(this,R.id.smartphonesList,null,mapFrom,mapTo);
        smartphonesList.setAdapter(cursorAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = new String[] {DatabaseHelper.ID,DatabaseHelper.PRODUCER,DatabaseHelper.MODEL};
        CursorLoader cursorLoader = new CursorLoader(this,MyContentProvider.URI_SMARTPHONE_TABLE,projection,null,null,null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_action, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.menu.add_action){
            addPhoneActivityLaucher();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addPhoneActivityLaucher(){

    }


}
