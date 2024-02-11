package com.voicebot.commondcenter.clientservice.endpoint;

import com.voicebot.commondcenter.clientservice.dto.DashboardDto;
import com.voicebot.commondcenter.clientservice.dto.DashboardSearchRequest;
import com.voicebot.commondcenter.clientservice.dto.ResponseBody;
import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.Service;
import com.voicebot.commondcenter.clientservice.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.voicebot.commondcenter.clientservice.entity.ServiceLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/dashboard/" )
@CrossOrigin
@Tag(name = "Dashboard", description = "Dashboard Management APIs")
public class DashboardEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardEndpoint.class);

    @Autowired
    private DashboardService dashboardService;

    @PostMapping( path = "search" )
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

    public ResponseEntity<?> getDashboardByClientIdAndStatusAndTimeframe(@RequestBody DashboardSearchRequest dashboardSearchRequest) {
        try {
            DashboardDto dashboardDto = new DashboardDto();
            Map<String, Map<String, Map<String, Map<String, Map<String, Integer>>>>> serviceLogs = dashboardService.getDashboardByClientIdAndStatusAndTimeframe(dashboardSearchRequest);
            dashboardDto.setServiceLogs(serviceLogs);

            System.out.println(dashboardDto.getServiceLogs());

            return ResponseEntity.ok(ResponseBody.builder().data(dashboardDto).message("").build());
        }catch (Exception exception){
            LOGGER.error("",exception);
            return ResponseEntity
                    .internalServerError()
                    .body(exception.getMessage());
        }
    }
}
