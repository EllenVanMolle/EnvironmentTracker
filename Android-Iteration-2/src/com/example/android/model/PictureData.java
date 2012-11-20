package com.example.android.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model class ImageData
 * @author		Liselot De Brabandere, Joris Schelfaut, Ellen Van Molle
 * @version		9 November 2012
 */
public class PictureData implements Serializable, Parcelable {
	
	/**
	 * Generated <code>serialVersionUID</code>.
	 */
	private static final long serialVersionUID = -8110465373728544659L;
	private long 	_id = -1;
	private double 	vigorous;
	private double 	nature;
	private double 	ocean;
	private double 	flower;
	private double 	saturation;
	private double 	brightness;
	private long	environment_id = -1;
	/**
	 * <p>
	 * Classes implementing the Parcelable interface must
	 * also have a static field called CREATOR, which is
	 * an object implementing the Parcelable.Creator interface.
	 * </p>
	 */
	public static final Parcelable.Creator<PictureData> CREATOR = new Parcelable.Creator<PictureData>() {
		
		public PictureData createFromParcel(Parcel in) {
		    return new PictureData(in);
		}
		
		public PictureData[] newArray(int size) {
		    return new PictureData[size];
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
		output.writeDouble(getVigorous());
		output.writeDouble(getNature());
		output.writeDouble(getOcean());
		output.writeDouble(getFlower());
		output.writeDouble(getSaturation());
		output.writeDouble(getBrightness());
		output.writeLong(getEnvironmentID());
	}
	
	private void readFromParcel(Parcel input) {
		// READ
		this.setID(input.readLong());
		this.setVigorous(input.readDouble());
		this.setNature(input.readDouble());
		this.setOcean(input.readDouble());
		this.setFlower(input.readDouble());
		this.setSaturation(input.readDouble());
		this.setBrightness(input.readDouble());
		this.setEnvironmentID(input.readLong());
	}
	
	/**
	 * Contructor for model class <code>PictureData</code>
	 */
	public PictureData() {
		super();
	}
	
	/**
	 * Contructor for model class <code>PictureData</code>
	 * @param id
	 * @param vigorous
	 * @param nature
	 * @param ocean
	 * @param flower
	 * @param saturation
	 * @param brightness
	 * @param environment_id
	 */
	public PictureData(long id, double vigorous, double nature,
			double ocean, double flower, double saturation, double brightness,
			long environment_id)
	{
		this();
		this.setBrightness(brightness);
		this.setEnvironmentID(environment_id);
		this.setFlower(flower);
		this.setNature(nature);
		this.setOcean(ocean);
		this.setSaturation(saturation);
		this.setVigorous(vigorous);
		this.setID(id);
	}
	
	/**
	 * Contructor for model class <code>PictureData</code>
	 * @param parcel
	 */
	private PictureData(Parcel parcel) {
		this();
		this.readFromParcel(parcel);
	}
	
	public void setID(long id) {
		this._id = id;
	}
	
	public void setBrightness(double brightness) {
		this.brightness = brightness;
	}
	
	public void setEnvironmentID(long environment_id) {
		this.environment_id = environment_id;
	}
	
	public void setFlower(double flower) {
		this.flower = flower;
	}
	
	public void setNature(double nature) {
		this.nature = nature;
	}
	
	public void setOcean(double ocean) {
		this.ocean = ocean;
	}
	
	public void setSaturation(double saturation) {
		this.saturation = saturation;
	}
	
	public void setVigorous(double vigorous) {
		this.vigorous = vigorous;
	}

	public long getID() {
		return _id;
	}
	
	public double getBrightness() {
		return brightness;
	}
	
	public long getEnvironmentID() {
		return environment_id;
	}
	
	public double getFlower() {
		return flower;
	}
	
	public double getNature() {
		return nature;
	}
	
	public double getOcean() {
		return ocean;
	}
	
	public double getSaturation() {
		return saturation;
	}
	
	public double getVigorous() {
		return vigorous;
	}
}
