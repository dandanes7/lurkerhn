package com.dd7.lurkerhn.rest.client;

import retrofit.RestAdapter;

public class RestClientFactory {

    public static HackerNewsApiClient createHackerNewsService() {
        return RestClientFactory.createRetrofitService(HackerNewsApiClient.class, HackerNewsApiClient.HNENDPOINT);
    }

    private static <T> T createRetrofitService(final Class<T> clazz, final String endPoint) {
        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(endPoint)
                .build();

        return restAdapter.create(clazz);
    }
}