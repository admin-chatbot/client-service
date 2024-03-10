package com.voicebot.commondcenter.clientservice.endpoint;


import com.voicebot.commondcenter.clientservice.dto.ResponseBody;
import com.voicebot.commondcenter.clientservice.entity.Service;
import com.voicebot.commondcenter.clientservice.entity.ServiceIntend;
import com.voicebot.commondcenter.clientservice.service.ServiceIntendService;
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
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*" )
@RequestMapping(path = "/api/v1/intend/")
@Tag(name = "Service Intend", description = "Service Intend APIs")
public class ServiceIntendEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceIntendEndpoint.class);
    @Autowired
    private ServiceIntendService  serviceIntendService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    public ResponseEntity<?> save(@NotNull @Valid @RequestBody ServiceIntend serviceIntend) {
        try {
            LOGGER.info("START SAVE ServiceIntend {}",serviceIntend);

            Optional<ServiceIntend> optionalServiceIntend =  serviceIntendService.findByIntend(serviceIntend.getIntend());
            ServiceIntend intend = null;
            if(optionalServiceIntend.isPresent()){
                intend = optionalServiceIntend.get();
                intend.setQuestions(serviceIntend.getQuestions());
            } else {
                intend = serviceIntend;
            }
            serviceIntend =  serviceIntendService.save(intend);
            return ResponseBuilder.ok("Service Intend is successfully on boarded.",serviceIntend);

        } catch (Exception exception) {
            LOGGER.error("", exception);
            return ResponseBuilder.build500(exception) ;
        }

    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    public ResponseEntity<?> getIntends() {
        try{
            List<ServiceIntend> serviceIntends = serviceIntendService.findAll();
            return ResponseBuilder.ok((serviceIntends!=null?serviceIntends.size():0)+ " intend found", serviceIntends);
        }catch (Exception exception) {
            LOGGER.error("", exception);
            return ResponseBuilder.build500(exception) ;
        }
    }

    @GetMapping(path = "byServiceId/{id}",consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    public ResponseEntity<?> getIntendsByServiceId(@PathVariable(name = "id") Long id) {
        try{
            List<ServiceIntend> serviceIntends = serviceIntendService.findByServiceId(id);
            return ResponseBuilder.ok((serviceIntends!=null?serviceIntends.size():0)+ " intend found", serviceIntends);
        }catch (Exception exception) {
            LOGGER.error("", exception);
            return ResponseBuilder.build500(exception) ;
        }
    }

    @GetMapping(path = "byIntend/{intend}",consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    public ResponseEntity<?> getIntendsByIntend(@PathVariable(name = "intend") String intend) {
        try{
            Optional<ServiceIntend> serviceIntends = serviceIntendService.findByIntend(intend);
            return ResponseBuilder.ok("", serviceIntends);
        }catch (Exception exception) {
            LOGGER.error("", exception);
            return ResponseBuilder.build500(exception) ;
        }
    }
}
