package com.example.Notflix;

import android.util.Log;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public final class MoviesAPIClient {
    private static final String url = "https://moviesdatabase.p.rapidapi.com/";
    private static AsyncHttpClient client;

    private static final String API_KEY = "901f541c9cmsh5bd0bc6195ab563p160170jsn62b759c3d40d";
    private static final String API_HOST = "moviesdatabase.p.rapidapi.com";

    public MoviesAPIClient() {
        client = new AsyncHttpClient();

        client.addHeader("X-RapidAPI-Key", API_KEY);
        client.addHeader("X-RapidAPI-Host", API_HOST);
    }

    public interface ResponseHandler<T> {
        public void onSuccess(T data);
    }

    public void getTitleByName(ResponseHandler<Title> handler, String name, RequestParams params) {
        params.add("limit", "1");
        client.get(getAbsoluteURL("titles/search/title/" + name), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject body) {
                try {
                    if (body.isNull("results")) return;
                    JSONArray results = body.getJSONArray("results");
                    if ( results.length() < 1) return;

                    JSONObject movie = results.getJSONObject(0);
                    String coverURL = null;

                    if (!movie.isNull("primaryImage")) {
                        JSONObject primaryImage = movie.getJSONObject("primaryImage");
                        coverURL = primaryImage.getString("url");
                    }

                    handler.onSuccess(new Title(
                            name,
                            coverURL
                    ));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getTitles(ResponseHandler<ArrayList<Title>> handler) {
        getTitles(handler, new RequestParams());
    }

    public void getTitles(ResponseHandler<ArrayList<Title>> handler, RequestParams params) {
        params.add("sort", "year.decr");
        params.add("endYear", "2022");
        client.get(getAbsoluteURL("titles"), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject body) {
                try {
                    JSONArray results = body.getJSONArray("results");
                    ArrayList<Title> movies = new ArrayList<>();

                    for (int i = 0; i < results.length(); i++) {
                        try {
                            JSONObject movie = results.getJSONObject(i);
                            String name = movie.getJSONObject("titleText").getString("text");

                            String coverURL = null;

                            if (!movie.isNull("primaryImage")) {
                                JSONObject primaryImage = movie.getJSONObject("primaryImage");
                                coverURL = primaryImage.getString("url");
                            }

                            movies.add(new Title(
                                    name,
                                    coverURL
                            ));
                        } catch (Exception e) {
                            e.printStackTrace();
                            continue;
                        }
                    }

                    handler.onSuccess(movies);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {

            }
        });
    }

    private static String getAbsoluteURL(String relativeURL) {
        return url + relativeURL;
    }
}
