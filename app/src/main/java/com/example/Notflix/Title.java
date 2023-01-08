package com.example.Notflix;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class Title {
    private String name;
    private String coverURL;
    private String id;

    public static HashMap<String, Title> titles = new HashMap<>();

    public Title(String name, @Nullable String coverURL, String id) {
        this.name = name;
        this.coverURL = coverURL;
        this.id = id;

        titles.put(name, this);
    }

    public static Title getTitle(String name) {
        return titles.get(name);
    }

    public String getName() {
        return this.name;
    }

    public String getCoverURL() {
        return this.coverURL;
    }

    public String toString() { return this.name; }

    public String getId() { return id; }
}
