package com.voicebot.commondcenter.clientservice.endpoint;

import com.voicebot.commondcenter.clientservice.dto.ResponseBody;
import com.voicebot.commondcenter.clientservice.entity.Authentication;
import com.voicebot.commondcenter.clientservice.entity.BuddyAdmin;
import com.voicebot.commondcenter.clientservice.entity.Client;
import com.voicebot.commondcenter.clientservice.entity.Login;
import com.voicebot.commondcenter.clientservice.enums.UserType;
import com.voicebot.commondcenter.clientservice.exception.EmailAlreadyRegistered;
import com.voicebot.commondcenter.clientservice.exception.InvalidUserNameAndPassword;
import com.voicebot.commondcenter.clientservice.service.AuthenticationService;
import com.voicebot.commondcenter.clientservice.service.BuddyAdminService;
import com.voicebot.commondcenter.clientservice.service.ClientService;
import com.voicebot.commondcenter.clientservice.service.UserService;
import com.voicebot.commondcenter.clientservice.utils.EncryptDecryptPassword;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/auth/",produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*" )
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthenticationEndpoint {

     @Autowired
     private ClientService clientService;

     @Autowired
     private UserService userService;

     @Autowired
     private BuddyAdminService buddyAdminService;

     @Autowired
     private AuthenticationService authenticationService;

    @PostMapping(path = "register/",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.APPLICATION_JSON_VALUE) }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.APPLICATION_JSON_VALUE) }) ,
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.APPLICATION_JSON_VALUE) })
    })
    private ResponseEntity<?> register(@Valid @RequestBody Authentication authentication) {
        try {
            if (authentication == null) {
                return  ResponseEntity.badRequest().body(ResponseBody.badRequest("Invalid input"));
            }

            if(authentication.getUserType() == null)
                return  ResponseEntity.badRequest().body(ResponseBody.badRequest("User Type should not null or empty"));

            authentication.setPassword(EncryptDecryptPassword.encryptPassword(authentication.getPassword()));

            if(authentication.getUserType().equals(UserType.CLIENT_ADMIN)) {
                Client client = authenticationService.registerClient(authentication);
                return  ResponseEntity.ok(ResponseBody.ok(client)) ;
            } else if (authentication.getUserType().equals(UserType.USER)) {

                return null;
            } else if (authentication.getUserType().equals(UserType.SUPER_ADMIN)) {
               BuddyAdmin buddyAdmin =  authenticationService.registerBuddyAdmin(authentication);
               return  ResponseEntity.ok(ResponseBody.ok(buddyAdmin)) ;
            }

        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }


        return null;
    }


    @PostMapping(path = "login/",consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> login(@Valid @RequestBody Login login) {
        try {
            Optional<Authentication> optionalAuthentication = authenticationService.login(login);

            if(optionalAuthentication.isPresent()) {
                Authentication authentication = optionalAuthentication.get();

                if(authentication.getUserType().equals(UserType.CLIENT_ADMIN)) {
                    Optional<Client> optionalClient = clientService.findByAuthenticationId(authentication.getId());
                    if(optionalClient.isPresent()) {
                        Client client = optionalClient.get();
                        authentication.setEntityId(client.getId());
                        authentication.setUserName(client.getEmail());
                        authentication.setName(client.getClientName());
                    }else {
                        return ResponseEntity.badRequest().body(ResponseBody.builder().data("Authentication Failed. Please check username and password and try again.").build());
                    }
                } else if (authentication.getUserType().equals(UserType.USER)) {

                } else {
                    Optional<BuddyAdmin> optionalBuddyAdmin =  buddyAdminService.findOneByAuthenticationId(authentication.getId());
                    if(optionalBuddyAdmin.isPresent()) {
                        BuddyAdmin buddyAdmin = optionalBuddyAdmin.get();
                        authentication.setEntityId(buddyAdmin.getId());
                        authentication.setUserName(buddyAdmin.getEmail());
                        authentication.setName(buddyAdmin.getName());
                    } else {
                        return ResponseEntity.badRequest().body(ResponseBody.builder().data("Authentication Failed. Please check username and password and try again.").build());
                    }
                }

                Authentication response = Authentication.builder()
                        .userName(authentication.getUserName())
                        .token(authentication.getToken())
                        .entityId(authentication.getEntityId())
                        .name(authentication.getName())
                        .userType(authentication.getUserType())
                        .build();

                return ResponseEntity.ok().body(ResponseBody.builder().data(response).build());
            }
            return ResponseEntity.badRequest().body(ResponseBody.builder().code(HttpStatus.OK.value()).data("Authentication Failed. Please check username and password and try again.").build());
        }catch (InvalidUserNameAndPassword invalidUserNameAndPassword){
            ResponseBody body = ResponseBody.builder().build();
            body.setCode(HttpStatus.OK.value());
            body.setMessage("Authentication Failed. Please check username and password and try again.");
            return  ResponseEntity.badRequest().body(body);
        }catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
