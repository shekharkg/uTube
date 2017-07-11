/*
 * Copyright (c)  2017 Shekhar Gupta. - All Rights Reserved
 */

package com.shekharkg.utube.holder;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shekharkg.utube.databinding.CommentItemBinding;
import com.shekharkg.utube.interfaces.GetPositionListener;

/**
 * Created by shekhar on 8/7/17.
 */

public class CommentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

  public CommentItemBinding commentItemBinding;
  private GetPositionListener listener;

  public CommentsViewHolder(View itemView) {
    super(itemView);
    commentItemBinding = DataBindingUtil.bind(itemView);
    commentItemBinding.getRoot().setOnClickListener(this);
  }

  public void setListener(GetPositionListener listener) {
    this.listener = listener;
  }

  @Override
  public void onClick(View view) {
    if (listener != null)
      listener.onItemClicked(getAdapterPosition());
  }
}
