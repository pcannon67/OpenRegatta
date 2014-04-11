package com.openregatta.fragments;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import com.openregatta.MainActivity;
import com.openregatta.R;
import com.openregatta.database.PerfRow;
import com.openregatta.services.NMEADataFrame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Keep calculating target using the theorical predicted performance polars
 * 
 * @author ddieffenthaler
 *
 */
public class TargetFragment extends RegattaFragment {
	
	private List<PerfRow> bestPerformances = null;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.target, container, false);
        
        super.attachDetector(rootView);
        
        return rootView;
    }
	
	@Override 
	public void Update(NMEADataFrame frame)
	{
		if(bestPerformances == null)
			bestPerformances = ((MainActivity)getActivity()).getBestPerformance();
		if(bestPerformances != null && frame!= null){
			
			boolean isDownwind = false;
			if(frame.wind.TrueWindAngle != -1){
				if(frame.wind.TrueWindAngle > 90)//we're going downwind
					isDownwind = true;
			}
			else if(frame.wind.ApparentWindAngle != -1){
				if(frame.wind.ApparentWindAngle > 50)//we're going downwind
					isDownwind = true;
			}
				
			if(frame.wind.TrueWindSpeed != -1)
			{
				Dictionary dict = new Hashtable();
				double twsJustAbove = Double.MAX_VALUE;
				double twsJustBelow = 0;
				for(int i = 0; i <= bestPerformances.size(); i++){
					PerfRow row = bestPerformances.get(i);
					if(row.getSail().equals("Dn") == isDownwind){
						if(row.getTws() >= frame.wind.TrueWindSpeed && row.getTws() < twsJustAbove)
							twsJustAbove = row.getTws();
						else if(row.getTws() < frame.wind.TrueWindSpeed && row.getTws() > twsJustBelow)
							twsJustBelow = row.getTws();
						dict.put(row.getTws(), row);
					}
				}
				
				PerfRow above;
				if(twsJustAbove != Double.MAX_VALUE)
					 above = (PerfRow)dict.get(twsJustAbove);
				else
					above = new PerfRow(-1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, "", true, -1);
				PerfRow below;
				if(twsJustBelow != 0)
					 below = (PerfRow)dict.get(twsJustBelow);
				else
					below = new PerfRow(-1,
							frame.wind.TrueWindSpeed, 
							frame.wind.TrueWindAngle,
							frame.attitude.SpeedOverWater,
							0, 0, 1, 1, 
							frame.wind.ApparentWindSpeed,
							frame.wind.ApparentWindAngle, 0, "", true, -1);
				
				double coefficientSpeed = (above.getV() - below.getV())/(above.getTws()-below.getTws());
				double targetSpeed = coefficientSpeed * frame.wind.TrueWindSpeed;
				
				double coefficientAwa = (above.getAwa() - below.getAwa())/(above.getTws()-below.getTws());
				double targetAwa = coefficientAwa * frame.wind.TrueWindSpeed;
				
				
				
			}
		}
		
	}
	
}
