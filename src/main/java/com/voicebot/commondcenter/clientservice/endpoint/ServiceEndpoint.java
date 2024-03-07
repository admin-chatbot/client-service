package com.voicebot.commondcenter.clientservice.endpoint;

import com.voicebot.commondcenter.clientservice.dto.ApplicationSearchRequest;
import com.voicebot.commondcenter.clientservice.dto.ResponseBody;



import com.voicebot.commondcenter.clientservice.dto.ServiceSearchRequest;
import com.voicebot.commondcenter.clientservice.entity.Application;


import com.voicebot.commondcenter.clientservice.entity.Client;
import com.voicebot.commondcenter.clientservice.entity.Service;
import com.voicebot.commondcenter.clientservice.entity.ServiceParameter;
import com.voicebot.commondcenter.clientservice.enums.Status;
import com.voicebot.commondcenter.clientservice.enums.UserType;
import com.voicebot.commondcenter.clientservice.service.ClientService;
import com.voicebot.commondcenter.clientservice.service.ServiceService;
import com.voicebot.commondcenter.clientservice.service.ServiceParameterService;
import com.voicebot.commondcenter.clientservice.service.impl.ApplicationServiceImpl;
import com.voicebot.commondcenter.clientservice.service.impl.ServiceServiceImpl;
import com.voicebot.commondcenter.clientservice.service.impl.ServiceParameterServiceImpl;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/service/" )
@Tag(name = "Service", description = "Service Management APIs")
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*", allowCredentials = "true")
public class ServiceEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceEndpoint.class);

    @Autowired
    private ServiceServiceImpl serviceService;
    @Autowired
    private ServiceParameterServiceImpl serviceParameterService;


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
    //public ResponseEntity<?> search(Pageable pageable) {
        public ResponseEntity<?> search(@PathVariable String keyword) {
        try {
            Service service = Service.builder().build();
            FieldUtils.writeDeclaredField(service,"clientId","1",true);
            FieldUtils.writeDeclaredField(service,"name","jitendra",true);
            FieldUtils.writeDeclaredField(service,"endPoint","1",true);
            FieldUtils.writeDeclaredField(service,"method","POST",true);
            FieldUtils.writeDeclaredField(service,"status","ACTIVE",true);
            System.out.println(service);
            return ResponseBuilder.ok("",null);
        }catch (Exception exception) {
            return ResponseBuilder.build500(exception);
        }
    }




    @GetMapping("byClient/{id}/status/{status}")
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
    public ResponseEntity<?> getAllServiceByClientIdAndStatus(@PathVariable(name = "id") Long clientId,
                                                              @PathVariable(name = "status") Status status) {
        return getServiceByClientAndStatus(clientId, status);
    }

    @GetMapping("status/{status}")
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
    public ResponseEntity<?> getAllServiceByClientAndStatus(@PathVariable(name = "status") Status status,
                                                            @RequestAttribute(value = "id") Long id) {
        return getServiceByClientAndStatus(id, status);
    }

    private ResponseEntity<?> getServiceByClientAndStatus(Long clientId, Status status) {
        try {

            Service service = Service.builder().clientId(clientId).status(status).build();
            Example<Service> serviceExample = Example.of(service);
            LOGGER.info("getAllServiceByClientId");
            LOGGER.info("Fetching services for clientId: {} and status: {}", clientId, status);
            List<Service> services = serviceService.findByExample(serviceExample);
            LOGGER.info("Number of services retrieved: {}", services.size());
            services.forEach(service1 -> {
                List<ServiceParameter> serviceParameters = serviceParameterService.findByServiceId(service1.getId());
                service1.setParameterCount(serviceParameters.size());

            });
            return ResponseEntity.ok(ResponseBody.builder()
                    .message(services != null ? String.valueOf(services.size()) : 0 + " services found.")
                    .code(HttpStatus.OK.value())
                    .data(services)
                    .build());
        }catch (Exception exception){
            LOGGER.error("",exception);
            return ResponseEntity
                    .internalServerError()
                    .body(exception.getMessage());
        }
    }


    @GetMapping(path="byClient/{clientId}/")
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
    public ResponseEntity<?> getAllServiceByClientId(@PathVariable(name="clientId") Long clientId) {
        return getServiceByClient(clientId);
    }

    @GetMapping(path="byClient/")
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
    public ResponseEntity<?> getAllServiceByClient(@RequestAttribute(value = "id") Long id) {
        return getServiceByClient(id);
    }

    private ResponseEntity<?> getServiceByClient(Long clientId) {
        try {
            LOGGER.info("getAllServiceByClientId");
            List<Service> services = serviceService.findAllByClientId(clientId);
            return ResponseEntity.ok(ResponseBody.builder()
                    .message(services != null ? String.valueOf(services.size()) : 0 + " services found.")
                    .code(HttpStatus.OK.value())
                    .data(services)
                    .build());
        }catch (Exception exception){
            LOGGER.error("",exception);
            return ResponseEntity
                    .internalServerError()
                    .body(exception.getMessage());
        }
    }

    @GetMapping(path="byApplication/{id}")
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
    public ResponseEntity<?> getServiceByApplicationId(@PathVariable(value="id") Long id) {
        try {
            LOGGER.info("getServiceByApplicationId");
            List<Service> services = serviceService.findAllByApplicationId(id);
            return ResponseEntity.ok(ResponseBody.builder()
                    .message(services!=null? String.valueOf(services.size()) :0+" services found.")
                    .code(HttpStatus.OK.value())
                    .data(services)
                    .build());
        }catch (Exception exception){
            LOGGER.error("",exception);
            return ResponseEntity
                    .internalServerError()
                    .body(exception.getMessage());
        }
    }


    @GetMapping(path="{clientId}/keyword")
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
    public ResponseEntity<?> getServiceByClientAndKeyword( @PathVariable(value = "clientId") Long clientId,
                                                           @RequestParam(value = "keyword") String keyword) {
        return getSerByClientAndKeyword(clientId, keyword);
    }


    @GetMapping(path="keyword")
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
    public ResponseEntity<?> getServiceByKeyword( @RequestAttribute(value = "id") Long clientId,
                                                           @RequestParam(value = "keyword") String keyword) {
        return getSerByClientAndKeyword(clientId, keyword);
    }


    private ResponseEntity<ResponseBody<Object>> getSerByClientAndKeyword(Long clientId, String keyword) {
        try{
            if(StringUtils.isBlank(keyword)) {
                return ResponseEntity.badRequest().body(ResponseBody.builder()
                        .message("Bad Request : keyword is missing.")
                        .code(HttpStatus.BAD_REQUEST.value())
                        .build());
            }

            List<Service> services = serviceService.findServiceByClientIdAndKeywordLike(clientId, keyword);
            return ResponseEntity.ok(ResponseBody.builder()
                    .message(services != null ? String.valueOf(services.size()) : 0 + " services found.")
                    .code(HttpStatus.OK.value())
                    .data(services)
                    .build());
        }catch (Exception exception) {
            LOGGER.error(exception.getMessage(),exception);
            return ResponseEntity.internalServerError().body(ResponseBody.builder()
                    .message(exception.getMessage())
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .exception(exception)
                    .build());
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
    public ResponseEntity<?> save(@RequestBody @Valid Service service,
                                  @RequestAttribute(value = "id") Long clientId,
                                  @RequestAttribute(name = "type") String type) {
        try {
            LOGGER.info("Client {} ",service);

            if(service.getClientId().equals(clientId)
                    || UserTypeUtils.isSuperAdmin(type)) {
                Service c = serviceService.save(service);
                return ResponseEntity.ok(ResponseBody.builder()
                        .message("")
                        .code(HttpStatus.OK.value())
                        .data(c)
                        .build());
            } else {
                return ResponseEntity.ok(ResponseBody.badRequest("UnAuthorize Request."));
            }
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
            @ApiResponse(responseCode = "206", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") })
    })
    public ResponseEntity<?> edit(@RequestBody @Valid Service service,
                                  @RequestAttribute(value = "id") Long clientId,
                                  @RequestAttribute(name = "type") String type) {
        try {
            LOGGER.info("Client {} ",service);

            if (service == null) {
                return ResponseEntity.badRequest().body(ResponseBody.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .build());
            }

            if(service.getClientId().equals(clientId)
                    || UserTypeUtils.isSuperAdmin(type)) {

                if (service.getId() == null) {
                    return ResponseEntity.badRequest().body(ResponseBody.builder()
                            .message("Service Id Should not be null.")
                            .code(HttpStatus.PARTIAL_CONTENT.value())
                            .build());
                }

                Optional<Service> serviceFromDb = serviceService.fetchOne(service.getId());

                if (serviceFromDb.isEmpty()) {
                    return ResponseEntity.badRequest().body(ResponseBody.builder()
                            .message("Service is not valid ")
                            .code(HttpStatus.BAD_REQUEST.value())
                            .build());
                }
                Service c = serviceService.edit(service);
                return ResponseEntity.ok(ResponseBody.builder().data(c).message("Service successfully updated.").code(HttpStatus.OK.value()).build());
            } else {
                return ResponseEntity.ok(ResponseBody.badRequest("UnAuthorize Request."));
            }

        }catch (Exception exception) {
            LOGGER.error(exception.getMessage(),exception);
            return ResponseEntity.internalServerError().body( ResponseBody.builder()
                    .message(exception.getMessage())
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .exception(exception)
                    .build()  );
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
    public ResponseEntity<?> search(@RequestBody ServiceSearchRequest searchRequest ,
                                    @RequestAttribute(value = "id") Long clientId,
                                    @RequestAttribute(name = "type") String type) {
        try {
            List<Service> services = null;
            if (UserTypeUtils.isSuperAdmin(type)) {
                services = serviceService.search(searchRequest);
            } else {
                searchRequest.setClientId(clientId);
                services = serviceService.search(searchRequest);
            }

            if(services ==null)
                services = new ArrayList<>();
            return ResponseBuilder.ok("",services);
        }catch (Exception exception) {
            LOGGER.error(exception.getMessage(),exception);
            return ResponseBuilder.build500(exception);
        }
    }



}
