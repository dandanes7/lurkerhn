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
import com.dd7.yahn.rest.client.service.HackerNewsApi;
import com.dd7.yahn.rest.client.service.ServiceFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final CardAdapter mCardAdapter = new CardAdapter(this);
        mRecyclerView.setAdapter(mCardAdapter);

        Button bClear = (Button) findViewById(R.id.button_clear);
        Button bFetch = (Button) findViewById(R.id.button_fetch);
        bClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCardAdapter.clear();
            }
        });
//        Button testButton = (Button) findViewById(R.id.test_button);
//        testButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TextView testTextView = (TextView) findViewById(R.id.test_text);
//                testTextView.setText("some test text fool");
//            }
//        });

        bFetch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final HackerNewsApi service = ServiceFactory.createRetrofitService(HackerNewsApi.class, HackerNewsApi.HNENDPOINT);

                List<Integer> storyIds= Arrays.asList(12922141,12921389,12922514,12921570,12920449,12919780,12922318,12920598,12920279);

                for (Integer id : storyIds) {
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
                                    mCardAdapter.addData(item);
                                }
                            });
                }

//                service.getTopStories().flatMap(storyIds )
//
//                for (Integer id : storyIds.subList(0, 30)) {
//                    service.getItem(id)
//                            .subscribeOn(Schedulers.newThread())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(new Subscriber<Item>() {
//                                @Override
//                                public void onCompleted() {
//                                    //
//                                }
//
//                                @Override
//                                public void onError(Throwable e) {
//                                    Log.e("Some error", e.getMessage());
//                                }
//
//                                @Override
//                                public void onNext(Item item) {
////                                    mCardAdapter.addData(new Item(integers.get(0)));
//                                    mCardAdapter.addData(item);
//                                }
//                            });
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
