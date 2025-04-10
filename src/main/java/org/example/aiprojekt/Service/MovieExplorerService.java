package org.example.aiprojekt.Service;

import org.example.aiprojekt.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class MovieExplorerService {
    private static final Logger LOGGER = Logger.getLogger(MovieExplorerService.class.getName());

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private TMDBService tmdbService;

    //Map til at holde styr på seneste forespørgsler
    private final Map<String, Long> recentTitles = new ConcurrentHashMap<>();
    private static final long TIME_LIMIT_MS = 10_000;

    @Autowired
    public MovieExplorerService(TMDBService tmdbService) {
        this.tmdbService = tmdbService;
    }
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
            
            response.put("title", details.getTitle());
            response.put("releaseDate", details.getReleaseDate());
            response.put("overview", details.getOverview());
            response.put("tagline", details.getTagline());
            response.put("runtime", details.getRuntime() + " minutes");
            response.put("genres", String.join(", ", details.getGenres()));
            response.put("originCountry", details.getOriginCountry());
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
            if (now - lastTime < TIME_LIMIT_MS) {
                throw new RuntimeException("Denne forespørgsel er allerede sendt for nylig. Vent lidt og prøv igen.");
            }
        }
        recentTitles.put(normalizedTitle, now);
    }
// CHATGPT PROMPT:
private String generatePrompt(MovieDTO movie) {
    return String.format("""
            As Kinogrisen (The Cinema Pig), a quirky and knowledgeable movie expert, analyze the movie '%s'.
            
            Based on these details:
            - Release Date: %s
            - Overview: %s
            - Tagline: %s
            - Runtime: %d minutes
            - Budget: $%d
            - Revenue: $%d
            
            Please provide:
            1. Three interesting and entertaining pieces of trivia about this movie
            2. Your personal recommendation as Kinogrisen, explaining why you love (or don't love) this movie
            
            Make your response fun and engaging, keeping in mind you're a movie-loving pig!
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

    private String formatNumber(Long number) {
        if (number == null || number == 0) {
            return "N/A";
        }
        return NumberFormat.getNumberInstance(Locale.US).format(number);
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