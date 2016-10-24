package com.codepath.nytquest.applications;

/**
 * Created by rpraveen on 10/23/16.
 */

import android.app.Application;

import com.codepath.nytquest.broadcastReceivers.ConnectivityReceiver;

public class NYTApplication extends Application {

  private static NYTApplication mInstance;

  @Override
  public void onCreate() {
    super.onCreate();

    mInstance = this;
  }

  public static synchronized NYTApplication getInstance() {
    return mInstance;
  }

  public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
    ConnectivityReceiver.connectivityReceiverListener = listener;
  }
}
