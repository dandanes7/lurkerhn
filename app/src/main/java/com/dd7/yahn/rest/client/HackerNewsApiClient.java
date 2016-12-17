package com.dd7.yahn.rest.client;

import com.dd7.yahn.rest.model.Item;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

import java.util.List;

public interface HackerNewsApiClient {

    String HNENDPOINT = "https://hacker-news.firebaseio.com/v0/";

    @GET("/topstories.json")
    Observable<List<Integer>> getTopStories();


    @GET("/beststories.json")
    Observable<List<Integer>> getBestStories();


    @GET("/item/{id}.json")
    Observable<Item> getItem(@Path("id") int id);

}
