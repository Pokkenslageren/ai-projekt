package org.example.aiprojekt.Service;

import org.example.aiprojekt.DTO.MovieDTO;
import org.example.aiprojekt.DTO.MovieSearchResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TMDBService {

    private final WebClient tmdbWebClient;

    @Autowired
    public TMDBService(WebClient tmdbWebClient) {
        this.tmdbWebClient = tmdbWebClient;
    }

    public MovieDTO searchMovie(String query) {
        MovieSearchResponseDTO searchResponse = tmdbWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/movie")
                        .queryParam("query", query)
                        .build())
                .retrieve()
                .bodyToMono(MovieSearchResponseDTO.class)
                .block();

        if (searchResponse != null && !searchResponse.getResults().isEmpty()) {
            return searchResponse.getResults().get(0);
        }

        return null;
    }

    public MovieDTO getMovieDetails(Long movieId) {
        return tmdbWebClient.get()
                .uri("/movie/" + movieId)
                .retrieve()
                .bodyToMono(MovieDTO.class)
                .block();
    }

    public List<MovieDTO> getSimilarMovies(Long movieId) {
        return tmdbWebClient.get()
                .uri("/movie/" + movieId + "/similar")
                .retrieve()
                .bodyToMono(MovieSearchResponseDTO.class)
                .block()
                .getResults()
                .stream()
                .limit(3) // Only get top 3 similar movies
                .collect(Collectors.toList());
    }
}