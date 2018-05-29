package com.example.marcin.sqlite;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


public class PhoneListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter cursorAdapter;
    private ListView smartphonesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_database);
        smartphonesList = (ListView) findViewById(R.id.smartphonesList);
        launchLoader();
        smartphonesList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        smartphonesList.setClickable(true);
        smartphonesList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) { }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.context_bar, menu);
                return true;
            }
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        deleteSelectedRows();
                        Toast.makeText(PhoneListActivity.this,"Usunięto wybrane wpisy",Toast.LENGTH_LONG);
                        return true;
                }
                return false;
            }
            @Override
            public void onItemCheckedStateChanged(ActionMode mode,int position, long id,
                                                  boolean checked) { }
        });

        smartphonesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PhoneListActivity.this, AddNewPhoneActivity.class);
                intent.putExtra("id",l);
                intent.putExtra("mode",1);
                startActivityForResult(intent, RESULT_OK);
            }
        });

    }


    private void deleteSelectedRows() {
        long checked[] = smartphonesList.getCheckedItemIds();

        for(int i = 0; i < checked.length; i++){
            getContentResolver().delete(ContentUris.withAppendedId(MyContentProvider.URI_SMARTPHONE_TABLE,checked[i]),null,null);
        }
    }
    private void launchLoader(){
        getLoaderManager().initLoader(0,null,this);
        String[] mapFrom = new String[] {DatabaseHelper.PRODUCER,DatabaseHelper.MODEL};
        int[] mapTo = new int[] {R.id.producerTextView,R.id.modelTextView};
        cursorAdapter = new SimpleCursorAdapter(this,R.layout.list_row,null,mapFrom,mapTo);
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
        if(item.getItemId() == R.id.add){
            addPhoneActivityLaucher();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addPhoneActivityLaucher(){
        Intent intent = new Intent(this,AddNewPhoneActivity.class);
        intent.putExtra("mode",0);
        startActivityForResult(intent,-1);
    }


}
