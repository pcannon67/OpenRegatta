package com.openregatta.fragments;

import com.openregatta.R;
import com.openregatta.services.NMEADataFrame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Show to the user distance to the starting line as well as time to the starting line
 * 
 * @author ddieffenthaler
 *
 */
public class StartFragment extends RegattaFragment {


	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.start, container, false);
    
        super.attachDetector(rootView);
        
        return rootView;
    }
	
	@Override 
	public void Update(NMEADataFrame frame)
	{
		
		
	}

}
