/*
 * Copyright (c)  2017 Shekhar Gupta. - All Rights Reserved
 */

package com.shekharkg.utube;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.shekharkg.utube.databinding.ActivityHomeBinding;

public class HomeActivity extends Activity {

  private ActivityHomeBinding homeBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
  }
}
