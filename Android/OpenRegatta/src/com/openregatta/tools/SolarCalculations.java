package com.openregatta.tools;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SolarCalculations {

	/**
	 * Calculate height of the sun above horizon for a given position and date
	 * @param lat Positive to N
	 * @param lon Positive to E
	 * @param timeZone Positive to E
	 * @param dst if daylight saving time set true
	 * @param date Local time for given time zone
	 * @return
	 */
	public static double CalculateSunHeight(double lat, double lon, Calendar cal){
		
		double adjustedTimeZone = cal.getTimeZone().getRawOffset()/3600000 + cal.getTimeZone().getDSTSavings()/3600000;
		
		double timeOfDay = (cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND))/(double)86400;
		
		double julianDay = dateToJulian(cal.getTime()) - adjustedTimeZone/24;
		
		double julianCentury = (julianDay-2451545)/36525;
		
		double geomMeanLongSun = (280.46646 + julianCentury * (36000.76983 + julianCentury * 0.0003032)) % 360;
		
		double geomMeanAnomSun = 357.52911+julianCentury*(35999.05029 - 0.0001537*julianCentury);
		
		double eccentEarthOrbit = 0.016708634-julianCentury*(0.000042037+0.0000001267*julianCentury);
		
		double sunEqOfCtr = Math.sin(Math.toRadians(geomMeanAnomSun))*(1.914602-julianCentury*(0.004817+0.000014*julianCentury))+Math.sin(Math.toRadians(2*geomMeanAnomSun))*(0.019993-0.000101*julianCentury)+Math.sin(Math.toRadians(3*geomMeanAnomSun))*0.000289;
		
		double sunTrueLong = geomMeanLongSun + sunEqOfCtr;
		
		double sunAppLong = sunTrueLong-0.00569-0.00478*Math.sin(Math.toRadians(125.04-1934.136*julianCentury));
		
		double meanObliqEcliptic = 23+(26+((21.448-julianCentury*(46.815+julianCentury*(0.00059-julianCentury*0.001813))))/60)/60;
		
		double obliqueCorr = meanObliqEcliptic+0.00256*Math.cos(Math.toRadians(125.04-1934.136*julianCentury));
		
		double sunDeclin = Math.toDegrees(Math.asin(Math.sin(Math.toRadians(obliqueCorr))*Math.sin(Math.toRadians(sunAppLong))));
		
		double varY = Math.tan(Math.toRadians(obliqueCorr/2))*Math.tan(Math.toRadians(obliqueCorr/2));
		
		double eqOfTime = 4*Math.toDegrees(varY*Math.sin(2*Math.toRadians(geomMeanLongSun))-2*eccentEarthOrbit*Math.sin(Math.toRadians(geomMeanAnomSun))+4*eccentEarthOrbit*varY*Math.sin(Math.toRadians(geomMeanAnomSun))*Math.cos(2*Math.toRadians(geomMeanLongSun))-0.5*varY*varY*Math.sin(4*Math.toRadians(geomMeanLongSun))-1.25*eccentEarthOrbit*eccentEarthOrbit*Math.sin(2*Math.toRadians(geomMeanAnomSun)));
		
		double trueSolarTime = (timeOfDay*1440+eqOfTime+4*lon-60*adjustedTimeZone) % 1440;
		
		double hourAngle;
		if(trueSolarTime/4<0)
			hourAngle = trueSolarTime/4+180;
		else
			hourAngle = trueSolarTime/4-180;
		
		double solarZenithAngle = Math.toDegrees(Math.acos(Math.sin(Math.toRadians(lat))*Math.sin(Math.toRadians(sunDeclin))+Math.cos(Math.toRadians(lat))*Math.cos(Math.toRadians(sunDeclin))*Math.cos(Math.toRadians(hourAngle))));
		
		double solarElevation = 90 - solarZenithAngle;
				
		double athmosphericRefraction;
		if(solarElevation>85)
			athmosphericRefraction = 0;
		else if(solarElevation>5)
			athmosphericRefraction = 58.1/Math.tan(Math.toRadians(solarElevation))-0.07/Math.pow(Math.tan(Math.toRadians(solarElevation)),3)+0.000086/Math.pow(Math.tan(Math.toRadians(solarElevation)),5);
		else if(solarElevation>-0.575)
			athmosphericRefraction = 1735+solarElevation*(-518.2+solarElevation*(103.4+solarElevation*(-12.79+solarElevation*0.711)));
		else
			athmosphericRefraction = -20.772/Math.tan(Math.toRadians(solarElevation));
		athmosphericRefraction /= 3600;
		
		double solarElevationCorrected = solarElevation + athmosphericRefraction;
		
		return solarElevationCorrected;
			
	}
	
	
	/**
	 * Return Julian day from date
	 * @param date
	 * @return
	 */
	public static double dateToJulian(Date date) {

		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		
		int a = (14-(calendar.get(Calendar.MONTH)+1))/12;
		int y = calendar.get(Calendar.YEAR) + 4800 - a;
		
		int m =  (calendar.get(Calendar.MONTH)+1) + 12*a;
		m -= 3;
		
		double jdn = calendar.get(Calendar.DAY_OF_MONTH) + (153.0*m + 2.0)/5.0 + 365.0*y + y/4.0 - y/100.0 + y/400.0 - 32045.0 + calendar.get(Calendar.HOUR_OF_DAY) / 24 + calendar.get(Calendar.MINUTE)/1440 + calendar.get(Calendar.SECOND)/86400;
	
		return jdn;
	} 
}
