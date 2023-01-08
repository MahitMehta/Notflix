package com.example.Notflix;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<TitleGroup> titleGroups;
    ImageView spotlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.items);

        titleGroups = new ArrayList<>();

        spotlight = findViewById(R.id.spotlight);

        RequestParams spotlightParams = new RequestParams();
        spotlightParams.add("sort", "year.decr");
        spotlightParams.add("titleType", "tvSeries");

        new MoviesAPIClient().getTitleByName((Title data) -> {
            if (data.getCoverURL() == null) return;

            Picasso.get()
                    .load(data.getCoverURL())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .centerCrop()
                    .fit()
                    .into(spotlight, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get()
                                    .load(data.getCoverURL())
                                    .centerCrop()
                                    .fit()
                                    .into(spotlight);
                        }
                    });

            spotlight.setOnClickListener((View) -> {
                Intent intent = new Intent(MainActivity.this, MovieActivity.class);

                intent.putExtra(MovieActivity.EXTRA_PARAM_ID, data.getName());

                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MainActivity.this,
                        // Now we provide a list of Pair items which contain the view we can transitioning
                        // from, and the name of the view it is transitioning to, in the launched activity
                        new Pair<>(spotlight,
                                MovieActivity.VIEW_NAME_HEADER_IMAGE)
                );

                // Now we can start the Activity, providing the activity options as a bundle
                ActivityCompat.startActivity(MainActivity.this, intent, activityOptions.toBundle());
            });
        }, "Wednesday", spotlightParams);

        TitleGroupsAdapter adapter = new TitleGroupsAdapter(this, R.layout.title_group, titleGroups);

        RequestParams params1 = new RequestParams();
        params1.add("titleType", "movie");
        params1.add("list", "most_pop_movies");

        new MoviesAPIClient().getTitles((ArrayList<Title> data) -> {
            adapter.add(new TitleGroup(data, "Popular on Netflix"));
            Utility.setListViewHeightBasedOnChildren(listView);
        }, params1);

        RequestParams params2 = new RequestParams();
        params2.add("genre", "Drama");
        params2.add("titleType", "movie");

        new MoviesAPIClient().getTitles((ArrayList<Title> data) -> {
            adapter.add(new TitleGroup(data, "Trending Now"));
            Utility.setListViewHeightBasedOnChildren(listView);
        }, params2);

        RequestParams params3 = new RequestParams();
        params3.add("genre", "Horror");
        params3.add("titleType", "movie");

        new MoviesAPIClient().getTitles((ArrayList<Title> data) -> {
            adapter.add(new TitleGroup(data, "Horror Recommendations"));
            Utility.setListViewHeightBasedOnChildren(listView);
        }, params3);

        RequestParams params4 = new RequestParams();
        params4.add("genre", "Comedy");
        params4.add("titleType", "movie");

        new MoviesAPIClient().getTitles((ArrayList<Title> data) -> {
            adapter.add(new TitleGroup(data, "Comedy Recommendations"));
            Utility.setListViewHeightBasedOnChildren(listView);
        }, params4);

        RequestParams params5 = new RequestParams();
        params5.add("genre", "Action");
        params5.add("titleType", "movie");

        new MoviesAPIClient().getTitles((ArrayList<Title> data) -> {
            adapter.add(new TitleGroup(data, "Popular Action Films"));
            Utility.setListViewHeightBasedOnChildren(listView);
        }, params5);

        RequestParams params6 = new RequestParams();
        params6.add("genre", "Adventure");
        params6.add("titleType", "movie");

        new MoviesAPIClient().getTitles((ArrayList<Title> data) -> {
            adapter.add(new TitleGroup(data, "Popular Adventure Films"));
            Utility.setListViewHeightBasedOnChildren(listView);
        }, params6);

        RequestParams params7 = new RequestParams();
        params7.add("genre", "Romance");
        params7.add("titleType", "movie");

        new MoviesAPIClient().getTitles((ArrayList<Title> data) -> {
            adapter.add(new TitleGroup(data, "Popular Romance Films"));
            Utility.setListViewHeightBasedOnChildren(listView);
        }, params7);

        RequestParams params8 = new RequestParams();
        params8.add("genre", "Sci-Fi");
        params8.add("titleType", "movie");

        new MoviesAPIClient().getTitles((ArrayList<Title> data) -> {
            adapter.add(new TitleGroup(data, "Popular Sci-Fi Films"));
            Utility.setListViewHeightBasedOnChildren(listView);
        }, params8);

        listView.setAdapter(adapter);


    }

    public void resizeMoviesList()
    {
      Utility.setListViewHeightBasedOnChildren(listView);
    }
}