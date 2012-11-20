package com.example.android.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model class SoundData
 * @author		Liselot De Brabandere, Joris Schelfaut, Ellen Van Molle
 * @version		9 November 2012
 */
public class SoundData implements Serializable, Parcelable {
	
	/**
	 * Generated <code>serialVersionUID</code>.
	 */
	private static final long serialVersionUID = 6731332692140324462L;
	private long	_id = -1;
	private double 	amplitude;
	private double 	frequency;
	private long 	environment_id = -1;
	/**
	 * <p>
	 * Classes implementing the Parcelable interface must
	 * also have a static field called CREATOR, which is
	 * an object implementing the Parcelable.Creator interface.
	 * </p>
	 */
	public static final Parcelable.Creator<SoundData> CREATOR = new Parcelable.Creator<SoundData>() {
		
		public SoundData createFromParcel(Parcel in) {
		    return new SoundData(in);
		}
		
		public SoundData[] newArray(int size) {
		    return new SoundData[size];
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
		output.writeDouble(getAmplitude());
		output.writeDouble(getFrequency());
		output.writeLong(getEnvironmentID());
	}

	/**
	 * @param parcel
	 */
	private void readFromParcel(Parcel parcel) {
		this.setID(parcel.readLong());
		this.setAmplitude(parcel.readDouble());
		this.setFrequency(parcel.readDouble());
		this.setEnvironmentID(parcel.readLong());
	}
	
	/**
	 * Constructor for the SoundData class.
	 * @param _id
	 * @param amplitude
	 * @param frequency
	 * @effect	setID(id)
	 * @effect	setAmplitude(amplitude)
	 * @effect	setFrequency(frequency)
	 */
	public SoundData(long id, double amplitude, double frequency, long environmentID) {
		super();
		this.setID(id);
		this.setAmplitude(amplitude);
		this.setFrequency(frequency);
		this.setEnvironmentID(environmentID);
	}
	
	/**
	 * Contructor for model class <code>SoundData</code>
	 * @param amplitude
	 * @param frequency
	 * @effect	setAmplitude(amplitude)
	 * @effect	setFrequency(frequency)
	 * @Raw
	 */
	public SoundData(double amplitude, double frequency) {
		super();
		this.setAmplitude(amplitude);
		this.setFrequency(frequency);
	}
	
	/**
	 * Contructor for model class <code>SoundData</code>
	 * @param parcel
	 */
	private SoundData(Parcel parcel) {
		this();
		this.readFromParcel(parcel);
	}

	/**
	 * A raw constructor for the <code>SoundData</code> class.
	 * @Raw
	 */
	public SoundData() {
		super();
	}
	
	/**
	 * @param id : the value to be set of the field id
	 */
	public final void setID(long id) {
		this._id = id;
	}
	
	/**
	 * @param environmentID : the value to be set of the field environmentID
	 */
	public final void setEnvironmentID(long environmentID) {
		this.environment_id = environmentID;
	}
	
	/**
	 * @param amplitude : the value to be set of the field amplitude
	 */
	public final void setAmplitude(double amplitude) {
		this.amplitude = amplitude;
	}
	
	/**
	 * @param frequency : the value to be set of the field frequency
	 */
	public final void setFrequency(double frequency) {
		this.frequency = frequency;
	}
	
	/**
	 * @return	the value of the field <code>id</code> of this instance.
	 */
	public long getID() {
		return _id;
	}
	
	/**
	 * @return	the value of the field <code>amplitude</code> of this instance.
	 */
	public double getAmplitude() {
		return amplitude;
	}
	
	/**
	 * @return	the value of the field <code>frequency</code> of this instance.
	 */
	public double getFrequency() {
		return frequency;
	}
	
	/**
	 * @return	the value of the field <code>environment_id</code> of this instance.
	 */
	public long getEnvironmentID() {
		return environment_id;
	}
}
