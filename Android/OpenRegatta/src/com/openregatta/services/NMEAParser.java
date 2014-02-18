package com.openregatta.services;

public final class NMEAParser {

	/* 
	 * Parses a string, line by line and creates a NMEADataFrame object 
	 * that contains the parsed information
	 */
	public static NMEADataFrame Parse(String frame)
	{
		NMEADataFrame formattedFrame = new NMEADataFrame();
		
		formattedFrame.frame = frame;
		
		return formattedFrame;
	}
	
}
