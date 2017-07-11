/*
 * Copyright (c)  2017 Shekhar Gupta. - All Rights Reserved
 */

package com.shekharkg.utube.bean;

import java.util.concurrent.TimeUnit;

/**
 * Created by shekhar on 8/7/17.
 */

public class VideoItem {

  private String videoId;
  private String comment;
  private int timeInMillis;
  private String timeToShow;

  public VideoItem(String videoId, String comment, int timeInMillis) {
    this.videoId = videoId;
    this.comment = comment;
    this.timeInMillis = timeInMillis;
    this.timeToShow = String.format("%d:%d:%d",
        TimeUnit.MILLISECONDS.toHours(timeInMillis),
        TimeUnit.MILLISECONDS.toMinutes(timeInMillis),
        TimeUnit.MILLISECONDS.toSeconds(timeInMillis) -
            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMillis)));
  }

  public String getVideoId() {
    return videoId == null ? "" : videoId;
  }

  public String getComment() {
    return comment == null ? "" : comment;
  }

  public int getTimeInMillis() {
    return timeInMillis;
  }

  public String getTimeToShow() {
    return timeToShow;
  }
}
