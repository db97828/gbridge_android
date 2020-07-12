package com.example.mymy;

public class Tv {
    private String name;
    private String original_name;
    private String poster_path;
    private String overview;
    private String backdrop_path;
    private String first_air_date;
    private String id;


    public Tv(String name, String original_name, String poster_path, String overview, String backdrop_path, String first_air_date, String id){
        this.name = name;
        this.original_name = original_name;
        this.poster_path = poster_path;
        this.overview = overview;
        this.backdrop_path = backdrop_path;
        this.first_air_date = first_air_date;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public String getId() {
        return id;
    }
}
