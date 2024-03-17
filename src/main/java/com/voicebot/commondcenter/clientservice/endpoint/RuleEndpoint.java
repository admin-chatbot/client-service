package com.voicebot.commondcenter.clientservice.endpoint;

import com.voicebot.commondcenter.clientservice.dto.ApplicationIDName;
import com.voicebot.commondcenter.clientservice.dto.ApplicationSearchRequest;
import com.voicebot.commondcenter.clientservice.dto.ResponseBody;
import com.voicebot.commondcenter.clientservice.entity.*;
import com.voicebot.commondcenter.clientservice.enums.Status;
import com.voicebot.commondcenter.clientservice.service.AuthenticationService;
import com.voicebot.commondcenter.clientservice.service.ClientService;
import com.voicebot.commondcenter.clientservice.service.RuleService;
import com.voicebot.commondcenter.clientservice.service.impl.ApplicationServiceImpl;
import com.voicebot.commondcenter.clientservice.service.impl.RuleServiceImpl;
import com.voicebot.commondcenter.clientservice.service.impl.ServiceServiceImpl;
import com.voicebot.commondcenter.clientservice.utils.ResponseBuilder;
import com.voicebot.commondcenter.clientservice.utils.UserTypeUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*" )
@RequestMapping(path = "/api/v1/rule/")
@Tag(name = "Rule", description = "Rule management APIs")
public class RuleEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleEndpoint.class);

    @Autowired
    private RuleService ruleService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Rule.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity<?> onBoard(
            @RequestAttribute(name = "id") Long id,
            @RequestAttribute(name = "type") String type,
            @RequestBody @Valid Rule rule) {
        try {
            Optional<Rule> alreadyPresent = ruleService.findARuleByClientIdAndRuleName(rule.getClientId(), rule.getRulename());
            if (alreadyPresent.isPresent()) {
                return ResponseBuilder.build400("Rule is already onboarded.");
            }
            Rule rule1 = ruleService.onBoard(rule);

            return ResponseBuilder.ok("Rule successfully onboard.", rule);
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
                @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Rule.class), mediaType = "application/json")}),
                @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json")}),
                @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
                @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json")})
        })
        public ResponseEntity<?> getRulesByClientId (@PathVariable(name = "clientId") Long clientId){
            try {
                LOGGER.info("getRuleByClientId");
                List<Rule> rules = ruleService.findRulesByClientId(clientId);
                return ResponseEntity.ok(ResponseBody.builder()
                        .message(rules != null ? String.valueOf(rules.size()) : 0 + " user found.")
                        .code(HttpStatus.OK.value())
                        .data(rules)
                        .build());
            }catch (Exception exception){
                LOGGER.error("",exception);
                return ResponseEntity
                        .internalServerError()
                        .body(exception.getMessage());
            }
        }
    }
