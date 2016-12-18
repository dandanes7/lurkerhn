package com.dd7.yahn.service;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceService {

    private static final String MAX_STORIES = "MAX_STORIES";
    private static final String FILE_PREFS = "file_prefs";
    private static final int MAX_STORIES_TO_DISPLAY_DEFAULT = 50;

    private static SharedPreferences sSharedPreferences;
    private Context mContext;

    public PreferenceService(Context mContext) {
        this.mContext = mContext;
        sSharedPreferences = mContext.getSharedPreferences(FILE_PREFS, Context.MODE_PRIVATE);
    }

    public void saveMaxStoriesSetting(int value) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putInt(MAX_STORIES, value);
        editor.commit();
    }

    public int getMaxStoriesSetting() {
        return sSharedPreferences.getInt(MAX_STORIES, MAX_STORIES_TO_DISPLAY_DEFAULT);
    }

}
