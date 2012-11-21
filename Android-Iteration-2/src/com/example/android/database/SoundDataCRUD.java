package com.example.android.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.model.SoundData;

/**
 * <p>
 * <code>SoundDataCRUD</code> class.
 * </p>
 * 
 * @author		Liselot De Brabandere, Joris Schelfaut, Ellen Van Molle
 * @version		14 November 2012
 */
public class SoundDataCRUD extends CRUD<SoundData> {
	
	//private static final String TABLE_ENVIRONMENTDATA 	= "EnvironmentData";
	private static final String TABLE_SOUNDDATA 		= "SoundData";
	
	private static final String ID 				= "id";
	private static final String AMPLITUDE 		= "amplitude";
    private static final String FREQUENCY 		= "frequency";
    private static final String ENVIRONMENT_ID	= "environment_id";
    
    private final String SOUNDDATA_TABLE_CREATE =
    		"CREATE TABLE " + TABLE_SOUNDDATA + " (" +
    				ID 				+ " " + INTEGER + " " + PRIMARY_KEY + " " + AUTOINCREMENT + ", " +
					AMPLITUDE 		+ " " + REAL + ", " +
					FREQUENCY 		+ " " + REAL + ", " +
					ENVIRONMENT_ID		+ " " + INTEGER + /*" " +
					FOREIGN_KEY		+ " (" + ENVIRONMENT_ID + ") " + REFERENCES + " " +
									TABLE_ENVIRONMENTDATA + "(" + ID + ")" +*/
			");";
    
	/**
	 * Constructor for the <code>SoundDataCRUD</code> class.
	 * @param databaseHandler the database handler to access the database.
	 */
	public SoundDataCRUD(DatabaseHandler databaseHandler) {
		super(databaseHandler);
	}
	
	@Override
	public long insert(SoundData record) {
		SQLiteDatabase db = this.databaseHandler.getWritableDatabase();
		
        ContentValues values = new ContentValues();
        values.put(AMPLITUDE,		record.getAmplitude());
        values.put(FREQUENCY,		record.getFrequency());
        values.put(ENVIRONMENT_ID, 	record.getEnvironmentID());
 
        // Inserting Row
        long id = db.insert(TABLE_SOUNDDATA, null, values);
        //db.close(); // Closing database connection
		return id;
	}

	@Override
	public SoundData select(long id) {
		SQLiteDatabase db = this.databaseHandler.getReadableDatabase();
	    
	    Cursor cursor = db.query(TABLE_SOUNDDATA,
	    		new String[] { ID, AMPLITUDE, FREQUENCY, ENVIRONMENT_ID },
	    		ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    // Initialize cursor :
	    if (cursor != null)
	        cursor.moveToFirst();
	    
	    // Retrieve data and create SoundData object :
	    SoundData data = new SoundData();
        data.setID(cursor.getLong(0));
        data.setAmplitude(cursor.getDouble(1));
        data.setFrequency(cursor.getDouble(2));
        data.setEnvironmentID(cursor.getLong(3));
        
        // Close cursor :
        cursor.close();
        
	    // return SoundData
	    return data;
	}

	@Override
	public List<SoundData> select() {
		List<SoundData> list = new ArrayList<SoundData>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SOUNDDATA;
 
        SQLiteDatabase db = this.databaseHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	SoundData data = new SoundData();
                data.setID(cursor.getLong(0));
                data.setAmplitude(cursor.getDouble(1));
                data.setFrequency(cursor.getDouble(2));
                data.setEnvironmentID(cursor.getLong(3));
                // Adding SoundData to list
                list.add(data);
            } while (cursor.moveToNext());
        }
        
        // Close cursor :
        cursor.close();
 
        // return SoundData list
        return list;
	}

	@Override
	public boolean update(SoundData record) {
		SQLiteDatabase db = this.databaseHandler.getWritableDatabase();
		
	    ContentValues values = new ContentValues();
	    values.put(AMPLITUDE, record.getAmplitude());
	    values.put(FREQUENCY, record.getFrequency());
	    values.put(ENVIRONMENT_ID, record.getEnvironmentID());
	    
	    // updating row
	    int rowsaffected = db.update(TABLE_SOUNDDATA, values, ID + " = ?",
	            new String[] { String.valueOf(record.getID()) });
	    return rowsaffected > 0;
	}

	@Override
	public boolean delete(long id) {
		SQLiteDatabase db = this.databaseHandler.getWritableDatabase();
	    db.delete(TABLE_SOUNDDATA, ID + " = ?",
	            new String[] { String.valueOf(id) });
	    //db.close();
	    return true;
	}

	@Override
	public boolean createTable(SQLiteDatabase db) {
		db.execSQL(SOUNDDATA_TABLE_CREATE);
		return true;
	}

	@Override
	public boolean dropTable(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOUNDDATA);
		return true;
	}

	@Override
	public int recordCount() {
		String countQuery = "SELECT * FROM " + TABLE_SOUNDDATA;
        SQLiteDatabase db = this.databaseHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        // Close cursor :
        cursor.close();
        // return count :
        return count;
	}

}
