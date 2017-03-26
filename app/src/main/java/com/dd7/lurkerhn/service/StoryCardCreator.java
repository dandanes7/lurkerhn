package com.dd7.lurkerhn.service;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import com.dd7.lurkerhn.R;
import com.dd7.lurkerhn.rest.model.Item;
import com.dd7.lurkerhn.rest.model.StoryCard;

public class StoryCardCreator {

    private Context mContext;

    public StoryCardCreator(Context mContext) {
        this.mContext = mContext;
    }

    public void build(Item item, StoryCard storyCard) {

        int score = item.getScore();
        String title = item.getTitle();
        storyCard.getmStoryTitle().setText(title);
        storyCard.getmStoryScore().setText(Integer.toString(score));
        storyCard.getmStoryBy().setText(item.getBy());
        storyCard.getmStoryTime().setText(item.getTimeFormatted());
        storyCard.getmStoryUrl().setText(item.getUrlDomainName());
        setScoreColor(storyCard, score);
    }

    private void setScoreColor(StoryCard storyCard, int score) {
        int color;
        if (score < 50) {
            color = ContextCompat.getColor(mContext, R.color.under50);
        } else if (score < 100) {
            color = ContextCompat.getColor(mContext, R.color.under100);
        } else if (score < 150) {
            color = ContextCompat.getColor(mContext, R.color.under150);
        } else if (score < 250) {
            color = ContextCompat.getColor(mContext, R.color.under250);
        } else {
            color = ContextCompat.getColor(mContext, R.color.over250);
        }
        storyCard.getmStoryScore().setTextColor(color);
    }
}