package com.chinmay.moviesapp;

class Movie {
    private final String title;
    private final String Movie_Overview;
    private final String Movie_Popularity;
    private final String imgid;
    private final String publishDate;

    public String getMovie_Overview() {
        return Movie_Overview;
    }

    public String getMovie_Popularity() {
        return Movie_Popularity;
    }


    public Movie(String title, String movie_Overview, String movie_Popularity, String imgid, String publishDate) {
        this.title = title;
        Movie_Overview = movie_Overview;
        Movie_Popularity = movie_Popularity;
        this.imgid = imgid;
        this.publishDate = publishDate;
    }


    public String getTitle() {
        return title;
    }

    public String getImgid() {
        return imgid;
    }


    public String getPublishDate() {
        return publishDate;
    }
}
