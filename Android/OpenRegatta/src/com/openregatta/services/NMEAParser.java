package com.openregatta.services;

public final class NMEAParser {

	/** 
	 * Parses a string, line by line and creates a NMEADataFrame object 
	 * that contains the parsed information
	 * 
	 * @param frame The string that contains NMEA0183 formatted data
	 * @return an object containing the parsed data converted to primitive data types
	 */
	public static NMEADataFrame Parse(String frame)
	{
		NMEADataFrame data = new NMEADataFrame();
		
		data.frame = frame;
		
		String[] split = frame.split("\n");
		
		for(int i = 0; i < split.length; i++)
		{
			parseMWV(data, split[i]);
			parseMWD(data, split[i]);
			parseRMC(data, split[i]);
			parseRMB(data, split[i]);
			parseVHW(data, split[i]);
			parseGLL(data, split[i]);
		}
		//An idea that we may never use:
		//calculateTrueValues(data);
		
		return data;
	}

	

	/**
	 * Parses MWD NMEA0183 sentence if found in the given frame and put information into the data object
	 * 
	 * MWD Wind Direction and Speed (TWD °M / °T and TWS) 
     * $IIMWD,357,T,000,M,8.00,N,4.11,M*49\r\n
     * 
	 * @param data Object that is being filled with the parsed information
	 * @param frame NMEA0183 formatted string
	 */
	private static void parseMWD(NMEADataFrame data, String frame) {
				
		if(frame.contains("MWD"))
		{//check that we are indeed receiving the correct sentence
			
			
		}
	}

	/**
	 * Parses RMB NMEA0183 sentence if found in the given frame and put information into the data object
	 * RMB Recommended minimum navigation information
     * $IIRMB,A,2.07,R,,WL-MARK2,4150.19,N,08730.43,W,2.09,105,1269.13,V,D*7D\r\n
	 * 
	 * @param data Object that is being filled with the parsed information
	 * @param frame NMEA0183 formatted string
	 */
	private static void parseRMB(NMEADataFrame data, String frame) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Parses VHW NMEA0183 sentence if found in the given frame and put information into the data object
	 * VHW Water Speed and Heading (°M / °T)
     * $IIVHW,322,T,325,M,5.92,N,10.97,K*63\r\n
	 * 
	 * @param data Object that is being filled with the parsed information
	 * @param frame NMEA0183 formatted string
	 */
	private static void parseVHW(NMEADataFrame data, String frame) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Parses GLL NMEA0183 sentence if found in the given frame and put information into the data object
	 * 
     * $IIGLL,4151.40,N,08734.88,W,140012.00,A,D*62\r\n
	 * 
	 * @param data Object that is being filled with the parsed information
	 * @param frame NMEA0183 formatted string
	 */
	private static void parseGLL(NMEADataFrame data, String frame) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Parses RMC NMEA0183 sentence if found in the given frame and put information into the data object
	 * 
     * RMC Recommended minimum specific GNSS data
     * $IIRMC,183701.00,A,4150.76,N,08733.12,W,6.20,330,,003,W,D*11\r\n
	 * 
	 * @param data Object that is being filled with the parsed information
	 * @param frame NMEA0183 formatted string
	 */
	private static void parseRMC(NMEADataFrame data, String frame) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Parses MWV NMEA0183 sentence if found in the given frame and put information into the data object
	 * 
	 * MWV Wind Speed and Angle (AWS and AWA, flag set to R)
     * $IIMWV,228,T,8.00,N,A*15\r\n
     * 
	 * @param data Object that is being filled with the parsed information
	 * @param frame NMEA0183 formatted string
	 */
	private static void parseMWV(NMEADataFrame data, String frame) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * If possible uses the measurement given by the instruments and calculate the true readings
	 * For example True Wind Speed can be calculated using AWA, AWS, SOG
	 * Another example True Wind Angle can be calculated using AWA, AWS, SOG, HDG
	 * 
	 * If true data is already existing in the data object, these values will be overriden with the ones calcualted here
	 * 
	 * @param data
	 */
	private static void calculateTrueValues(NMEADataFrame data) {
		// TODO Auto-generated method stub
		
	}
	
}
