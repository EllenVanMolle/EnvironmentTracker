package com.example.android.analysis;

import com.example.android.model.PictureData;

import android.graphics.Bitmap;

/**
 * <p>
 * <code>ImageAnalyzer</code> class. Takes data from a camera
 * and produces an image analysis.
 * Then the ImageAnalyzer returns a <code>PictureData</code> object.
 * </p>
 * <p>
 * See also:
 * <a href="http://nl.wikipedia.org/wiki/HSV_(kleurruimte)">HSV</a>
 * </p>
 * @author	Liselot De Brabandere, Joris Schelfaut, Ellen Van Molle
 * @version	9 November 2012
 */
public class ImageAnalyzer {
	
	// FIELDS :
	private Bitmap 			image;
	private long			environmentID = -1;
	private int[] 			pixels;
	private double[] 		percentageHueClass = new double[4];
	private double 			saturation;
	private double 			brightness;
	
	// CONSTANTS :
	private static final int VIGOROUS 	= 0;
	private static final int NATURE 	= 1;
	private static final int OCEAN 		= 2;
	private static final int FLOWER 	= 3;
	private static final int HUE 		= 0;
	private static final int SATURATION = 1;
	private static final int BRIGHTNESS	= 2;
	
	/**
	 * <p>
	 * Instantiates an ImageAnalyser object.
	 * </p>
	 * @param 	bitmap the image being analyzed
	 * @throws	NullPointerException
	 * @effect	setData(data)
	 */
	public ImageAnalyzer(Bitmap image, long environmentID) throws NullPointerException {
		this(image);
		this.setEnvironmentID(environmentID);
	}
	
	/**
	 * <p>
	 * Instantiates an ImageAnalyser object.
	 * </p>
	 * @param 	bitmap the image being analyzed
	 * @throws 	NullPointerException
	 * @Raw
	 */
	public ImageAnalyzer(Bitmap image) throws NullPointerException {
		super();
		this.setImage(image);
	}
	
	/**
	 * <p>
	 * Execute the analysis on the data. 
	 * Initializes the pixel array, analyzes the pixels
	 * and persists the data.</p>
	 * @effect	initializePixels()
	 * @effect	analyze()
	 * @effect	persist()
	 */
	public PictureData executeAnalysis() {
		// ANALYSIS
		this.initializePixels();
		return this.analyze();
		//this.persist();
	}
	
	/**
	 * @param bitmap the image to analyze
	 * @Model
	 * @post	| new.getBitmap() == bitmap
	 */
	private void setImage(Bitmap image) {
		this.image = image;
	}
	
	/**
	 * @param environmentID
	 * @Model
	 */
	private void setEnvironmentID(long environmentID) {
		this.environmentID = environmentID;
	}
	
	/**
     * Initializes the pixels from the data.
     * @param data
     * @Model
     */
    private void initializePixels() {
        int height 		= image.getHeight();
        int width 		= image.getWidth();
        pixels 			= new int[height * width];
        
        image.getPixels(pixels, 0, width, 0, 0, width, height);
	}

	/**
     * <p>Analyzes the pixels.</p>
     * <p>Pixel categories :
     * <ul>
     * 	<li>0°-90° 		Energy (warm) 	– Corn yellow</li>
     * 	<li>90°-180° 	Nature 			– Grass green</li>
     *  <li>180°- 270° 	Ocean 			– Blue</li>
     *  <li>270°-360° 	Flower 			– Violet</li>
     * </ul>
     * </p>
     * 
     * @return	PictureData object.
     * 
     * @Model
     * @note	http://www.roseindia.net/java/java-get-example/get-color-of-pixel.shtml
     * @note	http://www.workwithcolor.com/hsl-color-schemer-01.htm
     */
    private PictureData analyze() {
    	
    	int N 					= pixels.length;
    	int[] hue 				= new int[4];
    	double totalSaturation 	= 0;
    	double totalBrightness	= 0;
    	
    	if (N > 0) {
	    	// Loop through all the pixels.
	    	for (int i = 0; i < N; i++) {
	    		
	    		// Retrieving the pixel from the pixel array.
	    		int clr 	=  pixels[i];
	    		
	    		// Extracting the color values.
	    		int red   	= (clr & 0x00ff0000) >> 16;
			  	int green 	= (clr & 0x0000ff00) >> 8;
			  	int blue  	= (clr & 0x000000ff);
			  	
			  	// Extracting the HSV values.
			  	double[] hsv = transform_RGB_to_HSV(red, green, blue);
		  	
			  	// Collecting HUE values.
			  	if (hsv[HUE] <= 90) {
			  		hue[VIGOROUS]++;
			  	} else if (hsv[HUE] <= 180) {
			  		hue[NATURE]++;
			  	} else if (hsv[HUE] <= 270){
			  		hue[OCEAN]++;
			  	} else if (hsv[HUE] <= 360) {
			  		hue[FLOWER]++;
			  	} else {
			  		// TODO : show error: hue is incorrect, or thow exception, or break and reset.
			  	}
			  	
			  	// Collecting the saturation values.
			  	totalSaturation += hsv[SATURATION];
			  	
			  	// Collecting the brightness values.
			  	totalBrightness += hsv[BRIGHTNESS];
			  	
	    	}
	    	
	    	// Calculating percentages.
	    	this.percentageHueClass[VIGOROUS] = percentage(hue[VIGOROUS], N);
	    	this.percentageHueClass[NATURE] = percentage(hue[NATURE], N);
	    	this.percentageHueClass[OCEAN] = percentage(hue[OCEAN], N);
	    	this.percentageHueClass[FLOWER] = percentage(hue[FLOWER], N);
	    	
	    	// Calculating averages.
	    	this.saturation = percentage(totalSaturation, N);
	    	this.brightness = percentage(totalBrightness, N);
	    	
    	} else {
    		// TODO : show error message: no pixels found in image.
    	}
    	
    	// Create PictureData
    	PictureData data = new PictureData();
    	data.setVigorous(this.percentageHueClass[VIGOROUS]);
    	data.setNature(this.percentageHueClass[NATURE]);
    	data.setOcean(this.percentageHueClass[OCEAN]);
    	data.setFlower(this.percentageHueClass[FLOWER]);
    	data.setSaturation(this.saturation);
    	data.setBrightness(this.brightness);
    	data.setEnvironmentID(this.environmentID);
    	
    	return data;
	}
    
    /**
     * <p>Extracts HSV data from RGB data : <br />
     * Hue is given by transform_RGB_to_HSV(R,G,B)[0] <br />
     * Saturation is given by transform_RGB_to_HSV(R,G,B)[1]  <br />
     * Value is given by transform_RGB_to_HSV(R,G,B)[2]
     * </p>
     * The color analyzing by Liselot:
     * <a href="https://github.com/EllenVanMolle/EnvironmentTracker/blob/master/HTML5/assets/www/coloranalysis.js">
     * github</a>
     * @param red
     * @param green
     * @param blue
     * 
     * @return	the HSV array
     * @Model
     */
    private static double[] transform_RGB_to_HSV(int red, int green, int blue) {
    	double[] hsv = new double[3];
    	
    	double r = red   / 255;
    	double g = green / 255;
    	double b = blue  / 255;
    	
    	double minRGB = Math.min(r,Math.min(g,b));
    	double maxRGB = Math.max(r,Math.max(g,b));
	
		// Black-gray-white
		if (minRGB == maxRGB) {
			hsv[ImageAnalyzer.BRIGHTNESS] = minRGB;
		} else {
	    	// Colors other than black-gray-white:
	    	double d = ((r == minRGB)?(g - b):((b==minRGB)?(r-g):(b-r)));
	    	double h = ((r==minRGB)?3:((b==minRGB)?1:5));
	    	hsv[ImageAnalyzer.HUE] = 60 * (h - (d / (maxRGB - minRGB)));
	    	hsv[ImageAnalyzer.SATURATION] = ((maxRGB - minRGB) / maxRGB);
	    	hsv[ImageAnalyzer.BRIGHTNESS] = maxRGB;
		}
		
		return hsv;
	}

	/**
     * Returns the percentage a number is from a total.
     * @param number
     * @param total
     * @return	the percentage.
     * 			| result == (number * 100) / total
     * @Model
     */
    private double percentage(double number, double total) {
    	return (number * 100) / total;
    }
    
    /**
     * @return the brightness property.
     * @Basic
     */
    public double getBrightness() {
		return brightness;
	}
    
    /**
     * @return the saturation property.
     * @Basic
     */
    public double getSaturation() {
		return saturation;
	}
    
    /**
     * @return	the percentage of pixels in the VIGOROUS Hue class.
     * @Basic
     */
    public double getVigorous() {
		return percentageHueClass[ImageAnalyzer.VIGOROUS];
	}
    
    /**
     * @return	the percentage of pixels in the NATURE Hue class.
     * @Basic
     */
    public double getNature() {
		return percentageHueClass[ImageAnalyzer.NATURE];
	}
    
    /**
     * @return	the percentage of pixels in the OCEAN Hue class.
     * @Basic
     */
    public double getOcean() {
		return percentageHueClass[ImageAnalyzer.OCEAN];
	}
    
    /**
     * @return	the percentage of pixels in the FLOWER Hue class.
     * @Basic
     */
    public double getFlower() {
		return percentageHueClass[ImageAnalyzer.FLOWER];
	}
}
