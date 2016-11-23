package com.dd7.yahn.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dd7.yahn.R;
import com.dd7.yahn.rest.client.model.Item;

import java.util.ArrayList;
import java.util.List;

public class StoryCardAdapter extends RecyclerView.Adapter<StoryCardAdapter.ViewHolder> {

    private Context mContext;
    List<Item> mItems;

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
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.recycler_view, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Item item = mItems.get(i);
        int score = item.getScore();
        String title = item.getTitle();
        viewHolder.mStoryTitle.setText(title);
        viewHolder.mStoryScore.setText(Integer.toString(score));
        viewHolder.mStoryBy.setText(item.getBy());
        viewHolder.mStoryTime.setText(item.getTimeFormatted());
        viewHolder.mStoryUrl.setText(item.getUrlDomainName());

        setScoreColor(viewHolder, score);
    }

    private void setScoreColor(ViewHolder viewHolder, int score) {
        if (score < 50) {
            viewHolder.mStoryScore.setTextColor(mContext.getResources().getColor(R.color.under50));
        } else if (score < 100) {
            viewHolder.mStoryScore.setTextColor(mContext.getResources().getColor(R.color.under100));
        } else if (score < 150) {
            viewHolder.mStoryScore.setTextColor(mContext.getResources().getColor(R.color.under150));
        } else if (score < 250) {
            viewHolder.mStoryScore.setTextColor(mContext.getResources().getColor(R.color.under250));
        } else {
            viewHolder.mStoryScore.setTextColor(mContext.getResources().getColor(R.color.over250));
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private static ClickListener sClickListener;

    public void setOnItemClickListener(ClickListener clickListener) {
        StoryCardAdapter.sClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v, List<Item> items);

        void onItemLongClick(int position, View v, List<Item> items);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView mStoryScore;
        private TextView mStoryBy;
        private TextView mStoryUrl;
        private TextView mStoryTitle;
        private TextView mStoryTime;

        ViewHolder(View itemView) {
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