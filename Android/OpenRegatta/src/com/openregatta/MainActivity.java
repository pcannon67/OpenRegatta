package com.openregatta;

import java.util.ArrayList;
import java.util.List;

import com.openregatta.R;
import com.openregatta.fragments.CourseFragment;
import com.openregatta.fragments.DataFragment;
import com.openregatta.fragments.RegattaFragment;
import com.openregatta.fragments.StartFragment;
import com.openregatta.fragments.TargetFragment;
import com.openregatta.fragments.TimeFragment;
import com.openregatta.preferences.BoatPreferences;
import com.openregatta.preferences.NetworkPreferences;
import com.openregatta.services.NetworkService;
import com.openregatta.services.NMEADataFrame;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;


public class MainActivity extends Activity {

	static private final String TAG = "OpenRegatta";
	
	/** List containing all the fragments that the user can navigate to by sliding left and right */
	private final List<Fragment> mItems = new ArrayList<Fragment>();
	/** Current index that is shown to the user */
	private int frag_index = 0;
	 /** Messenger for communicating with service. */
    Messenger mService = null;
    /** Flag indicating whether we have called bind on the service. */
    boolean mIsBound;
	
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
		mItems.add(new CourseFragment());
		mItems.add(new DataFragment());
		
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
		
		Log.i(TAG, "fragment transation, frag_index = " + Integer.toString(frag_index));
	}
	
	public void ShowCurrentFragment()
	{
		frag_index = frag_index % mItems.size();
		
		FragmentManager mFragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_container, mItems.get(frag_index));
		fragmentTransaction.commit();
		
		Log.i(TAG, "fragment transation, frag_index = " + Integer.toString(frag_index));
	}
	
	public void ShowPreviousFragment()
	{
		frag_index = (frag_index - 1) % mItems.size();
		if (frag_index<0) frag_index += mItems.size(); //modulo operator return negative values

		FragmentManager mFragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_container, mItems.get(frag_index));
		fragmentTransaction.commit();
	
		Log.i(TAG, "fragment transation, frag_index = " + Integer.toString(frag_index));
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

                    // As part of the sample, tell the user what happened.
                    Toast.makeText(MainActivity.this, "Service connected",
                                    Toast.LENGTH_SHORT).show();
            }

            public void onServiceDisconnected(ComponentName className) {
                    // This is called when the connection with the service has been
                    // unexpectedly disconnected -- that is, its process crashed.
                    mService = null;

                    // As part of the sample, tell the user what happened.
                    Toast.makeText(MainActivity.this, "Service disconnedcted",
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
}
