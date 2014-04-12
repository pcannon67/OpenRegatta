package com.openregatta.services;

import java.util.ArrayList;
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
import android.widget.Toast;

/**
 * The Race service will keep track of parameters needed for the current race such as:
 * buoy locations, starting line location, time to the start
 * 
 * @author ddieffenthaler
 *
 */
public class RaceService extends Service {
        /** For showing and hiding our notification. */
        NotificationManager mNM;
        /** Keeps track of all current registered clients. */
        ArrayList<Messenger> mClients = new ArrayList<Messenger>();
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
}
