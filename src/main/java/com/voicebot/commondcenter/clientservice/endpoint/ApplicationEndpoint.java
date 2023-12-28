package com.voicebot.commondcenter.clientservice.endpoint;

import com.voicebot.commondcenter.clientservice.dto.ResponseBody;
import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.Client;
import com.voicebot.commondcenter.clientservice.service.ApplicationService;
import com.voicebot.commondcenter.clientservice.service.ClientService;
import com.voicebot.commondcenter.clientservice.service.ServiceParameterService;
import io.swagger.v3.core.util.OpenAPISchema2JsonSchema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.OpenAPI;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/application/")
@Tag(name = "Application", description = "Application management APIs")
public class ApplicationEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationEndpoint.class);

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ClientService clientService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Application.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    public ResponseEntity<?> onBoard(@RequestBody @Valid Application application){
        try {
            Optional<Client> client = clientService.findOne(application.getId());

            Application application1 = applicationService.onBoard(application);
            return ResponseEntity.ok(application1);
        }catch (Exception exception) {
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Application.class),
                    mediaType = "application/json") }
            ),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    public ResponseEntity<?> edit(@RequestBody @Valid Application application){

        LOGGER.info("edit, Application : {}",application);
        try {

            if(application == null) {
                return ResponseEntity.badRequest().body( ResponseBody.builder()
                        .message("Application is null.")
                        .code(HttpStatus.BAD_REQUEST.value())
                        .build() );
            }

            if(application.getId() == null ){
                return ResponseEntity.badRequest().body( ResponseBody.builder()
                        .message("Application Id should not null.")
                        .code(HttpStatus.PARTIAL_CONTENT.value())
                        .build() );
            }

            Optional<Application> applicationFromDB = applicationService.findOne(application.getId());

            if(applicationFromDB.isEmpty()) {
                return ResponseEntity.badRequest().body( ResponseBody.builder()
                        .message("Application Id is not valid.")
                        .code(HttpStatus.BAD_REQUEST.value())
                        .build() );
            }
            Optional<Client> client = clientService.findOne(application.getId());

            if (client.isEmpty()) {
                return ResponseEntity.badRequest().body( ResponseBody.builder()
                        .message("Invalid client.")
                        .code(HttpStatus.BAD_REQUEST.value())
                        .build() );
            }

            Application application1 = applicationService.edit(application);
            return ResponseEntity.ok(application1);
        }catch (Exception exception) {
            LOGGER.error(exception.getMessage(),exception);
            return ResponseEntity.internalServerError().body(ResponseBody.builder()
                    .message(exception.getMessage())
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .exception(exception)
                    .build() );
        }
    }

    @GetMapping
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Application.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.TEXT_PLAIN_VALUE) }) ,
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.TEXT_PLAIN_VALUE) })
    })
    public ResponseEntity<?> getAllApplication() {
        try {
            return ResponseEntity.ok(applicationService.find());
        }catch (Exception exception) {
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }

    @GetMapping( path = "page/" )
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Pageable.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.TEXT_PLAIN_VALUE) }) ,
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.TEXT_PLAIN_VALUE) })
    })
    public ResponseEntity<?> getAllApplication(Pageable pageable) {
        try {

            return ResponseEntity.ok(applicationService.find(pageable));
        }catch (Exception exception) {
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }
}
