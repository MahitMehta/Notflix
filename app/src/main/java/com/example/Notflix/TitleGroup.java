package com.example.Notflix;

import java.util.ArrayList;

public class TitleGroup {
    private ArrayList<Title> titles;
    private String name;

    public TitleGroup(ArrayList<Title> titles, String name) {
        this.titles = titles;
        this.name = name;
    }

    public ArrayList<Title> getTitles() {
        return titles;
    }

    public String getName() {
        return name;
    }
}
