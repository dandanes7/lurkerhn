package com.dd7.lurkerhn.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dd7.lurkerhn.R;
import com.dd7.lurkerhn.rest.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentCardAdapter extends RecyclerView.Adapter<CommentCardAdapter.CommentViewHolder> {

    private Context mContext;
    private List<Comment> mComments;

    public CommentCardAdapter(Context mContext) {
        this.mContext = mContext;
        mComments = new ArrayList<>();
    }

    public void addData(Comment comment) {
        mComments.add(comment);
        notifyDataSetChanged();
    }

    public void clear() {
        mComments.clear();
        notifyDataSetChanged();
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.view_comments, null, true);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new CommentViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder commentViewHolder, int i) {
        Comment comment = mComments.get(i);
        if (comment.getText() != null) {
            commentViewHolder.mCommentAuthor.setText(comment.getBy());
            commentViewHolder.mCommentText.setText(Html.fromHtml(comment.getText()).toString().trim()); //trim() to remove annoying whitespace
            commentViewHolder.mCommentTime.setText(comment.getTimeFormatted());
            commentViewHolder.setPadding(comment.getPadding());
        }
    }

    @Override
    public int getItemCount() {
        return mComments.size();
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

        public void setPadding(int padding) {
            final float scale = mContext.getResources().getDimensionPixelSize(R.dimen.text_margin);
            int padding_in_px = (int) (padding * scale + 0.5f);
            itemView.setPadding(padding_in_px, 0, 0, 0);
        }
    }
}
