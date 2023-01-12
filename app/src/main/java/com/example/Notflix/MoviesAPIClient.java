package com.example.Notflix;

import android.util.Log;

import androidx.annotation.Nullable;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

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
        params.add("exact", "true");
        params.add("info", "base_info");

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

                    String id = movie.getString("id");
                    String description = movie.getJSONObject("plot").getJSONObject("plotText").getString("plainText");
                    String year = getReleaseYear(movie);

                    handler.onSuccess(new Title(
                            name,
                            coverURL,
                            id,
                            description,
                            year
                    ));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public @Nullable String getReleaseYear(JSONObject movie) {
        try {
            return "" + movie.getJSONObject("releaseYear").getInt("year");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void getTitles(ResponseHandler<ArrayList<Title>> handler, RequestParams params) {
        params.add("sort", "year.decr");
        params.add("endYear", "2022");
        params.add("info", "base_info");

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

                            if (movie.isNull("primaryImage")) continue;

                            JSONObject primaryImage = movie.getJSONObject("primaryImage");
                            String coverURL = primaryImage.getString("url");
                            String id = movie.getString("id");
                            String year = getReleaseYear(movie);

                            String description = movie.getJSONObject("plot").getJSONObject("plotText").getString("plainText");

                            movies.add(new Title(
                                    name,
                                    coverURL,
                                    id,
                                    description,
                                    year
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
