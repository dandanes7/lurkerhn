package com.dd7.lurkerhn.rest.model;

import android.widget.TextView;

public class StoryCard {

    private TextView mStoryScore;
    private TextView mStoryBy;
    private TextView mStoryUrl;
    private TextView mStoryTitle;
    private TextView mStoryTime;

    public StoryCard() {
    }

    public TextView getmStoryScore() {
        return mStoryScore;
    }

    public void setmStoryScore(TextView mStoryScore) {
        this.mStoryScore = mStoryScore;
    }

    public TextView getmStoryBy() {
        return mStoryBy;
    }

    public void setmStoryBy(TextView mStoryBy) {
        this.mStoryBy = mStoryBy;
    }

    public TextView getmStoryUrl() {
        return mStoryUrl;
    }

    public void setmStoryUrl(TextView mStoryUrl) {
        this.mStoryUrl = mStoryUrl;
    }

    public TextView getmStoryTitle() {
        return mStoryTitle;
    }

    public void setmStoryTitle(TextView mStoryTitle) {
        this.mStoryTitle = mStoryTitle;
    }

    public TextView getmStoryTime() {
        return mStoryTime;
    }

    public void setmStoryTime(TextView mStoryTime) {
        this.mStoryTime = mStoryTime;
    }
}
