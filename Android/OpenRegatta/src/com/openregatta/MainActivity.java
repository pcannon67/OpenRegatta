package com.openregatta;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import com.openregatta.R;
import com.openregatta.database.DataHelper;
import com.openregatta.database.DataSource;
import com.openregatta.database.PerfRow;
import com.openregatta.fragments.DataFragment;
import com.openregatta.fragments.RegattaFragment;
import com.openregatta.fragments.TargetFragment;
import com.openregatta.preferences.BoatPreferences;
import com.openregatta.preferences.DisplayPreferences;
import com.openregatta.preferences.NetworkPreferences;
import com.openregatta.services.NetworkService;
import com.openregatta.services.NMEADataFrame;
import com.openregatta.tools.SolarCalculations;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.UiModeManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;


public class MainActivity extends Activity {
	
	static private final int PICK_DATA_FILE_REQUEST_CODE = 0;
	
	static private final int BOAT_ID = 367;
	
	/** List containing all the fragments that the user can navigate to by sliding left and right */
	private final List<Fragment> mItems = new ArrayList<Fragment>();
	/** Current index that is shown to the user */
	private int frag_index = 0;
	 /** Messenger for communicating with service. */
	private Messenger mService = null;
    /** Flag indicating whether we have called bind on the service. */
	private boolean mIsBound;
    /** Access to performance data storage and practical methods*/
	private DataSource mPerformanceDataSource = null;
    /** Best performances upwind and downwind, loaded for the selected boat from the database */
    private List<PerfRow> bestPerformances = null;
    /** Current theme selected */
    private int mThemeId = -1;
    /** 10 minutes interval for the handler checking the sun's position and adjusting the style */
    private final int interval = 600000;
    private Handler handler = new Handler();
    
    private Runnable runnable = new Runnable(){
        public void run() {
        	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        	float lat = sp.getFloat("lat", (float) 41.850);//get last known position or chicago's location
    		float lon = sp.getFloat("lon",(float) -87.649);
    		Calendar cal = GregorianCalendar.getInstance();
        	refreshStyleMode(lat, lon, cal);
        	handler.postDelayed(runnable, interval);
        }
    };
    
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
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
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        //selecting the appropriate theme if needed otherwise try to get appropriate theme
        if(savedInstanceState != null && savedInstanceState.getInt("mThemeId", -1) != -1) {
            mThemeId = savedInstanceState.getInt("mThemeId");
            this.setTheme(mThemeId);
        }
        else{
        	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        	float lat = sp.getFloat("lat", (float) 41.850);//get last known position or chicago's location
    		float lon = sp.getFloat("lon",(float) -87.649);
    		Calendar cal = GregorianCalendar.getInstance();
        	refreshStyleMode(lat, lon, cal);
        }
        
        handler.postDelayed(runnable, interval);
        
        mPerformanceDataSource = new DataSource(this);
        mPerformanceDataSource.open();
        
        if(mPerformanceDataSource.getCount() == 0)
        {//if we don't have any performance records, then open the file manager and select a file to be imported
        	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("file/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            // special intent for Samsung file manager
            Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
             // if you want any file type, you can skip next line 
            sIntent.putExtra("CONTENT_TYPE", "*/*"); 
            sIntent.addCategory(Intent.CATEGORY_DEFAULT);

            Intent chooserIntent;
            if (getPackageManager().resolveActivity(sIntent, 0) != null){
                // it is device with samsung file manager
                chooserIntent = Intent.createChooser(sIntent, "Open file");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent});
            }
            else {
                chooserIntent = Intent.createChooser(intent, "Open file");
            }

            try {
                startActivityForResult(chooserIntent, PICK_DATA_FILE_REQUEST_CODE);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getApplicationContext(), "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
        	bestPerformances = mPerformanceDataSource.getBestPerformancesForBoat(BOAT_ID);
        }
        
       
        //set layout for the main activity
		setContentView(R.layout.activity_main);
		
		//create and add fragments to the application
		//mItems.add(new TimeFragment());
		//mItems.add(new StartFragment());
		mItems.add(new TargetFragment());
		//mItems.add(new CourseFragment());
		mItems.add(new DataFragment());
		
		if(savedInstanceState != null)
			onRestoreInstanceState(savedInstanceState);
		else{
			SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
			frag_index = sharedPreferences.getInt("fragment_index", 0);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == PICK_DATA_FILE_REQUEST_CODE){
			String fileUriString = data.getDataString();
			Uri fileUri = Uri.parse(fileUriString);
			Log.d(getClass().getName(), "selected file " + fileUriString);
	        try{
	            int insertedRecords = mPerformanceDataSource.insertPolarsFromTSVFile(fileUri);
	            bestPerformances = mPerformanceDataSource.getBestPerformancesForBoat(BOAT_ID);
	            Log.d(getClass().getName(), "inserted records " + String.valueOf(insertedRecords));
	        }
	        catch(Exception ex)
	        {
	        	Toast.makeText(MainActivity.this, "Impossible to open performance file:" + ex.toString(), Toast.LENGTH_SHORT).show();
	        	Log.d(getClass().getName(), "Impossible to open performance file:" + ex.toString());
	        }
        }
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		doBindService();
		
		ShowCurrentFragment();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		doUnbindService();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  // Save UI state changes to the savedInstanceState.
	  // This bundle will be passed to onCreate if the process is
	  // killed and restarted.
	  savedInstanceState.putInt("fragment_index", frag_index);
	  savedInstanceState.putInt("mThemeId", mThemeId);
	}
	
	@Override
	protected void onStop(){
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
        case (R.id.display_settings):
	    	fragmentTransaction.addToBackStack(getResources().getString(R.string.preference_display_settings));
	    	fragmentTransaction.replace(R.id.fragment_container, new DisplayPreferences());
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
		
		Log.i(DataHelper.class.getName(),
				"fragment transation, frag_index = " + Integer.toString(frag_index));
	}
	
	public void ShowCurrentFragment()
	{
		frag_index = frag_index % mItems.size();
		
		FragmentManager mFragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_container, mItems.get(frag_index));
		fragmentTransaction.commit();
		
		Log.i(DataHelper.class.getName(),
				"fragment transation, frag_index = " + Integer.toString(frag_index));
	}
	
	public void ShowPreviousFragment()
	{
		frag_index = (frag_index - 1) % mItems.size();
		if (frag_index<0) frag_index += mItems.size(); //modulo operator return negative values

		FragmentManager mFragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_container, mItems.get(frag_index));
		fragmentTransaction.commit();
	
		Log.i(DataHelper.class.getName(),
				"fragment transation, frag_index = " + Integer.toString(frag_index));
	}
	
	public List<PerfRow> getBestPerformance()
	{
		return bestPerformances;
	}
	
	 /**
     * Handler of incoming messages from service.
     */
    class IncomingHandler extends Handler {
            @Override
            public void handleMessage(Message msg) {
                    switch (msg.what) {
                    case NetworkService.EVENT_DATA_INCOMING:
                    	RegattaFragment fragment = null;
                    	if(mItems.size() > frag_index && frag_index >= 0)
                    		 fragment = (RegattaFragment) mItems.get(frag_index);
                		if((NMEADataFrame) msg.obj != null && fragment != null)
                			fragment.Update((NMEADataFrame) msg.obj);

                		//updates last know position for the app
                		if(((NMEADataFrame) msg.obj).attitude.LongitudeE != -1
                				&& ((NMEADataFrame) msg.obj).attitude.LatitudeN != -1){
                			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                			SharedPreferences.Editor editor = sp.edit();
                			editor.putFloat("lat",((NMEADataFrame) msg.obj).attitude.LatitudeN);
                			editor.putFloat("lon", ((NMEADataFrame) msg.obj).attitude.LongitudeE);
                		}
                		break;
                    default:
                            super.handleMessage(msg);
                    }
            }
    }
	
	 /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                    // This is called when the connection with the service has been
                    // established, giving us the service object we can use to
                    // interact with the service. We are communicating with our
                    // service through an IDL interface, so get a client-side
                    // representation of that from the raw service object.
                    mService = new Messenger(service);

                    // We want to monitor the service for as long as we are
                    // connected to it.
                    try {
                            Message msg = Message.obtain(null,
                                            NetworkService.MSG_REGISTER_CLIENT);
                            msg.replyTo = mMessenger;
                            mService.send(msg);  
                            
                            // Give it some value as an example.
                            msg = Message.obtain(null, NetworkService.MSG_CONNECT_TCP, 1703, 
                                            0, "192.168.129.1");
                            mService.send(msg);
                            
                    } catch (RemoteException e) {
                            // In this case the service has crashed before we could even
                            // do anything with it; we can count on soon being
                            // disconnected (and then reconnected if it can be restarted)
                            // so there is no need to do anything here.
                    }
            }

            public void onServiceDisconnected(ComponentName className) {
                    // This is called when the connection with the service has been
                    // unexpectedly disconnected -- that is, its process crashed.
                    mService = null;

                    // As part of the sample, tell the user what happened.
                    Toast.makeText(MainActivity.this, "Service unexpectedly disconnedcted",
                                    Toast.LENGTH_SHORT).show();
            }
    };
	
	void doBindService() {
         // Establish a connection with the service. We use an explicit
         // class name because there is no reason to be able to let other
         // applications replace our component.
		 if(mIsBound == false){
	         bindService(new Intent(MainActivity.this, NetworkService.class),
	                         mConnection, Context.BIND_AUTO_CREATE);
	         mIsBound = true;
         }
	 }

	void doUnbindService() {
         if (mIsBound) {
                 // If we have received the service, and hence registered with
                 // it, then now is the time to unregister.
                 if (mService != null) {
                         try {
                                 Message msg = Message.obtain(null,
                                                 NetworkService.MSG_UNREGISTER_CLIENT);
                                 msg.replyTo = mMessenger;
                                 mService.send(msg);
                         } catch (RemoteException e) {
                                 // There is nothing special we need to do if the service
                                 // has crashed.
                         }
                 }

                 // Detach our existing connection.
                 unbindService(mConnection);
                 mIsBound = false;
         }
	 }
	
	public void refreshStyleMode(float lat, float lon, Calendar cal){
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		String displayMode = sp.getString("display_mode", "auto");
		
        if(displayMode.equals("auto")){
			double sunHeight = SolarCalculations.CalculateSunHeight(lat, lon, cal);
			if(sunHeight > 0 && mThemeId != R.style.AppTheme_Daylight)
			{//daylight mode
				mThemeId = R.style.AppTheme_Daylight;	
				this.recreate();
			}
			else if (sunHeight < 0 && sunHeight >= -6 && mThemeId != R.style.AppTheme_Dusk)
			{//civil dusk
				mThemeId = R.style.AppTheme_Dusk;
				this.recreate();
			}
			else if(sunHeight < -6 && mThemeId != R.style.AppTheme_Night)
			{//night mode
				mThemeId = R.style.AppTheme_Night;
				this.recreate();
			}
		}
        else{
        
        	if(displayMode.equals("day") && mThemeId != R.style.AppTheme_Daylight)
			{//daylight mode
				mThemeId = R.style.AppTheme_Daylight;	
				this.recreate();
			}
			else if (displayMode.equals("dusk") && mThemeId != R.style.AppTheme_Dusk)
			{//astronomical twilight, dusk mode
				mThemeId = R.style.AppTheme_Dusk;
				this.recreate();
			}
			else if(displayMode.equals("night") && mThemeId != R.style.AppTheme_Night)
			{//night mode
				mThemeId = R.style.AppTheme_Night;
				this.recreate();
			}
        }
        
	}
}
