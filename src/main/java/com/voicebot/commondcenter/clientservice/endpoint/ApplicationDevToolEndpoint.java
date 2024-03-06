package com.voicebot.commondcenter.clientservice.endpoint;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/devtool/")
@Tag(name = "DevTool", description = "DevTool")
public class ApplicationDevToolEndpoint {

    @GetMapping(path = "health")
    public ResponseEntity<?> getAllApplication( ) {
         return ResponseEntity.ok("UP");
    }

    @GetMapping(path = "random")
    public ResponseEntity<?> random( ) {
        return ResponseEntity.ok(new Random(10).nextInt());
    }
}
