package com.dd7.lurkerhn;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.dd7.lurkerhn.adapter.ClickListener;
import com.dd7.lurkerhn.adapter.StoryCardAdapter;
import com.dd7.lurkerhn.rest.client.RestClientFactory;
import com.dd7.lurkerhn.rest.client.HackerNewsApiClient;
import com.dd7.lurkerhn.rest.model.Item;
import com.dd7.lurkerhn.service.PreferenceService;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ActionBarActivity {

    private final String[] ACTIVITIES = {"Saved Stories", "Settings"};
    private static Map<String, Class> ACTIVITY_CLASSES;
    private static int MAX_STORIES = 50;
    private static String PREFERRED_CAT;

    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private HackerNewsApiClient mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ACTIVITY_CLASSES = new HashMap<>();
        ACTIVITY_CLASSES.put(ACTIVITIES[0], SavedStoriesActivity.class);
        ACTIVITY_CLASSES.put(ACTIVITIES[1], SettingsActivity.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mService = RestClientFactory.createHackerNewsService();

        getPreferences();
        prepareDrawerList();
        final StoryCardAdapter mStoryCardAdapter = prepareRecyclerViewAndGetCardAdapter();
        setUpSupportActionBar();
        prepareDrawerToggle();

        prepareSwipeRefreshLayout(mStoryCardAdapter);
        loadTopStories(mStoryCardAdapter, mService);
    }

    private void prepareSwipeRefreshLayout(StoryCardAdapter storyCardAdapter) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            loadTopStories(storyCardAdapter, mService);
        });
    }

    private void setUpSupportActionBar() {
        mActivityTitle = getTitle().toString();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void getPreferences() {
        PreferenceService preferenceService = new PreferenceService(mContext);
        MAX_STORIES = preferenceService.getMaxStoriesPref();
        PREFERRED_CAT = preferenceService.getCategoryPref();
    }


    private void prepareDrawerList() {
        ListView mDrawerList = (ListView) findViewById(R.id.drawerList);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.item_drawer_list, ACTIVITIES));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private StoryCardAdapter prepareRecyclerViewAndGetCardAdapter() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.story_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final StoryCardAdapter mStoryCardAdapter = new StoryCardAdapter(this);
        mStoryCardAdapter.setOnItemClickListener(new ItemClickListener());
        mRecyclerView.setAdapter(mStoryCardAdapter);
        return mStoryCardAdapter;
    }

    private void prepareDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void loadTopStories(final StoryCardAdapter storyCardAdapter, HackerNewsApiClient service) {
        storyCardAdapter.clear();
        Observable<List<Integer>> stories = service.getTopStories();

        String[] categories = getResources().getStringArray(R.array.categories);
        if (PREFERRED_CAT.equals(categories[1])) {
            stories = service.getBestStories();
        } else if (PREFERRED_CAT.equals(categories[2])) {
            stories = service.getNewStories();
        }

        stories.flatMapIterable(ids -> ids)
                .take(MAX_STORIES)
                .concatMapEager(id -> service.getItem(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Item>() {
                    @Override
                    public void onCompleted() {
//                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("NETWORKERROR", "Something went wrong" + e.getMessage(), e);
                        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Item item) {
                        storyCardAdapter.addData(item);
                    }
                });
    }

    private class ItemClickListener implements ClickListener {
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
            Intent intent = new Intent(mContext, StoryWebViewActivity.class);
            intent.putExtra("item", item);
            startActivity(intent);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                Intent intent = new Intent(mContext, Class.forName(ACTIVITY_CLASSES.get(ACTIVITIES[position]).getName()));
                startActivity(intent);
            } catch (ClassNotFoundException e) {
                Toast.makeText(mContext, "Could not find activity " + ACTIVITIES[position], Toast.LENGTH_SHORT).show();
            }
        }
    }
}
