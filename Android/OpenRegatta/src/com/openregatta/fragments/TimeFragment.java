package com.openregatta.fragments;

import com.openregatta.R;
import com.openregatta.services.NMEADataFrame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Keep track of time before the start and after the start
 * 
 * @author ddieffenthaler
 *
 */
public class TimeFragment extends RegattaFragment  {

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
	
        View rootView = inflater.inflate(
                R.layout.timer, container, false);
     
        super.attachDetector(rootView);
        
        return rootView;
    }
	
	@Override 
	public void Update(NMEADataFrame frame)
	{
		
		
	}

}
