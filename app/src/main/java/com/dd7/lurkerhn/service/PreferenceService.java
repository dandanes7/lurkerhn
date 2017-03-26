package com.dd7.lurkerhn.service;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceService {

    private static final String FILE_PREFS = "lurker_hn_prefs";
    private static final String MAX_STORIES = "MAX_STORIES";
    private static final String PREFERRED_CAT = "PREFERRED_CAT";

    private static final String PREFERRED_CAT_DEFAULT = "Top";
    private static final int MAX_STORIES_DEFAULT = 50;

    private static SharedPreferences sSharedPreferences;
    private Context mContext;

    public PreferenceService(Context mContext) {
        this.mContext = mContext;
        sSharedPreferences = mContext.getSharedPreferences(FILE_PREFS, Context.MODE_PRIVATE);
    }

    public void saveMaxStoriesPref(int value) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putInt(MAX_STORIES, value);
        editor.commit();
    }

    public void saveCategoryPref(String value) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putString(PREFERRED_CAT, value);
        editor.commit();
    }

    public int getMaxStoriesPref() {
        return sSharedPreferences.getInt(MAX_STORIES, MAX_STORIES_DEFAULT);
    }

    public String getCategoryPref() {
        return sSharedPreferences.getString(PREFERRED_CAT, PREFERRED_CAT_DEFAULT);
    }
}