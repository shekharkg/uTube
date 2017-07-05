/*
 * Copyright (c)  2017 Shekhar Gupta. - All Rights Reserved
 */

package com.shekharkg.utube;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.shekharkg.utube.databinding.ActivityHomeBinding;

public class HomeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

  private final int REQUEST_CODE = 1001;

  private ActivityHomeBinding homeBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);

    homeBinding.youtubePlayer.initialize(getString(R.string.youtube_api_key), this);
  }

  @Override
  public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
    if (!wasRestored)
      youTubePlayer.cueVideo("5u4G23_OohI");
  }

  @Override
  public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
    if (youTubeInitializationResult.isUserRecoverableError())
      youTubeInitializationResult.getErrorDialog(this, REQUEST_CODE).show();
    else
      Toast.makeText(this, youTubeInitializationResult.toString(), Toast.LENGTH_LONG).show();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_CODE)
      homeBinding.youtubePlayer.initialize(getString(R.string.youtube_api_key), this);
  }
}
