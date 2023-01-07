package com.example.Notflix;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TitleGroupsAdapter extends ArrayAdapter<TitleGroup> {
    int xmlResource;
    List<TitleGroup> list;
    Context ctx;

    public TitleGroupsAdapter(@NonNull Context ctx, int resource, @NonNull List<TitleGroup> objects) {
        super(ctx, resource, objects);
        xmlResource = resource;
        list = objects;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View adapterLayout = layoutInflater.inflate(xmlResource, null);

        TitleGroup group = list.get(position);
        TextView title = adapterLayout.findViewById(R.id.contentTitle);
        title.setText(group.getName());

        RecyclerView recyclerView = adapterLayout.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        // The number of Columns
        LinearLayoutManager layoutManager = new LinearLayoutManager( ctx, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        TitleGroupAdapter adapter = new TitleGroupAdapter(ctx, group.getTitles());
        recyclerView.setAdapter(adapter);

        return adapterLayout;
    }
}