package com.dd7.yahn.rest.client.adapter;

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

    List<Item> mItems;

    public CardAdapter() {
        super();
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
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Item item = mItems.get(i);
        viewHolder.storyId.setText(item.getId());

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView storyId;

        public ViewHolder(View itemView) {
            super(itemView);
            storyId = (TextView) itemView.findViewById(R.id.story_id);
        }
    }
}