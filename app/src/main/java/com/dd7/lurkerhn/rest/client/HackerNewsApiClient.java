package com.dd7.lurkerhn.rest.client;

import com.dd7.lurkerhn.rest.model.Item;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

import java.util.List;

public interface HackerNewsApiClient {

    String HNENDPOINT = "https://hacker-news.firebaseio.com/v0/";
    String HNWEB = "https://news.ycombinator.com/";

    @GET("/topstories.json")
    Observable<List<Integer>> getTopStories();

    @GET("/beststories.json")
    Observable<List<Integer>> getBestStories();

    @GET("/newstories.json")
    Observable<List<Integer>> getNewStories();

    @GET("/showstories.json")
    Observable<List<Integer>> getShowStories();

    @GET("/askstories.json")
    Observable<List<Integer>> getAskStories();

    @GET("/item/{id}.json")
    Observable<Item> getItem(@Path("id") int id);
}
