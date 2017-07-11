/*
 * Copyright (c)  2017 Shekhar Gupta. - All Rights Reserved
 */

package com.shekharkg.utube.activity;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.shekharkg.utube.R;
import com.shekharkg.utube.adapter.CommentsAdapter;
import com.shekharkg.utube.bean.VideoItem;
import com.shekharkg.utube.databinding.ActivityHomeBinding;
import com.shekharkg.utube.interfaces.VideoItemClickedListener;
import com.shekharkg.utube.logger.Logger;
import com.shekharkg.utube.storage.StorageHelper;
import com.shekharkg.utube.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Locale;

public class HomeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, View.OnClickListener, TextToSpeech.OnInitListener, VideoItemClickedListener {

  private final int REQUEST_CODE_YOU_TUBE = 1001;
  private final int REQUEST_CODE_TTS = 1002;

  private ActivityHomeBinding homeBinding;
  private TextToSpeech tts;
  private YouTubePlayer youTubePlayer;
  private StorageHelper storageHelper;
  private final String defaultUrl = "https://www.youtube.com/watch?v=5u4G23_OohI";
  private CommentsAdapter adapter;
  private String videoId = "";

  private AlertDialog alertDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
    initVars();
  }

  private void initVars() {
    storageHelper = StorageHelper.getStorageHelper(this);
    homeBinding.youtubePlayerView.initialize(getString(R.string.youtube_api_key), this);
    homeBinding.fab.setOnClickListener(this);
    tts = new TextToSpeech(this, this);

    setupAlert();

    homeBinding.videoUrlET.setText(defaultUrl);
    videoId = getParamVFromUrl("v");
    adapter = new CommentsAdapter(storageHelper.getComments(videoId));
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    homeBinding.commentsRV.setLayoutManager(layoutManager);
    homeBinding.commentsRV.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

    homeBinding.commentsRV.setAdapter(adapter);
    adapter.setOnClickListener(this);
    homeBinding.actionPlay.setOnClickListener(this);

    homeBinding.videoUrlET.addTextChangedListener(urlTextWatcher);
    homeBinding.videoUrlET.setOnTouchListener(etDrawableTouchListener);
  }

  private void setupAlert() {
    alertDialog = new AlertDialog.Builder(this).create();
    alertDialog.setTitle("Oops!!!");
    alertDialog.setMessage("NO INTERNET CONNECTION FOUND.");
    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        });
  }


  private String getParamVFromUrl(String param) {
    Uri uri = Uri.parse(homeBinding.videoUrlET.getText().toString().trim());
    return uri.getQueryParameter(param) == null ? "" : uri.getQueryParameter(param);
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
    else if (view == homeBinding.actionPlay) {
      videoId = getParamVFromUrl("v");
      if (youTubePlayer != null)
        youTubePlayer.cueVideo(videoId);
      adapter.updateList(storageHelper.getComments(videoId));
      adapter.notifyDataSetChanged();
    }
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
    VideoItem item = new VideoItem(videoId, text, youTubePlayer.getCurrentTimeMillis());
    storageHelper.insertComment(item);
    adapter.addVideoItem(item);
    adapter.notifyDataSetChanged();
  }

  @Override
  protected void onStart() {
    super.onStart();

    registerReceiver(networkBroadcastReceiver, new IntentFilter("NetworkConnectionBroadcast"));
  }

  @Override
  protected void onStop() {
    super.onStop();

    unregisterReceiver(networkBroadcastReceiver);
  }

  private BroadcastReceiver networkBroadcastReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (!intent.getBooleanExtra("internet", true))
        alertDialog.show();
      else
        alertDialog.dismiss();
    }
  };

  @Override
  public void onDestroy() {
    if (tts != null) {
      tts.stop();
      tts.shutdown();
    }
    super.onDestroy();
  }

  @Override
  public void onVideoItemClicked(int position, VideoItem videoItem) {
    speak(videoItem.getComment(), videoItem.getTimeInMillis());
  }

  private TextWatcher urlTextWatcher = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      String url = homeBinding.videoUrlET.getText().toString();
      if (url.isEmpty()) {
        homeBinding.actionPlay.setColorFilter(Color.LTGRAY);
        homeBinding.actionPlay.setClickable(false);
        homeBinding.videoUrlET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_content_paste, 0);
      } else {
        homeBinding.actionPlay.setColorFilter(getResources().getColor(R.color.colorAccent));
        homeBinding.actionPlay.setClickable(true);
        homeBinding.videoUrlET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear, 0);
      }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
  };

  private View.OnTouchListener etDrawableTouchListener = new View.OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent event) {

      if (event.getAction() == MotionEvent.ACTION_UP) {
        if (event.getRawX() >= (homeBinding.videoUrlET.getRight() - homeBinding.videoUrlET.getCompoundDrawables()[2].getBounds().width())) {
          if (homeBinding.videoUrlET.getText().toString().isEmpty())
            homeBinding.videoUrlET.setText(defaultUrl);
          else
            homeBinding.videoUrlET.setText("");

          return true;
        }
      }
      return false;
    }
  };
}
