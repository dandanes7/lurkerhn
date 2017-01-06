package com.dd7.yahn.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dd7.yahn.R;
import com.dd7.yahn.rest.model.Item;

import java.util.ArrayList;
import java.util.List;

public class SavedStoriesCardAdapter extends RecyclerView.Adapter<SavedStoriesCardAdapter.StoryViewHolder> {

    private Context mContext;
    private List<Item> mItems;
    private static ClickListener sClickListener;

    public SavedStoriesCardAdapter(Context mContext) {
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
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.activity_saved_stories, null, false);
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
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        SavedStoriesCardAdapter.sClickListener = clickListener;
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