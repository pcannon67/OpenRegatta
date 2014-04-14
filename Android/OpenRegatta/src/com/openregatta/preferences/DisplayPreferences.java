package com.openregatta.preferences;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.openregatta.MainActivity;
import com.openregatta.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

/**
 * Fragment that helps the user selecting parameters for the network connection
 * 
 * @author ddieffenthaler
 *
 */
public class DisplayPreferences extends PreferenceFragment {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.display_preferences);
        
        // Get a reference to the application default shared preferences.
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();

    	Preference ip_pref = findPreference("display_mode");
    	if(ip_pref instanceof ListPreference){
    		ListPreference ipPref = (ListPreference) ip_pref;
    		String display_mode = sp.getString("display_mode", "auto");
    		ipPref.setValueIndex(ipPref.findIndexOfValue(display_mode));
    	}
    }
    
    @Override
    public void onStop()
    {//saves preferences and commit to the shared preference for this application
    	super.onStop();
    	
    	Preference ip_pref = findPreference("display_mode");
    	String display_mode = "auto";
    	if(ip_pref instanceof ListPreference){
    		ListPreference ipPref = (ListPreference) ip_pref;
    		display_mode = ipPref.getValue();
    	}
    	
    	 // Get a reference to the application default shared preferences.
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        
    	SharedPreferences.Editor editor = sp.edit();
    	editor.putString("display_mode", display_mode);
    	editor.commit();
    	
		float lat = sp.getFloat("lat", (float) 41.850);//get last known position or chicago's location
		float lon = sp.getFloat("lon",(float) -87.649);
		Calendar cal = GregorianCalendar.getInstance();
    	((MainActivity) getActivity()).refreshStyleMode(lat, lon, cal);
    }
}