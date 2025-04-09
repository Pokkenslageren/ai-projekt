package org.example.aiprojekt.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class MovieDTO {
        @JsonProperty("id")
        private Long id;
        @JsonProperty("title")
        private String title;
        @JsonProperty("overview")
        private String overview;
        @JsonProperty("release_date")
        private String releaseDate;
        @JsonProperty("tagline")
        private String tagline;
        @JsonProperty("runtime")
        private Integer runtime;
        @JsonProperty("vote_average")
        private Double voteAverage;
        @JsonProperty("poster_path")
        private String posterPath;
        @JsonProperty("budget")
        private Long budget;
        @JsonProperty("revenue")
        private Long revenue;
        @JsonProperty("origin_country")
        private String originCountry;
        @JsonProperty("genres")
        private List<Genre> genres;

        public static class Genre {
            @JsonProperty("id")
            private Long id;

            @JsonProperty("name")
            private String name;


        }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTagline() { return tagline; }

    public void setTagline(String tagline) { this.tagline = tagline; }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Long getBudget() { return budget; }

    public void setBudget(Long budget) { this.budget = budget; }

    public long getRevenue() { return revenue; }

    public void setRevenue(long revenue) { this.revenue = revenue; }

    public String getOriginCountry() { return originCountry; }

    public void setOriginCountry(String originCountry) { this.originCountry = originCountry; }

    public List getGenres() {
        return genres;
    }

    public void setGenres(List genres) {
        this.genres = genres;
    }
}