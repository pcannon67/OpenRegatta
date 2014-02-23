package com.openregatta.preferences;

import com.openregatta.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
/**
 * Fragment that helps the user setting key parameters for the boat 
 * 
 * @author ddieffenthaler
 *
 */
public class BoatPreferences extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.boat_preferences);
        
        // Get a reference to the application default shared preferences.
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        
    	Preference lenght_pref = findPreference("pref_boat_lenght");
    	if(lenght_pref instanceof EditTextPreference){
    		EditTextPreference lengthPref = (EditTextPreference) lenght_pref;
    		float length = sp.getFloat("length", 30f);
    		lengthPref.setText(Float.toString(length));
    	}
    	
    	Preference offset_pref = findPreference("pref_boat_gps_offset");
    	if(lenght_pref instanceof EditTextPreference){
    		EditTextPreference offsetPref = (EditTextPreference) offset_pref;
    		float offset = sp.getFloat("offset", 30f);
    		offsetPref.setText(Float.toString(offset));
    	}
    	
    	Preference name_pref = findPreference("pref_boat_name");
    	if(name_pref instanceof EditTextPreference){
    		EditTextPreference namePref = (EditTextPreference) name_pref;
    		String name = sp.getString("name", "Entreprise");
    		namePref.setText(name);
    	}
    }
    
    @Override
    public void onStop()
    {//saves preferences and commit to the shared preference for this application
    	
    	super.onStop();
    	
    	Preference lenght_pref = findPreference("pref_boat_lenght");
    	float length = 30f;
    	if(lenght_pref instanceof EditTextPreference){
    		EditTextPreference lengthPref = (EditTextPreference) lenght_pref;
    		length = Float.parseFloat(lengthPref.getText());
    	}
    	
    	Preference offset_pref = findPreference("pref_boat_gps_offset");
    	float offset = 30f;
    	if(lenght_pref instanceof EditTextPreference){
    		EditTextPreference offsetPref = (EditTextPreference) offset_pref;
    		offset = Float.parseFloat(offsetPref.getText());
    	}
    	
    	Preference name_pref = findPreference("pref_boat_name");
    	String name = "Entreprise";
    	if(name_pref instanceof EditTextPreference){
    		EditTextPreference namePref = (EditTextPreference) name_pref;
    		name = namePref.getText();
    	}
    	
    	 // Get a reference to the application default shared preferences.
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
    	SharedPreferences.Editor editor = sp.edit();
    	editor.putString("name", name);
    	editor.putFloat("lenght", length);
    	editor.putFloat("offset", offset);
    	editor.commit();
    }

}
