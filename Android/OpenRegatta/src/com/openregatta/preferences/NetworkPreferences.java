package com.openregatta.preferences;

import com.openregatta.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

/**
 * Fragment that helps the user selecting parameters for the network connection
 * 
 * @author ddieffenthaler
 *
 */
public class NetworkPreferences extends PreferenceFragment {
	

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.network_preferences);
        
        // Get a reference to the application default shared preferences.
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        
    	Preference ip_pref = findPreference("pref_network_ip");
    	if(ip_pref instanceof EditTextPreference){
    		EditTextPreference ipPref = (EditTextPreference) ip_pref;
    		String ipv4 = sp.getString("ip", "192.168.1.1");
    		ipPref.setText(ipv4);
    	}
    	
    	Preference port_pref = findPreference("pref_network_port");
    	if(port_pref instanceof EditTextPreference){
    		EditTextPreference portpref = (EditTextPreference) port_pref;
    		int port = sp.getInt("port", 1703);
    		portpref.setText(Integer.toString(port));
    	}
    }
    
    @Override
    public void onStop()
    {//saves preferences and commit to the shared preference for this application
    	super.onStop();
    	
    	Preference ip_pref = findPreference("pref_network_ip");
    	String ipv4 ="";
    	if(ip_pref instanceof EditTextPreference){
    		EditTextPreference ipPref = (EditTextPreference) ip_pref;
    		ipv4 = ipPref.getText().toString();
    	}
    	
    	Preference port_pref = findPreference("pref_network_port");
    	int port = -1;
    	if(port_pref instanceof EditTextPreference){
    		EditTextPreference portpref = (EditTextPreference) port_pref;
    		port = Integer.parseInt(portpref.getText().toString());
    	}
  
    	 // Get a reference to the application default shared preferences.
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
    	SharedPreferences.Editor editor = sp.edit();
    	editor.putString("ip", ipv4);
    	editor.putInt("port", port);
    	
    	editor.commit();
    }
}