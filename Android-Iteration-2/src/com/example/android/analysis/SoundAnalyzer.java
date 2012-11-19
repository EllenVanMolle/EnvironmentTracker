package com.example.android.analysis;

import com.example.android.model.SoundData;

/**
 * <code>SoundAnalyzer</code> class.
 * @author	Liselot De Brabandere, Joris Schelfaut, Ellen Van Molle
 * @version	10 November 2012
 */
public class SoundAnalyzer {
	
	private double 	frequency = 0;
	private double 	amplitude = 0;
	private long 	environmentID;
	
	/**
	 * Instantiates a new SoundAnalyzer
	 * @param databaseHandler
	 * @param environmentID
	 */
	public SoundAnalyzer(long environmentID) {
		super();
		this.setEnvironmentID(environmentID);
	}
	
	/**
	 * @param environmentID
	 */
	private void setEnvironmentID(long environmentID) {
		this.environmentID = environmentID;
	}
	
	/**
	 * Analyzes sound and persists the result in the database.
	 */
	public final SoundData executeAnalysis() {
		return analyze();
	}
	
	/**
	 * @return
	 * @Model
	 */
	private SoundData analyze() {
		// TODO : getting frequency and amplitude from sound file.
		
		// Create SoundData
		SoundData data = new SoundData();
    	data.setAmplitude(this.amplitude);
    	data.setFrequency(this.frequency);
    	data.setEnvironmentID(this.environmentID);
    	
    	return data;
	}
}
