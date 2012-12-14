package com.example.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EnvironmentTrackerOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_TABLE_NAME = "Observation";
    private static final String DICTIONARY_TABLE_CREATE =
    		"CREATE TABLE " + DATABASE_TABLE_NAME + " (" +
    		"OBSERVATION_ID AUTONUMBER, " +
            "MOOD INTEGER, " +
    		"DAY INTEGER, " +
    		"HUE_CATEGORY INTEGER, " +
            "SATURATION SINGLE, " +
    		"BRIGHTNESS SINGLE," +
            "DECIBEL SINGLE" +
    		");";
	private static final String DATABASE_NAME = "EnvironmentDatabase";

    public EnvironmentTrackerOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DICTIONARY_TABLE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
