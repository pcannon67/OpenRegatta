package com.openregatta.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openregatta.R;
import com.openregatta.services.NMEADataFrame;

/**
 * The course fragment helps the user defining the course by setting the marks for the starting line
 * and for the upwind mark
 * 
 * @author ddieffenthaler
 *
 */
public class CourseFragment extends RegattaFragment {


	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.course, container, false);
    
        super.attachDetector(rootView);
        
        return rootView;
	}
	
	@Override 
	public void Update(NMEADataFrame frame)
	{
		
		
	}
}
