package com.openregatta.services;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
		public double LatitudeN = -1;
		/** 
		 * WGS84 longitude degrees positive to the East
		 */
		public double LongitudeE = -1;
		/** 
		 * Heading WGD84 terrestrial degrees
		 */
		public double HeadingT = -1;
		/** 
		 * Heading magnetic degrees
		 */
		public double HeadingM = -1;
		/**
		 * Speed over water in meters per second
		 */
		public double SpeedOverWater = -1;
		/**
		 * Speed over ground in meters per second
		 */
		public double SpeedOverGround = -1;
	}
	

		

	public class Wind
	{
		/**
		 * Apparent wind angle degrees
		 */
		public double ApparentWindAngle = -1;
		/**
		 * Apparent wind speed in meters per second
		 */
		public double ApparentWindSpeed = -1;
		/**
		 * True wind direction WGS84 degrees terrestrial
		 */
		public double TrueWindDirectionT = -1;
		/** 
		 * True wind direction magnetic
		 */
		public double TrueWindDirectionM = -1;
		/**
		 * True wind speed in meters per second
		 */
		public double TrueWindSpeed = -1;
	}
	
	public class Course
	{
		/**
		 * Bearing to waypoint in degrees
		 */
		public double BearingToWaypoint = -1;
		/**
		 * Distance to waypoiny in meters
		 */
		public double DistanceToWaypoint = -1;
	}
}
