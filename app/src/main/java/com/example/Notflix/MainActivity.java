package com.example.Notflix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<TitleGroup> titleGroups;
    ImageView spotlight;
    HashMap<String, RequestParams> categories;
    TitleGroupsAdapter adapter;

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

        adapter = new TitleGroupsAdapter(this, R.layout.title_group, titleGroups);

        if (savedInstanceState == null || savedInstanceState.isEmpty()) {
            categories = new HashMap<>();

            RequestParams params1 = new RequestParams();
            params1.add("titleType", "movie");
            params1.add("list", "most_pop_movies");

            RequestParams params2 = new RequestParams();
            params2.add("genre", "Drama");
            params2.add("titleType", "movie");

            RequestParams params3 = new RequestParams();
            params3.add("genre", "Horror");
            params3.add("titleType", "movie");

            RequestParams params4 = new RequestParams();
            params4.add("genre", "Comedy");
            params4.add("titleType", "movie");

            RequestParams params5 = new RequestParams();
            params5.add("genre", "Action");
            params5.add("titleType", "movie");

            RequestParams params6 = new RequestParams();
            params6.add("genre", "Adventure");
            params6.add("titleType", "movie");

            RequestParams params7 = new RequestParams();
            params7.add("genre", "Romance");
            params7.add("titleType", "movie");

            RequestParams params8 = new RequestParams();
            params8.add("genre", "Sci-Fi");
            params8.add("titleType", "movie");

            categories.put("Popular on Netflix", params1);
            categories.put("Trending Now", params2);
            categories.put("Horror Recommendations", params3);
            categories.put("Comedy Recommendations", params4);
            categories.put("Popular Action Films", params5);
            categories.put("Popular Adventure Films", params6);
            categories.put("Popular Romance Films", params7);
            categories.put("Popular Sci-Fi Films", params8);
        } else {
            categories = (HashMap<String, RequestParams>) savedInstanceState.getSerializable("catagories");
        }

        for (String key : categories.keySet()) {
            new MoviesAPIClient().getTitles((ArrayList<Title> data) -> {
                adapter.add(new TitleGroup(data, key));
                Utility.setListViewHeightBasedOnChildren(listView);
            }, categories.get(key));
        }

        listView.setAdapter(adapter);


    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        bundle.putSerializable("catagories", categories);
    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        categories = (HashMap<String, RequestParams>) bundle.getSerializable("catagories");
    }

    public void deleteCatagoryByName(String name) {
        CharSequence text = "Deleted \"" + name + "\" Row";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(MainActivity.this, text, duration);
        toast.show();

        categories.remove(name);
    }

    public void resizeMoviesList()
    {
      Utility.setListViewHeightBasedOnChildren(listView);
    }
}