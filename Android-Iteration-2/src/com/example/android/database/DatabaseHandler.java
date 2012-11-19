package com.example.android.database;

import java.util.Calendar;
import java.util.List;

import com.example.android.model.EnvironmentData;
import com.example.android.model.PictureData;
import com.example.android.model.SoundData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <p>
 * The <code>DatabaseHandler</code> class provides CRUD (create, read, update and delete) operations
 * for managing the database and model classes.
 * </p>
 * 
 * @author		Liselot De Brabandere, Joris Schelfaut, Ellen Van Molle
 * @version		9 November 2012
 * 
 * @link	http://www.w3schools.com/sql/sql_create_table.asp
 * @link	http://developer.android.com/guide/topics/data/data-storage.html#db
 * @link	http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
 * @link	http://www.vogella.com/articles/AndroidSQLite/article.html
 */
public class DatabaseHandler extends SQLiteOpenHelper {

	enum Policy {
		DROP_AND_CREATE, CREATE;
	}
	
	private Policy policy = Policy.DROP_AND_CREATE;
	
	// CONSTANTS :
	/**
	 * Version of the database.
	 */
    private static final int 	DATABASE_VERSION 		= 1;
    /**
     * Database name.
     */
    private static final String DATABASE_NAME 			= "Mumedata";
    
    /*
     * Table CRUDs
     */
    private final PictureDataCRUD 		pictureDataCRUD;
    private final SoundDataCRUD 		soundDataCRUD;
    private final EnvironmentDataCRUD	environmentDataCRUD;
    
    /**
     * Constructor: instantiates a new database with a context
     * <code>DATABASE_NAME</code> and
     * <code>DATABASE_VERSION</code>
     * @param context
     */
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.pictureDataCRUD = new PictureDataCRUD(this);
        this.environmentDataCRUD = new EnvironmentDataCRUD(this);
        this.soundDataCRUD = new SoundDataCRUD(this);
    }
    
    /**
     * When the database is created two tables are created as well:
     * <ul>
     * 	<li><code>TABLE_ENVIRONMENTDATA</code></li>
     * 	<li><code>TABLE_PICTUREDATA</code></li>
     *  <li><code>TABLE_SOUNDDATA</code></li>
     * </ul>
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
    	
    	if (getPolicy() == Policy.DROP_AND_CREATE) {
	    	// Drop older table if existed
			getEnvironmentDataCRUD().dropTable(db);
	        getPictureDataCRUD().dropTable(db);
	        getSoundDataCRUD().dropTable(db);
    	}
    	// Create new tables :
        getEnvironmentDataCRUD().createTable(db);
        getPictureDataCRUD().createTable(db);
        getSoundDataCRUD().createTable(db);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// Drop older table if existed
		getEnvironmentDataCRUD().dropTable(db);
        getPictureDataCRUD().dropTable(db);
        getSoundDataCRUD().dropTable(db);
        
        // Create tables again
        onCreate(db);
	}
	
	/**
	 * @return	interface for CRUD methods for the EnvironmentData table.
	 * @Immutable
	 * @Basic
	 */
	public EnvironmentDataCRUD getEnvironmentDataCRUD() {
		return environmentDataCRUD;
	}
	
	/**
	 * @return	interface for CRUD methods for the PictureData table.
	 * @Immutable
	 * @Basic
	 */
	public PictureDataCRUD getPictureDataCRUD() {
		return pictureDataCRUD;
	}
	
	/**
	 * @return	interface for CRUD methods for the SoundData table.
	 * @Immutable
	 * @Basic
	 */
	public SoundDataCRUD getSoundDataCRUD() {
		return soundDataCRUD;
	}
	
	/**
	 * Database creation policy.
	 * @return	Database creation policy.
	 */
	public Policy getPolicy() {
		return policy;
	}
	
	/**
	 * <p>Only to be used for test purposes.</p>
	 */
	public void insertTestData() {
		
		int records = getEnvironmentDataCRUD().recordCount();
		
		if (records != 0) {
			
			// REMOVE ALL RECORDS
			
			List<EnvironmentData> list1 = getEnvironmentDataCRUD().select();
			for(EnvironmentData ed : list1) {
				getEnvironmentDataCRUD().delete(ed.getID());
			}
			
			List<PictureData> list2 = getPictureDataCRUD().select();
			for(PictureData ed : list2) {
				getPictureDataCRUD().delete(ed.getID());
			}
			
			List<SoundData> list3 = getSoundDataCRUD().select();
			for(SoundData ed : list3) {
				getSoundDataCRUD().delete(ed.getID());
			}
		}
		
		// IF DATA BASE EMPTY
		if (records == 0) {
			
			// INSERT RECORD 1
			EnvironmentData envDat1 = new EnvironmentData();
			envDat1.setMoodrate(2.2);
			envDat1.setTimestamp(Calendar.getInstance());
			envDat1.setLatitude(45.1);
			envDat1.setLongitude(51.7);
			PictureData imgDat1		= new PictureData();
			imgDat1.setVigorous(30.3);
			imgDat1.setNature(20.2);
			imgDat1.setOcean(39.4);
			imgDat1.setFlower(10.1);
			imgDat1.setSaturation(20.6);
			imgDat1.setBrightness(40.9);
			SoundData soundDat1		= new SoundData();
			soundDat1.setAmplitude(0.1);
			soundDat1.setFrequency(0.3);
			
			long idenv1 	= getEnvironmentDataCRUD().insert(envDat1);
			imgDat1.setEnvironmentID(idenv1);
			soundDat1.setEnvironmentID(idenv1);
			long idimg1 	= getPictureDataCRUD().insert(imgDat1);
			long idsound1 	= getSoundDataCRUD().insert(soundDat1);
			envDat1.setID(idenv1);
			envDat1.setPictureID(idimg1);
			envDat1.setSoundID(idsound1);
			getPictureDataCRUD().update(imgDat1);
			//System.out.println(idenv1 + ", " + idimg1 + ", " + idsound1);
			
			
			// INSERT RECORD 2
			EnvironmentData envDat2 = new EnvironmentData();
			envDat2.setMoodrate(6.7);
			envDat2.setTimestamp(Calendar.getInstance());
			envDat2.setLatitude(40.9);
			envDat2.setLongitude(51.6);
			PictureData imgDat2		= new PictureData();
			imgDat2.setVigorous(39.4);
			imgDat2.setNature(10.1);
			imgDat2.setOcean(40.4);
			imgDat2.setFlower(10.1);
			imgDat2.setSaturation(15.7);
			imgDat2.setBrightness(23.5);
			SoundData soundDat2		= new SoundData();
			soundDat2.setAmplitude(0.1);
			soundDat2.setFrequency(0.4);
			
			long idenv2 	= getEnvironmentDataCRUD().insert(envDat2);
			imgDat2.setEnvironmentID(idenv2);
			soundDat2.setEnvironmentID(idenv2);
			long idimg2 	= getPictureDataCRUD().insert(imgDat2);
			long idsound2 	= getSoundDataCRUD().insert(soundDat2);
			envDat2.setID(idenv2);
			envDat2.setPictureID(idimg2);
			envDat2.setSoundID(idsound2);
			getPictureDataCRUD().update(imgDat2);
			//System.out.println(idenv2 + ", " + idimg2 + ", " + idsound2);
			
			
			// INSERT RECORD 3
			EnvironmentData envDat3 = new EnvironmentData();
			envDat3.setMoodrate(7.0);
			envDat3.setTimestamp(Calendar.getInstance());
			envDat3.setLatitude(41.2);
			envDat3.setLongitude(51.7);
			PictureData imgDat3		= new PictureData();
			imgDat3.setVigorous(18.2);
			imgDat3.setNature(19.2);
			imgDat3.setOcean(22.2);
			imgDat3.setFlower(30.3);
			imgDat3.setSaturation(18.6);
			imgDat3.setBrightness(27.4);
			SoundData soundDat3		= new SoundData();
			soundDat3.setAmplitude(0.2);
			soundDat3.setFrequency(0.3);
			
			long idenv3 	= getEnvironmentDataCRUD().insert(envDat3);
			imgDat3.setEnvironmentID(idenv3);
			soundDat3.setEnvironmentID(idenv3);
			long idimg3 	= getPictureDataCRUD().insert(imgDat3);
			long idsound3 	= getSoundDataCRUD().insert(soundDat3);
			envDat3.setID(idenv3);
			envDat3.setPictureID(idimg3);
			envDat3.setSoundID(idsound3);
			getPictureDataCRUD().update(imgDat3);
			//System.out.println(idenv3 + ", " + idimg3 + ", " + idsound3);
			
			
			// INSERT RECORD 4
			EnvironmentData envDat4 = new EnvironmentData();
			envDat4.setMoodrate(4.0);
			envDat4.setTimestamp(Calendar.getInstance());
			envDat4.setLatitude(41.5);
			envDat4.setLongitude(51.0);
			PictureData imgDat4		= new PictureData();
			imgDat4.setVigorous(9.1);
			imgDat4.setNature(30.3);
			imgDat4.setOcean(5.1);
			imgDat4.setFlower(55.5);
			imgDat4.setSaturation(36.5);
			imgDat4.setBrightness(7.7);
			SoundData soundDat4		= new SoundData();
			soundDat4.setAmplitude(0.5);
			soundDat4.setFrequency(0.5);
			
			long idenv4 	= getEnvironmentDataCRUD().insert(envDat4);
			imgDat4.setEnvironmentID(idenv4);
			soundDat4.setEnvironmentID(idenv4);
			long idimg4 	= getPictureDataCRUD().insert(imgDat4);
			long idsound4 	= getSoundDataCRUD().insert(soundDat4);
			envDat4.setID(idenv4);
			envDat4.setPictureID(idimg4);
			envDat4.setSoundID(idsound4);
			getPictureDataCRUD().update(imgDat4);
			//System.out.println(idenv4 + ", " + idimg4 + ", " + idsound4);
		} else {
			// TODO : logger : nothing has been written to database
		}
	}
}