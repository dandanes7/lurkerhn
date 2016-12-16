package com.dd7.yahn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import com.dd7.yahn.adapter.ClickListener;
import com.dd7.yahn.adapter.StoryCardAdapter;
import com.dd7.yahn.rest.model.Item;
import com.dd7.yahn.rest.service.HackerNewsApi;
import com.dd7.yahn.rest.service.ServiceFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_STORIES_TO_DISPLAY = 60;
    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        //TODO: navigation drawer

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.story_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final StoryCardAdapter mStoryCardAdapter = new StoryCardAdapter(this);
        mStoryCardAdapter.setOnItemClickListener(new ItemClickListener());
        mRecyclerView.setAdapter(mStoryCardAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {

            loadTopStories(mStoryCardAdapter);
            mSwipeRefreshLayout.setRefreshing(false);
        });
        loadTopStories(mStoryCardAdapter);
    }

    private void loadTopStories(final StoryCardAdapter mStoryCardAdapter) {
        final HackerNewsApi service = ServiceFactory.createRetrofitService(HackerNewsApi.class, HackerNewsApi.HNENDPOINT);
        mStoryCardAdapter.clear();

        service.getTopStories()
                .flatMapIterable(ids -> ids)
                .take(50)
                .concatMapEager(id -> service.getItem(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(item -> mStoryCardAdapter.addData(item));
                .subscribe(new Subscriber<Item>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("NETWORKERROR", "Something went wrong" + e.getMessage(), e);
                    }

                    @Override
                    public void onNext(Item item) {
                        mStoryCardAdapter.addData(item);
                    }
                });

//                service.getTopStories()
//                .flatMapIterable(ids -> ids).take(50)
//                .concatMapEager(new Func1<Integer, Observable<Item>>() {
//                    @Override
//                    public Observable<Item> call(Integer integer) {
//                        return service.getItem(integer);
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Item>() {
//                    @Override
//                    public void onCompleted() {
//                        //
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(Item item) {
//                        mStoryCardAdapter.addData(item);
//                    }
//                });

//        service.getTopStories()
//                .flatMapIterable(ids -> ids).take(50)
//                .concatMapEager(new Func1<Integer, Observable<Item>>() {
//                    @Override
//                    public Observable<Item> call(Integer integer) {
//                        return service.getItem(integer);
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Item>() {
//                    @Override
//                    public void onCompleted() {
//                        //
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(Item item) {
//                        mStoryCardAdapter.addData(item);
//                    }
//                });
    }

    private class ItemClickListener implements ClickListener {
        @Override
        public void onItemClick(int position, View v, List<Item> items) {
            Item item = items.get(position);
            Intent intent = new Intent(mContext, StoryDetail.class);
            intent.putExtra("item", item);
            startActivity(intent);
        }

        @Override
        public void onItemLongClick(int position, View v, List<Item> items) {
            Item item = items.get(position);
            Intent intent = new Intent(mContext, StoryContentWebViewer.class);
            intent.putExtra("item", item);
            startActivity(intent);
        }
    }
}
