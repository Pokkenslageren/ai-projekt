package org.example.aiprojekt.Controller;

import org.example.aiprojekt.Service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OpenAIController {

    @Autowired
    OpenAIService openAIService;

    @Value("${spring.ai.openai.api-key}")
    private String openApiKey;

    @GetMapping("/key")
    public String key() {
        return openApiKey;
    }

    @GetMapping("/test")
    public Map<String, Object> test(@RequestParam String movie) {
        System.out.println("🎬 /test endpoint hit with movie: " + movie);

        String prompt = "Find a movie like: " + movie;
        return openAIService.promptOpenAI(prompt);
    }
}