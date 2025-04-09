package org.example.aiprojekt.Controller;

import org.example.aiprojekt.DTO.MovieDTO;
import org.example.aiprojekt.Service.MovieExplorerService;
import org.example.aiprojekt.Service.TMDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "*")
public class MovieController {

    @Autowired
    MovieExplorerService movieExplorerService;

    @Value("${tmdb.access.token}")
    private String tmdbApiKey;

    @GetMapping("/explore")
    public ResponseEntity<Map<String, Object>> exploreMovie(@RequestParam String title) {
        try {
            Map<String, Object> result = movieExplorerService.exploreMovie(title);
            return ResponseEntity.ok(result);
        /*} catch (Exception e) {
            return ResponseEntity.badRequest().build();*/
        } catch (RuntimeException e) {
            return ResponseEntity.status(429).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Noget gik galt"));
        }
    }

    @GetMapping("/key")
    public String key() {
        return tmdbApiKey;
    }

    }
