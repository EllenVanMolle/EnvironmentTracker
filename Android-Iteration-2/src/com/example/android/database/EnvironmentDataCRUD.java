package com.example.android.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.model.EnvironmentData;

/**
 * <p>
 * <code>EnvironmentDataCRUD</code> class.
 * </p>
 * @author		Liselot De Brabandere, Joris Schelfaut, Ellen Van Molle
 * @version		14 November 2012
 */
public class EnvironmentDataCRUD extends CRUD<EnvironmentData> {
	
	/**
	 * Table name "EnvironmentData"
	 */
	private static final String TABLE_ENVIRONMENTDATA 	= "EnvironmentData";
	//private static final String TABLE_PICTUREDATA 		= "PictureData";
	//private static final String TABLE_SOUNDDATA 		= "SoundData";
	
	/*
	 * Column names
	 */
	private static final String ID 				= "id";
    private static final String ADDED_ON 		= "addded_on";
    private static final String MOODRATE 		= "moodrate";
    private static final String LONGITUDE 		= "longitude";
    private static final String LATITUDE 		= "latitude";
    private static final String PICTURE_ID 		= "picture_id";
    private static final String SOUND_ID 		= "sound_id";
    /*
     * Dates are added as text (no Timestamp data type available on SQLite)
     * @link http://www.sqlite.org/datatype3.html
     * @link http://www.sqlite.org/lang_createtable.html
     * 
     * http://www.sqlite.org/foreignkeys.html
     * FOREIGN KEY(trackartist) REFERENCES artist(artistid)
     */
    /**
     * Create table query.
     */
    private final String ENVIRONMENTDATA_TABLE_CREATE =
            "CREATE TABLE " + TABLE_ENVIRONMENTDATA + " (" +
					ID 				+ " " + INTEGER + " " + PRIMARY_KEY + " " + AUTOINCREMENT + ", " +
					ADDED_ON 		+ " " + TEXT + ", " +
					MOODRATE 		+ " " + REAL + ", " +
					LONGITUDE 		+ " " + REAL + ", " +
					LATITUDE 		+ " " + REAL + ", " +
					PICTURE_ID		+ " " + INTEGER + "," +
					SOUND_ID		+ " " + INTEGER +
            ");";
	
	/**
	 * Constructor for the <code>EnvironmentDataCRUD</code> class.
	 * @param databaseHandler the database handler to access the database.
	 */
	public EnvironmentDataCRUD(DatabaseHandler databaseHandler) {
		super(databaseHandler);
	}

	@Override
	public long insert(EnvironmentData record) {
		SQLiteDatabase db = this.databaseHandler.getWritableDatabase();
		
        ContentValues values = new ContentValues();
        values.put(ADDED_ON, 	record.getTimestamp());
        values.put(MOODRATE, 	record.getMoodrate());
        values.put(LONGITUDE, 	record.getLongitude());
        values.put(LATITUDE, 	record.getLatitude());
        if (record.getPictureID() > 0)
        	values.put(PICTURE_ID, 	record.getPictureID());
        if (record.getSoundID() > 0)
        	values.put(SOUND_ID, 	record.getSoundID());
 
        // Inserting Row
        long id = db.insert(TABLE_ENVIRONMENTDATA, null, values);
        //db.close(); // Closing database connection
        
        return id;
	}

	@Override
	public EnvironmentData select(long id) {
		SQLiteDatabase db = this.databaseHandler.getReadableDatabase();
	    
	    Cursor cursor = db.query(TABLE_ENVIRONMENTDATA,
	    		new String[] { ID, ADDED_ON, MOODRATE, LONGITUDE, LATITUDE,
	    						PICTURE_ID, SOUND_ID },
	    		ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    // Initialize cursor :
	    if (cursor != null)
	        cursor.moveToFirst();
	    
	    // Retrieve data and create EnvironmentData object :
	    EnvironmentData data = new EnvironmentData();
	    data.setID(cursor.getLong(0));					// ID
	    data.setTimestamp(cursor.getString(1));			// ADDED_ON
	    data.setMoodrate(cursor.getDouble(2));			// MOODRATE
	    data.setLongitude(cursor.getDouble(3));			// LONGITUDE
	    data.setLatitude(cursor.getDouble(4));			// LATITUDE
	    // Foreign keys
	    data.setPictureID(cursor.getLong(5));			// PICTURE_ID
	    data.setSoundID(cursor.getLong(6));				// SOUND_ID
	    
	    // Close cursor :
        cursor.close();
	    
	    // return EnvironmentData
	    return data;
	}

	@Override
	public List<EnvironmentData> select() {
		List<EnvironmentData> list = new ArrayList<EnvironmentData>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ENVIRONMENTDATA;
 
        SQLiteDatabase db = this.databaseHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                EnvironmentData data = new EnvironmentData();
                data.setID(cursor.getLong(0));
                data.setTimestamp(cursor.getString(1));
                data.setMoodrate(cursor.getDouble(2));
                data.setLongitude(cursor.getDouble(3));
                data.setLatitude(cursor.getDouble(4));
                // Foreign keys
        	    data.setPictureID(cursor.getLong(5));
        	    data.setSoundID(cursor.getLong(6));
                // Adding contact to list
                list.add(data);
            } while (cursor.moveToNext());
        }
        
        // Close cursor :
        cursor.close();
 
        // return contact list
        return list;
	}

	@Override
	public boolean update(EnvironmentData record) {
		SQLiteDatabase db = this.databaseHandler.getWritableDatabase();
		
	    ContentValues values = new ContentValues();
	    values.put(ADDED_ON, record.getTimestamp());
	    values.put(MOODRATE, record.getMoodrate());
	    values.put(LONGITUDE, record.getLongitude());
	    values.put(LATITUDE, record.getLatitude());
	    if (record.getPictureID() > 0)
        	values.put(PICTURE_ID, 	record.getPictureID());
        if (record.getSoundID() > 0)
        	values.put(SOUND_ID, 	record.getSoundID());
	 
	    // updating row
	    int rowsaffected = db.update(TABLE_ENVIRONMENTDATA, values, ID + " = ?",
	            new String[] { String.valueOf(record.getID()) });
	    return rowsaffected > 0;
	}

	@Override
	public boolean delete(long id) {
		SQLiteDatabase db = this.databaseHandler.getWritableDatabase();
	    db.delete(TABLE_ENVIRONMENTDATA, ID + " = ?",
	            new String[] { String.valueOf(id) });
	    //db.close();
	    return true;
	}

	@Override
	public boolean createTable(SQLiteDatabase db) {
		db.execSQL(ENVIRONMENTDATA_TABLE_CREATE);
		return true;
	}

	@Override
	public boolean dropTable(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENVIRONMENTDATA);
		return true;
	}

	@Override
	public int recordCount() {
		String countQuery = "SELECT * FROM " + TABLE_ENVIRONMENTDATA;
        SQLiteDatabase db = this.databaseHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        // Close cursor :
        cursor.close();
        // return count :
        return count;
	}	
}