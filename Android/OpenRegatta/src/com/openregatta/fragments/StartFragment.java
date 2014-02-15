package com.openregatta.fragments;

import com.openregatta.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StartFragment extends MotionFragment {


	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.start, container, false);
    
        super.attachDetector(rootView);
        
        return rootView;
    }

}
