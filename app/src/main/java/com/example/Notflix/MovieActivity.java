package com.example.Notflix;

import android.content.ClipData;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MovieActivity extends AppCompatActivity  {

    public static final String EXTRA_PARAM_ID = "detail:_id";

    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";

    public static final String VIEW_NAME_COVER_CONTAINER = "detail:cover:container";

    Title title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        String titleName = getIntent().getStringExtra(EXTRA_PARAM_ID);
        Log.d("url", titleName + "");
        title = Title.getTitle(titleName);

        ImageView cover = findViewById(R.id.imageview_header);
        CardView cover_container = findViewById(R.id.imageview_header_container);

        Log.d("url", title.getCoverURL());
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
        ViewCompat.setTransitionName(cover_container, VIEW_NAME_COVER_CONTAINER);
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
