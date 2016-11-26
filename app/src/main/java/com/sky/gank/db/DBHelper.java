package com.sky.gank.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sky.gank.db.GankTable.GankContentTable;

/**
 * Created by tonycheng on 2016/11/26.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "gank.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + GankTable.GankContentTable.NAME + "(" +
                GankContentTable.Cols.ID + ", " +
                GankContentTable.Cols.DESC + ", " +
                GankContentTable.Cols.PUBLISHED_AT + ", " +
                GankContentTable.Cols.TYPE + ", " +
                GankContentTable.Cols.URL + ", " +
                GankContentTable.Cols.WHO + ", " +
                GankContentTable.Cols.IMAGES + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
