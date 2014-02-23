package com.openregatta.tools;

public class Tools {

	 /** 
	  * Exception safe parsing for double values
	  * returns -1 if an exception occurs instead of returning the exception
	  * 
	  * @param string representation of the value to be parsed 
	  * @return the value parsed from the given string
	  */
	 public static double tryParse(String number)
	 {
		 try{
			 return Double.parseDouble(number);
		 }
		 catch(Exception e){
			return -1;
		 }
	 }
	 
	 /**
	  * Takes a speed in knots as an input and return a speed in meters per second
	  * 
	  * @param knots
	  * @return
	  */
	 public static double KnotsToMetersSecond(double knots)
	 {
		 return knots * 0.514444;
	 }
	 
	 /** 
	  * Takes a speed in meters per second as an input and returns a speed in knots
	  * 
	  * @param metersSecond
	  * @return
	  */
	 public static double MetersSecondToKnots(double metersSecond)
	 {
		 return metersSecond * 1.943844;
	 }
	 
	 /**
	  * Takes a distance in nautical miles as an input and return a distance in meters
	  * @param miles
	  * @return
	  */
	 public static double MilesToMeters(double miles)
	 {
		 return miles * 1852;
	 }
	 
	 /**
	  * Takes a distance in meters as an input and returns a distance in nautical miles
	  * @param meters
	  * @return
	  */
	 public static double MetersToMiles(double meters)
	 {
		 return meters / 1852;
	 }
	 
	 /** 
	  * Converts location formatted as XX YY.yy where XX are degrees and YY.yy are minutes
	  * into XX.XXXX where XX.XXXX is the same location in decimal degrees
	  * 
	  * @param degrees
	  * @param minutes
	  * @return
	  */
	 public static double DegreeMinutesToDegree(int degrees, double minutes)
	 {
		 return (double)degrees + minutes / 60;
	 }

	 /**
	  * Converts a speed in Kilometers per Hour in a speed in meters per second
	  * 
	  * @param tempValue
	  * @return
	  */
	public static double KphToMetersSecond(double kph) {
		
		return 0.277778 * kph ;
	}

	/**
	 * Converts a speed in statue miles per hour in a speed in meters per second
	 * @param mile
	 * @return
	 */
	public static double StatuteMileToMetersSecond(double mile) {
		
		return 0.44704 * mile;
	}
	
}
