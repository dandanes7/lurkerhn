package com.dd7.lurkerhn.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dd7.lurkerhn.R;
import com.dd7.lurkerhn.rest.model.Item;

import java.util.ArrayList;
import java.util.List;

public class StoryCardAdapter extends RecyclerView.Adapter<StoryCardAdapter.StoryViewHolder> {

    private Context mContext;
    private List<Item> mItems;
    private static ClickListener sClickListener;

    public StoryCardAdapter(Context mContext) {
        this.mContext = mContext;
        mItems = new ArrayList<>();
    }

    public void addData(Item item) {
        mItems.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public StoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.view_stories, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new StoryViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(StoryViewHolder storyViewHolder, int i) {
        Item item = mItems.get(i);
        int score = item.getScore();
        String title = item.getTitle();
        storyViewHolder.mStoryTitle.setText(title);
        storyViewHolder.mStoryScore.setText(Integer.toString(score));
        storyViewHolder.mStoryBy.setText(item.getBy());
        storyViewHolder.mStoryTime.setText(item.getTimeFormatted());
        storyViewHolder.mStoryUrl.setText(item.getUrlDomainName());

        setScoreColor(storyViewHolder, score);
    }

    private void setScoreColor(StoryViewHolder storyViewHolder, int score) {
        if (score < 50) {
            storyViewHolder.mStoryScore.setTextColor(mContext.getResources().getColor(R.color.under50));
        } else if (score < 100) {
            storyViewHolder.mStoryScore.setTextColor(mContext.getResources().getColor(R.color.under100));
        } else if (score < 150) {
            storyViewHolder.mStoryScore.setTextColor(mContext.getResources().getColor(R.color.under150));
        } else if (score < 250) {
            storyViewHolder.mStoryScore.setTextColor(mContext.getResources().getColor(R.color.under250));
        } else {
            storyViewHolder.mStoryScore.setTextColor(mContext.getResources().getColor(R.color.over250));
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        StoryCardAdapter.sClickListener = clickListener;
    }

    class StoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView mStoryScore;
        private TextView mStoryBy;
        private TextView mStoryUrl;
        private TextView mStoryTitle;
        private TextView mStoryTime;

        StoryViewHolder(View itemView) {
            super(itemView);
            mStoryScore = (TextView) itemView.findViewById(R.id.story_score);
            mStoryBy = (TextView) itemView.findViewById(R.id.story_by);
            mStoryUrl = (TextView) itemView.findViewById(R.id.story_url);
            mStoryTitle = (TextView) itemView.findViewById(R.id.story_title);
            mStoryTime = (TextView) itemView.findViewById(R.id.story_time);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPos = getAdapterPosition();
            sClickListener.onItemClick(adapterPos, v, mItems);
        }

        @Override
        public boolean onLongClick(View v) {
            int adapterPos = getAdapterPosition();
            sClickListener.onItemLongClick(adapterPos, v, mItems);
            return false;
        }
    }
}