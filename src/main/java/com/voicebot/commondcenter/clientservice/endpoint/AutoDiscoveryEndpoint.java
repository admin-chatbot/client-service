package com.voicebot.commondcenter.clientservice.endpoint;

import com.voicebot.commondcenter.clientservice.dto.AutoDiscoverServiceRequest;
import com.voicebot.commondcenter.clientservice.dto.AutoDiscoverServiceRequestBody;
import com.voicebot.commondcenter.clientservice.dto.ResponseBody;
import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.Service;
import com.voicebot.commondcenter.clientservice.entity.ServiceParameter;
import com.voicebot.commondcenter.clientservice.repository.ServiceParameterRepository;
import com.voicebot.commondcenter.clientservice.service.AutoDiscoveryService;
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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/service/")
@CrossOrigin
@Tag(name = "AutoDiscovery", description = "Auto Discovery Service APIs")
public class  AutoDiscoveryEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoDiscoveryEndpoint.class);

    @Autowired
    private AutoDiscoveryService autoDiscoveryService;

    @Autowired
    private ServiceService serviceService;
    @Autowired
    private ServiceParameterService serviceParameterService;


    @PostMapping(path = "discover/")
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Application.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    public ResponseEntity<?> discover(@RequestParam(name = "url") String url) {
        try{
            List<Service> services = autoDiscoveryService.discover(new URL(url));
            return ResponseEntity.ok(services);
        }catch (Exception exception) {
            LOGGER.error(exception.getMessage(),exception);
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }


    @PostMapping(path = "load/")
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") })
    })
    public ResponseEntity<?> loadService(@RequestBody @Valid  AutoDiscoverServiceRequest autoDiscoverServiceRequest) {
        ResponseBody responseBody = null;
        try {

            if(autoDiscoverServiceRequest==null) {
                responseBody =  ResponseBody.builder().code(HttpStatus.BAD_REQUEST.value()).message("Invalid request.Please check again").build();
                return ResponseEntity.badRequest().body(responseBody);
            }

            List<Service> finalServices = new ArrayList<>();

            List<Service> services = autoDiscoveryService.discover(new URL(autoDiscoverServiceRequest.getUrl()));
            /*for (Service service: services) {
                if(StringUtils.equalsIgnoreCase())
            }*/

            finalServices = services.stream().filter(s -> autoDiscoverServiceRequest
                    .getAutoDiscoverServiceRequestBodies().stream().anyMatch(r ->
                            StringUtils.equalsIgnoreCase(s.getEndpoint(), r.getEndpoint())
                                    && StringUtils.equalsIgnoreCase(s.getMethod(), r.getMethod())
                                    && StringUtils.equalsIgnoreCase(s.getName(), r.getName())
                    )).toList();

            for (Service service: finalServices) {
                ArrayList<ServiceParameter> serviceParameter = service.getServiceParameters();
                service.setClientId(autoDiscoverServiceRequest.getServiceId());
                service.setApplicationId(autoDiscoverServiceRequest.getApplicationId());
                try {
                    Service result = serviceService.save(service);

                    ArrayList<ServiceParameter> parameters = serviceParameter.stream().map(sp -> {
                        sp.setServiceId(result.getId());
                        return sp;
                    }).collect(Collectors.toCollection(ArrayList::new));

                     serviceParameterService.save(parameters);
                }catch (DuplicateKeyException duplicateKeyException) {
                    LOGGER.warn("Service is already registered with us. Service  : {}",service);
                }

            }

            return ResponseEntity.ok(ResponseBody.builder().code(HttpStatus.OK.value()).message("Services is successfully onboarded.").build());
        }catch (Exception exception) {
            LOGGER.error(exception.getMessage(),exception);
            assert responseBody != null;
            return ResponseEntity.internalServerError().body( ResponseBody.builder()
                        .message(exception.getMessage())
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .exception(exception)
                    .build()  );
        }
    }
}
