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
import com.dd7.yahn.adapter.StoryCardAdapter;
import com.dd7.yahn.rest.client.model.Item;
import com.dd7.yahn.rest.client.service.HackerNewsApi;
import com.dd7.yahn.rest.client.service.ServiceFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_STORIES_TO_DISPLAY = 15;
    private Context context;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        final StoryCardAdapter mStoryCardAdapter = new StoryCardAdapter(this);
        mStoryCardAdapter.setOnItemClickListener(new ItemClickListener());

        mRecyclerView.setAdapter(mStoryCardAdapter);


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loadTopStories(mStoryCardAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        loadTopStories(mStoryCardAdapter);

    }

    private void loadTopStories(final StoryCardAdapter mStoryCardAdapter) {
        final HackerNewsApi service = ServiceFactory.createRetrofitService(HackerNewsApi.class, HackerNewsApi.HNENDPOINT);
        mStoryCardAdapter.clear();
        service.getTopStories().subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<Integer>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(context, "Could not fetch stories", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(List<Integer> integers) {
                for (Integer id : integers.subList(0, MAX_STORIES_TO_DISPLAY)) {
                    service.getItem(id)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<Item>() {
                                @Override
                                public void onCompleted() {
                                    //
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e("Some error", e.getMessage());
                                }

                                @Override
                                public void onNext(Item item) {
                                    mStoryCardAdapter.addData(item);
                                }
                            });
                }
            }
        });
    }

    private class ItemClickListener implements StoryCardAdapter.ClickListener {
        @Override
        public void onItemClick(int position, View v, List<Item> items) {
            Item item = items.get(position);

            Toast.makeText(context, "You have selected pos " + item.getTitle() + " with title: ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, StoryDetail.class);
            intent.putExtra("item", item);
            startActivity(intent);
        }

        @Override
        public void onItemLongClick(int position, View v, List<Item> items) {
            Item item = items.get(position);
            Intent intent = new Intent(context, StoryContent.class);
            intent.putExtra("item", item);
            startActivity(intent);
        }
    }
}
