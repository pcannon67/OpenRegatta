package com.openregatta.services;

public class NMEADataFrame {
	
	public String frame = "";
	
	public class Attitude
	{
		public float Latitude = -1;
		public float Longitude = -1;
		public float Heading = -1;
		public float SpeedOverWater = -1;
		public float SpeedOverGround = -1;
	}
	
	public class DateTime
	{
		public DateTime DateTimeGPS = null;
	}
	
	public class Wind
	{
		public float ApparentWindAngle = -1;
		public float ApparentWindSpeed = -1;
		public float TrueWindDirection = -1;
		public float TrueWindSpeed = -1;
	}
	
	public class Course
	{
		public float BearingToWaypoint = -1;
		public float DistanceToWaypoint = -1;
	}
}
