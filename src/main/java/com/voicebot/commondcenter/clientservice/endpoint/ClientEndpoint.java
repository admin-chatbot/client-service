package com.voicebot.commondcenter.clientservice.endpoint;

import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.Client;
import com.voicebot.commondcenter.clientservice.enums.Status;
import com.voicebot.commondcenter.clientservice.service.ClientService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/client/" )
@Tag(name = "Client", description = "Client Management APIs")
@CrossOrigin
public class ClientEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientEndpoint.class);

    @Autowired
    private ClientService clientService;


    @GetMapping
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Client.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    public ResponseEntity<?> getAllClient() {
        try {
            List<Client> clients = clientService.findAll();
            return ResponseEntity.ok(clients);
        }catch (Exception exception){
            LOGGER.error("",exception);
            return ResponseEntity
                    .internalServerError()
                    .body(exception.getMessage());
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Client.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    public ResponseEntity<?> save(@RequestBody @Valid Client client) {

        try {
            LOGGER.info("Client {} ",client);
            client.setStatus(Status.NEW);
            Client c =  clientService.register(client);
            return ResponseEntity.ok(c);
        }catch (Exception exception) {
            LOGGER.error("",exception);
            return ResponseEntity
                    .internalServerError()
                    .body(exception.getMessage());
        }
    }
}
