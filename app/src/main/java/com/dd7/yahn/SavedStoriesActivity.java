package com.dd7.yahn;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.dd7.yahn.adapter.SavedStoriesCardAdapter;
import com.dd7.yahn.rest.client.ClientFactory;
import com.dd7.yahn.rest.client.HackerNewsApiClient;
import com.dd7.yahn.rest.model.Item;
import com.dd7.yahn.service.SavedStoriesService;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.List;

public class SavedStoriesActivity extends AppCompatActivity {

    private Context mContext;
    private SavedStoriesService savedStoriesService;
    private HackerNewsApiClient service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_stories);

        mContext = getApplicationContext();
        savedStoriesService = new SavedStoriesService(mContext);
        try {
            final SavedStoriesCardAdapter savedStoriesCardAdapter = new SavedStoriesCardAdapter(mContext);
            service = ClientFactory.createRetrofitService(HackerNewsApiClient.class, HackerNewsApiClient.HNENDPOINT);

            Observable.from(savedStoriesService.getItems())
                    .map(stringId -> Integer.valueOf(stringId))
                    .concatMapEager(id -> service.getItem(id))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
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
                            Toast.makeText(mContext, "Item: "+String.valueOf(item.getId()), Toast.LENGTH_LONG).show();
                            savedStoriesCardAdapter.addData(item);
                        }
                    });


        } catch (IOException e) {
            Toast.makeText(mContext, "Could not read saved ids: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
