/*
 * Copyright (c)  2017 Shekhar Gupta. - All Rights Reserved
 */

package com.shekharkg.utube.interfaces;

import com.shekharkg.utube.bean.VideoItem;

/**
 * Created by shekhar on 8/7/17.
 */

public interface VideoItemClickedListener {
  
  void onVideoItemClicked(int position, VideoItem videoItem);
}
