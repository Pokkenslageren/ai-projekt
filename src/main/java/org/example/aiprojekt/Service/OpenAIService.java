package org.example.aiprojekt.Service;

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
        this.webClient = webClientBuilder.baseUrl("https://api.mistral.ai/v1/chat/completions").build();
    } // Skal rettes

    @Value("${openai.api.key}")
    private String openapikey;

    public Map<String, Object> promptOpenAI() {

        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setModel("mistral-small-latest");
        requestDTO.setTemperature(1.0);
        requestDTO.setMaxTokens(200);

        List<Message> lstMessages = new ArrayList<>(); //en liste af messages med roller
        lstMessages.add(new Message("system", "You are a helpful assistant."));
        lstMessages.add(new Message("user", "Give a list of 3 good french red wines"));
        requestDTO.setMessages(lstMessages);

        ResponseDTO response = webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(openapikey))
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