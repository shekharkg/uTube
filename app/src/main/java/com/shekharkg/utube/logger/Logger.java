/*
 * Copyright (c)  2017 Shekhar Gupta. - All Rights Reserved
 */

package com.shekharkg.utube.logger;

import android.util.Log;

import com.shekharkg.utube.BuildConfig;

/**
 * Created by shekhar on 8/7/17.
 */

public class Logger {

  private static final String TAG = "<<uTube>>";
  private static final boolean isDebug = BuildConfig.DEBUG;

  public static void i(String logText) {
    if (isDebug)
      Log.i(TAG, logText);
  }

  public static void wtf(String logText) {
    if (isDebug)
      Log.wtf(TAG, logText);
  }

}
