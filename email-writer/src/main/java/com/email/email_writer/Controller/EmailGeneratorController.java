package com.email.email_writer.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailGeneratorController
{
    public ResponseEntity<String> generateemail(@RequestBody EmailRequest emailRequest)
    {
        return ResponseEntity.ok("");
    }
}
