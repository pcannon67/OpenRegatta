package com.openregatta.services;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.openregatta.tools.Tools;

/**
 * Parses the NMEA strings and place the data into a container object
 * 
 * @author ddieffenthaler
 *
 */
public final class NMEA0183Parser {

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
		
		//iterate over all the lines provided in the frame
		//each line represents one NMEA sentence to be parsed
		for(int i = 0; i < split.length; i++)
		{
			String sentence = split[i].replaceAll("(\\r|\\n)", ""); //removes all potential end of line characters
			
			if(validLine(sentence)){
			
				parseMWV(data, sentence);
				parseMWD(data, sentence);
				parseRMC(data, sentence);
				parseRMB(data, sentence);
				parseVHW(data, sentence);
				parseGLL(data, sentence);
				parseGGA(data, sentence);
				parseDPT(data, sentence);
				parseHDG(data, sentence);
				parseHDM(data, sentence);
				parseHDT(data, sentence);
				parseMTW(data, sentence);
				parseVLW(data, sentence);
				parseVTG(data, sentence);
			}
		}
		
		return data;
	}

	/**
	 * Parses VTG NMEA0183 sentence if found in the given frame and put information into the data object
	 * 
	 * === VTG - Track made good and Ground speed ===
	 * $--VTG,x.x,T,x.x,M,x.x,N,x.x,K,m,*hh
	 * Example : 
	 * 
	 * 1. Track Degrees
	 * 2. T = True
	 * 3. Track Degrees
	 * 4. M = Magnetic
	 * 5. Speed Knots
	 * 6. N = Knots
	 * 7. Speed Kilometers Per Hour
	 * 8. K = Kilometers Per Hour
	 * 9. FAA mode indicator (NMEA 2.3 and later)
	 * 10. Checksum
	 * 
	 * @param data
	 * @param sentence
	 */
	private static void parseVTG(NMEADataFrame data, String sentence) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Parses VLW NMEA0183 sentence if found in the given frame and put information into the data object
	 * 
	 * === VLW - Distance Traveled through Water ===
	 * $--VLW,x.x,N,x.x,N*hh
	 * Example :
	 * 
	 * 1. Total cumulative distance
	 * 2. N = Nautical Miles
	 * 3. Distance since Reset
	 * 4. N = Nautical Miles
	 * 5. Checksum
	 *  
	 * @param data
	 * @param sentence
	 */
	private static void parseVLW(NMEADataFrame data, String sentence) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Parses MTW NMEA0183 sentence if found in the given frame and put information into the data object
	 * 
	 * === MTW - Mean Temperature of Water ===
	 * $--MTW,x.x,C*hh
	 * Example : 
	 * 
	 * 1. Degrees
	 * 2. Unit of Measurement, Celcius
	 * 3. Checksum
	 *  
	 * @param data
	 * @param sentence
	 */
	private static void parseMTW(NMEADataFrame data, String sentence) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Parses HDT NMEA0183 sentence if found in the given frame and put information into the data object
	 * 
	 * === HDT - Heading - True ===
	 * $--HDT,x.x,T*hh
	 * Example : 
	 * 
	 * 1. Heading Degrees, true
	 * 2. T = True
	 * 3. Checksum
	 * 
	 * @param data
	 * @param sentence
	 */
	private static void parseHDT(NMEADataFrame data, String sentence) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Parses HDM NMEA0183 sentence if found in the given frame and put information into the data object
	 * 
	 * === HDM - Heading - Magnetic ===
	 * $--HDM,x.x,M*hh
	 * Example : 
	 * 
	 * 1. Heading Degrees, magnetic
	 * 2. M = magnetic
	 * 3. Checksum
	 * 
	 * @param data
	 * @param sentence
	 */
	private static void parseHDM(NMEADataFrame data, String sentence) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Parses HDG NMEA0183 sentence if found in the given frame and put information into the data object
	 * 
	 * === HDG - Heading - Deviation & Variation ===
	 * $--HDG,x.x,x.x,a,x.x,a*hh
	 * Example : 
	 * 
	 * 1. Magnetic Sensor heading in degrees
	 * 2. Magnetic Deviation, degrees
	 * 3. Magnetic Deviation direction, E = Easterly, W = Westerly
	 * 4. Magnetic Variation degrees
	 * 5. Magnetic Variation direction, E = Easterly, W = Westerly
	 * 6. Checksum
	 * 
	 * @param data
	 * @param sentence
	 */
	private static void parseHDG(NMEADataFrame data, String sentence) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Parses DPT NMEA0183 sentence if found in the given frame and put information into the data object
	 * 
	 * === DPT - Depth of Water ===
	 * $--DPT,x.x,x.x*hh
	 * Example : 
	 * 
	 * 1. Depth, meters
	 * 2. Offset from transducer, 
     *     positive means distance from tansducer to water line
     *     negative means distance from transducer to keel
	 * 3. Checksum
	 * 
	 * @param data
	 * @param sentence
	 */
	private static void parseDPT(NMEADataFrame data, String sentence) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Parses GGA NMEA0183 sentence if found in the given frame and put information into the data object
	 * 
	 * === GGA - Global Positioning System Fix Data ===
	 * $--GGA,hhmmss.ss,llll.ll,a,yyyyy.yy,a,x,xx,x.x,x.x,M,x.x,M,x.x,xxxx*hh
	 * Example : 
	 * 
	 * 1. Universal Time Coordinated (UTC)
	 * 2. Latitude
	 * 3. N or S (North or South)
	 * 4. Longitude
	 * 5. E or W (East or West)
	 * 6. GPS Quality Indicator,
	 *      - 0 - fix not available,
	 *      - 1 - GPS fix,
	 *      - 2 - Differential GPS fix
	 *            (values above 2 are 2.3 features)
	 *      - 3 = PPS fix
	 *      - 4 = Real Time Kinematic
	 *      - 5 = Float RTK
	 *      - 6 = estimated (dead reckoning)
	 *      - 7 = Manual input mode
	 *      - 8 = Simulation mode
	 * 7. Number of satellites in view, 00 - 12
	 * 8. Horizontal Dilution of precision (meters)
	 * 9. Antenna Altitude above/below mean-sea-level (geoid) (in meters)
	 * 10. Units of antenna altitude, meters
	 * 11. Geoidal separation, the difference between the WGS-84 earth
	 *      ellipsoid and mean-sea-level (geoid), "-" means mean-sea-level
	 *      below ellipsoid
	 * 12. Units of geoidal separation, meters
	 * 13. Age of differential GPS data, time in seconds since last SC104
	 *      type 1 or 9 update, null field when DGPS is not used
	 * 14. Differential reference station ID, 0000-1023
	 * 15. Checksum
	 * 
	 * @param data
	 * @param sentence
	 */
	private static void parseGGA(NMEADataFrame data, String sentence) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Parses MWD NMEA0183 sentence if found in the given frame and put information into the data object
	 * 
	 * MWD Wind Direction and Speed (TWD °M / °T and TWS) 
     * Example : $IIMWD,357,T,000,M,8.00,N,4.11,M*49
     * 
     * 1. Wind direction, 0.0 to 359.9 degrees True, to the nearest 0.1 degree
     * 2. T = True
     * 3. Wind direction, 0.0 to 359.9 degrees Magnetic, to the nearest 0.1 
	 *    degree
     * 4. M = Magnetic
     * 5. Wind speed, knots, to the nearest 0.1 knot.
     * 6. N = Knots
     * 7. Wind speed, meters/second, to the nearest 0.1 m/s.
     * 8. M = Meters/second
     * 
	 * @param data Object that is being filled with the parsed information
	 * @param frame NMEA0183 formatted string
	 */
	private static void parseMWD(NMEADataFrame data, String frame) {
				
		 String[] dataSplit = frame.split(",");
         if (dataSplit[0].toLowerCase().contains("mwd"))
         {
        	 float tempValue;
             if (dataSplit.length >= 3 &&
                 (tempValue = Tools.tryParse(dataSplit[1])) != -1
                 && dataSplit[2].toLowerCase().equals("t")){
                 data.wind.TrueWindDirectionT = tempValue;
             }
             if (dataSplit.length >= 5 &&
                 (tempValue = Tools.tryParse(dataSplit[3])) != -1
                 && dataSplit[4].toLowerCase().equals("m")){
                 data.wind.TrueWindDirectionM = tempValue;
             }
             if (dataSplit.length >= 7 &&
                 (tempValue = Tools.tryParse(dataSplit[5])) != -1
                 && dataSplit[6].toLowerCase().equals("n")){
                 data.wind.TrueWindSpeed = Tools.KnotsToMetersSecond(tempValue);
             }
             if (dataSplit.length >= 9 &&
                 (tempValue = Tools.tryParse(dataSplit[7])) != -1
                 && dataSplit[8].toLowerCase().equals("m")){
                 data.wind.TrueWindSpeed = tempValue;
             }
         }
	}

	/**
	 * Parses RMB NMEA0183 sentence if found in the given frame and put information into the data object
     * 
     * === RMB - Recommended Minimum Navigation Information ===
     * Example : $IIRMB,A,2.07,R,,WL-MARK2,4150.19,N,08730.43,W,2.09,105,1269.13,V,D*7D\r\n
	 * 
	 * 1. Status, A= Active, V = Void
	 * 2. Cross Track error - nautical miles
	 * 3. Direction to Steer, Left or Right
	 * 4. TO Waypoint ID
	 * 5. FROM Waypoint ID
	 * 6. Destination Waypoint Latitude
	 * 7. N or S
	 * 8. Destination Waypoint Longitude
	 * 9. E or W
	 * 10. Range to destination in nautical miles
	 * 11. Bearing to destination in degrees True
	 * 12. Destination closing velocity in knots
	 * 13. Arrival Status, A = Arrival Circle Entered
	 * 14. FAA mode indicator (NMEA 2.3 and later)
	 * 15. Checksum
	 * 
	 * @param data Object that is being filled with the parsed information
	 * @param frame NMEA0183 formatted string
	 */
	private static void parseRMB(NMEADataFrame data, String frame) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Parses VHW NMEA0183 sentence if found in the given frame and put information into the data object
	 * 
	 * === VHW - Water speed and heading ===
	 * $--VHW,x.x,T,x.x,M,x.x,N,x.x,K*hh
     * Example : $IIVHW,322,T,325,M,5.92,N,10.97,K*63
	 * 
	 * 1. Degress True
	 * 2. T = True
	 * 3. Degrees Magnetic
	 * 4. M = Magnetic
	 * 5. Knots (speed of vessel relative to the water)
	 * 6. N = Knots
	 * 7. Kilometers (speed of vessel relative to the water)
	 * 8. K = Kilometers
	 * 9. Checksum
	 * 
	 * @param data Object that is being filled with the parsed information
	 * @param frame NMEA0183 formatted string
	 */
	private static void parseVHW(NMEADataFrame data, String frame) {
		
		 String[] dataSplit = frame.split(",");
         if (dataSplit[0].toLowerCase().contains("vhw"))
         {
        	 float tempValue;
             if (dataSplit.length >= 3 &&
                 (tempValue = Tools.tryParse(dataSplit[1])) != -1
                 && dataSplit[2].toLowerCase().equals("t")){
                 data.attitude.HeadingT = tempValue;
             }
             if (dataSplit.length >= 5 &&
                 (tempValue = Tools.tryParse(dataSplit[3])) != -1
                 && dataSplit[4].toLowerCase().equals("m")){
                 data.attitude.HeadingM = tempValue;
             }
             if (dataSplit.length >= 7 &&
                 (tempValue = Tools.tryParse(dataSplit[5])) != -1
                 && dataSplit[6].toLowerCase().equals("n")){
                 data.attitude.SpeedOverWater = Tools.KnotsToMetersSecond(tempValue);
             }
         }
	}

	/**
	 * Parses GLL NMEA0183 sentence if found in the given frame and put information into the data object
	 * 
	 * === GLL - Geographic Position - Latitude/Longitude ===
	 * $--GLL,llll.ll,a,yyyyy.yy,a,hhmmss.ss,a,m,*hh
     * Example : $IIGLL,4151.40,N,08734.88,W,140012.00,A,D*62
	 * 
	 * 1. Latitude
	 * 2. N or S (North or South)
	 * 3. Longitude
	 * 4. E or W (East or West)
	 * 5. Universal Time Coordinated (UTC)
	 * 6. Status A - Data Valid, V - Data Invalid
	 * 7. FAA mode indicator (NMEA 2.3 and later)
	 * 8. Checksum
	 * 
	 * @param data Object that is being filled with the parsed information
	 * @param frame NMEA0183 formatted string
	 */
	private static void parseGLL(NMEADataFrame data, String frame) {
		
		 String[] dataSplit = frame.split(",");
         if (dataSplit[0].toLowerCase().contains("gll"))
         {
        	 float tempValue;
             if (dataSplit.length >= 3 &&
                 (tempValue = Tools.tryParse(dataSplit[1])) != -1
                 && dataSplit[2].toLowerCase().equals("n")){
            	 float minutes = (tempValue % 100);
            	 float degrees = (float)(tempValue / 100) % 100;
             	
                data.attitude.LatitudeN = Tools.DegreeMinutesToDegree(degrees, minutes);
             }
             if (dataSplit.length >= 5 &&
                 (tempValue = Tools.tryParse(dataSplit[3])) != -1
                 && dataSplit[4].toLowerCase().equals("w")){
            	 float minutes = (tempValue % 100);
            	 float degrees = (int)(tempValue / 100) % 100;
            	 
                 data.attitude.LongitudeE = -Tools.DegreeMinutesToDegree(degrees, minutes);
             }
             if (dataSplit.length >= 3 &&
                 (tempValue = Tools.tryParse(dataSplit[1])) != -1
                 && dataSplit[2].toLowerCase().equals("s")){
            	 float minutes = (tempValue % 100);
            	 float degrees = (int)(tempValue / 100) % 100;
            	 
                 data.attitude.LatitudeN = -Tools.DegreeMinutesToDegree(degrees, minutes);
             }
             if (dataSplit.length >= 5 &&
                 (tempValue = Tools.tryParse(dataSplit[3])) != -1
                 && dataSplit[4].toLowerCase().equals("e")){
            	 float minutes = (tempValue % 100);
            	 float degrees = (int)(tempValue / 100) % 100;
            	 
                 data.attitude.LongitudeE = Tools.DegreeMinutesToDegree(degrees, minutes);
             }
         }
	}

	/**
	 * Parses RMC NMEA0183 sentence if found in the given frame and put information into the data object
	 * 
	 * === RMC - Recommended Minimum Navigation Information ===
	 * $--RMC,hhmmss.ss,A,llll.ll,a,yyyyy.yy,a,x.x,x.x,xxxx,x.x,a,m,*hh<CR><LF>
	 * Example : $IIRMC,183701.00,A,4150.76,N,08733.12,W,6.20,330,,003,W,D*11\r\n
	 *  
	 * 1. UTC Time
	 * 2. Status, V=Navigation receiver warning A=Valid
	 * 3. Latitude
	 * 4. N or S
	 * 5. Longitude
	 * 6. E or W
	 * 7. Speed over ground, knots
	 * 8. Track made good, degrees true
	 * 9. Date, ddmmyy
	 * 10. Magnetic Variation, degrees
	 * 11. E or W
	 * 12. FAA mode indicator (NMEA 2.3 and later)
	 * 13. Checksum
	 *  
	 * @param data Object that is being filled with the parsed information
	 * @param frame NMEA0183 formatted string
	 */
	private static void parseRMC(NMEADataFrame data, String frame) {
		
		String[] dataSplit = frame.split(",");
        if (dataSplit[0].toLowerCase().contains("rmc"))
        {
        	float tempValue;
            if (dataSplit.length >= 2 && 
                (tempValue = Tools.tryParse(dataSplit[1])) != -1){
            	int seconds = (int)(tempValue % 100);
            	int minutes = (int)(tempValue / 100) % 100;
            	int hours = (int)(tempValue /10000);
            
            	if(data.dateTime == null)
            		data.dateTime = new GregorianCalendar();
            	
            	data.dateTime.set(Calendar.SECOND, seconds);
            	data.dateTime.set(Calendar.MINUTE, minutes);
            	data.dateTime.set(Calendar.HOUR, hours);
            	
            }
            if (dataSplit.length >= 5 &&
                (tempValue = Tools.tryParse(dataSplit[3])) != -1
                && dataSplit[2].toLowerCase().equals("n")){
            	float minutes = (tempValue % 100);
            	float degrees = (int)(tempValue / 100) % 100;
            	
                data.attitude.LatitudeN = Tools.DegreeMinutesToDegree(degrees, minutes);
            }
            if (dataSplit.length >= 5 &&
                (tempValue = Tools.tryParse(dataSplit[3])) != -1
                && dataSplit[2].toLowerCase().equals("s")){
            	float minutes = (tempValue % 100);
            	float degrees = (int)(tempValue / 100) % 100;
            	
                data.attitude.LatitudeN = -Tools.DegreeMinutesToDegree(degrees, minutes);
            }
            if (dataSplit.length >= 7 &&
                (tempValue = Tools.tryParse(dataSplit[5])) != -1
                && dataSplit[4].toLowerCase().equals("w")){
            	float minutes = (tempValue % 100);
            	float degrees = (int)(tempValue / 100) % 100;
             	 
                data.attitude.LongitudeE = -Tools.DegreeMinutesToDegree(degrees, minutes);
            }
            if (dataSplit.length >= 7 &&
                (tempValue = Tools.tryParse(dataSplit[5])) != -1
                && dataSplit[4].toLowerCase().equals("e")){
            	float minutes = (tempValue % 100);
            	float degrees = (int)(tempValue / 100) % 100;
            	
                data.attitude.LongitudeE = Tools.DegreeMinutesToDegree(degrees, minutes);
            }
            if (dataSplit.length >= 8 &&
                (tempValue = Tools.tryParse(dataSplit[7])) != -1){
                data.attitude.SpeedOverGround = Tools.KnotsToMetersSecond(tempValue);
            }
            if (dataSplit.length >= 10 &&
                    (tempValue = Tools.tryParse(dataSplit[9])) != -1){
            	int year = (int)(tempValue % 100);
            	int month = (int)(tempValue / 100) % 100;
            	int day = (int)(tempValue /10000);
            
            	if(data.dateTime == null)
            		data.dateTime = new GregorianCalendar();
            	
            	data.dateTime.set(year, month, day);
            }
        }
	}

	/**
	 * Parses MWV NMEA0183 sentence if found in the given frame and put information into the data object
	 * 
	 * === MWV - Wind Speed and Angle ===
	 * $--MWV,x.x,a,x.x,a*hh
     * Example : $IIMWV,228,T,8.00,N,A*15\r\n
     * 
     * 1) Wind Angle, 0 to 360 degrees
     * 2) Reference, R = Relative, T = True (if R then angle and speed are relative, if T then angle and speed are Theorical)
	 * 3) Wind Speed
	 * 4) Wind Speed Units, K = km/hr, M = m/s, N = knots, S = statute 
	 * 5) Status, A = Data Valid
	 * 6) Checksum
	 * 
	 * @param data Object that is being filled with the parsed information
	 * @param frame NMEA0183 formatted string
	 */
	private static void parseMWV(NMEADataFrame data, String frame) {
		//this needs to be fixed, in order to account for the 'r' relative or 't' theorical true wind readings
		String[] dataSplit = frame.split(",");
        if (dataSplit[0].toLowerCase().contains("mwv"))
        {
            
        	float tempValue;
            if(dataSplit[2].toLowerCase().equals("t"))
            {//true wind readings
            	if((tempValue = Tools.tryParse(dataSplit[3])) != -1)
            		if(dataSplit[4].toLowerCase().equals("n"))
            			data.wind.TrueWindSpeed  = Tools.KnotsToMetersSecond(tempValue);
            		if(dataSplit[4].toLowerCase().equals("k"))
            			data.wind.TrueWindSpeed  = Tools.KphToMetersSecond(tempValue);
            		if(dataSplit[4].toLowerCase().equals("m"))
            			data.wind.TrueWindSpeed  = tempValue;
            		if(dataSplit[4].toLowerCase().equals("s"))
            			data.wind.TrueWindSpeed  = Tools.StatuteMileToMetersSecond(tempValue);
            	if((tempValue = Tools.tryParse(dataSplit[1])) != -1)
            		data.wind.TrueWindAngle = tempValue;
            	
            }
            else if (dataSplit[2].toLowerCase().equals("r"))
            {//apparent wind readings
            	if((tempValue = Tools.tryParse(dataSplit[3])) != -1)
            		if(dataSplit[4].toLowerCase().equals("n"))
            			data.wind.ApparentWindSpeed  = Tools.KnotsToMetersSecond(tempValue);
            		if(dataSplit[4].toLowerCase().equals("k"))
            			data.wind.ApparentWindSpeed  = Tools.KphToMetersSecond(tempValue);
            		if(dataSplit[4].toLowerCase().equals("m"))
            			data.wind.ApparentWindSpeed  = tempValue;
            		if(dataSplit[4].toLowerCase().equals("s"))
            			data.wind.ApparentWindSpeed  = Tools.StatuteMileToMetersSecond(tempValue);
            	if((tempValue = Tools.tryParse(dataSplit[1])) != -1)
            		data.wind.ApparentWindAngle = tempValue;
            			
            }
        }
	}
	
	/**
	 * Calculates the checksum from an NMEA0183 sentence
	 * returns the calculated checksum as a string of two characters
	 * 
	 * @param sentence must be formatted as follow : $data*
	 * the checksum will be computer from the characters between the $ and * characters
	 * @return 2-digit hex value of the computed checksum
	 */
	 public static String getChecksum(String sentence)
     {		 
		 if(sentence.length()>=5 && //two chars for $ and * three chars for the nmea code min
				 sentence.contains("$") && sentence.contains("*")){
		 
	         //Start with first Item
	         int checksum = (byte) sentence.charAt(sentence.indexOf('$') + 1);
	         // Loop through all chars to get a checksum
	         for (int i = sentence.indexOf('$') + 2; i < sentence.indexOf('*'); i++)
	         {
	             // No. XOR the checksum with this character's value
	             checksum ^= (byte) sentence.charAt(i);
	         }
	         
	         // Return the checksum formatted as a two-character hexadecimal
	         StringBuilder sb = new StringBuilder();
	         sb.append(Integer.toHexString(checksum));
	         if (sb.length() < 2) {
	             sb.insert(0, '0'); // pad with leading zero if needed
	         }
	         return sb.toString();
         }
		 else
			 return "";
     }
	 
	 /**
	  * This method checks if a sentence is valid comparing the checksum at the end of the line
	  * and the calculated checksum from the data at the beginning of the line
	  * 
	  * The method uses the data contained between $ and * to calculate the checksum
	  * then uses the two characters following the * to compare with the computed checksum
	  * 
	  * @param must be formatted as follow : $data*checksum
	  * @return
	  */
	 public static boolean validLine(String sentence)
	 {
		 String checksum = getChecksum(sentence);
         String[] split = sentence.split("\\*");
         
         return (split.length == 2 && split[1].substring(0, 2).toLowerCase().equals(checksum.toLowerCase()));
	 }
}
