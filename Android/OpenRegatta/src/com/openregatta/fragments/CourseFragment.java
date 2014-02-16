package com.openregatta.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openregatta.R;

public class CourseFragment extends MotionFragment {


	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.course, container, false);
    
        super.attachDetector(rootView);
        
        return rootView;
	}
}
