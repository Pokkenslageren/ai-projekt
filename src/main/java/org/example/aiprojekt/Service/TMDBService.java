package org.example.aiprojekt.Service;

import org.example.aiprojekt.DTO.MovieDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TMDBService {

    private final WebClient tmdbWebClient;

    @Autowired
    public TMDBService(WebClient tmdbWebClient) {
        this.tmdbWebClient = tmdbWebClient;
    }

    public MovieDTO searchMovie(String query) {
        return tmdbWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/movie")
                        .queryParam("query", query)
                        .build())
                .retrieve()
                .bodyToMono(MovieDTO.class)
                .block();
    }

    public MovieDTO getMovieDetails(Long movieId) {
        return tmdbWebClient.get()
                .uri("/movie/" + movieId)
                .retrieve()
                .bodyToMono(MovieDTO.class)
                .block();
    }
}