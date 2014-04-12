package com.openregatta.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Random;

import com.openregatta.MainActivity;
import com.openregatta.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;
/**
 * This service is in charge of keeping the connection alive with the server and 
 * parse NMEA data that is coming in. It will also send messages to all clients when
 * new data is available.
 * 
 * @author ddieffenthaler
 *
 */
public class NetworkService extends Service {
        /** For showing and hiding our notification. */
        NotificationManager mNM;
        /** Keeps track of all current registered clients. */
        ArrayList<Messenger> mClients = new ArrayList<Messenger>();
        /** Port used for TCP and UDP connections */
        int port = 1703;
        /** IP used for the connection if necessary */
        String ip = "192.168.192.1";
        /** Thread for TCP connection */
        Thread thread;
        /** Runnable that uses a tcp socket to read stream of data */
        TCPSocketRunnable tcpSocketRunnable;
        
        /**
         * Command to the service to register a client, receiving callbacks from the
         * service. The Message's replyTo field must be a Messenger of the client
         * where callbacks should be sent.
         */
        public static final int MSG_REGISTER_CLIENT = 1;
        /**
         * Command to the service to unregister a client, ot stop receiving
         * callbacks from the service. The Message's replyTo field must be a
         * Messenger of the client as previously given with MSG_REGISTER_CLIENT.
         */
        public static final int MSG_UNREGISTER_CLIENT = 2;
        /**
         * Command to service to connect using TCP Socket using specific IP and port
         * given as arg0 and obj of the Message
         */
        public static final int MSG_CONNECT_TCP = 3;       
        /**
         * Event sent to the clients in case there was an issue with the connection and
         * we lost the socket
         */
        public static final int EVENT_DISCONECTED = 4;        
        /**
         * Event sent to the clients in order to warn them that new incoming data is available
         * also attach the data object to the Message so clients can update the visuals 
         */
        public static final int EVENT_DATA_INCOMING = 5;     
        
        /**
         * Handler of incoming messages from clients.
         */
        class IncomingHandler extends Handler {
                @Override
                public void handleMessage(Message msg) {
                        switch (msg.what) {
                        case MSG_REGISTER_CLIENT:
                                mClients.add(msg.replyTo);
                                break;
                        case MSG_UNREGISTER_CLIENT:
                                mClients.remove(msg.replyTo);
                                break;
                        case MSG_CONNECT_TCP:
                                port = (Integer) msg.arg1;
                                ip = (String) msg.obj.toString();
                                if(thread != null && thread.isAlive()
                                		&& tcpSocketRunnable != null){
                                	tcpSocketRunnable.shouldContinue = false;
                                	try {
										thread.join();
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
                                	}
                                else
                                {
                                	tcpSocketRunnable = new TCPSocketRunnable();
                                	thread = new Thread(tcpSocketRunnable);
                                	thread.start();
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

        @Override
        public void onCreate() {
                mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                // Display a notification about us starting.
                showNotification(); 
        }

        @Override
        public void onDestroy() {
                // Cancel the persistent notification.
                mNM.cancel(1);

                // Tell the user we stopped.
                Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show();
        }

        /**
         * When binding to the service, we return an interface to our messenger for
         * sending messages to the service.
         */
        @Override
        public IBinder onBind(Intent intent) {
                return mMessenger.getBinder();
        }

        /**
         * Show a notification while this service is running.
         */
        private void showNotification() {
                // In this sample, we'll use the same text for the ticker and the
                // expanded notification
                CharSequence text = "Remote service started";

                // Set the icon, scrolling text and timestamp
                Notification notification = new Notification(R.drawable.ic_launcher, text,
                                System.currentTimeMillis());

                // The PendingIntent to launch our activity if the user selects this
                // notification
                PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                                new Intent(this, MainActivity.class), 0);

                // Set the info for the views that show in the notification panel.
                notification.setLatestEventInfo(this, "Remote Service", text,
                                contentIntent);

                // Send the notification.
                // We use a string id because it is a unique number. We use it later to
                // cancel.
                mNM.notify(1, notification);
        }
        
        class TCPSocketRunnable implements Runnable {

        	SocketAddress socketAddress;
        	Socket s = new Socket();
        	boolean shouldContinue = true;
        	
        	public void run()
        	{
        		SocketAddress socketAddress = new InetSocketAddress("192.168.129.1", 1703);
        		try {               
			       s.connect(socketAddress);
			       InputStream in = s.getInputStream();
			       Reader reader = new InputStreamReader(in);
			       BufferedReader bufferedReader = new BufferedReader(reader);
			       
			       String packagedData = "";
				   
				   	for (String data; (data = bufferedReader.readLine()) != null;)
				   	{
					    if(!data.equals(""))
					    	packagedData = packagedData.concat(data + "\n");
						if(packagedData.toLowerCase().contains("rmc")){
							for (int i = mClients.size() - 1; i >= 0; i--) {
								try {
									mClients.get(i).send(
						            Message.obtain(null, EVENT_DATA_INCOMING, 0, 0, NMEA0183Parser.Parse(packagedData)));
								} 
								catch (RemoteException e) {
						                // The client is dead. Remove it from the list;
										// we are going through the list from back to front
										// so this is safe to do inside the loop.
						               mClients.remove(i);
								}
						   }
						   packagedData = "";
						   }
							   
						if(!shouldContinue)
							break;  
				   }
				   s.close();
			       
        		} catch (IOException e) {
			       e.printStackTrace();
        		}
        	}
        }
}
