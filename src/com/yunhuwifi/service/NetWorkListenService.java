package com.yunhuwifi.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;


public class NetWorkListenService  extends BroadcastReceiver { 
    
 public static interface NetworkAvailableListener { 
     public void networkAvailable();  
     public void networkUnavailable(); 
 } 
    
 private NetworkAvailableListener networkAvailableListener; 
 private final ConnectivityManager connectivityManager; 
 private boolean connection = false; 
    
 public NetWorkListenService(Context context) { 
     connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
     checkConnectionOnDemand(); 
 } 
    
 public void bind(Context context) { 
     IntentFilter filter = new IntentFilter(); 
     filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION); 
     context.registerReceiver(this, filter); 
     checkConnectionOnDemand(); 
 } 
    
 public void unbind(Context context) { 
     context.unregisterReceiver(this); 
 } 

 private void checkConnectionOnDemand() { 
     final NetworkInfo info = connectivityManager.getActiveNetworkInfo(); 
     if (info == null || info.getState() != State.CONNECTED) { 
         if (connection == true) { 
             connection = false; 
             if (networkAvailableListener != null) networkAvailableListener.networkUnavailable(); 
         } 
     } 
     else { 
         if (connection == false) { 
             connection = true; 
             if (networkAvailableListener != null) networkAvailableListener.networkAvailable(); 
         } 
     } 
 } 
    
 @Override 
 public void onReceive(Context context, Intent intent) { 
     if (connection == true && intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) { 
         connection = false; 
         if (networkAvailableListener != null) { 
             networkAvailableListener.networkUnavailable(); 
         } 
     } 
     else if (connection == false && !intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) { 
         connection = true; 
         if (networkAvailableListener != null) { 
             networkAvailableListener.networkAvailable(); 
         } 
     } 
 } 
    
 public boolean hasConnection() { 
     return connection; 
 } 
    
 public void setOnNetworkAvailableListener(NetworkAvailableListener listener) { 
     this.networkAvailableListener = listener; 
 } 

}
