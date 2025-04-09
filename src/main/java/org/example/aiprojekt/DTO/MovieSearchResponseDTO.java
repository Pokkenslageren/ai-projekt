package org.example.aiprojekt.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class MovieSearchResponseDTO {
    private int page;
    @JsonProperty("results")
    private List<MovieDTO> results;
    @JsonProperty("total_results")
    private int totalResults;
    @JsonProperty("total_pages")
    private int totalPages;

    // Getters and setters
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MovieDTO> getResults() {
        return results;
    }

    public void setResults(List<MovieDTO> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}