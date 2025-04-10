package org.example.aiprojekt.Service;

import org.example.aiprojekt.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class MovieExplorerService {

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private TMDBService tmdbService;

    private final Map<String, Long> recentTitles = new ConcurrentHashMap<>();
    private static final long TIME_LIMIT_MS = 10_000;

    public Map<String, Object> exploreMovie(String movieTitle) {
        if (movieTitle == null || movieTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie title cannot be empty");
        }

        validateRequestTiming(movieTitle);
        Map<String, Object> response = new HashMap<>();

        try {
            // Get initial movie data
            MovieDTO movie = tmdbService.searchMovie(movieTitle);
            if (movie == null) {
                throw new RuntimeException("No movie found with title: " + movieTitle);
            }

            // Get detailed movie info
            MovieDTO details = tmdbService.getMovieDetails(movie.getId());

            // Get similar movies
            List<MovieDTO> similarMovies = tmdbService.getSimilarMovies(movie.getId());

            // Generate AI analysis
            String prompt = generatePrompt(details);
            Map<String, Object> aiAnalysis = openAIService.getMovieAnalysis(prompt);

            // Format genres
            String genres = details.getGenres() != null ?
                    details.getGenres().stream()
                            .map(genre -> genre.getName())
                            .collect(Collectors.joining(", ")) :
                    "";

            response.put("title", details.getTitle());
            response.put("releaseDate", details.getReleaseDate());
            response.put("overview", details.getOverview());
            response.put("tagline", details.getTagline());
            response.put("runtime", details.getRuntime() + " minutes");
            response.put("genres", genres);
            response.put("budget", details.getBudget());
            response.put("revenue", details.getRevenue());
            response.put("rating", details.getVoteAverage() + "/10");
            response.put("interestingTrivia", aiAnalysis.get("analysis"));
            response.put("similarMovies", similarMovies.stream()
                    .limit(3)
                    .map(m -> Map.of(
                            "title", m.getTitle(),
                            "releaseDate", m.getReleaseDate(),
                            "rating", m.getVoteAverage()
                    ))
                    .collect(Collectors.toList()));
            response.put("status", "success");

            return response;

        } catch (WebClientResponseException e) {
            throw new RuntimeException("Failed to communicate with external API: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage());
        }
    }

    private void validateRequestTiming(String movieTitle) {
        String normalizedTitle = movieTitle.trim().toLowerCase();
        long now = System.currentTimeMillis();

        if (recentTitles.containsKey(normalizedTitle)) {
            long lastTime = recentTitles.get(normalizedTitle);
            long timeElapsed = now - lastTime;
            if (timeElapsed < TIME_LIMIT_MS) {
                long waitTimeSeconds = (TIME_LIMIT_MS - timeElapsed) / 1000;
                throw new RuntimeException("Vent venligst " + waitTimeSeconds + " sekunder før du søger igen.");
            }
        }
        recentTitles.put(normalizedTitle, now);
        recentTitles.entrySet().removeIf(entry -> (now - entry.getValue() > TIME_LIMIT_MS));
    }

    private String generatePrompt(MovieDTO movie) {
        return String.format("""
                As Kinogrisen (The Cinema Pig), a quirky and knowledgeable movie expert, analyze the movie '%s'.
                
              
                Please provide:
                1. Basic movie details:
                
                - title: %s
                - Release Date: %s
                - Overview: %s
                - Tagline: %s
                - Runtime: %d minutes
                - Budget: $%d
                - Revenue: $%d
                
                2. Three interesting and entertaining pieces of trivia about this movie
                
                3. Your personal recommendation as Kinogrisen, explaining why you love (or don't love) this movie
                
                Make your response fun and engaging, keeping in mind you're a movie-loving pig! Answer in danish.
                """,
                movie.getTitle(),
                movie.getReleaseDate(),
                movie.getOverview(),
                movie.getTagline(),
                movie.getRuntime(),
                movie.getBudget(),
                movie.getRevenue()
        );
    }
}