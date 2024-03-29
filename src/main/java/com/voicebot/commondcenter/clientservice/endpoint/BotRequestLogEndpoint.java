package com.voicebot.commondcenter.clientservice.endpoint;

import com.voicebot.commondcenter.clientservice.dto.ApplicationSearchRequest;
import com.voicebot.commondcenter.clientservice.dto.ResponseBody;
import com.voicebot.commondcenter.clientservice.entity.*;
import com.voicebot.commondcenter.clientservice.enums.Status;
import com.voicebot.commondcenter.clientservice.service.ClientService;
import com.voicebot.commondcenter.clientservice.service.impl.ApplicationServiceImpl;
import com.voicebot.commondcenter.clientservice.service.impl.BotRequestLogServiceImpl;
import com.voicebot.commondcenter.clientservice.service.impl.ServiceServiceImpl;
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
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.voicebot.commondcenter.clientservice.utils.ResponseBuilder.build500;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*" )
@RequestMapping(path = "/api/v1/botrequestlog/")
@Tag(name = "BotRequestLog", description = "Bot Request Log APIs")
public class BotRequestLogEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(BotRequestLogEndpoint.class);

    @Autowired
    private BotRequestLogServiceImpl botRequestLogService;

    @GetMapping("/{userId}/")
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
    public ResponseEntity<?> getAllBotRequestLogsByUser(@PathVariable(name = "userId") String userId) {
        try {

            List<BotRequestLog> botRequestLogs = botRequestLogService.findLatestDocumentsForUser(userId);
            return ResponseEntity.ok(ResponseBody.builder()
                    .message(botRequestLogs!=null? String.valueOf(botRequestLogs.size()) :0+" botRequests found.")
                    .code(HttpStatus.OK.value())
                    .data(botRequestLogs)
                    .build());
        }catch (Exception exception) {
            return ResponseBuilder.build500(exception);
        }
    }

    @GetMapping("/request/{requestId}")
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
    public ResponseEntity<?> getAllBotRequestLogsByUserAndRequest(@PathVariable(name = "requestId") String requestId) {
        try {
            List<BotRequestLog> botRequestLogs = botRequestLogService.findBotRequestLogsByRequestId(requestId);
            return ResponseEntity.ok(ResponseBody.builder()
                    .message(botRequestLogs!=null? String.valueOf(botRequestLogs.size()) :0+" botRequests found.")
                    .code(HttpStatus.OK.value())
                    .data(botRequestLogs)
                    .build());
        }catch (Exception exception) {
            return ResponseBuilder.build500(exception);
        }
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ServiceLog.class),mediaType = MediaType.APPLICATION_JSON_VALUE) }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ResponseBody.class),mediaType = MediaType.APPLICATION_JSON_VALUE) }) ,
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = ResponseBody.class),mediaType = MediaType.APPLICATION_JSON_VALUE) })
    })
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    public ResponseEntity<?> save(@RequestBody @Valid BotRequestLog botRequestLog) {

        try {
            LOGGER.info("serviceLog {} ",botRequestLog);
            BotRequestLog c =  botRequestLogService.save(botRequestLog);
            return ResponseEntity.ok(ResponseBody.builder()
                    .message("Logged.")
                    .code(HttpStatus.OK.value())
                    .data(c)
                    .build());
        }catch (Exception exception) {
            LOGGER.error(exception.getMessage(),exception);
            return build500(exception);
        }
    }
}
