package com.example.marcin.sqlite;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AndroidException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewPhoneActivity extends AppCompatActivity {
    static long ADD_MODE = 0;
    static long EDIT_MODE = 1;
    EditText producerEditText;
    EditText modelEditText;
    EditText androidVersionEditText;
    EditText wwwEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_phone);
        Button wwwButton = (Button)findViewById(R.id.wwwButton);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        Button saveButton = (Button)findViewById(R.id.saveButton);
        producerEditText = (EditText)findViewById(R.id.producerEditText);
        modelEditText = (EditText)findViewById(R.id.modelEditText);
        androidVersionEditText = (EditText)findViewById(R.id.androidVersionEditText);
        wwwEditText = (EditText)findViewById(R.id.wwwEditText);
        final Intent backIntend = new Intent(this, PhoneListActivity.class);
        final DatabaseHelper databaseHelper = new DatabaseHelper(this);
        final int mode = ((Bundle)getIntent().getExtras()).getInt("mode");
        final long id = ((Bundle)getIntent().getExtras()).getLong("id");

        if(mode == EDIT_MODE){
            fillTheFields(id);
        }


        wwwButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = ((EditText)findViewById(R.id.wwwEditText)).getText().toString();

                if(address.startsWith("www")){
                    Intent browserIntend = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + address));
                    startActivity(browserIntend);
                }else{
                    Toast.makeText(AddNewPhoneActivity.this,"Podano nieprawidłowy adres strony!",Toast.LENGTH_LONG).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED,backIntend);
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String producerText = producerEditText.getText().toString();
                String modelText = modelEditText.getText().toString();
                String androidVersionText = androidVersionEditText.getText().toString();
                String wwwText = wwwEditText.getText().toString();

                if(checkFieldsCorrectness(producerText,modelText,androidVersionText,wwwText)){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseHelper.PRODUCER,producerText);
                    contentValues.put(DatabaseHelper.MODEL,modelText);
                    contentValues.put(DatabaseHelper.ANDROID_VERSION,androidVersionText);
                    contentValues.put(DatabaseHelper.WWW,wwwText);

                    if(mode == ADD_MODE){
                        Uri newUri = getContentResolver().insert(MyContentProvider.URI_SMARTPHONE_TABLE,contentValues);
                        ADD_MODE = Integer.parseInt(newUri.getLastPathSegment());
                        Toast.makeText(AddNewPhoneActivity.this,"Dodano do bazy!",Toast.LENGTH_LONG).show();
                    }else{
                        getContentResolver().update(ContentUris.withAppendedId(MyContentProvider.URI_SMARTPHONE_TABLE,id),contentValues,null,null);
                        Toast.makeText(AddNewPhoneActivity.this,"Zmodyfikowano wpis",Toast.LENGTH_LONG).show();
                    }

                    setResult(RESULT_OK,backIntend);
                    finish();
                }

            }
        });
    }

    private void fillTheFields(long id){
        String[] projection = {DatabaseHelper.PRODUCER,DatabaseHelper.MODEL,DatabaseHelper.ANDROID_VERSION,DatabaseHelper.WWW};
        Cursor cursor = getContentResolver().query(ContentUris.withAppendedId(MyContentProvider.URI_SMARTPHONE_TABLE,id),projection,null,null,null);
        cursor.moveToFirst();
            producerEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.PRODUCER)));
            modelEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.MODEL)));
            androidVersionEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ANDROID_VERSION)));
            wwwEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.WWW)));
            cursor.close();
    }
    private boolean checkFieldsCorrectness(String producerText,String modelText,String androidVersionText,String wwwText){

        boolean correctnessState = true;
        String message = "";

        if(producerText.isEmpty() || modelText.isEmpty() || androidVersionText.isEmpty() || wwwText.isEmpty()){
            Toast.makeText(this,"Żadne pole nie może pozostać puste!",Toast.LENGTH_LONG).show();
        }else {
                if(!producerText.matches("[A-Za-z]{2,25}")){
                    message += " producent ";
                }

                if(!modelText.matches("[A-Za-z0-9 ]{1,20}")){
                    message += " model ";
                }

                if(!androidVersionText.matches("([0-9]\\.[0-9]){1}(\\.[0-9]){0,1}")){
                    message += " wersja androida ";
                }

                if(!wwwText.startsWith("www.")){
                message += " adres www ";
                }
            Toast.makeText(this,"Uzupełnij poprawnie pole(a): " + message,Toast.LENGTH_LONG).show();
        }

        if(message != ""){
            correctnessState = false;
        }

        return correctnessState;
    }

}
