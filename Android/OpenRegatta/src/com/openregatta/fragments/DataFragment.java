package com.openregatta.fragments;

import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.openregatta.R;
import com.openregatta.services.NMEADataFrame;
import com.openregatta.tools.Tools;

/**
 * This fragment is intended to provide a few parameters from the received
 * NMEA information in order to make sure that all data is received correctly
 * This frame should probably not be used in production 
 *
 * @author ddieffenthaler
 *
 */
public class DataFragment extends RegattaFragment {


	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.data, container, false);
    
        super.attachDetector(rootView);
        
        return rootView;
	}
	
	@Override 
	public void Update(NMEADataFrame frame)
	{
		TextView date = (TextView) getView().findViewById(R.id.textViewDate);
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		date.setText(dateformat.format(frame.dateTime.getTime()));
		
		TextView latitude = (TextView) getView().findViewById(R.id.textViewLAT);
		latitude.setText(Double.toString(frame.attitude.LatitudeN));
		
		TextView longitude = (TextView) getView().findViewById(R.id.textViewLON);
		longitude.setText(Double.toString(frame.attitude.LongitudeE));
		
		TextView sow = (TextView) getView().findViewById(R.id.textViewSOW);
		sow.setText(Double.toString(Tools.MetersSecondToKnots(frame.attitude.SpeedOverWater)));
		
		TextView sog = (TextView) getView().findViewById(R.id.textViewSOG);
		sog.setText(Double.toString(Tools.MetersSecondToKnots(frame.attitude.SpeedOverGround)));
		
		
		
		TextView time = (TextView) getView().findViewById(R.id.textViewTime);
		SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
		time.setText(timeformat.format(frame.dateTime.getTime()));
		
		if(frame.wind.ApparentWindAngle != -1){
			TextView awa = (TextView) getView().findViewById(R.id.textViewAWA);
			awa.setText(Double.toString(frame.wind.ApparentWindAngle));
		}
		
		if(frame.wind.ApparentWindSpeed != -1){
			TextView aws = (TextView) getView().findViewById(R.id.textViewAWS);
			aws.setText(Double.toString(Tools.MetersSecondToKnots(frame.wind.ApparentWindSpeed)));
		}
		
		if(frame.wind.TrueWindDirectionT != -1){
			TextView twd = (TextView) getView().findViewById(R.id.textViewTWD);
			twd.setText(Double.toString(frame.wind.TrueWindDirectionT));
		}
		
		if(frame.wind.TrueWindSpeed != -1){
			TextView tws = (TextView) getView().findViewById(R.id.textViewTWS);
			tws.setText(Double.toString(Tools.MetersSecondToKnots(frame.wind.TrueWindSpeed)));
		}
	}
}
