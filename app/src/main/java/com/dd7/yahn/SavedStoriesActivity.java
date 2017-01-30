package com.dd7.yahn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.dd7.yahn.adapter.ClickListener;
import com.dd7.yahn.adapter.SavedStoriesCardAdapter;
import com.dd7.yahn.rest.client.ClientFactory;
import com.dd7.yahn.rest.client.HackerNewsApiClient;
import com.dd7.yahn.rest.model.Item;
import com.dd7.yahn.service.SavedStoriesRepository;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.List;

public class SavedStoriesActivity extends AppCompatActivity {

    private Context mContext;
    private SavedStoriesRepository mDatabaseService;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private HackerNewsApiClient mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_stories);

        mContext = getApplicationContext();
        mDatabaseService = new SavedStoriesRepository(mContext);
        mService = ClientFactory.createRetrofitService(HackerNewsApiClient.class, HackerNewsApiClient.HNENDPOINT);

        final SavedStoriesCardAdapter savedStoriesCardAdapter = prepareSavedStoriesCardAdapter();
        prepareSwipeRefreshLayout(savedStoriesCardAdapter);
        loadSavedStories(savedStoriesCardAdapter, mService);
    }

    private void prepareSwipeRefreshLayout(SavedStoriesCardAdapter savedStoriesCardAdapter) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.saved_stories_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            loadSavedStories(savedStoriesCardAdapter, mService);
            mSwipeRefreshLayout.setRefreshing(false);
        });
    }

    private void loadSavedStories(final SavedStoriesCardAdapter savedStoriesCardAdapter, HackerNewsApiClient service) {
        savedStoriesCardAdapter.clear();
        mSwipeRefreshLayout.setRefreshing(true);
        Observable.from(mDatabaseService.getItems().keySet())
                .map(Integer::valueOf)
                .concatMapEager(id -> service.getItem(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Item>() {
                    @Override
                    public void onCompleted() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("NETWORKERROR", "Something went wrong" + e.getMessage(), e);
                    }

                    @Override
                    public void onNext(Item item) {
                        savedStoriesCardAdapter.addData(item);
                    }
                });
    }

    private SavedStoriesCardAdapter prepareSavedStoriesCardAdapter() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.saved_stories_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final SavedStoriesCardAdapter savedStoriesCardAdapter = new SavedStoriesCardAdapter(this);
        savedStoriesCardAdapter.setOnItemClickListener(new SavedStoriesActivity.SavedStoriesClickListener());
        mRecyclerView.setAdapter(savedStoriesCardAdapter);
        return savedStoriesCardAdapter;
    }

    private class SavedStoriesClickListener implements ClickListener {
        @Override
        public void onItemClick(int position, View v, List<Item> items) {
            Item item = items.get(position);
            Intent intent = new Intent(mContext, StoryContentActivity.class);
            intent.putExtra("item", item);
            startActivity(intent);
        }

        @Override
        public void onItemLongClick(int position, View v, List<Item> items) {
            Item item = items.get(position);
            mDatabaseService.delete(String.valueOf(item.getId()));
            Toast.makeText(mContext, "Deleted item " + String.valueOf(item.getId()), Toast.LENGTH_LONG).show();
            recreate();
        }
    }
}
