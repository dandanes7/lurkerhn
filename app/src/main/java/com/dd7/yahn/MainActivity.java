package com.dd7.yahn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.dd7.yahn.rest.client.adapter.CardAdapter;
import com.dd7.yahn.rest.client.model.Item;
import com.dd7.yahn.rest.client.model.ItemList;
import com.dd7.yahn.rest.client.service.HackerNewsApi;
import com.dd7.yahn.rest.client.service.ServiceFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final CardAdapter mCardAdapter = new CardAdapter();
        mRecyclerView.setAdapter(mCardAdapter);

        Button bClear = (Button) findViewById(R.id.button_clear);
        Button bFetch = (Button) findViewById(R.id.button_fetch);
        bClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCardAdapter.clear();
            }
        });
        Button testButton = (Button) findViewById(R.id.test_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView testTextView = (TextView) findViewById(R.id.test_text);
                testTextView.setText("some test text fool");
            }
        });

        bFetch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HackerNewsApi service = ServiceFactory.createRetrofitService(HackerNewsApi.class, HackerNewsApi.HNENDPOINT);
                service.loadTopStories()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ItemList>() {
                            @Override
                            public void onCompleted() {
                                //
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("Some error", e.getMessage());
                            }

                            @Override
                            public void onNext(ItemList itemList) {
                                mCardAdapter.addData(new Item(itemList.getIds().get(0)));
                            }
                        });

//                for(int id: ids) {
//                    service.getItem(id)
//                            .subscribeOn(Schedulers.newThread())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(new Subscriber<Item>() {
//                                @Override
//                                public final void onCompleted() {
//                                    // do nothing
//                                }
//
//                                @Override
//                                public final void onError(Throwable e) {
//                                    Log.e("GithubDemo", e.getMessage());
//                                }
//
//                                @Override
//                                public final void onNext(Item response) {
//                                    mCardAdapter.addData(response);
//                                }
//                            });
//                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}
