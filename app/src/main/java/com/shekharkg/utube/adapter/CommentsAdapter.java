/*
 * Copyright (c)  2017 Shekhar Gupta. - All Rights Reserved
 */

package com.shekharkg.utube.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.shekharkg.utube.R;
import com.shekharkg.utube.bean.VideoItem;
import com.shekharkg.utube.holder.CommentsViewHolder;
import com.shekharkg.utube.interfaces.GetPositionListener;
import com.shekharkg.utube.interfaces.VideoItemClickedListener;

import java.util.List;

/**
 * Created by shekhar on 8/7/17.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsViewHolder> implements GetPositionListener {

  private VideoItemClickedListener onClickListener;
  private List<VideoItem> videoItems;

  public CommentsAdapter(List<VideoItem> videoItems) {
    this.videoItems = videoItems;
  }

  @Override
  public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new CommentsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false), this);
  }

  @Override
  public void onBindViewHolder(CommentsViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return videoItems.size();
  }


  public void setOnClickListener(VideoItemClickedListener onClickListener) {
    this.onClickListener = onClickListener;
  }

  @Override
  public void onItemClicked(int position) {
    if (onClickListener != null)
      onClickListener.onVideoItemClicked(position, videoItems.get(position));
  }
}
