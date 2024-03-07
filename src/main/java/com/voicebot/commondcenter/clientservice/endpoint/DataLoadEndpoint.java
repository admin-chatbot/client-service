package com.voicebot.commondcenter.clientservice.endpoint;


import com.voicebot.commondcenter.clientservice.batch.service.ServiceTestDataTask;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*" )
@RequestMapping(path = "/api/v1/data/load/")
@Tag(name = "Test Load", description = "Data Load APIs")
public class DataLoadEndpoint {


    @Autowired
    private ServiceTestDataTask serviceTestDataTask;

    @GetMapping(path="service/test/")
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    public ResponseEntity<?> getLoadServiceTestData() {
        try{
            serviceTestDataTask.task();
            return ResponseEntity.ok("Triggred.");
        }catch (Exception exception) {
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }
}
