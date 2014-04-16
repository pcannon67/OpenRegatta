package com.openregatta.fragments;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import com.openregatta.MainActivity;
import com.openregatta.R;
import com.openregatta.database.DataHelper;
import com.openregatta.database.PerfRow;
import com.openregatta.services.NMEADataFrame;
import com.openregatta.tools.Tools;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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
	
	private final String[] options = {"d AWA","% SOW","% VMG"};
	
	private int preferedTop = 0;//awa option
	private int preferedBottom = 1;//sow option
	
	private double differentAwa = Double.MIN_VALUE;
	private double percentSow = Double.MIN_VALUE;
	private double percentVmg = Double.MIN_VALUE;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.target, container, false);
        
        //get best perfromance for the selected boat and database
        if(bestPerformances == null)
			bestPerformances = ((MainActivity)getActivity()).getBestPerformance();
        
        //get the parameters display from the user preferences
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferedTop = sp.getInt("target_topdisplay", 0);//get last known position or chicago's location
        preferedBottom = sp.getInt("target_bottomdisplay", 1);
        
        //set tap listener for top and bottom labels, allow to change what is shown
        TextView top_label = (TextView) rootView.findViewById(R.id.label_target_top_label);
        View.OnClickListener topHandler = new View.OnClickListener() {
            public void onClick(View v) {
            	preferedTop = (preferedTop+1)%3;
            	setDisplayValues();
            	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
    			SharedPreferences.Editor editor = sp.edit();
    			editor.putInt("target_topdisplay", preferedTop);
    			editor.commit();
            }
          };
        top_label.setOnClickListener(topHandler);
        
        TextView bot_label = (TextView) rootView.findViewById(R.id.label_target_bottom_label);
        View.OnClickListener botHandler = new View.OnClickListener() {
            public void onClick(View v) {
            	preferedBottom = (preferedBottom+1)%3;
            	setDisplayValues();
            	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
    			SharedPreferences.Editor editor = sp.edit();
    			editor.putInt("target_bottomdisplay", preferedBottom);
    			editor.commit();
            }
          };
        bot_label.setOnClickListener(botHandler);
        
        
        super.attachDetector(rootView);
        
        return rootView;
    }
	
	@Override 
	public void Update(NMEADataFrame frame)
	{
		this.calculateTargets(frame);
		
		this.setDisplayValues();
	}
	
	private void calculateTargets(NMEADataFrame frame){
		
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
					
					if(frame.attitude.SpeedOverWater != -1){
						double slopeSpeed = (above.getV() - below.getV())/(above.getTws() - below.getTws());
						double constantSpeed = below.getV() - slopeSpeed * below.getTws();
						double targetSpeed = slopeSpeed * Tools.MetersSecondToKnots(frame.wind.TrueWindSpeed) + constantSpeed;

						percentSow = (Tools.MetersSecondToKnots(frame.attitude.SpeedOverWater) / targetSpeed)*100;
						
						Log.i(TargetFragment.class.getName(), "Calculated SOW, target=" + String.format("%.0f", targetSpeed)
						+ " Actual=" + String.format("%.0f", Tools.MetersSecondToKnots(frame.attitude.SpeedOverWater))
						+ " TWS=" + String.format("%.0f",Tools.MetersSecondToKnots(frame.wind.TrueWindSpeed))
						+ " Orientation=" + (isDownwind?"Downind":"Upwind"));
					}
					
					if(frame.wind.TrueWindAngle != -1 && frame.attitude.SpeedOverWater != -1){
					double slopeSpeed = (above.getV() - below.getV())/(above.getTws() - below.getTws());
						double constantSpeed = below.getV() - slopeSpeed * below.getTws();
						double targetSpeed = slopeSpeed * Tools.MetersSecondToKnots(frame.wind.TrueWindSpeed) + constantSpeed;
						
						double slopeTwa = (above.getTwa() - below.getTwa())/(above.getTws() - below.getTws());
						double constantTwa = below.getTwa() - slopeTwa * below.getTws();
						double targetTwa = slopeTwa * Tools.MetersSecondToKnots(frame.wind.TrueWindSpeed) + constantTwa;
								
						double twaCorrected = frame.wind.TrueWindAngle;
						if(twaCorrected > 180)
							twaCorrected = 360 - twaCorrected;
						
						double targetVmg = targetSpeed * Math.sin(targetTwa*2*Math.PI/360);
						double currentVmg = Tools.MetersSecondToKnots(frame.attitude.SpeedOverWater) * Math.sin(twaCorrected*2*Math.PI/360);
						
						percentVmg = (currentVmg/targetVmg)*100;
						
						Log.i(TargetFragment.class.getName(), "Calculated VMG, target=" + String.format("%.0f", targetVmg)
								+ " Actual=" + String.format("%.0f", currentVmg)
								+ " TWS=" + String.format("%.0f",Tools.MetersSecondToKnots(frame.wind.TrueWindSpeed))
								+ " Orientation=" + (isDownwind?"Downind":"Upwind"));
					}
					
					if(frame.wind.ApparentWindAngle != -1){
						double slopeAwa = (above.getAwa() - below.getAwa())/(above.getTws()-below.getTws());
						double constantAwa = below.getAwa() - slopeAwa * below.getTws();
						double targetAwa = slopeAwa * Tools.MetersSecondToKnots(frame.wind.TrueWindSpeed) + constantAwa;
						
						double awaCorrected = frame.wind.ApparentWindAngle;
						if(awaCorrected > 180)
							awaCorrected = 360 - awaCorrected;
						differentAwa = targetAwa - awaCorrected;
						Log.i(TargetFragment.class.getName(), "Calculated AWA, target=" + String.format("%.0f", targetAwa)
								+ " Actual=" + String.format("%.0f", awaCorrected)
								+ " TWS=" + String.format("%.0f",Tools.MetersSecondToKnots(frame.wind.TrueWindSpeed))
								+ " Orientation=" + (isDownwind?"Downind":"Upwind"));
					}
				}
			}
		}
	}

	private void setDisplayValues(){
		
		String dAwa = "--";
		if(differentAwa != Double.MIN_VALUE){
			dAwa = String.format("%.0f", differentAwa);
			if((int)differentAwa>0)
				dAwa = "+" + dAwa;
		}
		
		String pSow = "--";
		if(percentSow != Double.MIN_VALUE){
			pSow = String.format("%.0f%%",percentSow);
		}
		
		String pVmg = "--";
		if(percentVmg != Double.MIN_VALUE){
			pVmg = String.format("%.0f%%",percentVmg);
		}
		
		
		//set top display
		if(getView()!=null){//avoids screen rotation issues
			TextView top_label = (TextView) getView().findViewById(R.id.label_target_top_label);
			TextView top_value = (TextView) getView().findViewById(R.id.textView_target_top_value);
			if(preferedTop == 0){
				top_label.setText(options[preferedTop]);
				top_value.setText(dAwa);
			}
			else if(preferedTop == 1){
				top_label.setText(options[preferedTop]);
				top_value.setText(pSow);
			}
			else if(preferedTop == 2){
				top_label.setText(options[preferedTop]);
				top_value.setText(pVmg);
			}
				
			//set bottom display
			TextView bot_label = (TextView) getView().findViewById(R.id.label_target_bottom_label);
			TextView bot_value = (TextView) getView().findViewById(R.id.textView_target_bottom_value);
			if(preferedBottom == 0){
				bot_label.setText(options[preferedBottom]);
				bot_value.setText(dAwa);
			}
			else if(preferedBottom == 1){
				bot_label.setText(options[preferedBottom]);
				bot_value.setText(pSow);
			}
			else if(preferedBottom == 2){
				bot_label.setText(options[preferedBottom]);
				bot_value.setText(pVmg);
			}
		}
	}
}
