package com.dd7.yahn.rest.client.service;

import com.dd7.yahn.rest.client.model.ItemList;
import com.dd7.yahn.rest.client.model.Item;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface HackerNewsApi {

    String HNENDPOINT = " https://hacker-news.firebaseio.com/v0/";

    @GET("topstories.json")
    Observable<ItemList> loadTopStories();

    @GET("item/{id}.json")
    Observable<Item> getItem(@Path("id") int id);

}
