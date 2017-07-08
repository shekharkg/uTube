/*
 * Copyright (c)  2017 Shekhar Gupta. - All Rights Reserved
 */

package com.shekharkg.utube.bean;

/**
 * Created by shekhar on 8/7/17.
 */

public class VideoItem {

  private String videoId;
  private String comment;
  private int timeInMillis;

  public VideoItem(String videoId, String comment, int timeInMillis) {
    this.videoId = videoId;
    this.comment = comment;
    this.timeInMillis = timeInMillis;
  }

  public String getVideoId() {
    return videoId;
  }

  public String getComment() {
    return comment == null ? "" : comment;
  }

  public int getTimeInMillis() {
    return timeInMillis;
  }
}
