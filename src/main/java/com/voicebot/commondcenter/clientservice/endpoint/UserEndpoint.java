package com.voicebot.commondcenter.clientservice.endpoint;


import com.voicebot.commondcenter.clientservice.dto.ApplicationSearchRequest;
import com.voicebot.commondcenter.clientservice.dto.ResponseBody;
import com.voicebot.commondcenter.clientservice.dto.UserSearchRequest;
import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.Authentication;
import com.voicebot.commondcenter.clientservice.entity.Service;
import com.voicebot.commondcenter.clientservice.entity.User;
import com.voicebot.commondcenter.clientservice.enums.Status;
import com.voicebot.commondcenter.clientservice.enums.UserType;
import com.voicebot.commondcenter.clientservice.service.AuthenticationService;
import com.voicebot.commondcenter.clientservice.service.UserService;
import com.voicebot.commondcenter.clientservice.utils.PasswordUtils;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*" )
@RequestMapping(path = "/api/v1/user/")
@Tag(name = "User", description = "User management APIs")
public class UserEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceEndpoint.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") })
    })
    public ResponseEntity<?> save(@RequestBody @Valid User user) {
        try {
            LOGGER.info("user {} ",user);

            if (user==null) {
                return ResponseEntity.badRequest().body( ResponseBody.builder()
                        .message("Invalid Request.")
                        .code(HttpStatus.BAD_REQUEST.value())
                        .build()  );
            }

            Optional<User> byEmail = userService.findUserByEmail(user.getEmail());
            if(byEmail.isPresent()) {
                return ResponseEntity.ok( ResponseBody.builder()
                        .message("Email is already registered with us.")
                        .code(HttpStatus.OK.value())
                        .build()  );
            }

            Optional<User> byMobile = userService.findUserByMobile(user.getMobileNumber());
            if(byMobile.isPresent()) {
                return ResponseEntity.ok( ResponseBody.builder()
                        .message("Mobile is already registered with us.")
                        .code(HttpStatus.OK.value())
                        .build());
            }

            Optional<User> byEmpId = userService.findUserByEmpId(user.getEmpId());
            if(byEmpId.isPresent()) {
                return ResponseEntity.ok( ResponseBody.builder()
                        .message("Employee is already registered with us.")
                        .code(HttpStatus.OK.value())
                        .build());
            }

            Authentication authentication = new Authentication();
            authentication.setUserName(user.getEmail());
            authentication.setUserName(user.getEmail());
            authentication.setPassword(PasswordUtils.generate());
            authentication.setUserType(UserType.USER);
            authentication.setMobileNumber(user.getMobileNumber());

            Authentication responseAuthentication = authenticationService.register(authentication);

            if(responseAuthentication!=null && responseAuthentication.getId()!=null) {
                user.setAuthenticationId(responseAuthentication.getId());
                User u =  userService.save(user);

                return ResponseEntity.ok(ResponseBody.builder()
                        .message("Successfully registered with us.")
                        .code(HttpStatus.OK.value())
                        .data(u)
                        .build());
            }else {
                return ResponseEntity.badRequest().body( ResponseBody.builder()
                        .message("Failed to register with us.")
                        .code(HttpStatus.BAD_REQUEST.value())
                        .build());
            }


        }catch (Exception exception) {
            LOGGER.error(exception.getMessage(),exception);
            return ResponseEntity.internalServerError().body( ResponseBody.builder()
                    .message(exception.getMessage())
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .exception(exception)
                    .build());
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") })
    })
    public ResponseEntity<?> edit(@RequestBody @Valid User user) {
        try {
            LOGGER.info("user {} ",user);

            if (user==null) {
                return ResponseEntity.badRequest().body( ResponseBody.builder()
                        .message("Invalid Request.")
                        .code(HttpStatus.BAD_REQUEST.value())
                        .build()  );
            }

            if (user.getId()==null || user.getId() == 0) {
                return ResponseEntity.badRequest().body( ResponseBody.builder()
                        .message("Invalid Request.")
                        .code(HttpStatus.BAD_REQUEST.value())
                        .build()  );
            }

            User u =  userService.edit(user);
            return ResponseEntity.ok(u);
        }catch (Exception exception) {
            LOGGER.error(exception.getMessage(),exception);
            return ResponseEntity.internalServerError().body( ResponseBody.builder()
                    .message(exception.getMessage())
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .exception(exception)
                    .build());
        }
    }


    @GetMapping(path="byClient/{clientId}/")
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") })
    })
    public ResponseEntity<?> getUserByClientId(@PathVariable(name="clientId") Long clientId) {
        return getUsrByClient(clientId);
    }

    @GetMapping(path="byClient/")
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") })
    })
    public ResponseEntity<?> getUserByClient(@RequestAttribute(name="id") Long clientId) {
        return getUsrByClient(clientId);
    }

    private ResponseEntity<?> getUsrByClient(Long clientId) {
        try {
            LOGGER.info("getUserByClientId");
            List<User> users = userService.findUsersByClientId(clientId);
            return ResponseEntity.ok(ResponseBody.builder()
                    .message(users != null ? String.valueOf(users.size()) : 0 + " user found.")
                    .code(HttpStatus.OK.value())
                    .data(users)
                    .build());
        }catch (Exception exception){
            LOGGER.error("",exception);
            return ResponseEntity
                    .internalServerError()
                    .body(exception.getMessage());
        }
    }

    @GetMapping(path="byClient/{clientId}/page")
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") })
    })
    public ResponseEntity<?> getUserByClientId(@PathVariable(name="clientId") Long clientId, Pageable pageable) {
        return getUsrByClient(clientId, pageable);
    }

    @GetMapping(path="byClient/page")
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") })
    })
    public ResponseEntity<?> getUserByClient(@RequestAttribute(name="id") Long clientId, Pageable pageable) {
        return getUsrByClient(clientId, pageable);
    }

    private ResponseEntity<?> getUsrByClient(Long clientId, Pageable pageable) {
        try {
            LOGGER.info("getAllServiceByClientId");
            Page<User> users = userService.findUsersByClientId(clientId, pageable);
            return ResponseEntity.ok(users);
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
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") })
    })
    public ResponseEntity<?> getUsers( @RequestAttribute(name = "id") Long id,
                                       @RequestAttribute(name = "type") String type ) {
        try {
            LOGGER.info("getUsers");
            if(UserTypeUtils.isSuperAdmin(type)) {
                List<User> users = userService.getRepository().findAll();
                return ResponseEntity.ok(ResponseBody.builder()
                        .message(String.valueOf(users.size()))
                        .code(HttpStatus.OK.value())
                        .data(users)
                        .build());
            } else if (UserTypeUtils.isClientAdmin(type)) {
                List<User> users = userService.getRepository().findUsersByClientId(id);
                return ResponseEntity.ok(ResponseBody.builder()
                        .message(String.valueOf(users.size()))
                        .code(HttpStatus.OK.value())
                        .data(users)
                        .build());
            } else {
                return ResponseEntity.ok(ResponseBody.builder()
                        .message("")
                        .code(HttpStatus.OK.value())
                        .data(userService.findById(id))
                        .build());
            }
        }catch (Exception exception){
            LOGGER.error("",exception);
            return ResponseEntity
                    .internalServerError()
                    .body(exception.getMessage());
        }
    }

    @GetMapping(path="page/")
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = ResponseBody.class), mediaType = "application/json") })
    })
    public ResponseEntity<?> getUsers(@RequestAttribute(name = "id") Long id,
                                      @RequestAttribute(name = "type") String type,
                                      Pageable pageable) {
        try {
            LOGGER.info("getUsers");
            if(UserTypeUtils.isSuperAdmin(type)) {
                Page<User> users = userService.getRepository().findAll(pageable);
                return ResponseEntity.ok(ResponseBody.builder()
                        .message(String.valueOf(users.getSize()))
                        .code(HttpStatus.OK.value())
                        .data(users)
                        .build());
            } else if (UserTypeUtils.isClientAdmin(type)) {
                Page<User> users = userService.getRepository().findUsersByClientId(id,pageable);
                return ResponseEntity.ok(ResponseBody.builder()
                        .message(String.valueOf(users.getSize()))
                        .code(HttpStatus.OK.value())
                        .data(users)
                        .build());
            } else {
                return ResponseEntity.ok(ResponseBody.builder()
                        .message("")
                        .code(HttpStatus.OK.value())
                        .data(userService.findById(id))
                        .build());
            }
        }catch (Exception exception){
            LOGGER.error("",exception);
            return ResponseEntity
                    .internalServerError()
                    .body(exception.getMessage());
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
    public ResponseEntity<?> search(@RequestBody UserSearchRequest searchRequest ,
                                    @RequestAttribute(value = "id") Long clientId,
                                    @RequestAttribute(name = "type") String type) {
        try {
            List<User> users = null;
            if (UserTypeUtils.isSuperAdmin(type)) {
                 users = userService.search(searchRequest);
            } else if (UserTypeUtils.isClientAdmin(type)) {
                searchRequest.setClientId(clientId);
                users = userService.search(searchRequest);
            }else {
                users = new ArrayList<>();
            }
            return ResponseBuilder.ok("",users);
        }catch (Exception exception) {
            LOGGER.error(exception.getMessage(),exception);
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
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Application.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.TEXT_PLAIN_VALUE) }) ,
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.TEXT_PLAIN_VALUE) })
    })
    public ResponseEntity<?> getAllApplicationByClientIdAndStatus(@PathVariable(name = "id") Long clientId,
                                                                  @PathVariable(name = "status") Status status) {
        return getUsrByClientAndStatus(clientId, status);
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
    public ResponseEntity<?> getUserByClientIdAndStatus(@RequestAttribute(name = "id") Long clientId,
                                                                  @PathVariable(name = "status") Status status) {
        return getUsrByClientAndStatus(clientId, status);
    }

    private ResponseEntity<?> getUsrByClientAndStatus(Long clientId, Status status) {
        try {

            User user = User.builder().clientId(clientId).status(status).build();
            Example<User> userExample = Example.of(user);

            List<User> users = userService.findByExample(userExample);
            return ResponseEntity.ok(ResponseBody.builder()
                    .message(users != null ? String.valueOf(users.size()) : 0 + " user found.")
                    .code(HttpStatus.OK.value())
                    .data(users)
                    .build());
        }catch (Exception exception) {
            return ResponseBuilder.build500(exception);
        }
    }

}
