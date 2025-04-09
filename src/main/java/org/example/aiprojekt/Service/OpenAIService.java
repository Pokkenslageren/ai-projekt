package org.example.aiprojekt.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.aiprojekt.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    private final WebClient webClient;

    @Autowired
    public OpenAIService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.openai.com/v1/chat/completions") // ✅ NO SLASH
                .build();
    }

    @Value("${spring.ai.openai.api-key}")
    private String openApiKey;

    public Map<String, Object> getMovieAnalysis(String prompt) {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setModel("gpt-4-turbo-preview");
        requestDTO.setTemperature(0.7);
        requestDTO.setMaxTokens(100);
        requestDTO.setPresencePenalty(0);

        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", "You are a knowledgeable film critic and movie expert. Provide detailed, insightful analysis and recommendations."));
        messages.add(new Message("user", prompt));
        requestDTO.setMessages(messages);

        ResponseDTO response = webClient.post()
                .uri("")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(openApiKey))
                .bodyValue(requestDTO)
                .retrieve()
                .bodyToMono(ResponseDTO.class)
                .block();

        Map<String, Object> result = new HashMap<>();
        if (response != null && !response.getChoices().isEmpty()) {
            result.put("analysis", response.getChoices().get(0).getMessage().getContent());
            result.put("usage", response.getUsage());
        } else {
            throw new RuntimeException("Failed to get response from OpenAI");
        }

        return result;
    }

    public Map<String, Object> promptOpenAI(String userPrompt) {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setModel("gpt-4-turbo");
        requestDTO.setTemperature(1.0);
        requestDTO.setMaxTokens(200);

        List<Message> lstMessages = new ArrayList<>();
        lstMessages.add(new Message("system", "You are a helpful and knowledgeable film critic and movie expert."));
        lstMessages.add(new Message("user", userPrompt)); // Clean, raw input
        requestDTO.setMessages(lstMessages);

        // 🔍 Log the full JSON request:
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonPayload = mapper.writeValueAsString(requestDTO);
            System.out.println("🧠 Sending to OpenAI:\n" + jsonPayload);
        } catch (Exception e) {
            System.out.println("⚠️ Could not serialize requestDTO: " + e.getMessage());
        }

        ResponseDTO response = webClient.post()
                .uri("") // ✅ use "/" instead of ""
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(openApiKey))
                .bodyValue(requestDTO)
                .retrieve()
                .bodyToMono(ResponseDTO.class)
                .block();

        List<Choice> lst = response.getChoices();
        Usage usg = response.getUsage();

        Map<String, Object> map = new HashMap<>();
        map.put("Usage", usg);
        map.put("Choices", lst);

        return map;
    }
}