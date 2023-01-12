package com.example.Notflix;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class MovieActivity extends AppCompatActivity  {

    public static final String EXTRA_PARAM_ID = "detail:_id";

    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";

    Title title;
    TextView contentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        String titleName = getIntent().getStringExtra(EXTRA_PARAM_ID);
        Log.d("url", titleName + "");
        title = Title.getTitle(titleName);

        TextView description = findViewById(R.id.description);
        description.setText(title.getDescription());

        ImageView cover = findViewById(R.id.imageview_header);
        contentName = (TextView) findViewById(R.id.movieName);
        contentName.setText(title.getName());

        TextView releaseYear = findViewById(R.id.release_year);
        if (title.getYear() != null) releaseYear.setText(title.getYear());

        findViewById(R.id.play).setOnClickListener((View v) -> {
            String url = "https://soap2day.ac/search/keyword/" + title.getName();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        });


        Picasso.get()
                .load(title.getCoverURL())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .resize(700, 1000)
                .centerCrop()
                .into(cover, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(title.getCoverURL())
                                .resize(700, 1000)
                                .centerCrop()
                                .into(cover);
                    }
                });

        ViewCompat.setTransitionName(cover, VIEW_NAME_HEADER_IMAGE);
    }


    @RequiresApi(21)
    private boolean addTransitionListener() {
        final Transition transition = getWindow().getSharedElementEnterTransition();

        if (transition != null) {
            // There is an entering shared element transition so add a listener to it
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionStart(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionResume(Transition transition) {
                    // No-op
                }
            });
            return true;
        }

        // If we reach here then we have not added a listener
        return false;
    }
}
