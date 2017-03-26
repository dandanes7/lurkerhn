package com.dd7.lurkerhn;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.dd7.lurkerhn.adapter.CommentCardAdapter;
import com.dd7.lurkerhn.rest.client.RestClientFactory;
import com.dd7.lurkerhn.rest.client.HackerNewsApiClient;
import com.dd7.lurkerhn.rest.model.Comment;
import com.dd7.lurkerhn.rest.model.Item;
import com.dd7.lurkerhn.service.SavedStoriesRepository;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StoryContentActivity extends AppCompatActivity {

    private static final String ASK_HN_TAG = "Ask HN:";
    private static final String TELL_HN_TAG = "Tell HN:";

    private Context mContext;
    private Item mStory;
    private HackerNewsApiClient mService;
    private SavedStoriesRepository mDatabaseService;
    private boolean mStoryIsSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
        mContext = getApplicationContext();
        setUpToolbarAndGetStoryFromIntent();

        final CommentCardAdapter mCommentCardAdapter = prepareRecyclerViewAndGetCardAdapter();
        mService = RestClientFactory.createHackerNewsService();

        if (mStory.getKids() != null) {
            Observable.from(mStory.getKids()).concatMapEager(id -> mService.getItem(id))
                    .map(item -> new Comment(item))
                    .concatMapEager(firstRowKid -> getComments(firstRowKid))
                    .filter(comment -> {
                        if (comment != null && comment.getText() != null) {
                            return true;
                        } else {
                            return false;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Comment>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("ERROR", "Something went wrong" + e.getMessage(), e);
                        }

                        @Override
                        public void onNext(Comment comment) {
                            mCommentCardAdapter.addData(comment);
                        }
                    });
        }
    }

    private void setUpToolbarAndGetStoryFromIntent() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStory = (Item) getIntent().getSerializableExtra("item");
        setTitle(mStory.getTitle());
        setUpButtonsAndAddContentToTextViews(mStory);
    }

    private CommentCardAdapter prepareRecyclerViewAndGetCardAdapter() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.comment_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        final CommentCardAdapter mCommentCardAdapter = new CommentCardAdapter(this);
        mRecyclerView.setAdapter(mCommentCardAdapter);
        return mCommentCardAdapter;
    }

    public Observable<Comment> getComments(Comment comment) {
        if (comment.getKids() == null || comment.getKids().isEmpty()) {
            return Observable.just(comment);
        } else {
            return Observable.concatEager(
                    Observable.just(comment),
                    Observable.from(comment.getKids())
                            .concatMapEager(id -> mService.getItem(id))
                            .map(Comment::new)
                            .doOnNext(com -> com.increasePadding(comment.getPadding()))
                            .concatMapEager(this::getComments)
            );
        }
    }

    private void setUpButtonsAndAddContentToTextViews(final Item item) {
        ImageButton saveButton = (ImageButton) findViewById(R.id.save_story);
        mDatabaseService = new SavedStoriesRepository(mContext);
        mStoryIsSaved = mDatabaseService.exists(String.valueOf(item.getId()));
        if (mStoryIsSaved) {
            saveButton.setColorFilter(Color.argb(255, 255, 255, 255));
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mStoryIsSaved) {
                        mDatabaseService.delete(String.valueOf(item.getId()));
                        saveButton.setColorFilter(Color.argb(0, 0, 0, 0));
                        mStoryIsSaved = false;
                    } else {
                        mDatabaseService.save(item);
                        saveButton.setColorFilter(Color.argb(255, 255, 255, 255));
                        mStoryIsSaved = true;
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, "Could not save story", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageButton shareButton = (ImageButton) findViewById(R.id.share_story);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, item.getTitle());
                shareIntent.putExtra(Intent.EXTRA_TEXT, item.getUrl());
                Intent chooser = Intent.createChooser(shareIntent, getString(R.string.share_story));
                if (shareIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
            }
        });

        ImageButton viewButton = (ImageButton) findViewById(R.id.view_story);
        viewButton.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, StoryWebViewActivity.class);
            intent.putExtra("item", item);
            startActivity(intent);
        });

        ImageButton openInBrowserButton = (ImageButton) findViewById(R.id.open_story);
        openInBrowserButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(item.getUrl()));
            startActivity(intent);
        });


        TextView storyBy = (TextView) findViewById(R.id.story_by);
        storyBy.setText(item.getBy());
        TextView storyUrl = (TextView) findViewById(R.id.story_url);
        storyUrl.setText(item.getUrl());

        //Gotta treat ASK HN as a special case, because url does not exist and sometimes there's text
        if (item.getTitle().startsWith(ASK_HN_TAG) || item.getTitle().startsWith(TELL_HN_TAG)) {
            viewButton.setEnabled(false);
            openInBrowserButton.setEnabled(false);
            viewButton.setAlpha(.5f);
            openInBrowserButton.setAlpha(.5f);

            storyUrl.setText("-");
            setUpAskHnStoryText(item);
        }
    }

    private void setUpAskHnStoryText(final Item item) {
        if (item.getText() != null) {
            TextView storyText = (TextView) findViewById(R.id.story_text);
            storyText.setText(Html.fromHtml(item.getText()));
            storyText.setOnClickListener(new View.OnClickListener() {
                private boolean minimized;

                @Override
                public void onClick(View v) {
                    if (!minimized) {
                        storyText.setText("<<Click to expand>>");
                        minimized = true;
                    } else {
                        storyText.setText(Html.fromHtml(item.getText()));
                        minimized = false;
                    }
                }
            });
        }
    }
}