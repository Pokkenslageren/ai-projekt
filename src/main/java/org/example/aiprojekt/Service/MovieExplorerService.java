package org.example.aiprojekt.Service;

import org.example.aiprojekt.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class MovieExplorerService {
    private static final Logger LOGGER = Logger.getLogger(MovieExplorerService.class.getName());

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private TMDBService tmdbService;

    public Map<String, Object> exploreMovie(String movieTitle) {
        if (movieTitle == null || movieTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie title cannot be empty");
        }

        Map<String, Object> response = new HashMap<>();
        MovieDTO movie = null;
        MovieDTO details = null;
        Map<String, Object> aiAnalysis = null;

        try {
            // search
            movie = tmdbService.searchMovie(movieTitle);
            if (movie == null) {
                throw new RuntimeException("No movie found with title: " + movieTitle);
            }

            // get
            details = tmdbService.getMovieDetails(movie.getId());
            if (details == null) {
                throw new RuntimeException("Could not fetch details for movie: " + movieTitle);
            }

            // Generate AI analysis
            String prompt = generatePrompt(details);
            aiAnalysis = openAIService.getMovieAnalysis(prompt);

            response.put("movieDetails", details);
            response.put("aiAnalysis", aiAnalysis);

            response.put("status", "success");
            response.put("query", movieTitle);

        } catch (WebClientResponseException e) {
            LOGGER.log(Level.SEVERE, "API call failed: " + e.getMessage(), e);
            throw new RuntimeException("Failed to communicate with external API: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage());
        }

        return response;
    }
// CHATGPT PROMPT:
    private String generatePrompt(MovieDTO movie) {
        return String.format(
                """
                Based on the movie '%s' (%s), please provide:
                
                1. THEMES AND ANALYSIS:
                - Main themes and motifs
                - Directorial style and cinematographic elements
                - Cultural or historical significance
                
                2. INTERESTING TRIVIA:
                - Three fascinating behind-the-scenes facts
                - Production challenges or unique aspects
                - Impact on the film industry
                
                3. SIMILAR MOVIE RECOMMENDATIONS:
                - Three specific movie recommendations
                - Brief explanation for each recommendation
                - Why fans of this movie would enjoy them
                
                Movie Overview for context: %s
                
                Please structure your response clearly with these three sections.
                """,
                movie.getTitle(),
                movie.getReleaseDate(),
                movie.getOverview()
        );
    }

    // matches criteria?
    private boolean isMovieMatch(MovieDTO movie, String searchTitle) {
        return movie.getTitle().toLowerCase().contains(searchTitle.toLowerCase()) ||
                (movie.getOverview() != null &&
                        movie.getOverview().toLowerCase().contains(searchTitle.toLowerCase()));
    }

    // format movie data
    private Map<String, Object> formatMovieResponse(MovieDTO movie) {
        Map<String, Object> formatted = new HashMap<>();
        formatted.put("id", movie.getId());
        formatted.put("title", movie.getTitle());
        formatted.put("overview", movie.getOverview());
        formatted.put("releaseDate", movie.getReleaseDate());
        formatted.put("rating", movie.getVoteAverage());
        formatted.put("posterPath", movie.getPosterPath() != null ?
                "https://image.tmdb.org/t/p/w500" + movie.getPosterPath() : null);
        return formatted;
    }
}