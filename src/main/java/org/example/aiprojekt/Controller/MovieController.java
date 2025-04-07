package org.example.aiprojekt.Controller;

import org.example.aiprojekt.Service.MovieExplorerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "*")
public class MovieController {

    @Autowired
    private MovieExplorerService movieExplorerService;

    @GetMapping("/explore")
    public ResponseEntity<Map<String, Object>> exploreMovie(@RequestParam String title) {
        try {
            Map<String, Object> result = movieExplorerService.exploreMovie(title);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}