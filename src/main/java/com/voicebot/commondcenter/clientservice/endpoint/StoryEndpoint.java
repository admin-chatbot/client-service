package com.voicebot.commondcenter.clientservice.endpoint;

import com.voicebot.commondcenter.clientservice.dto.ResponseBody;
import com.voicebot.commondcenter.clientservice.entity.Step;
import com.voicebot.commondcenter.clientservice.entity.Story;
import com.voicebot.commondcenter.clientservice.service.AuthenticationService;
import com.voicebot.commondcenter.clientservice.service.StepService;
import com.voicebot.commondcenter.clientservice.service.StoryService;
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
@RequestMapping(path = "/api/v1/story/")
@Tag(name = "Story", description = "Story management APIs")
public class StoryEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoryEndpoint.class);

    @Autowired
    private StoryService storyService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Story.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity<?> onBoard(
            @RequestAttribute(name = "id") Long id,
            @RequestAttribute(name = "type") String type,
            @RequestBody @Valid Story story) {
        try {
            Story story1 = storyService.onBoard(story);

            return ResponseBuilder.ok("Story successfully onboard.", story);
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
                List<Story> stories = storyService.findStoriesByClientId(clientId);
                return ResponseEntity.ok(ResponseBody.builder()
                        .message(stories != null ? String.valueOf(stories.size()) : 0 + " step found.")
                        .code(HttpStatus.OK.value())
                        .data(stories)
                        .build());
            }catch (Exception exception){
                LOGGER.error("",exception);
                return ResponseEntity
                        .internalServerError()
                        .body(exception.getMessage());
            }
        }
    }
