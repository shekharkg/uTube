/*
 * Copyright (c)  2017 Shekhar Gupta. - All Rights Reserved
 */

package com.shekharkg.utube.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.shekharkg.utube.logger.Logger;

/**
 * Created by shekhar on 7/11/17.
 */

public class NetworkReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
      NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
      if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
        Logger.i("Internet YAY");
        Intent internetIntent = new Intent("NetworkConnectionBroadcast");
        internetIntent.putExtra("internet", true);
        context.sendBroadcast(internetIntent);
      } else if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED) {
        Logger.wtf("No internet :(");
        Intent noInternetIntent = new Intent("NetworkConnectionBroadcast");
        noInternetIntent.putExtra("internet", false);
        context.sendBroadcast(noInternetIntent);
      }
    }
  }

}
