package org.example.aiprojekt.DTO;

import java.util.List;

public class MovieExplorerResponseDTO {
    private String title;
    private String releaseDate;
    private String overview;
    private String tagline;
    private String runtime;
    private String[] genres;
    private String originCountry;
    private Long budget;
    private Long revenue;
    private Double rating;
    private String interestingTrivia;
    private List<MovieDTO> similarMovies;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public Long getBudget() {
        return budget;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
    }

    public Long getRevenue() {
        return revenue;
    }

    public void setRevenue(Long revenue) {
        this.revenue = revenue;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getInterestingTrivia() {
        return interestingTrivia;
    }

    public void setInterestingTrivia(String interestingTrivia) {
        this.interestingTrivia = interestingTrivia;
    }

    public List<MovieDTO> getSimilarMovies() {
        return similarMovies;
    }

    public void setSimilarMovies(List<MovieDTO> similarMovies) {
        this.similarMovies = similarMovies;
    }
}