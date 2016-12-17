package com.dd7.yahn.service;

import android.content.Context;
import android.content.SharedPreferences;
import com.dd7.yahn.R;

public class PreferenceService {

    public static final String FILE_PREFS = "file_prefs";
//    public static final String FILE_SAVED_ITEMS = "file_saved_items";

    private static final int MAX_STORIES_TO_DISPLAY_DEFAULT = 50;
    public static final String MAX_STORIES = "MAX_STORIES";

    private static SharedPreferences mSharedPreferences;
    private Context mContext;

    public PreferenceService(Context mContext) {
        this.mContext = mContext;
        mSharedPreferences = mContext.getSharedPreferences(FILE_PREFS, Context.MODE_PRIVATE);
    }

    public void saveMaxStoriesSetting(int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(MAX_STORIES, value);
        editor.commit();
    }

    public int getMaxStoriesSetting() {
        return mSharedPreferences.getInt(MAX_STORIES, MAX_STORIES_TO_DISPLAY_DEFAULT);
    }

}
