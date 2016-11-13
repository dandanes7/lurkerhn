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

    private static ClickListener clickListener;

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
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

    public void setOnItemClickListener(ClickListener clickListener) {
        CardAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v, List<Item> items);

        void onItemLongClick(int position, View v, List<Item> items);
    }
}