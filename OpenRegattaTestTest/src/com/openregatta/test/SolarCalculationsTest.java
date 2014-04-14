/**
 * 
 */
package com.openregatta.test;

import com.openregatta.tools.SolarCalculations;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
/**
 * @author User
 *
 */
public class SolarCalculationsTest extends SolarCalculations {

	public void testSunHeight(){
		
		float lat = (float) 41.85;
		float lon = (float) -87.65;
		Calendar cal = new GregorianCalendar(2010,6,21);
		cal.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
		long miliseconds = cal.getTimeInMillis();
		
		for(long offset = 0; offset < 86400000; offset+=360000)
		{		
			cal.setTimeInMillis(miliseconds + offset);
			String dateTimeLocal = cal.getTime().toLocaleString();
			String dateTimeGMT = cal.getTime().toGMTString();
			double height = CalculateSunHeight(lat, lon, cal);
		
		
		}
	}

}
