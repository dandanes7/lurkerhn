package com.dd7.yahn.rest.client.service;

import com.dd7.yahn.rest.client.model.ItemList;
import com.dd7.yahn.rest.client.model.Item;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

import java.util.List;

public interface HackerNewsApi {

    String HNENDPOINT = "https://hacker-news.firebaseio.com/v0/";

    @GET("/topstories.json")
    Observable<List<Integer>> loadTopStories();


    @GET("/beststories.json")
    Observable<List<Integer>> loadBestStories();


    @GET("/item/{id}.json")
    Observable<Item> getItem(@Path("id") int id);

}
