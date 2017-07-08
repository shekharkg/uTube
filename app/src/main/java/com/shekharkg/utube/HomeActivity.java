/*
 * Copyright (c)  2017 Shekhar Gupta. - All Rights Reserved
 */

package com.shekharkg.utube;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.shekharkg.utube.databinding.ActivityHomeBinding;
import com.shekharkg.utube.logger.Logger;
import com.shekharkg.utube.storage.StorageHelper;

import java.util.ArrayList;
import java.util.Locale;

public class HomeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, View.OnClickListener, TextToSpeech.OnInitListener {

  private final int REQUEST_CODE_YOU_TUBE = 1001;
  private final int REQUEST_CODE_TTS = 1002;

  private ActivityHomeBinding homeBinding;
  private TextToSpeech tts;
  private YouTubePlayer youTubePlayer;
  private StorageHelper storageHelper;
  private String videoId = "5u4G23_OohI";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);

    storageHelper = StorageHelper.getStorageHelper(this);
    homeBinding.youtubePlayerView.initialize(getString(R.string.youtube_api_key), this);
    homeBinding.fab.setOnClickListener(this);

    tts = new TextToSpeech(this, this);
  }

  @Override
  public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
    this.youTubePlayer = youTubePlayer;
    if (!wasRestored)
      youTubePlayer.cueVideo(videoId);
  }

  @Override
  public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
    if (youTubeInitializationResult.isUserRecoverableError())
      youTubeInitializationResult.getErrorDialog(this, REQUEST_CODE_YOU_TUBE).show();
    else
      Toast.makeText(this, youTubeInitializationResult.toString(), Toast.LENGTH_LONG).show();
  }

  @Override
  public void onClick(View view) {
    if (view == homeBinding.fab)
      listen();
  }

  @Override
  public void onInit(int status) {
    if (status == TextToSpeech.SUCCESS) {
      int result = tts.setLanguage(Locale.US);
      if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
        Logger.wtf("This Language is not supported");
      }
    } else {
      Logger.i("Initilization Failed!");
    }
  }

  private void speak(String text, int timeInMillis) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    } else {
      tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    if (youTubePlayer != null)
      youTubePlayer.seekToMillis(timeInMillis);
  }

  private void listen() {
    Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
    i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

    try {
      startActivityForResult(i, REQUEST_CODE_TTS);
    } catch (ActivityNotFoundException a) {
      Toast.makeText(this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
    }
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE_TTS && resultCode == RESULT_OK && null != data) {
      ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
      String inSpeech = res.get(0);
      recognition(inSpeech);
    } else if (requestCode == REQUEST_CODE_YOU_TUBE)
      homeBinding.youtubePlayerView.initialize(getString(R.string.youtube_api_key), this);
  }

  private void recognition(String text) {
    Logger.i(text + "");
    Logger.wtf(youTubePlayer.getCurrentTimeMillis() + "");
  }


  @Override
  public void onDestroy() {
    if (tts != null) {
      tts.stop();
      tts.shutdown();
    }
    super.onDestroy();
  }
}
