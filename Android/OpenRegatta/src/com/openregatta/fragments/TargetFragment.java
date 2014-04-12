package com.openregatta.fragments;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import com.openregatta.MainActivity;
import com.openregatta.R;
import com.openregatta.database.PerfRow;
import com.openregatta.services.NMEADataFrame;
import com.openregatta.tools.Tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
				if(frame.wind.ApparentWindAngle > 180 && 360 - frame.wind.ApparentWindAngle > 50
						|| frame.wind.ApparentWindAngle < 180 && frame.wind.ApparentWindAngle > 50)//we're going downwind
					isDownwind = true;
			}
				
			if(Tools.MetersSecondToKnots(frame.wind.TrueWindSpeed) != -1)
			{
				Dictionary<Float, PerfRow> dict = new Hashtable<Float, PerfRow>();
				float twsJustAbove = Float.MAX_VALUE;
				float twsJustBelow = 0;
				for(int i = 0; i < bestPerformances.size(); i++){
					PerfRow row = bestPerformances.get(i);
					if(row.getSail().equals("Dn") == isDownwind){
						if(row.getTws() >= Tools.MetersSecondToKnots(frame.wind.TrueWindSpeed) && row.getTws() < twsJustAbove)
							twsJustAbove = row.getTws();
						else if(row.getTws() < Tools.MetersSecondToKnots(frame.wind.TrueWindSpeed) && row.getTws() > twsJustBelow)
							twsJustBelow = row.getTws();
						dict.put(row.getTws(), row);
					}
				}
				
				PerfRow above;
				if(twsJustAbove != Double.MAX_VALUE)
					 above = (PerfRow)dict.get((float)twsJustAbove);
				else
					 above = new PerfRow(-1,
							 Tools.MetersSecondToKnots(frame.wind.TrueWindSpeed), 
							frame.wind.TrueWindAngle,
							Tools.MetersSecondToKnots(frame.attitude.SpeedOverWater),
							0, 0, 1, 1, 
							frame.wind.ApparentWindSpeed,
							frame.wind.ApparentWindAngle, 0, "", true, -1);
				PerfRow below;
				if(twsJustBelow != 0)
					 below = (PerfRow)dict.get((float)twsJustBelow);
				else
					 below = new PerfRow(-1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, "", true, -1);
					
				if(above != null && below != null){
					double slopeSpeed = (above.getV() - below.getV())/(above.getTws()-below.getTws());
					double constantSpeed = below.getV() - slopeSpeed * below.getTws();
					double targetSpeed = slopeSpeed * Tools.MetersSecondToKnots(frame.wind.TrueWindSpeed) + constantSpeed;
				
					TextView sow = (TextView) getView().findViewById(R.id.textView_target_sow);
					if(frame.attitude.SpeedOverWater != -1){
						double percentSow = (Tools.MetersSecondToKnots(frame.attitude.SpeedOverWater) / targetSpeed)*100;
						sow.setText(String.format("%.0f%%",percentSow));
					}
					
					double slopeAwa = (above.getAwa() - below.getAwa())/(above.getTws()-below.getTws());
					double constantAwa = below.getAwa() - slopeAwa * below.getTws();
					double targetAwa = slopeAwa * Tools.MetersSecondToKnots(frame.wind.TrueWindSpeed) + constantAwa;
					
					TextView awa = (TextView) getView().findViewById(R.id.textView_target_awa);
					if(frame.wind.ApparentWindAngle != -1){
						double awaCorrected = frame.wind.ApparentWindAngle;
						if(awaCorrected > 180)
							awaCorrected = 360 - awaCorrected;
						double differentAwa = targetAwa - awaCorrected;
						String value = String.format("%.0f", differentAwa);
						if(differentAwa>0)
							value = "+" + value;
						awa.setText(value);
					}
				}
			}
		}
		
	}
	
}
