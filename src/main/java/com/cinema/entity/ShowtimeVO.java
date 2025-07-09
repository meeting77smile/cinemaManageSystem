package com.cinema.entity;

import lombok.Data;

@Data
public class ShowtimeVO extends Showtime {
    private String movieTitle;
    private String cinemaName;
    private String hallName;
    private Integer duration;

    public String getMovieTitle() {
        return movieTitle;
    }
    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }
    public String getCinemaName() {
        return cinemaName;
    }
    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }
    public String getHallName() {
        return hallName;
    }
    public void setHallName(String hallName) {
        this.hallName = hallName;
    }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
} 