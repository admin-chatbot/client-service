package com.voicebot.commondcenter.clientservice.endpoint;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/devtool/")
@Tag(name = "DevTool", description = "DevTool")
public class ApplicationDevToolEndpoint {

    @GetMapping(path = "health")
    public ResponseEntity<?> getAllApplication( ) {
         return ResponseEntity.ok("UP");
    }
}
