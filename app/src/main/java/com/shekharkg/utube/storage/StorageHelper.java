/*
 * Copyright (c)  2017 Shekhar Gupta. - All Rights Reserved
 */

package com.shekharkg.utube.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shekharkg.utube.bean.VideoItem;
import com.shekharkg.utube.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shekhar on 8/7/17.
 */

public class StorageHelper extends SQLiteOpenHelper {

  private static final String DB_U_TUBE = "dbUTube";
  private static final int DB_VERSION = 1;

  private static StorageHelper storageHelper;
  private SQLiteDatabase sqLiteDatabase;

  private static final String TBL_COMMENTS = "tblComments";

  private static final String COL_ID = "_id";
  private static final String COL_VIDEO_ID = "videoId";
  private static final String COL_COMMENT = "comment";
  private static final String COL_TIME = "timeInMillis";

  private static final String CREATE_TABLE = "CREATE TABLE " +
      TBL_COMMENTS + "( " +
      COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
      COL_VIDEO_ID + " TEXT NOT NULL, " +
      COL_COMMENT + " TEXT, " +
      COL_TIME + " INTEGER);";

  private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_COMMENTS;

  private StorageHelper(Context context) {
    super(context, DB_U_TUBE, null, DB_VERSION);
    sqLiteDatabase = getWritableDatabase();
  }

  public static StorageHelper getStorageHelper(Context context) {
    if (storageHelper == null)
      storageHelper = new StorageHelper(context);
    return storageHelper;
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(CREATE_TABLE);
    Logger.i("TABLE CREATED: " + TBL_COMMENTS);
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    sqLiteDatabase.execSQL(DROP_TABLE);
    Logger.i("TABLE DROPPED: " + TBL_COMMENTS);
    onCreate(sqLiteDatabase);
  }

  public synchronized long insertComment(VideoItem videoItem) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(COL_VIDEO_ID, videoItem.getVideoId());
    contentValues.put(COL_COMMENT, videoItem.getComment());
    contentValues.put(COL_TIME, videoItem.getTimeInMillis());
    return sqLiteDatabase.insert(TBL_COMMENTS, null, contentValues);
  }

  public synchronized List<VideoItem> getComments(String videoId) {
    List<VideoItem> videoItems = new ArrayList<>();

    String[] columns = new String[]{COL_VIDEO_ID, COL_COMMENT, COL_TIME};

    Cursor cursor = sqLiteDatabase.query(true, TBL_COMMENTS, columns, COL_VIDEO_ID + " =?",
        new String[]{videoId}, null, null, null, null);

    while (cursor.moveToNext()) {
      videoItems.add(new VideoItem(cursor.getString(0), cursor.getString(1), cursor.getInt(2)));
    }

    cursor.close();
    return videoItems;
  }
}
