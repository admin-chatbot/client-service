package com.voicebot.commondcenter.clientservice.endpoint;

import com.voicebot.commondcenter.clientservice.dto.ApplicationIDName;
import com.voicebot.commondcenter.clientservice.dto.ApplicationSearchRequest;
import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.Client;
import com.voicebot.commondcenter.clientservice.entity.Service;
import com.voicebot.commondcenter.clientservice.enums.Status;
import com.voicebot.commondcenter.clientservice.service.ClientService;
import com.voicebot.commondcenter.clientservice.service.impl.ApplicationServiceImpl;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*" )
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
    public ResponseEntity<?> onBoard(
            @RequestAttribute(name = "id") Long id,
            @RequestAttribute(name = "type") String type,
            @RequestBody @Valid Application application ){
        try {

            if(UserTypeUtils.isClientAdmin(type)) {
                application.setClintId(id);
            }

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


    @PutMapping(path = "deleteById/{appId}" ,consumes = MediaType.APPLICATION_JSON_VALUE)
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
    public ResponseEntity<?> deleteApplication(@PathVariable(name = "appId") Long id,
                                  @RequestAttribute(name = "id") Long clientId,
                                  @RequestAttribute(name = "type") String type){

        try {
            Optional<Application> application;
            if (UserTypeUtils.isSuperAdmin(type)) {
                application = applicationService.findOne(id);
            } else {
                application = applicationService.findByClientAndId(clientId, id);
            }

            if(application.isPresent()) {
                application.get().setStatus(Status.INACTIVE);
                applicationService.repository().save(application.get());
            }

            return ResponseBuilder.ok("Application found.", application);
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
    public ResponseEntity<?> edit(@RequestBody @Valid Application application,
                                  @RequestAttribute(name = "id") Long id,
                                  @RequestAttribute(name = "type") String type){

        LOGGER.info("edit, Application : {}",application);
        try {

            if(application == null) {
                return ResponseBuilder.build400("Application is null.");
            }

            if(application.getId() == null ){
                return ResponseBuilder.build400("Application Id should not null.");
            }

            if(UserTypeUtils.isClientAdmin(type)) {
                application.setClintId(id);
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

    @GetMapping(path = "getName")
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    public ResponseEntity<?> getApplicationNames(@RequestParam(name = "ids") String ids,@RequestAttribute(name = "id") Long id) {
        try{
            List<Application> applications ;
            LOGGER.info("ids : {}",ids);
            if(!StringUtils.isBlank(ids)) {
                String[] applicationIds =  StringUtils.split(ids,",");
                Long[] a = new Long[applicationIds.length];
                for(int i = 0; i < applicationIds.length; i++) {
                    try {
                        a[i] = Long.valueOf(applicationIds[i]);
                    } catch (NumberFormatException ignored){
                        LOGGER.error("Invalid application id : {}", applicationIds[i]);
                    }
                }
                applications = applicationService.repository().findAllById(Arrays.asList(a));
            } else {
                applications = applicationService.findByClint(id);
            }

           List<ApplicationIDName> applicationIdNames = new ArrayList<>();
           for(Application application : applications) {
               applicationIdNames.add(ApplicationIDName.builder().id(application.getId()).Name(application.getName()).build());
           }
           return ResponseBuilder.ok("Application found.", applicationIdNames);

        }catch (Exception exception) {
             LOGGER.error(exception.getMessage(), exception);
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
    public ResponseEntity<?> getAllApplication(@RequestAttribute(name = "id") Long id,
                                               @RequestAttribute(name = "type") String type) {
        try {
            List<Application> applications = new ArrayList<>();
            if(UserTypeUtils.isSuperAdmin(type)) {
                  applications = applicationService.find();
            }else if(UserTypeUtils.isClientAdmin(type)) {
                applications = applicationService.findByClint(id);
            } else {
                return ResponseBuilder.ok("Invalid request",applications);
            }
            return ResponseBuilder.ok((applications != null ? String.valueOf(applications.size()) : String.valueOf(0)),applications);
        }catch (Exception exception) {
            return ResponseBuilder.build500(exception);
        }
    }

    @GetMapping("byClient/{id}/")
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
        return getAppByClient(clientId);
    }

    @GetMapping("byId/{appid}/")
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
    public ResponseEntity<?> getApplicationById(@PathVariable(name = "appid") Long appId,
                                                @RequestAttribute(name = "id") Long id,
                                                @RequestAttribute(name = "type") String type) {
        try {
           Optional<Application> application;
            if (UserTypeUtils.isSuperAdmin(type)) {
                application = applicationService.findOne(appId);
            } else {
                application = applicationService.findByClientAndId(id, appId);
            }
            return ResponseBuilder.ok("Application found.", application);
        }catch (Exception exception) {
            return ResponseBuilder.build500(exception);
        }
    }

    @GetMapping("byClient/")
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
    public ResponseEntity<?> getAllApplicationByClientId_client_user(@RequestAttribute(name = "id") Long clientId) {
        return getAppByClient(clientId);
    }

    private ResponseEntity<?> getAppByClient(Long clientId) {
        try {
            List<Application> applications = applicationService.findByClint(clientId);
            return ResponseBuilder.ok(applications != null ? String.valueOf(applications.size()) : 0 + " application found.", applications);
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
        return getAppByClientAndStatus(clientId, status);
    }

    @GetMapping("status/{status}")
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
    public ResponseEntity<?> getAllApplicationByClientIdAndStatus_client_admin(@RequestAttribute(name = "id") Long clientId,
                                                                  @PathVariable(name = "status") Status status) {
        return getAppByClientAndStatus(clientId, status);
    }

    private ResponseEntity<?> getAppByClientAndStatus(Long clientId, Status status) {
        try {

            Application application = Application.builder().clintId(clientId).status(status).build();
            Example<Application> applicationExample = Example.of(application);
            List<Application> applications = applicationService.findByExample(applicationExample);
            applications.forEach(application1 -> {
                List<Service> services = serviceService.findAllByApplicationId(application1.getId());
                application1.setServiceCount(services.size());
             });
            return ResponseBuilder.ok((applications != null ? String.valueOf(applications.size()) : 0) + " application found.",applications);

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
    public ResponseEntity<?> getAllApplication( @RequestAttribute(name = "id") Long id,
                                                @RequestAttribute(name = "type") String type,
                                                Pageable pageable) {
        try {
            Page<Application> applications = new PageImpl<>(new ArrayList<>());

            if(UserTypeUtils.isSuperAdmin(type)) {
                applications = applicationService.find(pageable);
            }else if (UserTypeUtils.isClientAdmin(type)){
                applications = applicationService.findByClient(pageable,id);
            }
            return ResponseBuilder.ok("",applications);
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
    public ResponseEntity<?> search(@RequestAttribute(name = "id") Long id,
                                    @RequestAttribute(name = "type") String type,
                                    @RequestBody ApplicationSearchRequest searchRequest ) {
        try {

            if (searchRequest==null){
                return ResponseBuilder.invalidRequest("Search Critic should not empty.");
            }

            if (UserTypeUtils.isClientAdmin(type)){
                searchRequest.setClientId(id);
            }
            List<Application> applications =  applicationService.search(searchRequest);
            return ResponseBuilder.ok("",applications);
        }catch (Exception exception) {
            LOGGER.error(exception.getMessage(),exception);
            return ResponseBuilder.build500(exception);
        }
    }


}
