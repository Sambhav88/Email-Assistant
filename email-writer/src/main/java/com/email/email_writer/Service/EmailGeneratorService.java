package com.email.email_writer.Service;

import com.email.email_writer.Model.EmailRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class EmailGeneratorService
{
    //@Autowired
    private final WebClient webClient;
    public EmailGeneratorService(WebClient.Builder webClientBuilder)
    {
        System.out.println("this is constr witth webbuilder ");
        this.webClient = WebClient.builder().build();
    }

    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    @Value("${gemini.api.key}")
    private String geminiApiKey;
    public String generateEmailReply(EmailRequest emailRequest) throws JsonProcessingException {
        //Build the prompt
        String prompt = buildPrompt(emailRequest);

        //Craft a request
//        Map<String,Object> requestBody=Map.of(
//                k1:"Contents", new Object[] {
//                        Map.of(k1:"parts",new Object[]{
//                            Map.of(k1:"text",prompt)
//                        })
//               }
//        );
        Map<String,Object> textmap=Map.of("text",prompt);
        Map<String,Object> partsmap=Map.of("parts", List.of(textmap));
        Map<String,Object> contentsmap=Map.of("contents",List.of(partsmap));

        //Do request and get response
        String Response = webClient.post()
                .uri((geminiApiUrl+geminiApiKey).trim())
                .header("Content-Type","application/json")
                .bodyValue(contentsmap)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        //return response
                return ExtractResponseContent(Response);
    }

    private String ExtractResponseContent(String response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode myJson = objectMapper.readTree(response) ;

                return myJson.path("candidates")
                             .get(0)
                             .path("content")

                             .path("parts")
                             .get(0)
                             .path("text").asText();
    }

    private String buildPrompt(EmailRequest emailRequest)
    {
         StringBuilder prompt = new StringBuilder();
         prompt.append("Generate a professional email reply for the following email content.Please don't generate the subject line ");
         if(emailRequest.getTone() !=null && !emailRequest.getTone().isEmpty())
         {
             prompt.append("Use a").append(emailRequest.getTone()).append("tone.");
         }
         prompt.append("\nOriginal email:\n").append(emailRequest.getEmailContent());
         return prompt.toString();

    }
}
