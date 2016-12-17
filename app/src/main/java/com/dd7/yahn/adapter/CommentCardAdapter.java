package com.dd7.yahn.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dd7.yahn.R;
import com.dd7.yahn.rest.model.Item;

import java.util.ArrayList;
import java.util.List;

public class CommentCardAdapter extends RecyclerView.Adapter<CommentCardAdapter.CommentViewHolder> {

    private Context mContext;
    private List<Item> mItems;

    public CommentCardAdapter(Context mContext) {
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
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.comment_recycler_view, null, true);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new CommentViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder commentViewHolder, int i) {
        Item item = mItems.get(i);
        if (item.getText() != null) {
            commentViewHolder.mCommentAuthor.setText(item.getBy());
            commentViewHolder.mCommentText.setText(Html.fromHtml(item.getText()));
            commentViewHolder.mCommentTime.setText(item.getTimeFormatted());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        private TextView mCommentText;
        private TextView mCommentAuthor;
        private TextView mCommentTime;

        CommentViewHolder(View itemView) {
            super(itemView);
            mCommentAuthor = (TextView) itemView.findViewById(R.id.comment_author);
            mCommentText = (TextView) itemView.findViewById(R.id.comment_text);
            mCommentTime = (TextView) itemView.findViewById(R.id.comment_time);
        }
    }
}
