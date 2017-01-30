package com.dd7.yahn.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.dd7.yahn.rest.model.Item;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class SavedStoriesRepository extends SQLiteOpenHelper {

    private static final String DB_NAME = "SavedStories";
    private static final String DB_TABLE = "SavedStories";
    private static final int DB_VERSION = 1;
    private static final String COL_ID = "ID";
    private static final String COL_ADDED = "ADDED";
    private static final String DB_CREATE = "CREATE TABLE SAVEDSTORIES (" + COL_ID + " integer primary key, " + COL_ADDED + " text not null);";
    private static final String SQL_SELECT = "SELECT * FROM " + DB_TABLE;

    public SavedStoriesRepository(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void save(Item item) {
        if (!exists(String.valueOf(item.getId()))) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_ID, item.getId());
            values.put(COL_ADDED, getCurrentTime());
            db.insert(DB_TABLE, COL_ID, values);
            db.close();
        }
    }

    public void delete(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE, COL_ID + "=?", new String[]{id});
        db.close();
    }

    public boolean exists(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DB_TABLE + " WHERE " + COL_ID + "=? ORDER BY ID", new String[]{id});
        boolean exists = false;
        if (cursor.moveToFirst()) {
            exists = true;
        }
        cursor.close();
        db.close();
        return exists;
    }

    public Map<String, String> getItems() {
        Map<String, String> ids = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_SELECT, null);
        if (cursor.moveToFirst()) {
            do {
                ids.put(cursor.getString(0), cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return ids;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
        onCreate(db);
    }

    private String getCurrentTime() {
        DateFormat df = DateFormat.getTimeInstance();
        df.setTimeZone(TimeZone.getTimeZone("gmt"));
        return df.format(new Date());
    }
}
