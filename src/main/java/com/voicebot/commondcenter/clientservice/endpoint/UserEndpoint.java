package com.voicebot.commondcenter.clientservice.endpoint;


import com.voicebot.commondcenter.clientservice.dto.ResponseBody;
import com.voicebot.commondcenter.clientservice.entity.Service;
import com.voicebot.commondcenter.clientservice.entity.User;
import com.voicebot.commondcenter.clientservice.service.UserService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/user/")
@Tag(name = "User", description = "User management APIs")
public class UserEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceEndpoint.class);

    @Autowired
    private UserService userService;

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

            User u =  userService.save(user);
            return ResponseEntity.ok(ResponseBody.builder()
                    .message("Successfully registered with us.")
                    .code(HttpStatus.OK.value())
                    .data(u)
                    .build());
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


    @GetMapping(path="{clientId}/")
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
        try {
            LOGGER.info("getAllServiceByClientId");
            List<User> users = userService.findUsersByClientId(clientId);
            return ResponseEntity.ok(ResponseBody.builder()
                    .message(users!=null? String.valueOf(users.size()) :0+" user found.")
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

    @GetMapping(path="{clientId}/page")
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
        try {
            LOGGER.info("getAllServiceByClientId");
            Page<User> users = userService.findUsersByClientId(clientId,pageable);
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
    public ResponseEntity<?> getUsers() {
        try {
            LOGGER.info("getAllServiceByClientId");
            List<User> users = userService.getRepository().findAll();
            return ResponseEntity.ok(ResponseBody.builder()
                    .message(String.valueOf(users.size()))
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
    public ResponseEntity<?> getUsers(Pageable pageable) {
        try {
            LOGGER.info("getAllServiceByClientId");
            Page<User> users = userService.getRepository().findAll(pageable);
            return ResponseEntity.ok(users);
        }catch (Exception exception){
            LOGGER.error("",exception);
            return ResponseEntity
                    .internalServerError()
                    .body(exception.getMessage());
        }
    }
}
