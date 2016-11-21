package com.dd7.yahn.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.dd7.yahn.R;
import com.dd7.yahn.rest.client.model.Item;

import java.util.ArrayList;
import java.util.List;

public class StoryCardAdapter extends RecyclerView.Adapter<StoryCardAdapter.ViewHolder> {

    private Context context;
    List<Item> mItems;

    public StoryCardAdapter(Context context) {
        this.context = context;
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
        View rootView = LayoutInflater.from(context).inflate(R.layout.recycler_view, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Item item = mItems.get(i);
        int score = item.getScore();
        String title = item.getTitle();
        viewHolder.storyTitle.setText(title);
        viewHolder.storyScore.setText(Integer.toString(score));
        viewHolder.storyBy.setText(item.getBy());
        viewHolder.storyTime.setText(item.getTimeFormatted());
        viewHolder.storyUrl.setText(item.getUrlDomainName());

        setScoreColor(viewHolder, score);
    }

    private void setScoreColor(ViewHolder viewHolder, int score) {
        if (score < 50) {
            viewHolder.storyScore.setTextColor(context.getResources().getColor(R.color.under50));
        } else if (score < 100) {
            viewHolder.storyScore.setTextColor(context.getResources().getColor(R.color.under100));
        } else if (score < 150) {
            viewHolder.storyScore.setTextColor(context.getResources().getColor(R.color.under150));
        } else if (score < 250) {
            viewHolder.storyScore.setTextColor(context.getResources().getColor(R.color.under250));
        } else {
            viewHolder.storyScore.setTextColor(context.getResources().getColor(R.color.over250));
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    private static ClickListener clickListener;

    public void setOnItemClickListener(ClickListener clickListener) {
        StoryCardAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v, List<Item> items);

        void onItemLongClick(int position, View v, List<Item> items);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView storyScore;
        TextView storyBy;
        TextView storyUrl;
        TextView storyTitle;
        TextView storyTime;

        ViewHolder(View itemView) {
            super(itemView);
            storyScore = (TextView) itemView.findViewById(R.id.story_score);
            storyBy = (TextView) itemView.findViewById(R.id.story_by);
            storyUrl = (TextView) itemView.findViewById(R.id.story_url);
            storyTitle = (TextView) itemView.findViewById(R.id.story_title);
            storyTime = (TextView) itemView.findViewById(R.id.story_time);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPos = getAdapterPosition();
            clickListener.onItemClick(adapterPos, v, mItems);
        }

        @Override
        public boolean onLongClick(View v) {
            int adapterPos = getAdapterPosition();
            clickListener.onItemLongClick(adapterPos, v, mItems);
            return false;
        }

    }
}