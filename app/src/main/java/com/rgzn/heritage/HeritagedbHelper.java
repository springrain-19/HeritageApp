package com.rgzn.heritage;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HeritagedbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "heritage.db";
    private static final int DATABASE_VERSION = 12;

    public HeritagedbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_HERITAGE_TABLE =
                "CREATE TABLE " + HeritageContract.HeritageEntry.TABLE_NAME + " (" +
                        HeritageContract.HeritageEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        HeritageContract.HeritageEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                        HeritageContract.HeritageEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                        HeritageContract.HeritageEntry.COLUMN_MEDIA_PATH + " TEXT NOT NULL, " +
                        HeritageContract.HeritageEntry.COLUMN_LIKES + " INTEGER NOT NULL DEFAULT 0, " +
                        HeritageContract.HeritageEntry.COLUMN_FAVORITES + " INTEGER NOT NULL DEFAULT 0, " +
                        HeritageContract.HeritageEntry.COLUMN_IMAGE_RES_NAME + " TEXT NOT NULL" + // 确保列被包括
                        ");";

        Log.d("HeritageDB", "creating data into the database");
        db.execSQL(SQL_CREATE_HERITAGE_TABLE);
        insertHeritageData(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HeritageContract.HeritageEntry.TABLE_NAME);
        onCreate(db);
        Log.d("HeritageDB", "Upgrading database from version " + oldVersion + " to " + newVersion);
//        if (oldVersion < 12) {
//
//            db.execSQL("ALTER TABLE " + HeritageContract.HeritageEntry.TABLE_NAME + " ADD COLUMN " + HeritageContract.HeritageEntry.COLUMN_IMAGE_RES_NAME + " TEXT NOT NULL DEFAULT 'default_value'");
//        }


    }

    private void insertHeritageData(SQLiteDatabase db) {
        insertHeritage(db,"京剧", "京剧，又称平剧、京戏等，中国国粹之一，是中国影响力最大的戏曲剧种，分布地以北京为中心，遍及全国各地。清代乾隆五十五年起，原在南方演出的三庆、四喜、春台、和春等多以安徽籍艺人为主的四大徽班陆续进入北京，与来自湖北的汉调艺人合作，同时接受了昆曲、秦腔的部分剧目、曲调和表演方法，不断吸收地方民间曲调，最终形成京剧。" , "android.resource://com.rgzn.zjyxmz/raw/media",55,77,"art");
        insertHeritage(db,"点茶", "点茶是唐、宋代的一种沏茶方法。点茶是分茶的基础,所以点茶法的起始不会晚于五代。点茶是古代沏茶方法之一。点茶,也常用来在斗茶时进行。它可以在二人或二人以上进行,但也可以独个自煎(水)、自点(茶)、自品,它给人带来的身心享受,能唤来无穷的回味。", "android.resource://com.rgzn.zjyxmz/raw/mediaskill",66,23,"skill");
        Log.d("HeritageDB", "Inserting data into the database");
    }

    private void insertHeritage(SQLiteDatabase db, String name, String description, String mediaPath, int likenum, int fanum, String imageResName) {
        ContentValues values = new ContentValues();
        values.put(HeritageContract.HeritageEntry.COLUMN_NAME, name);
        values.put(HeritageContract.HeritageEntry.COLUMN_DESCRIPTION, description);
        values.put(HeritageContract.HeritageEntry.COLUMN_MEDIA_PATH, mediaPath);
        values.put(HeritageContract.HeritageEntry.COLUMN_LIKES, likenum);
        values.put(HeritageContract.HeritageEntry.COLUMN_FAVORITES, fanum);
        values.put(HeritageContract.HeritageEntry.COLUMN_IMAGE_RES_NAME, imageResName);
        db.insert(HeritageContract.HeritageEntry.TABLE_NAME, null, values);

    }



}
