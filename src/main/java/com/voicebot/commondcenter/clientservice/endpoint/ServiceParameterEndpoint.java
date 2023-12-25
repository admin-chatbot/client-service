package com.voicebot.commondcenter.clientservice.endpoint;

import com.voicebot.commondcenter.clientservice.dto.ResponseBody;
import com.voicebot.commondcenter.clientservice.entity.Service;
import com.voicebot.commondcenter.clientservice.entity.ServiceParameter;
import com.voicebot.commondcenter.clientservice.service.ServiceParameterService;
import com.voicebot.commondcenter.clientservice.service.ServiceService;
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
@RequestMapping(path = "/api/v1/service/parameter/" )
@Tag(name = "ServiceParameter", description = "Service Parameter Management APIs")
@CrossOrigin
public class ServiceParameterEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceParameterEndpoint.class);

    @Autowired
    private ServiceParameterService serviceParameterService;

    @Autowired
    private ServiceService serviceService;


    @GetMapping(path="{serviceId}/")
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Service.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") })
    })
    public ResponseEntity<?> getAllServiceByClientId(@PathVariable(name="serviceId") Long serviceId) {
        try {
            List<ServiceParameter> services = serviceParameterService.findByServiceId(serviceId);

            LOGGER.info("Result found. {}", services.size());

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("ServiceParameter List {}",services);
            return ResponseEntity.ok(services);
        }catch (Exception exception){
            LOGGER.error("",exception);
            return ResponseEntity
                    .internalServerError()
                    .body(exception.getMessage());
        }
    }


    @GetMapping(path="")
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Service.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") })
    })
    public ResponseEntity<?> getAll() {
        try {
            List<ServiceParameter> services = serviceParameterService.find();

            LOGGER.info("Result found. {}", services.size());

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("ServiceParameter List {}",services);
            return ResponseEntity.ok(services);
        }catch (Exception exception){
            LOGGER.error("",exception);
            return ResponseEntity
                    .internalServerError()
                    .body(exception.getMessage());
        }
    }



    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Service.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") })
    })
    public ResponseEntity<?> save(@RequestBody @Valid ServiceParameter serviceParameter) {
        try {
            LOGGER.info("serviceParameter {} ",serviceParameter);

            Optional<Service> service = serviceService.fetchOne(serviceParameter.getServiceId());

            if(service.isEmpty()) {
                return ResponseEntity.internalServerError().body( ResponseBody.builder()
                        .message("Invalid Service.")
                        .code(HttpStatus.BAD_REQUEST.value())
                        .build());
            }

            ServiceParameter c =  serviceParameterService.save(serviceParameter);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Inserted successfully. ServiceParameter {}",c);

            return ResponseEntity.ok(c);
        }catch (Exception exception) {
            LOGGER.error(exception.getMessage(),exception);
            return ResponseEntity.internalServerError().body( ResponseBody.builder()
                    .message(exception.getMessage())
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .exception(exception)
                    .build()  );
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Service.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") })
    })
    public ResponseEntity<?> edit(@RequestBody @Valid ServiceParameter serviceParameter) {
        try {
            LOGGER.info("serviceParameter {} ",serviceParameter);

            if(serviceParameter == null) {
                return ResponseEntity.internalServerError().body( ResponseBody.builder()
                        .message("Server Parameter is null.")
                        .code(HttpStatus.BAD_REQUEST.value())
                        .build());
            }

            if(serviceParameter.getId() == null) {
                return ResponseEntity.internalServerError().body( ResponseBody.builder()
                        .message("Server Parameter id is null.")
                        .code(HttpStatus.PARTIAL_CONTENT.value())
                        .build());
            }

            Optional<Service> service = serviceService.fetchOne(serviceParameter.getServiceId());

            if(service.isEmpty()) {
                return ResponseEntity.internalServerError().body( ResponseBody.builder()
                        .message("Invalid Service.")
                        .code(HttpStatus.BAD_REQUEST.value())
                        .build());
            }


            ServiceParameter c =  serviceParameterService.save(serviceParameter);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Inserted successfully. ServiceParameter {}",c);

            return ResponseEntity.ok(c);
        }catch (Exception exception) {
            LOGGER.error(exception.getMessage(),exception);
            return ResponseEntity.internalServerError().body( ResponseBody.builder()
                    .message(exception.getMessage())
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .exception(exception)
                    .build()  );
        }
    }
}
