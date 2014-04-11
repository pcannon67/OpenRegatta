package com.openregatta.services;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This class holds parsed data
 * 
 * @author ddieffenthaler
 *
 */
public class NMEADataFrame {
	
	public String frame = "";
	public Attitude attitude = new Attitude();
	public Calendar dateTime = new GregorianCalendar();
	public Wind wind = new Wind();
	public Course course = new Course();
	
	public class Attitude
	{
		/** 
		 * WGS84 latitude degrees positive to the North
		 */
		public float LatitudeN = -1;
		/** 
		 * WGS84 longitude degrees positive to the East
		 */
		public float LongitudeE = -1;
		/** 
		 * Heading WGD84 terrestrial degrees
		 */
		public float HeadingT = -1;
		/** 
		 * Heading magnetic degrees
		 */
		public float HeadingM = -1;
		/**
		 * Speed over water in meters per second
		 */
		public float SpeedOverWater = -1;
		/**
		 * Speed over ground in meters per second
		 */
		public float SpeedOverGround = -1;
	}
	

		

	public class Wind
	{
		/**
		 * Apparent wind angle degrees
		 * 0 to 360 degrees with 0 as dead ahead increasing when rotating clockwise
		 */
		public float ApparentWindAngle = -1;
		/** 
		 * True wind angle in degrees
		 * 0 to 360 degrees with 0 as dead ahead increasing when rotating clockwise
		 */
		public float TrueWindAngle = -1;
		/**
		 * Apparent wind speed in meters per second
		 */
		public float ApparentWindSpeed = -1;
		/**
		 * True wind direction WGS84 degrees terrestrial
		 */
		public float TrueWindDirectionT = -1;
		/** 
		 * True wind direction magnetic
		 */
		public float TrueWindDirectionM = -1;
		/**
		 * True wind speed in meters per second
		 */
		public float TrueWindSpeed = -1;
	}
	
	public class Course
	{
		/**
		 * Bearing to waypoint in degrees
		 */
		public float BearingToWaypoint = -1;
		/**
		 * Distance to waypoiny in meters
		 */
		public float DistanceToWaypoint = -1;
	}
}
