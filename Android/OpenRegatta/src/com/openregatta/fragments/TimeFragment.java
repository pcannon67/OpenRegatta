package com.openregatta.fragments;

import com.openregatta.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TimeFragment extends MotionFragment  {

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
	
        View rootView = inflater.inflate(
                R.layout.timer, container, false);
     
        super.attachDetector(rootView);
        
        return rootView;
    }

}
