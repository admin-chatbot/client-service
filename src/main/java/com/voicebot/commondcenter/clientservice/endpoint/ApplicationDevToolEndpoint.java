package com.voicebot.commondcenter.clientservice.endpoint;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*" )
@RequestMapping(path = "/api/v1/devtool/")
@Tag(name = "DevTool", description = "DevTool")
public class ApplicationDevToolEndpoint {

    private Random random
            = new Random(4);

    @GetMapping(path = "health")
    public ResponseEntity<?> getAllApplication( ) {
         return ResponseEntity.ok("UP");
    }

    @GetMapping(path = "random")
    public ResponseEntity<?> random( ) {
        return ResponseEntity.ok(random.nextInt());
    }
}
