package com.openregatta;

import java.util.ArrayList;
import java.util.List;

import com.openregatta.R;
import com.openregatta.fragments.StartFragment;
import com.openregatta.fragments.TargetFragment;
import com.openregatta.fragments.TimeFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;


public class MainActivity extends Activity {

	private final List<Fragment> mItems = new ArrayList<Fragment>();
	private int frag_index = 0;
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onRestoreInstanceState(savedInstanceState);
	  // Restore UI state from the savedInstanceState.
	  // This bundle has also been passed to onCreate.
	  frag_index = savedInstanceState.getInt("fragment_index");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//removes title bar and set full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        //set layout for the main activity
		setContentView(R.layout.activity_main);
		
		//create and add fragments to the application
		mItems.add(new TimeFragment());
		mItems.add(new StartFragment());
		mItems.add(new TargetFragment());
		
		if(savedInstanceState != null)
			onRestoreInstanceState(savedInstanceState);
		else{
			SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
			frag_index = sharedPreferences.getInt("fragment_index", 0);
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		ShowCurrentFragment();
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  // Save UI state changes to the savedInstanceState.
	  // This bundle will be passed to onCreate if the process is
	  // killed and restarted.
	  savedInstanceState.putInt("fragment_index", frag_index);
	}
	
	@Override
	public void onStop(){
		super.onStop();
		
		SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
    	editor.putInt("fragment_index", frag_index);
    	editor.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		FragmentManager mFragmentManager = getFragmentManager();
	    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		switch (item.getItemId()) {
        case (R.id.boat_settings):
		    fragmentTransaction.addToBackStack(getResources().getString(R.string.boat_settings));
        	fragmentTransaction.replace(R.id.fragment_container, new BoatPreferences());
        	fragmentTransaction.commit();
        	break;
        case (R.id.network_settings):
	    	fragmentTransaction.addToBackStack(getResources().getString(R.string.network_settings));
	    	fragmentTransaction.replace(R.id.fragment_container, new NetworkPreferences());
	    	fragmentTransaction.commit();
	    	break;
		}
		return false;
	}
	
	public void ShowNextFragment()
	{
		int newIndex = (frag_index + 1) % mItems.size();
		frag_index = newIndex;
		
		FragmentManager mFragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_container, mItems.get(frag_index));
		fragmentTransaction.commit();
	}
	
	public void ShowCurrentFragment()
	{
		int newIndex = frag_index % mItems.size();
		frag_index = newIndex;
		
		FragmentManager mFragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_container, mItems.get(frag_index));
		fragmentTransaction.commit();
	}
	
	public void ShowPreviousFragment()
	{
		int newIndex = (frag_index + 1) % mItems.size();
		frag_index = newIndex;
		
		FragmentManager mFragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_container, mItems.get(frag_index));
		fragmentTransaction.commit();
	}
}
