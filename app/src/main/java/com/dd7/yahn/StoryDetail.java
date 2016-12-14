package com.dd7.yahn;

import android.content.Context;
import android.content.Intent;
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
import com.dd7.yahn.adapter.ClickListener;
import com.dd7.yahn.adapter.CommentCardAdapter;
import com.dd7.yahn.rest.model.Item;
import com.dd7.yahn.rest.service.HackerNewsApi;
import com.dd7.yahn.rest.service.ServiceFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StoryDetail extends AppCompatActivity {

    private static final String ID_FILE = "HnReaderSavedStories";
    private static final String SAVE_STORY = "SaveStory";
    private static final String ASK_HN_TAG = "Ask HN:";
    private static final String TELL_HN_TAG = "Tell HN:";

    private Context mContext;
    private Item mStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
        mContext = getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStory = (Item) getIntent().getSerializableExtra("item");
        setTitle(mStory.getTitle());
        setUpButtonsAndAddContentToTextViews(mStory);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.comment_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        final CommentCardAdapter mCommentCardAdapter = new CommentCardAdapter(this);
        mRecyclerView.setAdapter(mCommentCardAdapter);

        final HackerNewsApi service = ServiceFactory.createRetrofitService(HackerNewsApi.class, HackerNewsApi.HNENDPOINT);

//        Observable.from(mStory.getKids())
//                .concatMapEager(id -> service.getItem(id))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(item -> mCommentCardAdapter.addData(item));

        Observable.from(mStory.getKids())
                .concatMapEager(id -> service.getItem(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> mCommentCardAdapter.addData(item));

    }

    private void setUpButtonsAndAddContentToTextViews(final Item item) {
        ImageButton saveButton = (ImageButton) findViewById(R.id.save_story);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readSavedStories();
                saveStory(item);
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
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StoryContentWebViewer.class);
                intent.putExtra("item", item);
                startActivity(intent);
            }
        });

        ImageButton openInBrowserButton = (ImageButton) findViewById(R.id.open_story);
        openInBrowserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(item.getUrl()));
                startActivity(intent);
            }
        });


        TextView storyBy = (TextView) findViewById(R.id.story_by);
        storyBy.setText(item.getBy());
        TextView storyUrl = (TextView) findViewById(R.id.story_url);
        storyUrl.setText(item.getUrl());
        if (item.getTitle().startsWith(ASK_HN_TAG) || item.getTitle().startsWith(TELL_HN_TAG)) {
            viewButton.setEnabled(false);
            openInBrowserButton.setEnabled(false);
            viewButton.setAlpha(.5f);
            openInBrowserButton.setAlpha(.5f);
            storyUrl.setText("-");
            //This adds Ask HN: description
            TextView storyText = (TextView) findViewById(R.id.story_text);
            storyText.setText(Html.fromHtml(item.getText()));
        }
    }

    private void saveStory(Item item) {
        if (!savedStories.contains(String.valueOf(item.getId()))) {
            FileOutputStream outputStream = null;
            try {
                outputStream = openFileOutput(ID_FILE, Context.MODE_APPEND);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                outputStreamWriter.append(String.valueOf(item.getId()));
                outputStreamWriter.append("\n");
                outputStreamWriter.close();
            } catch (Exception e) {
                Log.e(SAVE_STORY, "Could not save story id, " + e.getMessage());
            }
        }
    }

    private Set<String> savedStories;

    private void readSavedStories() {
        BufferedReader input = null;
        File file = null;
        savedStories = new HashSet<>();
        try {
            file = new File(getFilesDir(), ID_FILE);

            input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = input.readLine()) != null) {
                savedStories.add(line);
            }
            Log.d(SAVE_STORY, "Stories saved:" + Arrays.toString(savedStories.toArray()));
        } catch (IOException e) {
            Log.e(SAVE_STORY, "Could not load story id, " + e.getMessage());
        }
    }

    private class ItemClickListener implements ClickListener {

        @Override
        public void onItemClick(int position, View v, List<Item> items) {

        }

        @Override
        public void onItemLongClick(int position, View v, List<Item> items) {
            //Todo: here i could actually share a comment url.
        }
    }
}