package com.voicebot.commondcenter.clientservice.endpoint;

import com.voicebot.commondcenter.clientservice.dto.ResponseBody;
import com.voicebot.commondcenter.clientservice.entity.Step;
import com.voicebot.commondcenter.clientservice.service.AuthenticationService;
import com.voicebot.commondcenter.clientservice.service.StepService;
import com.voicebot.commondcenter.clientservice.utils.ResponseBuilder;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*" )
@RequestMapping(path = "/api/v1/step/")
@Tag(name = "Step", description = "Step management APIs")
public class StepEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(StepEndpoint.class);

    @Autowired
    private StepService stepService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Step.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity<?> onBoard(
            @RequestAttribute(name = "id") Long id,
            @RequestAttribute(name = "type") String type,
            @RequestBody @Valid Step step) {
        try {
            Optional<Step> alreadyPresent = stepService.findAStepByClientIdAndIntent(step.getClientId(), step.getIntent());
            if (alreadyPresent.isPresent()) {
                return ResponseBuilder.build400("Step is already onboarded.");
            }
            Step step1 = stepService.onBoard(step);

            return ResponseBuilder.ok("Step successfully onboard.", step);
        } catch (Exception exception) {
            return ResponseBuilder.build500(exception);
        }
    }
        @GetMapping(path = "byClient/{clientId}/")
        @Operation(parameters = {
                @Parameter(in = ParameterIn.HEADER
                        , name = "X-AUTH-LOG-HEADER"
                        , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
        })
        @ApiResponses({
                @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Step.class), mediaType = "application/json")}),
                @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json")}),
                @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
                @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json")})
        })
        public ResponseEntity<?> getStepsByClientId (@PathVariable(name = "clientId") Long clientId){
            try {
                LOGGER.info("getStepsByClientId");
                List<Step> steps = stepService.findStepByClientId(clientId);
                return ResponseEntity.ok(ResponseBody.builder()
                        .message(steps != null ? String.valueOf(steps.size()) : 0 + " step found.")
                        .code(HttpStatus.OK.value())
                        .data(steps)
                        .build());
            }catch (Exception exception){
                LOGGER.error("",exception);
                return ResponseEntity
                        .internalServerError()
                        .body(exception.getMessage());
            }
        }
    }
