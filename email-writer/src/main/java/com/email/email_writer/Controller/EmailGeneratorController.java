package com.email.email_writer.Controller;

import com.email.email_writer.Model.EmailRequest;
import com.email.email_writer.Service.EmailGeneratorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/email")

public class EmailGeneratorController
{
    @Autowired
    EmailGeneratorService service;

    @PostMapping("/generate")
    public ResponseEntity<String> generateemail(@RequestBody EmailRequest emailRequest) throws JsonProcessingException {
        String Response = service.generateEmailReply(emailRequest);
        return ResponseEntity.ok(Response);
    }
}
