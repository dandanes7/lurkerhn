package com.dd7.lurkerhn.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dd7.lurkerhn.R;
import com.dd7.lurkerhn.rest.model.Item;
import com.dd7.lurkerhn.rest.model.StoryCard;
import com.dd7.lurkerhn.service.StoryCardCreator;

import java.util.ArrayList;
import java.util.List;

public class StoryCardAdapter extends RecyclerView.Adapter<StoryCardAdapter.StoryViewHolder> {

    private Context mContext;
    private List<Item> mItems;
    private static ClickListener sClickListener;
    private StoryCardCreator mStoryCardCreator;

    public StoryCardAdapter(Context mContext) {
        this.mContext = mContext;
        mStoryCardCreator = new StoryCardCreator(mContext);
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
        mStoryCardCreator.build(item, storyViewHolder.storyCard);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        StoryCardAdapter.sClickListener = clickListener;
    }

    class StoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        StoryCard storyCard;

        StoryViewHolder(View itemView) {
            super(itemView);
            storyCard = new StoryCard();
            storyCard.setmStoryScore((TextView) itemView.findViewById(R.id.story_score));
            storyCard.setmStoryBy((TextView) itemView.findViewById(R.id.story_by));
            storyCard.setmStoryUrl((TextView) itemView.findViewById(R.id.story_url));
            storyCard.setmStoryTitle((TextView) itemView.findViewById(R.id.story_title));
            storyCard.setmStoryTime((TextView) itemView.findViewById(R.id.story_time));
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