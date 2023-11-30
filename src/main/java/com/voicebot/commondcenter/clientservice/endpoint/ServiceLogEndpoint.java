package com.voicebot.commondcenter.clientservice.endpoint;


import com.voicebot.commondcenter.clientservice.dto.ResponseBody;
import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.ServiceLog;
import com.voicebot.commondcenter.clientservice.service.ServiceLogService;
import static com.voicebot.commondcenter.clientservice.utils.ResponseBuilder.*;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/service/log/" )
@Tag(name = "Service Log", description = "Service Log Management APIs")
public class ServiceLogEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceLogEndpoint.class);
    @Autowired
    private ServiceLogService serviceLogService;

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
    public ResponseEntity<?> save(@RequestBody @Valid ServiceLog serviceLog) {

        try {
            LOGGER.info("serviceLog {} ",serviceLog);
            ServiceLog c =  serviceLogService.save(serviceLog);
            return ResponseEntity.ok(c);
        }catch (Exception exception) {
            LOGGER.error(exception.getMessage(),exception);
            return build500(exception);
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
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResponseBody.class),mediaType = MediaType.APPLICATION_JSON_VALUE) }) ,
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = ResponseBody.class),mediaType = MediaType.APPLICATION_JSON_VALUE) })
    })
    public ResponseEntity<?> get() {
        try {
            return ResponseEntity.ok(serviceLogService.get());
        }catch (Exception exception) {
            LOGGER.error(exception.getMessage(),exception);
            return build500(exception);
        }
    }

    @GetMapping(path = "page/")
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Application.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResponseBody.class),mediaType = MediaType.APPLICATION_JSON_VALUE) }) ,
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = ResponseBody.class),mediaType = MediaType.APPLICATION_JSON_VALUE) })
    })
    public ResponseEntity<?> get(Pageable pageable) {
        try {
            return ResponseEntity.ok(serviceLogService.get(pageable));
        }catch (Exception exception) {
            LOGGER.error(exception.getMessage(),exception);
            return build500(exception);
        }
    }


}
