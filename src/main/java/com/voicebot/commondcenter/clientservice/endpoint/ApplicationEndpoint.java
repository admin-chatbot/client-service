package com.voicebot.commondcenter.clientservice.endpoint;

import com.voicebot.commondcenter.clientservice.dto.ApplicationSearchRequest;
import com.voicebot.commondcenter.clientservice.dto.ResponseBody;
import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.Client;
import com.voicebot.commondcenter.clientservice.entity.Service;
import com.voicebot.commondcenter.clientservice.enums.Status;
import com.voicebot.commondcenter.clientservice.service.ApplicationService;
import com.voicebot.commondcenter.clientservice.service.ClientService;
import com.voicebot.commondcenter.clientservice.service.ServiceParameterService;
import com.voicebot.commondcenter.clientservice.service.impl.ApplicationServiceImpl;
import com.voicebot.commondcenter.clientservice.service.impl.ServiceServiceImpl;
import com.voicebot.commondcenter.clientservice.utils.ResponseBuilder;
import io.swagger.v3.core.util.OpenAPISchema2JsonSchema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.OpenAPI;
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
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/application/")
@Tag(name = "Application", description = "Application management APIs")
public class ApplicationEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationEndpoint.class);

    @Autowired
    private ApplicationServiceImpl applicationService;

    @Autowired
    private ServiceServiceImpl serviceService;
    @Autowired
    private ClientService clientService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Application.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    public ResponseEntity<?> onBoard(@RequestBody @Valid Application application){
        try {


            Optional<Client> client = clientService.findOne(application.getClintId());

            if(client.isEmpty()) {
                return ResponseBuilder.build400("Invalid Client.");            }

            Optional<Application> alreadyPresent = applicationService.findApplicationByClientAndName(application.getClintId(),application.getName());
            if(alreadyPresent.isPresent()) {
                return ResponseBuilder.build400("Application is already onboarded.");
            }



            Application application1 = applicationService.onBoard(application);

            return ResponseBuilder.ok("Application successfully onboard.",application1);

        }catch (Exception exception) {
            return ResponseBuilder.build500(exception);
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Application.class),
                    mediaType = "application/json") }
            ),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    public ResponseEntity<?> edit(@RequestBody @Valid Application application){

        LOGGER.info("edit, Application : {}",application);
        try {

            if(application == null) {
                return ResponseBuilder.build400("Application is null.");
            }

            if(application.getId() == null ){
                return ResponseBuilder.build400("Application Id should not null.");
            }

            Optional<Application> applicationFromDB = applicationService.findOne(application.getId());

            if(applicationFromDB.isEmpty()) {
                return ResponseBuilder.build400("Application Id is not valid.");
            }
            Optional<Client> client = clientService.findOne(application.getClintId());

            if (client.isEmpty()) {
                return ResponseBuilder.build400("Invalid client.");
            }

            Application application1 = applicationService.edit(application);

            return ResponseBuilder.ok("Application successfully updated.",application1);

        }catch (Exception exception) {
            LOGGER.error(exception.getMessage(),exception);
            return ResponseBuilder.build500(exception);
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
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.TEXT_PLAIN_VALUE) }) ,
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.TEXT_PLAIN_VALUE) })
    })
    public ResponseEntity<?> getAllApplication() {
        try {
            List<Application> applications = applicationService.find();
            return ResponseEntity.ok(ResponseBody.builder()
                    .message( (applications!=null? String.valueOf(applications.size()) : String.valueOf(0)) )
                    .code(HttpStatus.OK.value())
                    .data(applications)
                    .build());
        }catch (Exception exception) {
            return ResponseBuilder.build500(exception);
        }
    }

    @GetMapping("/{id}/")
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
    public ResponseEntity<?> getAllApplicationByClientId(@PathVariable(name = "id") Long clientId) {
        try {
            List<Application> applications = applicationService.findByClint(clientId);

            return ResponseEntity.ok(ResponseBody.builder()
                    .message(applications!=null? String.valueOf(applications.size()) :0+" application found.")
                    .code(HttpStatus.OK.value())
                    .data(applications)
                    .build());
        }catch (Exception exception) {
            return ResponseBuilder.build500(exception);
        }
    }

    @GetMapping("/byClient/{id}/status/{status}")
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
    public ResponseEntity<?> getAllApplicationByClientIdAndStatus(@PathVariable(name = "id") Long clientId,
                                                                  @PathVariable(name = "status") Status status) {
        try {

            Application application = Application.builder().clintId(clientId).status(status).build();
            Example<Application> applicationExample = Example.of(application);

            List<Application> applications = applicationService.findByExample(applicationExample);
           applications.forEach(application1 -> {
                List<Service> services = serviceService.findAllByApplicationId(application1.getId());
                application1.setServiceCount(services.size());
            });
            return ResponseEntity.ok(ResponseBody.builder()
                    .message(applications!=null? String.valueOf(applications.size()) :0+" application found.")
                    .code(HttpStatus.OK.value())
                    .data(applications)
                    .build());
        }catch (Exception exception) {
            return ResponseBuilder.build500(exception);
        }
    }

    @GetMapping( path = "page/" )
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
    public ResponseEntity<?> getAllApplication(Pageable pageable) {
        try {
            Page<Application> applications = applicationService.find(pageable);
            return ResponseEntity.ok(ResponseBody.builder()
                    .message("")
                    .code(HttpStatus.OK.value())
                    .data(applications)
                    .build());
        }catch (Exception exception) {
            return ResponseBuilder.build500(exception);
        }
    }


    @GetMapping( path = "search/{keyword}/" )
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
    public ResponseEntity<?> search(Pageable pageable) {
        try {
            Application application = Application.builder().build();
            FieldUtils.writeDeclaredField(application,"name","jitendra",true);
            FieldUtils.writeDeclaredField(application,"clintId","1",true);
            System.out.println(application);
            return ResponseBuilder.ok("",null);
        }catch (Exception exception) {
            return ResponseBuilder.build500(exception);
        }
    }

    @PostMapping( path = "search/" )
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Pageable.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.TEXT_PLAIN_VALUE) }) ,
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = ResponseEntity.class),mediaType = MediaType.TEXT_PLAIN_VALUE) })
    })
    public ResponseEntity<?> search(@RequestBody ApplicationSearchRequest searchRequest ) {
        try {
            List<Application> applications =  applicationService.search(searchRequest);
            return ResponseBuilder.ok("",applications);
        }catch (Exception exception) {
            exception.printStackTrace();
            return ResponseBuilder.build500(exception);
        }
    }


}
