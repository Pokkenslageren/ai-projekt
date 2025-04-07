package org.example.aiprojekt.Controller;

import org.example.aiprojekt.Service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OpenAIController {

    @Autowired
    OpenAIService openAIService;

    @Value("${API_KEY}")
    private String openapikey;

    @GetMapping("/key")
    public String key() {
        return openapikey;
    }

    @GetMapping("/test")
    public Map<String, Object> test() {
        Map<String, Object> testmap = openAIService.promptOpenAI();
        return testmap;
    }


}