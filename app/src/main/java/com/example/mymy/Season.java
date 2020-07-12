package com.example.mymy;

public class Season {

    String name;
    String season_number;
    String poster_path;
    String id;

    public Season(String name, String season_number, String poster_path, String id) {
        this.name = name;
        this.season_number = season_number;
        this.poster_path = poster_path;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getSeason_number() {
        return season_number;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getId() {
        return id;
    }
}
