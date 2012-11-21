package com.example.android.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.model.PictureData;

/**
 * <p>
 * <code>PictureDataCRUD</code> class.
 * </p>
 * @author		Liselot De Brabandere, Joris Schelfaut, Ellen Van Molle
 * @version		14 November 2012
 */
public class PictureDataCRUD extends CRUD<PictureData> {
	
	private static final String TABLE_PICTUREDATA 		= "PictureData";
	//private static final String TABLE_ENVIRONMENTDATA 	= "EnvironmentData";
	
	private static final String ID 				= "id";
	private static final String VIGOROUS 		= "vigorous";
    private static final String NATURE	 		= "nature";
    private static final String OCEAN	 		= "ocean";
    private static final String FLOWER 			= "flower";
    private static final String SATURATION 		= "saturation";
    private static final String BRIGHTNESS 		= "brightness";
    private static final String ENVIRONMENT_ID	= "environment_id";
    
    private final String PICTUREDATA_TABLE_CREATE =
            "CREATE TABLE " + TABLE_PICTUREDATA + " (" +
            		ID 				+ " " + INTEGER + " " + PRIMARY_KEY + " " + AUTOINCREMENT + ", " +
					VIGOROUS 		+ " " + REAL + ", " +
					NATURE	 		+ " " + REAL + ", " +
					OCEAN	 		+ " " + REAL + ", " +
					FLOWER	 		+ " " + REAL + ", " +
					SATURATION 		+ " " + REAL + ", " +
					BRIGHTNESS 		+ " " + REAL + ", " +
					ENVIRONMENT_ID		+ " " + INTEGER +/* " " +
					FOREIGN_KEY		+ " (" + ENVIRONMENT_ID + ") " + REFERENCES + " " +
									TABLE_ENVIRONMENTDATA + "(" + ID + ")" + */
            ");";
    
	/**
	 * Constructor for the <code>PictureDataCRUD</code> class.
	 * @param databaseHandler the database handler to access the database.
	 */
	public PictureDataCRUD(DatabaseHandler databaseHandler) {
		super(databaseHandler);
	}
	
	@Override
	public long insert(PictureData record) {
		SQLiteDatabase db = this.databaseHandler.getWritableDatabase();
		
        ContentValues values = new ContentValues();
        values.put(VIGOROUS, 		record.getVigorous());
        values.put(NATURE, 			record.getNature());
        values.put(OCEAN, 			record.getOcean());
        values.put(FLOWER, 			record.getFlower());
        values.put(SATURATION, 		record.getSaturation());
        values.put(BRIGHTNESS, 		record.getBrightness());
        values.put(ENVIRONMENT_ID, 	record.getEnvironmentID());
 
        // Inserting Row
        long id = db.insert(TABLE_PICTUREDATA, null, values);
        //db.close(); // Closing database connection
		return id;
	}

	@Override
	public PictureData select(long id) {
		SQLiteDatabase db = this.databaseHandler.getReadableDatabase();
	    
	    Cursor cursor = db.query(TABLE_PICTUREDATA,
	    		new String[] { ID, VIGOROUS, NATURE, OCEAN, FLOWER,
	    			SATURATION, BRIGHTNESS },
	    		ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    // Initialize cursor :
	    if (cursor != null)
	        cursor.moveToFirst();
	    
	    // Retrieve data and create PictureData object :
	    PictureData data = new PictureData();
        data.setID(cursor.getLong(0));
        data.setVigorous(cursor.getDouble(1));
        data.setNature(cursor.getDouble(2));
        data.setOcean(cursor.getDouble(3));
        data.setFlower(cursor.getDouble(4));
        data.setSaturation(cursor.getDouble(5));
        data.setBrightness(cursor.getDouble(6));
	    
        // Close cursor (http://www.vogella.com/articles/AndroidSQLite/article.html#sqliteoverview_cursor) :
        cursor.close();
        
	    // return PictureData
	    return data;
	}

	@Override
	public List<PictureData> select() {
		List<PictureData> list = new ArrayList<PictureData>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PICTUREDATA;
 
        SQLiteDatabase db = this.databaseHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PictureData data = new PictureData();
                data.setID(cursor.getLong(0));
                data.setVigorous(cursor.getDouble(1));
                data.setNature(cursor.getDouble(2));
                data.setOcean(cursor.getDouble(3));
                data.setFlower(cursor.getDouble(4));
                data.setSaturation(cursor.getDouble(5));
                data.setBrightness(cursor.getDouble(6));
                // Adding PictureData to list
                list.add(data);
            } while (cursor.moveToNext());
        }
 
        // Close cursor :
        cursor.close();
        
        // return PictureData list
        return list;
	}

	@Override
	public boolean update(PictureData record) {
		SQLiteDatabase db = this.databaseHandler.getWritableDatabase();
		
	    ContentValues values = new ContentValues();
	    values.put(VIGOROUS, record.getVigorous());
	    values.put(NATURE, record.getNature());
	    values.put(OCEAN, record.getOcean());
	    values.put(FLOWER, record.getFlower());
	    values.put(SATURATION, record.getSaturation());
	    values.put(BRIGHTNESS, record.getBrightness());
	    values.put(ENVIRONMENT_ID, record.getEnvironmentID());
	    
	    // updating row
	    int rowsaffected = db.update(TABLE_PICTUREDATA, values, ID + " = ?",
	            new String[] { String.valueOf(record.getID()) });
	    return rowsaffected > 0;
	}

	@Override
	public boolean delete(long id) {
		SQLiteDatabase db = this.databaseHandler.getWritableDatabase();
	    db.delete(TABLE_PICTUREDATA, ID + " = ?",
	            new String[] { String.valueOf(id) });
	    //db.close();
	    return true;
	}

	@Override
	public boolean createTable(SQLiteDatabase db) {
		db.execSQL(PICTUREDATA_TABLE_CREATE);
		return true;
	}

	@Override
	public boolean dropTable(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PICTUREDATA);
		return true;
	}

	@Override
	public int recordCount() {
		String countQuery = "SELECT * FROM " + TABLE_PICTUREDATA;
        SQLiteDatabase db = this.databaseHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        // Close cursor :
        cursor.close();
        // return count :
        return count;
	}
}
