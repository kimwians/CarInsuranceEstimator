package com.example.carinsuranceapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ClientDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "carinsurance.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_CLIENT = "create table client (_id integer primary key autoincrement, " +
            "clientname text not null, age text, gender text, marriagestatus text, " +
            "streetaddress text, city text, state text, zipcode text, " +
            "carvalue text, monthlyrate text);";

    public ClientDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CLIENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ClientDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to " + newVersion);
        /*
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS client");
            onCreate(db);
        }
         */
    }
}
