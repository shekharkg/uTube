/*
 * Copyright (c)  2017 Shekhar Gupta. - All Rights Reserved
 */

package com.shekharkg.utube;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.shekharkg.utube.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

  private ActivityHomeBinding homeBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
  }
}
