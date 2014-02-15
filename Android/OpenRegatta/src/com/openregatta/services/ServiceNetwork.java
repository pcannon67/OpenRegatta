package com.openregatta.services;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class ServiceNetwork extends Service {

	Socket s;
    PrintStream os;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return myBinder;
    }

    private final IBinder myBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public ServiceNetwork getService() {
            return ServiceNetwork.this;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        s = new Socket();
    }

    public void IsBoundable(){
        Toast.makeText(this,"I bind like butter", Toast.LENGTH_LONG).show();
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent, flags, startId);
        Toast.makeText(this,"Service created ...", Toast.LENGTH_LONG).show();
        Runnable connect = new connectSocket();
        new Thread(connect).start();
        return START_STICKY;
    }

    class connectSocket implements Runnable {

        @Override
        public void run() {
            SocketAddress socketAddress = new InetSocketAddress("192.168.1.104", 4505);
            try {               
                s.connect(socketAddress);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            s.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        s = null;
    }

}
