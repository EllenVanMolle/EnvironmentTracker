package com.example.android.model;

import java.util.Calendar;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model class SoundData
 * @author		Liselot De Brabandere, Joris Schelfaut, Ellen Van Molle
 * @version		9 November 2012
 * 
 * @note	http://developer.android.com/reference/android/os/Parcelable.html
 * @note	http://blog.logicexception.com/2012/09/a-parcelable-tutorial-for-android.html
 */
public class EnvironmentData implements Serializable, Parcelable {
	
	/**
	 * Generated <code>serialVersionUID</code>.
	 */
	private static final long serialVersionUID = 4428536387599390989L;
	private long 	_id = -1;
	private String 	timestamp;
	private double	moodrate;
	private double 	longitude;
	private double 	latitude;
	private long	sound_id = -1;
	private long	picture_id = -1;
	
	/**
	 * <p>
	 * Classes implementing the Parcelable interface must
	 * also have a static field called CREATOR, which is
	 * an object implementing the Parcelable.Creator interface.
	 * </p>
	 */
	public static final Parcelable.Creator<EnvironmentData> CREATOR = new Parcelable.Creator<EnvironmentData>() {
		
		public EnvironmentData createFromParcel(Parcel in) {
		    return new EnvironmentData(in);
		}
		
		public EnvironmentData[] newArray(int size) {
		    return new EnvironmentData[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel output, int flags) {
		// WRITE
		output.writeLong(getID());
		output.writeString(getTimestamp());
		output.writeDouble(getMoodrate());
		output.writeDouble(getLongitude());
		output.writeDouble(getLatitude());
		output.writeLong(getPictureID());
		output.writeLong(getSoundID());
	}
	
	private void readFromParcel(Parcel input) {
		// READ
		this.setID(input.readLong());
		this.setTimestamp(input.readString());
		this.setMoodrate(input.readDouble());
		this.setLongitude(input.readDouble());
		this.setLatitude(input.readDouble());
		this.setPictureID(input.readLong());
		this.setSoundID(input.readLong());
	}
	
	/**
	 * Constructor for the model class EnvironmentData
	 * @Raw
	 */
	public EnvironmentData() {
		super();
	}
	
	/**
	 * Constructor for the model class EnvironmentData
	 * @param 	timestamp
	 * @param 	moodrate
	 * @param 	longitude
	 * @param 	latitude
	 * 
	 * @effect	setLatitude(latitude)
	 * @effect	setLongitude(longitude)
	 * @effect	setMoodrate(moodrate)
	 * @effect	setTimestamp(timestamp)
	 * @Raw
	 */
	public EnvironmentData(Calendar timestamp, double moodrate, double longitude, double latitude) {
		this();
		this.setLatitude(latitude);
		this.setLongitude(longitude);
		this.setMoodrate(moodrate);
		this.setTimestamp(timestamp);
	}
	
	/**
	 * Constructor for the model class EnvironmentData
	 * @param 	timestamp
	 * @param 	moodrate
	 * @param 	longitude
	 * @param 	latitude
	 * 
	 * @effect	this(timestamp, moodrate, longitude, latitude)
	 * @effect	setID(id)
	 * @Raw
	 */
	public EnvironmentData(long id, Calendar timestamp, double moodrate, double longitude, double latitude) {
		this(timestamp, moodrate, longitude, latitude);
		this.setID(id);
	}
	
	/**
	 * Constructor for the model class EnvironmentData
	 * @param parcel
	 */
	private EnvironmentData(Parcel parcel) {
		this();
		this.readFromParcel(parcel);
	}
	
	public final void setID(long id) {
		this._id = id;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public void setMoodrate(double moodrate) {
		this.moodrate = moodrate;
	}
	
	/**
	 * @param 	timestamp
	 * @effect	format(timestamp)
	 */
	public void setTimestamp(Calendar timestamp) {
		this.timestamp = format(timestamp);
	}
	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public long getID() {
		return _id;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public double getMoodrate() {
		return moodrate;
	}
	
	/**
	 * @return	
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * <p>
	 * Formats a <code>Calender</code> object as
	 * <b>TIMESTAMP - format: YYYY-MM-DD HH:MM:SS</b>
	 * </p>
	 * @param 	timestamp
	 * @return	the formatted timestamp.
	 * 			| result == (timestamp.get(Calendar.YEAR) + "-" + 
	 * 			  			timestamp.get(Calendar.MONTH) + "-" + 
	 * 			  			timestamp.get(Calendar.DAY_OF_MONTH) + " " + 
	 * 		      			timestamp.get(Calendar.HOUR_OF_DAY) + ":" + 
	 * 			  			timestamp.get(Calendar.MINUTE) + ":" + 
	 * 			  			timestamp.get(Calendar.SECOND)
	 */
	public static String format(Calendar timestamp) {
		String formattedDate = 
				timestamp.get(Calendar.YEAR) + "-" +
				timestamp.get(Calendar.MONTH) + "-" +
				timestamp.get(Calendar.DAY_OF_MONTH) + " " +
				timestamp.get(Calendar.HOUR_OF_DAY) + ":" +
				timestamp.get(Calendar.MINUTE) + ":" +
				timestamp.get(Calendar.SECOND);
		return formattedDate;
	}

	public long getSoundID() {
		return sound_id;
	}

	public void setSoundID(long sound_id) {
		this.sound_id = sound_id;
	}

	public long getPictureID() {
		return picture_id;
	}

	public void setPictureID(long picture_id) {
		this.picture_id = picture_id;
	}
}
