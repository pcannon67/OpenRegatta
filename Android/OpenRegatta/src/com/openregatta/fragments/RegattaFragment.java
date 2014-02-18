package com.openregatta.fragments;

import com.openregatta.MainActivity;
import com.openregatta.services.NMEADataFrame;

import android.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


public abstract class RegattaFragment extends Fragment {

	static private final String TAG = "OpenRegatta";

	public void attachDetector(View rootView) {
		
		final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                        float velocityY) {
                        Log.i(TAG, "onFling has been called!");
                        final int SWIPE_MIN_DISTANCE = 120;
                        final int SWIPE_MAX_OFF_PATH = 250;
                        final int SWIPE_THRESHOLD_VELOCITY = 200;
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                                return false;
                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.i(TAG, "Right to Left");
                                ((MainActivity)getActivity()).ShowNextFragment();
                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.i(TAG, "Left to Right");
                                ((MainActivity)getActivity()).ShowPreviousFragment();
                            }
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        rootView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gesture.onTouchEvent(event);
                }
            });
        
	}

	public abstract void Update(NMEADataFrame frame);
	
}
