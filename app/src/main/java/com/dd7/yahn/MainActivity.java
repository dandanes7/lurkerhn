package com.dd7.yahn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.dd7.yahn.adapter.ClickListener;
import com.dd7.yahn.adapter.StoryCardAdapter;
import com.dd7.yahn.rest.model.Item;
import com.dd7.yahn.rest.client.HackerNewsApiClient;
import com.dd7.yahn.rest.client.ClientFactory;
import com.dd7.yahn.service.PreferenceService;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String[] CATEGORIES = {"Saved Stories", "Settings"};
    private int MAX_STORIES_TO_DISPLAY = 50;

    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        PreferenceService preferenceService = new PreferenceService(mContext);
        MAX_STORIES_TO_DISPLAY = preferenceService.getMaxStoriesSetting();

        ListView mDrawerList = (ListView) findViewById(R.id.drawerList);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, CATEGORIES));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

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
        final HackerNewsApiClient service = ClientFactory.createRetrofitService(HackerNewsApiClient.class, HackerNewsApiClient.HNENDPOINT);
        mStoryCardAdapter.clear();

        service.getTopStories()
                .flatMapIterable(ids -> ids)
                .take(MAX_STORIES_TO_DISPLAY)
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

    public class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String name = "com.dd7.yahn." + CATEGORIES[position] + "Activity";
            try {
                Intent intent = new Intent(mContext, Class.forName(name));
                startActivity(intent);
            } catch (ClassNotFoundException e) {
                Toast.makeText(mContext, "Could not find activity "+name, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
