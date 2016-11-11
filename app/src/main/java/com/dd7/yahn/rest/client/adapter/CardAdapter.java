package com.dd7.yahn.rest.client.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dd7.yahn.R;
import com.dd7.yahn.rest.client.model.Item;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private Context context;
    List<Item> mItems;

    public CardAdapter(Context context) {
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
//        View v = LayoutInflater.from(viewGroup.getContext())
//                .inflate(R.layout.recycler_view, viewGroup, false);
//        return new ViewHolder(v);
        View rootView = LayoutInflater.from(context).inflate(R.layout.recycler_view, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Item item = mItems.get(i);
        viewHolder.storyScore.setText(Integer.toString(item.getScore()));
        viewHolder.storyBy.setText(item.getBy());
//        viewHolder.storyTime.setText(String.valueOf(new Date(item.getTime())));
        viewHolder.storyTime.setText(item.getTimeFormatted());
        viewHolder.storyTitle.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView storyScore;
        TextView storyBy;
        TextView storyTitle;
        TextView storyTime;

        ViewHolder(View itemView) {
            super(itemView);
            storyScore = (TextView) itemView.findViewById(R.id.story_score);
            storyBy = (TextView) itemView.findViewById(R.id.story_by);
            storyTitle = (TextView) itemView.findViewById(R.id.story_title);
            storyTime = (TextView) itemView.findViewById(R.id.story_time);
        }
    }
}