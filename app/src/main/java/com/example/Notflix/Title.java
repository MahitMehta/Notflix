package com.example.Notflix;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class Title {
    private String name;
    private String coverURL;
    private String id;
    private String description;
    private String year;

    public static HashMap<String, Title> titles = new HashMap<>();

    public Title(String name, @Nullable String coverURL, String id, String description, String year) {
        this.name = name;
        this.coverURL = coverURL;
        this.id = id;
        this.description = description;
        this.year = year;

        titles.put(name, this);
    }

    public static Title getTitle(String name) {
        return titles.get(name);
    }

    public String getYear() { return this.year; }

    public String getName() {
        return this.name;
    }

    public String getCoverURL() {
        return this.coverURL;
    }

    public String toString() { return this.name; }

    public String getDescription() { return this.description; }

    public String getId() { return id; }
}
