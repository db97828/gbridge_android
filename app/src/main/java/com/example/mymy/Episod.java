package com.example.mymy;

public class Episod {
    String episode_number;
    String name;
    String overview;
    String vote_average;
    String still_path;
    String show_id;

    public Episod(String episode_number, String name, String overview, String vote_average, String still_path, String show_id) {
        this.episode_number = episode_number;
        this.name = name;
        this.overview = overview;
        this.vote_average = vote_average;
        this.still_path = still_path;
        this.show_id = show_id;
    }

    public String getEpisode_number() {
        return episode_number;
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getStill_path() {
        return still_path;
    }

    public String getShow_id() {
        return show_id;
    }
}
