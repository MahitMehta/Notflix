package com.example.Notflix;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TitleGroupAdapter extends RecyclerView.Adapter<TitleGroupAdapter.ViewHolder> {
    private ArrayList<Title> titles;
    private Context context;

    public TitleGroupAdapter(Context context, ArrayList<Title> titles) {
        super();

        this.context = context;
        this.titles = titles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie, viewGroup, false);

        ViewHolder holder = new ViewHolder(v, context, titles.get(i));
        return holder;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Title movie = titles.get(position);
        String coverURL = movie.getCoverURL();

        if (coverURL != null) {
            Picasso.get()
                    .load(coverURL)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .resize(350, 500)
                    .centerCrop()
                    .into(viewHolder.movieCover, new Callback() {
                        @Override
                        public void onSuccess() {
                          //  ((MainActivity) context).resizeMoviesList();
                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get()
                                    .load(coverURL)
                                    .resize(350, 500)
                                    .centerCrop()
                                    .into(viewHolder.movieCover);
                        }
                    });
        }

    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return titles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener  {
        ImageView movieCover;
        Context context;
        Title title;

        private ItemClickListener clickListener;
        ViewHolder(View itemView, Context context, Title title) {
            super(itemView);
            this.context = context;
            movieCover = itemView.findViewById(R.id.cover);
            this.title = title;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
        void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }
        @Override
        public void onClick(View view) {
            Intent intent = new Intent((MainActivity) context, MovieActivity.class);

            intent.putExtra(MovieActivity.EXTRA_PARAM_ID, title.getName());

            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    (MainActivity) context,
                    // Now we provide a list of Pair items which contain the view we can transitioning
                    // from, and the name of the view it is transitioning to, in the launched activity
                    new Pair<>(view.findViewById(R.id.cover),
                            MovieActivity.VIEW_NAME_HEADER_IMAGE),
                    new Pair<>(view.findViewById(R.id.cover_container),
                            MovieActivity.VIEW_NAME_COVER_CONTAINER)
                    );

            // Now we can start the Activity, providing the activity options as a bundle
            ActivityCompat.startActivity((MainActivity) context, intent, activityOptions.toBundle());
        }
        @Override
        public boolean onLongClick(View view) {

            return true;
        }
    }
}
